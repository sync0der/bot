package uz.giza.bot.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessMailing {

    private final UserService userService;
    private final SendMessageService sendMessageService;

    public void execute(Update update) {
        String text = update.getMessage().getText().substring(10);
        List<User> users = userService.getAll();
        int count = 0;
        for (User user : users) {
            sendMessageService.sendMessage(user.getChatId(), text);
            count++;
        }
        sendMessageService.sendMessage(update.getMessage().getChatId(), "Рассылка завершена.\nОтправлено: " + count + " пользователям");
    }
}
