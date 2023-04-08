package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.CreateActivityCategoryDto
import pl.edu.agh.ii.io.jungleGirls.dto.CreateActivityCategoryResponseDto
import pl.edu.agh.ii.io.jungleGirls.dto.CreateActivityDto
import pl.edu.agh.ii.io.jungleGirls.model.ActivityCategory
import pl.edu.agh.ii.io.jungleGirls.service.ActivityCategoryService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService

@RestController
@RequestMapping("api/activity_category")
class ActivityCategoryCreationController(
    private val activityCategoryService: ActivityCategoryService,
    private val tokenService: TokenService
) {

    @PostMapping("/create")
    fun createActivity(@RequestBody payload: CreateActivityCategoryDto,@RequestHeader("Authorization") token: String): String {
        val user = tokenService.parseToken(token.substring("Bearer".length))

        val activityCategory = ActivityCategory(
            name = payload.name,
            description = payload.description,
            instructorId = user.id!!
        )

        when(val result = activityCategoryService.createCategory(activityCategory,user.id)){
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            is Either.Right -> return "Activity category created successfully"
        }
    }

    @GetMapping("/create")
    fun getAllActivityCategories(@RequestHeader("Authorization") token: String): CreateActivityCategoryResponseDto{
        val user = tokenService.parseToken(token.substring("Bearer".length))

        return CreateActivityCategoryResponseDto(activityCategoryService.getAllNames(user.id!!))
    }
}