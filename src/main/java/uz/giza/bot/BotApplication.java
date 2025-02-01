package uz.giza.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import uz.giza.bot.entity.Course;
import uz.giza.bot.entity.User;
import uz.giza.bot.repository.CourseRepository;
import uz.giza.bot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@EnableJpaAuditing
@EnableCaching
@SpringBootApplication
@RequiredArgsConstructor
public class BotApplication {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            if (courseRepository.count() == 0) {
                List<Course> courses = List.of(
                        new Course(1L, "Java Basics", "Introduction to Java programming", 89000L),
                        new Course(2L, "Spring Boot", "Building web applications with Spring Boot", 99000L),
                        new Course(3L, "Docker Fundamentals", "Learn containerization with Docker", 100000L),
                        new Course(4L, "PostgreSQL Mastery", "Database management with PostgreSQL", 250000L),
                        new Course(5L, "Microservices Architecture", "Design and implement microservices", 156000L)
                );

                courseRepository.saveAll(courses);
                System.out.println("5 fake courses have been saved.");
            }

            if (userRepository.count() < 4) {
                List<User> users = new ArrayList<>();
                Random random = new Random();
                List<Course> allCourses = courseRepository.findAll();

                for (int i = 1001; i <= 10000; i++) {
                    User user = User.builder()
                            .phoneNumber(String.valueOf(Long.parseLong("012345") + i))
                            .fullName("User " + i)
                            .userName("user_" + i)
                            .chatId(100000L + i)
                            .utmTag("rslk0003")
                            .registeredAt(LocalDateTime.now())
                            .build();
                    users.add(user);
                }

                userRepository.saveAll(users);
                System.out.println("1000 fake users have been saved.");
            }
        };
    }

}
