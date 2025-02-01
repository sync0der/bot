package uz.giza.bot.service.callback;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackHandler {
    void execute(Update update);
}
