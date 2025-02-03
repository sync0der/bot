package uz.giza.bot.service.command.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.CommandMapping;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.input.UserStates;

import java.util.List;

@Service
@CommandMapping(values = {CommandName.CHANGE_PHONE_NUMBER})
@RequiredArgsConstructor
public class PhoneNumberChangerCommandHandler implements CommandHandler {
    private final UserService userService;
    private final SendMessageService sendMessageService;


    @Override
    @Async
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        User user = userService.updateUserState(chatId, UserStates.WAITING_FOR_PHONE_NUMBER);
        String message = String.format("""
                📌 <b>Sizning telefon raqamingiz:</b> <code>%s</code>
                
                📲 <b>Yangi telefon raqamingizni yuboring!</b>
                
                ❌ Jarayonni bekor qilish uchun <b>🔙 "Ortga"</b> tugmasini bosing.
                """, user.getPhoneNumber());
        sendPhoneNumberRequest(chatId, message);

    }

    private void sendPhoneNumberRequest(Long chatId, String message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow row1 = new KeyboardRow(List.of(
                KeyboardButton.builder()
                        .text("Telefon raqamini yuborish")
                        .requestContact(true)
                        .build()
        ));
        KeyboardRow row2 = new KeyboardRow(List.of(
                KeyboardButton.builder()
                        .text("\uD83D\uDD19 Ortga")
                        .build()
        ));

        replyKeyboardMarkup.setKeyboard(List.of(row1, row2));
        sendMessageService.sendMessageWithReplyKeyboard(chatId, replyKeyboardMarkup, message);
    }
}
