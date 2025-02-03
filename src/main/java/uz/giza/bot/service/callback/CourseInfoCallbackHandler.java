package uz.giza.bot.service.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.giza.bot.entity.Course;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.CourseService;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.command.handlers.Handler;
import uz.giza.bot.service.input.UserStates;
import uz.giza.bot.util.StaticMessages;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseInfoCallbackHandler implements Handler {
    private final CourseService courseService;
    private final SendMessageService sendMessageService;
    private final UserService userService;

    @Override
    @Async
    public void execute(Update update) {
        String data = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        User user = userService.get(chatId);
        String message = "👇🏻Xarid qilish uchun <b> Kursga yozilish </b> tugmasini bosing";

        List<Course> courseList = courseService.getAll();
        for (Course course : courseList) {
            if (data.equals(course.getName())) {
//                sendMessageService.deleteMessage(chatId, messageId);
                sendMessageService.sendPhotoWithCaption(chatId, FileService.COURSE_1_PHOTO_KEY, StaticMessages.COURSE_1_DESCRIPTION);
                if (user.getTargetCourse() == null || !user.getTargetCourse().equals(course))
                    user.setTargetCourse(course);
                userService.updateUserState(user, UserStates.BACK_TO_COURSE_LIST);
                sendMessageService.sendMessageWithReplyKeyboard(chatId, createReplyKeyboardMarkup(), message);
//                sendMessageService.deleteMessage(chatId, messageId - 1);
                break;
            }
        }
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(CommandName.PURCHASE.getCommandName());

        KeyboardRow row2 = new KeyboardRow();
        row2.add(CommandName.BACK.getCommandName());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(row1, row2));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}
//    public InlineKeyboardMarkup createKeyboardMarkup() {
//        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
//        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
//        rowInLine.add(InlineKeyboardButton.builder()
//                .text("Sotib olish")
//                .callbackData(CallbackName.BUY_COURSE.getCommandName())
//                .build()
//        );
//        rowsInLine.add(rowInLine);
//        return new InlineKeyboardMarkup(rowsInLine);
//
//    }



