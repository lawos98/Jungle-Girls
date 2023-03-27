package pl.edu.agh.ii.io.jungleGirls.db;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.ii.io.jungleGirls.model.Student;
import reactor.core.publisher.Mono;

@Repository
public interface StudentDao extends ReactiveCrudRepository<Student,Long> {

    Mono<Student> findByNick(String username);
    Mono<Student> findByIndex(Long index);
}
