package uz.giza.bot.service.command.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.CommandMapping;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.input.UserStates;

import java.util.List;

@Service
@CommandMapping(values = {CommandName.CONFIRM})
@Slf4j
@RequiredArgsConstructor
public class OfferConfirmationHandler implements CommandHandler {


    private final SendMessageService sendMessageService;
    private final UserService userService;

    @Override
    @Async
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        sendMessageService.sendMessageWithReplyKeyboard(chatId, createKeyboardMarkup(), "To'lov turini tanlang \uD83D\uDCC4");
        userService.updateUserState(chatId, UserStates.BACK_TO_COURSE_PURCHASE);
    }

    private ReplyKeyboardMarkup createKeyboardMarkup() {
        KeyboardRow row1 = new KeyboardRow();
//        row1.add(CommandName.CLICK_PAYMENT.getCommandName());
//        row1.add(CommandName.PAYME_PAYMENT.getCommandName());
        row1.add(CommandName.TRANSFER.getCommandName());

//        KeyboardRow row2 = new KeyboardRow();
//        row2.add(CommandName.UZUM_PAYMENT.getCommandName());

        KeyboardRow row3 = new KeyboardRow();
        row3.add(CommandName.BACK.getCommandName());
        row3.add(CommandName.MENU.getCommandName());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(row1, row3));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;


    }
}
