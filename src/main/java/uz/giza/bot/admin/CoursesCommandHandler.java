package uz.giza.bot.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.giza.bot.entity.Course;
import uz.giza.bot.service.CourseService;
import uz.giza.bot.service.SendMessageService;

import java.util.List;

@Service
@RequiredArgsConstructor
@AdminMapping(values = {AdminCommands.COURSES})
public class CoursesCommandHandler implements AdminHandler {
    private final CourseService courseService;
    private final SendMessageService sendMessageService;

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        sendMessageService.sendMessageWithReplyKeyboard(chatId, createInlineKeyboardMarkup(), "Выберите нужный курс:");
    }

    private InlineKeyboardMarkup createInlineKeyboardMarkup() {
        List<Course> courseList = courseService.getAll();
        List<List<InlineKeyboardButton>> rowsInline = courseList.stream()
                .map(course -> {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(course.getName());
                    button.setCallbackData(course.getName());
                    return List.of(button);
                })
                .toList();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsInline);
        return inlineKeyboardMarkup;
    }

}
