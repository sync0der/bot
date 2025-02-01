package uz.giza.bot.service.command.handlers.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.CommandMapping;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.command.handlers.CommandHandler;
import uz.giza.bot.service.command.handlers.MainCommandHandler;

@Service
@RequiredArgsConstructor
@Slf4j
@CommandMapping(values = {CommandName.CLICK_PAYMENT})
public class ClickPaymentHandler implements CommandHandler {
    private final UserService userService;
    private final SendMessageService sendMessageService;
    private final PaymentHandler paymentHandler;
    private final MainCommandHandler mainCommandHandler;

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        sendMessageService.sendMessageWithReplyKeyboard(
                chatId,
                paymentHandler.createInlineKeyboard("https://t.me/+998996441708"),
                paymentHandler.getMessage(chatId));
        mainCommandHandler.execute(update);

    }


}
