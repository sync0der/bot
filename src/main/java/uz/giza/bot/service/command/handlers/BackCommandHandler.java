package uz.giza.bot.service.command.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.service.command.CommandMapping;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.input.InputContainer;
import uz.giza.bot.service.input.UserStates;

@Service
@CommandMapping(values = {CommandName.BACK})
@Slf4j
@RequiredArgsConstructor
public class BackCommandHandler implements CommandHandler {
    private final BackCommandContainer backCommandContainer;

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        UserStates userState = InputContainer.userStates.get(chatId);
        backCommandContainer.getBackCommandHandler(userState)
                .ifPresentOrElse(handler -> handler.execute(update),
                        () -> log.warn("Unknown userState: {}", userState));
    }
}
