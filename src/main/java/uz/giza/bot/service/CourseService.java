package uz.giza.bot.service;

import uz.giza.bot.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAll();

    Course get(String courseName);

}
