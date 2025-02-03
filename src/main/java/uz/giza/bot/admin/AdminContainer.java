package uz.giza.bot.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminContainer {
    private final AdminCommandFactory adminCommandFactory;


    @Async
    public void handleCommand(Update update){
        String commandIdentifier = update.getMessage().getText();
        adminCommandFactory.getCommand(commandIdentifier)
                .ifPresentOrElse(handler -> handler.execute(update),
                        () -> log.warn("Unknown command: {}", commandIdentifier));
    }
}
