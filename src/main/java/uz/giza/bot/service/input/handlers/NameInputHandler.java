package uz.giza.bot.service.input.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.command.handlers.SettingsInputHandler;
import uz.giza.bot.service.input.UserStates;

@Component
@InputMapping(UserStates.WAITING_FOR_NAME)
@RequiredArgsConstructor
@Slf4j
public class NameInputHandler implements InputHandler {

    private final SendMessageService sendMessageService;
    private final UserService userService;
    private final SettingsInputHandler settingsInputHandler;
    private final PhoneNumberInputHandler phoneNumberInputHandler;

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String input = update.getMessage().getText();
        User user = userService.get(chatId);

        if (input.equals(CommandName.BACK.getCommandName())) {
            settingsInputHandler.execute(update);
            userService.updateUserState(user, UserStates.DEFAULT);
        } else {
            if (input.split(" ").length < 2) {
                sendMessageService.sendMessage(chatId, "Iltimos ism-sharifingizni bo'sh joy bilan ajaratib yozing!");
            } else {
                if (user.getFullName() != null) {
                    sendMessageService.sendMessage(chatId, "Sizning ismingiz muvaffaqiyatli o'zgartirildi!");
                    settingsInputHandler.execute(update);
                    user.setFullName(input);
                    userService.updateUserState(user, UserStates.DEFAULT);
                } else {
                    phoneNumberInputHandler.sendPhoneNumberRequest(chatId);
                    user.setFullName(input);
                    userService.updateUserState(user, UserStates.WAITING_FOR_PHONE_NUMBER);
                }
            }
        }
    }
}
