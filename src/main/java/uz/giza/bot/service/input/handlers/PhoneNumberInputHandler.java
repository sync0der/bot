package uz.giza.bot.service.input.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.command.handlers.MainCommandHandler;
import uz.giza.bot.service.command.handlers.SettingsInputHandler;
import uz.giza.bot.service.input.UserStates;

import java.util.List;

@Component
@InputMapping(UserStates.WAITING_FOR_PHONE_NUMBER)
@RequiredArgsConstructor
@Slf4j
public class PhoneNumberInputHandler implements InputHandler {
    private final UserService userService;
    private final SendMessageService sendMessageService;
    private final SettingsInputHandler settingsInputHandler;
    private final MainCommandHandler mainCommandHandler;


    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        User user = userService.get(chatId);
        String phoneNumber = update.getMessage().hasContact() ?
                update.getMessage().getContact().getPhoneNumber() :
                update.getMessage().getText();
        if (update.getMessage().hasText() && update.getMessage().getText().equals(CommandName.BACK.getCommandName())) {
            settingsInputHandler.execute(update);
            userService.updateUserState(user, UserStates.DEFAULT);
        } else if (user.getPhoneNumber() != null && user.getPhoneNumber().equals(phoneNumber)) {
            sendMessageService.sendMessage(chatId, "Bunday raqam bilan ro'yxatdan o'tilgan, boshqa telefon raqam kiriting");
        } else if (user.getPhoneNumber() == null) {
            mainCommandHandler.execute(update);
            user.setPhoneNumber(phoneNumber);
            userService.updateUserState(user, UserStates.DEFAULT);
        } else {
            sendMessageService.sendMessage(chatId, "Sizning telefon raqamingiz muvaffaqiyatli o'zgartirildi! ♻\uFE0F");
            user.setPhoneNumber(phoneNumber);
            settingsInputHandler.execute(update);
            userService.updateUserState(user, UserStates.DEFAULT);
        }
    }


    public void sendPhoneNumberRequest(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow row = new KeyboardRow(List.of(
                KeyboardButton.builder()
                        .text("Telefon raqamini ulashish")
                        .requestContact(true)
                        .build()
        ));

        replyKeyboardMarkup.setKeyboard(List.of(row));
        sendMessageService.sendMessageWithReplyKeyboard(chatId, replyKeyboardMarkup, "Iltimos, telefon raqamingizni " +
                "ulashing\\!");
    }


}
