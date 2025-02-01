package uz.giza.bot.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.giza.bot.service.SendMessageService;

import java.util.List;

@Service
@RequiredArgsConstructor
@AdminMapping(values = {AdminCommands.USERS})
public class UsersCommandHandler implements AdminHandler{
    private final SendMessageService sendMessageService;

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        sendMessageService.sendMessageWithReplyKeyboard(chatId, createReplyKeyboardMarkup(), "Выберите опции: ");
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(AdminCommands.SORT_USERS.getCommandName());
        row1.add(AdminCommands.ALL_USERS.getCommandName());

        KeyboardRow row2 = new KeyboardRow();
        row2.add(AdminCommands.MENU.getCommandName());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(row1, row2));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }



}
