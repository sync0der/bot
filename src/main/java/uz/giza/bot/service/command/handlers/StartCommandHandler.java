package uz.giza.bot.service.command.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.CommandMapping;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.input.UserStates;
import uz.giza.bot.service.input.handlers.PhoneNumberInputHandler;

@Service
@CommandMapping(values = {CommandName.START})
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandler {
    private final SendMessageService sendMessageService;
    private final UserService userService;
    private final MainCommandHandler mainCommandHandler;
    private final PhoneNumberInputHandler phoneNumberInputHandler;

    @Override
    @Async
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String utm = null;
        String text = update.getMessage().getText();
        if (text.length() > 6)
            utm = text.substring(7);
        if (userService.userExists(chatId)) {
            mainCommandHandler.execute(update);
        } else {
            sendMessageService.sendMessage(chatId, """
                    Assalomu alaykum, hurmatli foydalanuvchi! 😊
                    Yulduz Mavlyanovaning botiga xush kelibsiz!
                    """);
            phoneNumberInputHandler.sendPhoneNumberRequest(chatId);
            String fullName = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();
            String userName = update.getMessage().getFrom().getUserName();
            userService.save(chatId, userName, fullName, UserStates.WAITING_FOR_PHONE_NUMBER, utm);
//            userService.save(chatId, fullName, UserStates.WAITING_FOR_PHONE_NUMBER);
        }
    }
}
