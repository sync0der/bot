package uz.giza.bot.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.giza.bot.service.SendMessageService;

import java.util.List;

@Service
@Slf4j
@AdminMapping(values = {AdminCommands.MENU, AdminCommands.START, AdminCommands.MAIN})
@RequiredArgsConstructor
public class MainAdminMenuCommandHandler implements AdminHandler {
    private final SendMessageService sendMessageService;

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        sendMessageService.sendMessageWithReplyKeyboard(chatId, createReplyKeyboardMarkup(), AdminCommands.MENU.getCommandName());
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(AdminCommands.MAILING.getCommandName());

        KeyboardRow row2 = new KeyboardRow();
        row2.add(AdminCommands.USERS.getCommandName());
        row2.add(AdminCommands.COURSES.getCommandName());


        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(row1, row2));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}
