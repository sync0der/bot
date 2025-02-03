package uz.giza.bot.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.giza.bot.service.SendMessageService;

import java.util.List;

@Service
@RequiredArgsConstructor
@AdminMapping(values = {AdminCommands.SORT_USERS})
public class SortCommandHandler implements AdminHandler {

    private final SendMessageService sendMessageService;

    @Override
    @Async
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        sendMessageService.sendMessageWithReplyKeyboard(chatId, createReplyKeyboardMarkup(), "Выберите категорию " +
                "сортировки");
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(AdminCommands.SORT_USERS_BY_UTM.getCommandName());
        row1.add(AdminCommands.SORT_USERS_BY_COURSE.getCommandName());

        KeyboardRow row2 = new KeyboardRow();
        row2.add(AdminCommands.SORT_USERS_BY_PURCHASE.getCommandName());

        KeyboardRow row3 = new KeyboardRow();
        row3.add(AdminCommands.MENU.getCommandName());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(row1, row2, row3));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

}
