package uz.giza.bot.service.command.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.command.CommandMapping;
import uz.giza.bot.service.command.CommandName;

import java.util.ArrayList;
import java.util.List;

@Service
@CommandMapping(values = {CommandName.SUPPORT})
@RequiredArgsConstructor
public class SupportInputHandler implements CommandHandler {
    private final SendMessageService sendMessageService;


    @Override
    @Async
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();

        String message = String.format("""
                <b>Menejer:</b> <i>%s</i>
                
                <b>Telefon raqami:</b> <i>%s</i>
                """, "Shamsiddin", "+998999810278");
        sendMessageService.sendMessageWithReplyKeyboard(chatId, createKeyboardMarkup(), message);
    }


    public InlineKeyboardMarkup createKeyboardMarkup() {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        rowInLine.add(InlineKeyboardButton.builder()
                .text("Telegram orqali bog'lanish")
                .url("https://t.me/+998999810278")
                .build()
        );
        rowsInLine.add(rowInLine);
        return new InlineKeyboardMarkup(rowsInLine);
    }

}
