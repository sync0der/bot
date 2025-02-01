package uz.giza.bot.admin;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface AdminHandler {
    void execute(Update update);
}
