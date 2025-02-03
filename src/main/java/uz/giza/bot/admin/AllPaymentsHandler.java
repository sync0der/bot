package uz.giza.bot.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.giza.bot.entity.User;
import uz.giza.bot.entity.UserPaymentStatus;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@AdminMapping(values = {AdminCommands.ALL_PAYMENTS})
public class AllPaymentsHandler implements AdminHandler {


    private final UserService userService;
    private final SendMessageService sendMessageService;

    @Override
    @Async
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        List<User> allPayedUsers = userService.getAllPayedUsers(UserPaymentStatus.DECLINED);
        if (allPayedUsers.isEmpty())
            sendMessageService.sendMessage(chatId, "Не найдено!");
        else
            for (User allPayedUser : allPayedUsers) {
                if (!allPayedUser.getPhotosUrl().isEmpty()) {
                    String message = String.format("%s\n%s\n%s\n%d", allPayedUser.getFullName(), allPayedUser.getPhoneNumber(), allPayedUser.getPhotosUrl().getLast(), allPayedUser.getPhotosUrl().size());
                    sendMessageService.sendMessageWithReplyKeyboard(chatId, createInlineKeyboard(allPayedUser.getChatId().toString()), message);
                }
            }
    }


    public InlineKeyboardMarkup createInlineKeyboard(String chatId) {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton button1 = InlineKeyboardButton.builder()
                .text("Одобрить")
                .callbackData(chatId)
                .build();
        InlineKeyboardButton button2 = InlineKeyboardButton.builder()
                .text("Отметить")
                .callbackData("tick " + chatId)
                .build();
        rowInLine.add(button1);
        rowInLine.add(button2);
        rowsInLine.add(rowInLine);
        return new InlineKeyboardMarkup(rowsInLine);
    }
}
