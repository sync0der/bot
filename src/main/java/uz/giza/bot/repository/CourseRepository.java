package uz.giza.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.giza.bot.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course getByName(String name);
}
