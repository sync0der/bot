package uz.giza.bot.service.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.handlers.MainCommandHandler;

@Slf4j
@Service
@CallbackMapping(value = CallbackName.PAYMENT_CARD)
@RequiredArgsConstructor
public class PaymentCardCallbackHandler implements CallbackHandler {
    private final SendMessageService sendMessageService;
    private final MainCommandHandler mainCommandHandler;
    private final UserService userService;

    @Override
    @Async
    public void execute(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
//        sendMessageService.deleteMessage(chatId, update.getCallbackQuery().getMessage().getMessageId());
        User user = userService.get(chatId);
        String message = String.format("""
                <b><code>8600140415670802</code> - <code>Mavlyanova Yulduzxon</code></b>
                
                <i>💳 Ushbu karta raqamiga kursning to'lov summasini to'lang va <b>to'lov chekini ushbu botga yuboring!</b>
                
                Moderator tomonidan to'lov chekingiz tasdiqlangandan so'ng, siz barcha darslar joylangan kanalga havolani olasiz.
                </i> \s
                
                To'lov summasi: <code><b>%s</b></code> so'm
                
                """, user.getTargetCourse().getPrice());
        sendMessageService.sendMessage(chatId, message);
        mainCommandHandler.execute(update);
    }
}
