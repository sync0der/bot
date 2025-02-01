package uz.giza.bot.service.callback;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.giza.bot.entity.Course;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.CourseService;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.handlers.Handler;
import uz.giza.bot.service.input.UserStates;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseInfoCallbackHandler implements Handler {
    private final CourseService courseService;
    private final SendMessageService sendMessageService;
    private final UserService userService;

    @Override
    public void execute(Update update) {
        String data = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        User user = userService.get(chatId);

        List<Course> courseList = courseService.getAll();
        for (Course course : courseList) {
            if (data.equals(course.getName())) {
                String message = String.format("%s\n\n%s", course.getName(), course.getDescription());
                sendMessageService.sendEditMessageWithReplyKeyboard(chatId, messageId, message, createKeyboardMarkup());
                if (user.getTargetCourse() == null || !user.getTargetCourse().equals(course))
                    user.setTargetCourse(course);
                userService.updateUserState(user, UserStates.BACK_TO_COURSE_LIST);
                break;
            }
        }


    }

    public InlineKeyboardMarkup createKeyboardMarkup() {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        rowInLine.add(InlineKeyboardButton.builder()
                .text("Sotib olish")
                .callbackData(CallbackName.BUY_COURSE.getCommandName())
                .build()
        );
        rowsInLine.add(rowInLine);
        return new InlineKeyboardMarkup(rowsInLine);

    }


}
