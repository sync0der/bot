package uz.giza.bot.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.giza.bot.entity.Course;
import uz.giza.bot.entity.User;
import uz.giza.bot.entity.UserPaymentStatus;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByChatId(@NotBlank Long chatId);

    User findByChatId(@NotBlank Long chatId);


    List<User> findAllByUtmTag(String utmTag);

    List<User> findAllByTargetCourse_Name(String targetCourseName);

    List<User> findAllByTargetCourse(Course targetCourse);

    List<User> findAllByTargetCourse_Id(Long targetCourseId);

    List<User> findAllByStatus(UserPaymentStatus status);

    @Query("SELECT u FROM User u WHERE u.phoneNumber IS NULL OR u.phoneNumber NOT LIKE '+%' AND (u.phoneNumber NOT LIKE '1%' AND u.phoneNumber NOT LIKE '2%' AND u.phoneNumber NOT LIKE '3%' AND u.phoneNumber NOT LIKE '4%' AND u.phoneNumber NOT LIKE '5%' AND u.phoneNumber NOT LIKE '6%' AND u.phoneNumber NOT LIKE '7%' AND u.phoneNumber NOT LIKE '8%' AND u.phoneNumber NOT LIKE '9%' AND u.phoneNumber NOT LIKE '0%')")
    List<User> findUsersWithInvalidPhoneNumber();

}
