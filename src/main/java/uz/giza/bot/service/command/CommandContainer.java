package uz.giza.bot.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommandContainer {
    private final CommandFactory commandFactory;

    public void handleCommand(Update update) {
        String commandIdentifier = update.getMessage().getText();
        commandFactory.getCommand(commandIdentifier)
                .ifPresentOrElse(handler -> handler.execute(update),
                        () -> log.warn("Unknown command: {}", commandIdentifier));
    }
}
