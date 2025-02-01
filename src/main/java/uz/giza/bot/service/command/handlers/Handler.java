package uz.giza.bot.service.command.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {
    void execute(Update update);
}
