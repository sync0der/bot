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
@CommandMapping(values = {CommandName.MAIN, CommandName.MENU})
@RequiredArgsConstructor
public class MainCommandHandler implements CommandHandler {
    private final SendMessageService sendMessageService;

    @Override
    @Async
    public void execute(Update update) {
        ReplyKeyboardMarkup keyboardMarkup = createKeyboardMarkup();
        Long chatId = update.hasCallbackQuery()
                ? update.getCallbackQuery().getMessage().getChatId()
                : update.getMessage().getChatId();

        sendMessageService.sendMessageWithReplyKeyboard(chatId, keyboardMarkup, CommandName.MENU.getCommandName());
    }

    private ReplyKeyboardMarkup createKeyboardMarkup() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(CommandName.BUY_COURSE.getCommandName());

        KeyboardRow row2 = new KeyboardRow();
        row2.add(CommandName.SUPPORT.getCommandName());
        row2.add(CommandName.SETTINGS.getCommandName());


        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(row1, row2));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;


    }
}
