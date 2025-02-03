package uz.giza.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import uz.giza.bot.BotMessageSender;
import uz.giza.bot.service.callback.FileService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class SendMessageServiceImpl implements SendMessageService {
    private final BotMessageSender botMessageSender;
    private final FileService fileService;

    public SendMessageServiceImpl(@Lazy BotMessageSender botMessageSender,
                                  FileService fileService) {
        this.botMessageSender = botMessageSender;
        this.fileService = fileService;
    }


    @Override
    @Async
    public void sendMessage(Long chatId, String message) {
        sendMessageWithReplyKeyboard(chatId, null, message);
    }

    @Override
    @Async
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
    @Async
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
    @Async
    public void sendEditMessage(Long chatId, int messageId, String message) {
        sendEditMessageWithReplyKeyboard(chatId, messageId, message, null);
    }

    @Override
    @Async
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
    @Async
    public void sendForwardMessage(ForwardMessage forwardMessage) {
        botMessageSender.sendMessage(forwardMessage);
    }


    @Override
    @Async
    public void deleteMessage(Long chatId, Integer messageId) {
        DeleteMessage message = DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
        botMessageSender.deleteMessage(message);
    }

    @Override
    @Async
    public void deleteMessages(Long chatId, int[] messageIds) {
        for (int messageId : messageIds) {
            deleteMessage(chatId, messageId);
        }
    }

    @Override
    @Async
    public void sendFile(Long chatId, String fileKey, String fileName) {
        InputStream fileStream = new ByteArrayInputStream(fileService.getFileData(fileKey));
        SendDocument document = SendDocument.builder()
                .chatId(chatId)
                .document(new InputFile(fileStream, fileName))
                .build();
        sendFile(document);
    }

    @Override
    @Async
    public void sendPhotoWithCaption(Long chatId, String fileKey, String caption) {
        byte[] fileData = fileService.getFileData(fileKey);
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData)) {
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(new InputFile(inputStream, "photo.jpg"))
                    .caption(caption)
                    .parseMode("HTML")
                    .build();
            botMessageSender.sendPhoto(sendPhoto);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendPhotoWithCaption(SendPhoto sendPhoto) {
        botMessageSender.sendPhoto(sendPhoto);
    }

    @Override
    @Async
    public String createChatInviteLink(Long chatId) {
        CreateChatInviteLink link = CreateChatInviteLink.builder()
                .chatId(chatId)
                .name("link")
                .memberLimit(1)
                .build();

        return botMessageSender.createChatInviteLink(link);
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
