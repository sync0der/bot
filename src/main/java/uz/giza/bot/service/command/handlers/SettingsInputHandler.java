package uz.giza.bot.service.command.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.command.CommandMapping;
import uz.giza.bot.service.command.CommandName;

import java.util.List;

@Service
@CommandMapping(values = {CommandName.SETTINGS})
@RequiredArgsConstructor
public class SettingsInputHandler implements CommandHandler {
    private final SendMessageService sendMessageService;


    @Override
    @Async
    public void execute(Update update) {
        ReplyKeyboardMarkup keyboardMarkup = createKeyboardMarkup();
        sendMessageService.sendMessageWithReplyKeyboard(update.getMessage().getChatId(), keyboardMarkup, CommandName.SETTINGS.getCommandName());
    }


    private ReplyKeyboardMarkup createKeyboardMarkup() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(CommandName.CHANGE_NAME.getCommandName());

        KeyboardRow row2 = new KeyboardRow();
        row2.add(CommandName.CHANGE_PHONE_NUMBER.getCommandName());

        KeyboardRow row3 = new KeyboardRow();
        row3.add(CommandName.MENU.getCommandName());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(row1, row2, row3));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}
