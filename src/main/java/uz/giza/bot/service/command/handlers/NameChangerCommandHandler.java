package uz.giza.bot.service.command.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.CommandMapping;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.input.UserStates;

import java.util.List;

@Service
@CommandMapping(values = {CommandName.CHANGE_NAME})
@RequiredArgsConstructor
public class NameChangerCommandHandler implements CommandHandler {
    private final UserService userService;
    private final SendMessageService sendMessageService;

    @Override
    @Async
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        User user = userService.updateUserState(chatId, UserStates.WAITING_FOR_NAME);
        String message = String.format("""
                <b>Sizning ismingiz:</b> <code>%s</code> \s
                
                <b>Ismingizni o'zgartirish uchun, yangi ism kiriting:</b> \s
                
                ❌ Jarayonni bekor qilish uchun <b>🔙 Ortga</b> tugmasini bosing.
                """, user.getFullName());

        sendMessageService.sendMessageWithReplyKeyboard(chatId, createKeyboardMarkup(), message);
    }

    private ReplyKeyboardMarkup createKeyboardMarkup() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(CommandName.BACK.getCommandName());
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(row1));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}
