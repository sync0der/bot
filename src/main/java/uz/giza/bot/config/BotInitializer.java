package uz.giza.bot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.giza.bot.controller.TelegramBotController;

@Lazy
@Component
@Slf4j
@RequiredArgsConstructor
public class BotInitializer {
    private final TelegramBotController telegramBotController;

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        try {
            new TelegramBotsApi(DefaultBotSession.class).registerBot(telegramBotController);
            log.info("Telegram bot successfully registered!");
        } catch (TelegramApiException e) {
            log.error("Error during bot registration", e);

        }
    }
}
