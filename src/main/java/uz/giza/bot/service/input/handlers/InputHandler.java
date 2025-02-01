package uz.giza.bot.service.input.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface InputHandler {
    void execute(Update update);
}
