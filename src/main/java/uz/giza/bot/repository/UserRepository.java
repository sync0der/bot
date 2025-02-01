package uz.giza.bot.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.giza.bot.entity.Course;
import uz.giza.bot.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByChatId(@NotBlank Long chatId);

    User findByChatId(@NotBlank Long chatId);


    List<User> findAllByUtmTag(String utmTag);

    List<User> findAllByTargetCourse_Name(String targetCourseName);

    List<User> findAllByTargetCourse(Course targetCourse);

    List<User> findAllByTargetCourse_Id(Long targetCourseId);
}
