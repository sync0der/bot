package uz.giza.bot.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.service.UserService;

@Service
@RequiredArgsConstructor
public class ProcessImage {
    private final UserService userService;

    @Async
    public void processImages(Update update, String photoUrl) {
        Message message = update.getMessage();
        userService.addPhotoUrl(message.getChatId(), photoUrl);
    }
}
