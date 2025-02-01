package uz.giza.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import uz.giza.bot.BotMessageSender;

import java.util.ArrayList;
import java.util.List;

@Service
//@DependsOn("botInitializer")
@Slf4j
public class SendMessageServiceImpl implements SendMessageService {
    private final BotMessageSender botMessageSender;
    public static List<Integer> messagesIdToDelete = new ArrayList<>();
    private final CourseService courseService;


    public SendMessageServiceImpl(@Lazy BotMessageSender botMessageSender, CourseService courseService) {
        this.botMessageSender = botMessageSender;
        this.courseService = courseService;
    }


    @Override
    public void sendMessage(Long chatId, String message) {
        sendMessageWithReplyKeyboard(chatId, null, message);
    }

    @Override
    public void sendMessageWithReplyKeyboard(Long chatId, ReplyKeyboard keyboard, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .replyMarkup(keyboard)
                .parseMode("HTML")
                .build();

        botMessageSender.sendMessage(sendMessage);
    }

    @Override
    public void sendFile(SendDocument sendDocument) {
        botMessageSender.sendFile(sendDocument);
//        try {
//            File file = ResourceUtils.getFile("classpath:static/docs/offer.pdf");
//
//            if (!file.exists() || !file.isFile()) {
//                log.error("File not found or is not a valid file: " + file.getAbsolutePath());
//                return;
//            }
//
//            SendDocument document = SendDocument.builder()
//                    .chatId(chatId)
//                    .caption(caption)
//                    .document(new InputFile(file, file.getName()))
//                    .build();
//
//            telegramBotController.execute(document);
//        } catch (FileNotFoundException e) {
//            log.error("File not found: ", e);
//        } catch (Exception e) {
//            log.error("Error sending file: ", e);
//        }
    }

    @Override
    public void sendEditMessage(Long chatId, int messageId, String message) {
        sendEditMessageWithReplyKeyboard(chatId, messageId, message, null);
    }

    @Override
    public void sendEditMessageWithReplyKeyboard(Long chatId, int messageId, String message, InlineKeyboardMarkup keyboard) {
        EditMessageText messageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(message)
                .replyMarkup(keyboard)
                .build();
        botMessageSender.editMessage(messageText);
    }

    @Override
    public void sendForwardMessage(ForwardMessage forwardMessage) {
        botMessageSender.sendMessage(forwardMessage);
    }


    @Override
    public void deleteMessage(Long chatId, Integer messageId) {
        DeleteMessage message = DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messagesIdToDelete.getFirst())
                .build();
        botMessageSender.deleteMessage(message);
        messagesIdToDelete.removeFirst();
    }

    @Override
    public void putMessageIdToDelete(Integer messageId) {
        messagesIdToDelete.add(messageId);
    }

}


//        ClassPathResource classPathResource = new ClassPathResource("static/docs/offer.pdf");
//
//        try (InputStream inputStream = classPathResource.getInputStream()) {
//            SendDocument document = SendDocument.builder()
//                    .chatId(chatId)
//                    .document(new InputFile(inputStream, "static/docs/offer.pdf"))
//                    .caption("Quyidagi oferta bilan tanishib chiqing va tasdiqlash tugmasini bosing.")
//                    .build();
//            telegramBotController.execute(document);
//        } catch (IOException | TelegramApiException e) {
//            log.warn("failed to send doc!");
//        }
