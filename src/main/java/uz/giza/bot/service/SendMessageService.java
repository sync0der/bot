package uz.giza.bot.service;

import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public interface SendMessageService {
    void sendMessage(Long chatId, String message);

    void sendMessageWithReplyKeyboard(Long chatId, ReplyKeyboard keyboard, String message);

    void sendFile(SendDocument sendDocument);

    void sendEditMessage(Long chatId, int messageId, String message);

    void sendEditMessageWithReplyKeyboard(Long chatId, int messageId, String message, InlineKeyboardMarkup keyboard);

    void sendForwardMessage(ForwardMessage forwardMessage);

    void deleteMessage(Long chatId, Integer messageId);

    void putMessageIdToDelete(Integer messageId);

}
