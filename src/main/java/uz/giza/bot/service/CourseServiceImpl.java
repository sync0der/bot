package uz.giza.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.giza.bot.entity.Course;
import uz.giza.bot.repository.CourseRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course get(String courseName) {
        return courseRepository.getByName(courseName);
    }
}
