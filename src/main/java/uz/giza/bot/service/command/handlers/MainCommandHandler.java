package uz.giza.bot.service.command.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.command.CommandMapping;
import uz.giza.bot.service.command.CommandName;

import java.util.List;

@Service
@CommandMapping(values = {CommandName.MAIN, CommandName.MENU})
@RequiredArgsConstructor
public class MainCommandHandler implements CommandHandler {
    private final SendMessageService sendMessageService;

    @Override
    public void execute(Update update) {
        ReplyKeyboardMarkup keyboardMarkup = createKeyboardMarkup();
        sendMessageService.sendMessageWithReplyKeyboard(update.getMessage().getChatId(), keyboardMarkup, "Asosiy menyu \uD83C\uDFE0");
    }

    private ReplyKeyboardMarkup createKeyboardMarkup() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Kursga yozilish \uD83D\uDCDD");
        row1.add("Mening xaridlarim \uD83D\uDECD");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Aloqa \uD83D\uDCDE");
        row2.add("Sozlamalar ⚙\uFE0F");


        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(row1, row2));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;


    }
}
