package uz.giza.bot.admin.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.admin.AdminCommands;
import uz.giza.bot.admin.AdminHandler;
import uz.giza.bot.admin.AdminMapping;
import uz.giza.bot.entity.Course;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.CourseService;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@AdminMapping(values = {AdminCommands.SORT_USERS_BY_COURSE})
public class ExportUsersByCoursesHandler implements AdminHandler {
    private final CourseService courseService;
    private final UserService userService;
    private final SendMessageService sendMessageService;
    private final UserReportHandler userReportHandler;


    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        List<Course> courses = courseService.getAll();
        for (Course course : courses) {
            List<User> users = userService.getAllByTargetCourse(course.getId());
            if (users.isEmpty()) {
                sendMessageService.sendMessage(chatId, "Пользователей по целевому курсу: " + course.getName() + " не найдено!");
            } else {
                byte[] data = userReportHandler.generateUserExcelReport(users, "users_by_course");
                userReportHandler.sendUserReport(chatId, "users_by_course", "Пользователи по целевому курсу: " + course.getName(), data);
            }
        }

    }
}
