package uz.giza.bot.service.command.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler extends Handler {
    @Override
    void execute(Update update);
}
