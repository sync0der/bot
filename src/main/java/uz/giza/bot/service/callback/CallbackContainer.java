package uz.giza.bot.service.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackContainer {
    private final CallbackFactory callbackFactory;
    private final CourseInfoCallbackHandler courseInfoCallbackHandler;

    @Async
    public void handleCallback(Update update){
        String data = update.getCallbackQuery().getData();
        callbackFactory.getCallbackHandler(data)
                .ifPresentOrElse(handler -> handler.execute(update),
                        () -> courseInfoCallbackHandler.execute(update));
    }
}
