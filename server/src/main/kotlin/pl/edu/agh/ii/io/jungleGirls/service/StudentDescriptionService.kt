package pl.edu.agh.ii.io.jungleGirls.service

import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.model.StudentDescription
import pl.edu.agh.ii.io.jungleGirls.repository.StudentDescriptionRepository

@Service
class StudentDescriptionService(
    private val studentDescriptionRepository: StudentDescriptionRepository
) {
    fun findById(id: Long): StudentDescription? {
        return studentDescriptionRepository.findById(id).block()
    }
}