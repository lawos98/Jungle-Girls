package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import pl.edu.agh.ii.io.jungleGirls.model.ReleaseInfo
import pl.edu.agh.ii.io.jungleGirls.model.StudentDescription
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class GitHubService(private val restTemplate: RestTemplate) {

    fun getLastCommit(studentDescription: StudentDescription,activityName:String): Pair<LocalDateTime?,String?> {
        val repoUrl = studentDescription.githubLink ?: return Pair(null,"Student ${studentDescription.id} doesn't have github link")
        val (repoOwner,repoName) = parseGithubUrl(repoUrl) ?: return Pair(null,"Student ${studentDescription.id} has invalid github link")
        return when(val releaseInfo = getReleaseInfo(repoOwner,repoName,activityName.replace(" ","_"))){
            is Either.Left -> Pair(null,releaseInfo.value)
            is Either.Right -> Pair(releaseInfo.value.createdAt, null)
        }
    }

    private fun getReleaseInfo(owner: String, repo: String, tag: String): Either<String, ReleaseInfo> {
        return isUserExist(owner)
            .flatMap { isRepoExist(owner, repo) }
            .flatMap { isTagExist(owner, repo, tag) }
            .flatMap {
                val url = "https://api.github.com/repos/$owner/$repo/releases/tags/$tag"
                val response = restTemplate.getForEntity(url, String::class.java)
                if (response.statusCode == HttpStatus.OK) {
                    val createdAt = parseCreatedAt(response.body)
                    ReleaseInfo(createdAt).right()
                } else {
                    "Failed to get release info: ${response.statusCode}".left()
                }
            }
    }

    private fun isUserExist(user: String): Either<String, None> {
        val url = "https://api.github.com/users/$user"
        try{
            restTemplate.getForEntity(url, String::class.java)
        }
        catch (e: HttpClientErrorException){
            "User does not exist: $user".left()
        }
        return None.right()
    }

    private fun isRepoExist(user: String, repo: String): Either<String, None> {
        val url = "https://api.github.com/repos/$user/$repo"
        try{
            restTemplate.getForEntity(url, String::class.java)
        }
        catch (e: HttpClientErrorException){
            "Repository does not exist: $user/$repo".left()
        }
        return None.right()
    }

    private fun isTagExist(user: String, repo: String, tag: String): Either<String, None> {
        val url = "https://api.github.com/repos/$user/$repo/git/ref/tags/$tag"
        try{
            restTemplate.getForEntity(url, String::class.java)
        }
        catch (e: HttpClientErrorException){
            return "Tag does not exist: $user/$repo/$tag".left()
        }
        return None.right()
    }

    private fun parseCreatedAt(body: String?): LocalDateTime {
        val json = JSONObject(body)
        val createdAt = json.getString("created_at")
        return LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME)
    }

    fun parseGithubUrl(url: String): Pair<String, String>? {
        val uri = UriComponentsBuilder.fromHttpUrl(url).build()
        val pathSegments = uri.pathSegments
        if (pathSegments.size == 2 && pathSegments[0] != null && pathSegments[1] != null) {
            return Pair(pathSegments[0], pathSegments[1])
        }
        return null
    }
}
