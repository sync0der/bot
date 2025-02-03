package uz.giza.bot.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.input.UserStates;
import uz.giza.bot.service.input.handlers.PhoneNumberInputHandler;

import java.util.List;


@Service
@RequiredArgsConstructor
@AdminMapping(values = {AdminCommands.PROCESS_PHONE_NUMBER})
public class ProcessPhoneNumber implements AdminHandler {
    private final UserService userService;
    private final SendMessageService sendMessageService;
    private final PhoneNumberInputHandler phoneNumberInputHandler;

    @Override
    public void execute(Update update) {
        List<User> users = userService.getAllWithUndefinedPhoneNumber();
        for (User user : users) {
            phoneNumberInputHandler.sendPhoneNumberRequest(user.getChatId());
            sendMessageService.sendMessage(user.getChatId(), "Pastdagi <b>Telefon raqamini ulashish</b> tugmasi orqali telefon raqamingizni yuborishingiz mumkin!");
            userService.updateUserState(user.getChatId(), UserStates.WAITING_FOR_PHONE_NUMBER);
        }
    }
}
