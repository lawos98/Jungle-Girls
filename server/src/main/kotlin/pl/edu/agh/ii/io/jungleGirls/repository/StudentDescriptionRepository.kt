package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import pl.edu.agh.ii.io.jungleGirls.model.StudentDescription

interface StudentDescriptionRepository :ReactiveCrudRepository<StudentDescription, Long> {

}