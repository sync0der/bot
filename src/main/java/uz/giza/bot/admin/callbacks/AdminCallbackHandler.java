package uz.giza.bot.admin.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface AdminCallbackHandler {
    void execute(Update update);
}
