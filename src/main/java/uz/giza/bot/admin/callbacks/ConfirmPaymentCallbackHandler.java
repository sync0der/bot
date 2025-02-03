package uz.giza.bot.admin.callbacks;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.entity.User;
import uz.giza.bot.entity.UserPaymentStatus;
import uz.giza.bot.repository.UserRepository;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.handlers.Handler;

@Service
@RequiredArgsConstructor
public class ConfirmPaymentCallbackHandler implements Handler {
    private final UserService userService;
    private final SendMessageService sendMessageService;
    private final UserRepository userRepository;

    @Override
    @Async
    public void execute(Update update) {
        String data = update.getCallbackQuery().getData();
        if (data.startsWith("tick")) {
            String chatId = data.substring(5);
            User user = userService.get(Long.parseLong(chatId));
            user.setFullName(user.getFullName() + "  ОТМЕЧЕНО");
            userRepository.save(user);

        } else {
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            sendMessageService.deleteMessage(update.getCallbackQuery().getMessage().getChatId(), messageId);
            String link = sendMessageService.createChatInviteLink(-1002362049784L);

            String message = String.format("""
                    To‘lov uchun rahmat!🥰 
                    Mana sizning havolangiz:
                    
                    %s
                    """, link);

            sendMessageService.sendMessage(Long.parseLong(data), message);
            User user = userService.get(Long.parseLong(data));
            user.setStatus(UserPaymentStatus.CONFIRMED);
            userService.save(user);
        }
    }
}
