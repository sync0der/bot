package uz.giza.bot;

import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface BotMessageSender {
    void sendMessage(SendMessage sendMessage);

    void sendFile(SendDocument sendDocument);

    void editMessage(EditMessageText messageText);

    void sendMessage(ForwardMessage message);

    void deleteMessage(DeleteMessage message);
}
