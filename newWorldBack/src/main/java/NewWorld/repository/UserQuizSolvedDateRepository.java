package NewWorld.repository;

import NewWorld.domain.User;
import NewWorld.domain.UserQuizSolvedDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserQuizSolvedDateRepository extends JpaRepository<UserQuizSolvedDate, Long> {
    List<UserQuizSolvedDate> findByUser(User user);
}