package uz.giza.bot.service.command.handlers.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.entity.User;
import uz.giza.bot.payme.PaymeService;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.CommandMapping;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.command.handlers.CommandHandler;
import uz.giza.bot.service.command.handlers.MainCommandHandler;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@CommandMapping(values = {CommandName.PAYME_PAYMENT})
public class PaymePaymentHandler implements CommandHandler {
    private final UserService userService;
    private final SendMessageService sendMessageService;
    private final PaymentHandler paymentHandler;
    private final MainCommandHandler mainCommandHandler;
    private final PaymeService paymeService;

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String orderId = UUID.randomUUID().toString();
        double amount = 5000.0; //TODO: 50 000 so‘m
        String paymentUrl = paymeService.getPaymentUrl("6145252615", amount);
        sendMessageService.sendMessageWithReplyKeyboard(
                chatId,
                paymentHandler.createInlineKeyboard(paymentUrl),
                paymentHandler.getMessage(chatId));
        mainCommandHandler.execute(update);
        User user = userService.get(chatId);
        user.getPaidCourses().add(user.getTargetCourse());
        userService.save(user);

    }

}
