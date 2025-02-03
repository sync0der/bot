package uz.giza.bot.service.command.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.giza.bot.entity.Course;
import uz.giza.bot.service.CourseService;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.CommandMapping;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.input.UserStates;

import java.util.List;

@Service
@CommandMapping(values = {CommandName.BUY_COURSE})
@RequiredArgsConstructor
public class CourseCommandHandler implements CommandHandler {
    private final CourseService courseService;
    private final SendMessageService sendMessageService;
    private final UserService userService;

    @Override
    @Async
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        sendMessageService.sendMessageWithReplyKeyboard(chatId, createReplyKeyboardMarkup(), "Kursni tanlang");
        sendMessageService.sendMessageWithReplyKeyboard(chatId, createInlineKeyboardMarkup(), "Mavjud kurslar:");
        userService.updateUserState(chatId, UserStates.BACK_TO_MAIN);
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

    private ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(CommandName.BACK.getCommandName());
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(row1));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }


}
