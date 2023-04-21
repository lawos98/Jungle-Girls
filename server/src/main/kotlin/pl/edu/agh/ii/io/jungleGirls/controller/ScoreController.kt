package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.ActivityScore
import pl.edu.agh.ii.io.jungleGirls.dto.ActivityScoreList
import pl.edu.agh.ii.io.jungleGirls.service.ScoreService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService

@RestController
@RequestMapping("/api/score")
class ScoreController(
    private val tokenService: TokenService,
    private val scoreService: ScoreService
) {
    @GetMapping("/{id}")
    fun getScores(@PathVariable id:Long, @RequestHeader("Authorization") token: String): List<ActivityScoreList> {
        val instructor = tokenService.parseToken(token.substring("Bearer".length))
        when(val score=scoreService.getScores(id,instructor.id!!)){
            is Either.Right ->{
                return score.value
            }
            is Either.Left -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,score.value)

            }
        }
    }

    @GetMapping()
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
        when(val scoreList=scoreService.updateScores(id,instructor.id!!,scores)){
            is Either.Right ->{
                return scoreList.value
            }
            is Either.Left -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,scoreList.value)

            }
        }
    }


}