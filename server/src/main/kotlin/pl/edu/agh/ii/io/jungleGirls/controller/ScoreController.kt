package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.ActivityScore
import pl.edu.agh.ii.io.jungleGirls.dto.ActivityScoreList
import pl.edu.agh.ii.io.jungleGirls.service.CourseGroupService
import pl.edu.agh.ii.io.jungleGirls.service.ScoreService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService
import java.io.BufferedWriter
import java.io.File
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files

@RestController
@RequestMapping("/api/score")
class ScoreController(
    private val tokenService: TokenService,
    private val scoreService: ScoreService,
    private val courseGroupService: CourseGroupService,
) {
    @GetMapping("/{id}")
    fun getScores(@PathVariable id:Long, @RequestHeader("Authorization") token: String): List<ActivityScoreList> {
        val instructor = tokenService.parseToken(token.substring("Bearer".length))
        when(val score=scoreService.getScores(id, instructor.id)){
            is Either.Right ->{
                return score.value
            }
            is Either.Left -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,score.value)

            }

        }
    }

    @GetMapping("/to-csv/{groupId}")
    fun downloadCSV(response: HttpServletResponse, @PathVariable groupId:Long, @RequestHeader("Authorization") token: String){
        val instructor = tokenService.parseToken(token.substring("Bearer".length))
        if(!courseGroupService.checkLecturerGroup(instructor.id,groupId)){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Group does not belongs to instructor")
        }
        val filename = "group_${groupId}_grades.csv"

        response.contentType = "text/csv"
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")

        val outputStream = response.outputStream
        val writer = OutputStreamWriter(outputStream, StandardCharsets.UTF_8)
        val bufferedWriter = BufferedWriter(writer)

        scoreService.generateCSV(instructor.id,groupId,bufferedWriter)
        bufferedWriter.flush()

        outputStream.close()
        outputStream.flush()
    }
    @GetMapping
    fun getScore(@RequestHeader("Authorization") token: String):List<ActivityScore>{
        val user = tokenService.parseToken(token.substring("Bearer".length))
        when(val score = scoreService.getScore(user)){
            is Either.Right ->return score.value
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST,score.value)
        }
    }

    @PutMapping("/{id}")
    fun updateScores(@PathVariable id:Long, @RequestBody scores:List<ActivityScoreList>, @RequestHeader("Authorization") token: String):List<ActivityScoreList>{
        val instructor = tokenService.parseToken(token.substring("Bearer".length))
        when(val scoreList=scoreService.updateScores(id, instructor.id,scores)){
            is Either.Right ->{
                return scoreList.value
            }
            is Either.Left -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,scoreList.value)

            }
        }
    }


}
