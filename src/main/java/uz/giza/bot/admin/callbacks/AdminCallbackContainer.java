package uz.giza.bot.admin.callbacks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCallbackContainer {
    private final AdminCallbackFactory adminCallbackFactory;
    private final ConfirmPaymentCallbackHandler confirmPaymentCallbackHandler;

    @Async
    public void handleCallback(Update update){
        String data = update.getCallbackQuery().getData();
        adminCallbackFactory.getCallbackHandler(data)
                .ifPresentOrElse(handler -> handler.execute(update),
                        () -> confirmPaymentCallbackHandler.execute(update));
    }
}
