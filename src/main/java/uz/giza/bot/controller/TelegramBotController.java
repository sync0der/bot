package uz.giza.bot.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.giza.bot.BotMessageSender;
import uz.giza.bot.admin.AdminContainer;
import uz.giza.bot.admin.ProcessMailing;
import uz.giza.bot.admin.callbacks.AdminCallbackContainer;
import uz.giza.bot.service.callback.CallbackContainer;
import uz.giza.bot.service.callback.CourseInfoCallbackHandler;
import uz.giza.bot.service.command.CommandContainer;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.command.ProcessImage;
import uz.giza.bot.service.command.handlers.StartCommandHandler;
import uz.giza.bot.service.input.InputContainer;
import uz.giza.bot.service.input.UserStates;

import java.util.List;

@Component
@Slf4j
public class TelegramBotController extends TelegramLongPollingBot implements BotMessageSender {
    private final String username;
    public static String COMMAND_PREFIX = "/";
    private final CommandContainer commandContainer;
    private final InputContainer inputContainer;
    private final CourseInfoCallbackHandler courseInfoCallbackHandler;
    private final CallbackContainer callbackContainer;
    private final AdminContainer adminContainer;
    private final StartCommandHandler startCommandHandler;
    private final ProcessImage processImage;
    private final AdminCallbackContainer adminCallbackContainer;
    private final ProcessMailing processMailing;

    @Value("${admin.chat_id}")
    private String adminChatId;

    public TelegramBotController(@Value("${bot.token}") String botToken,
                                 @Value("${bot.username}") String username,
                                 CommandContainer commandContainer,
                                 InputContainer inputContainer,
                                 CourseInfoCallbackHandler courseInfoCallbackHandler,
                                 CallbackContainer callbackContainer,
                                 AdminContainer adminContainer,
                                 StartCommandHandler startCommandHandler,
                                 ProcessImage processImage, AdminCallbackContainer adminCallbackContainer, ProcessMailing processMailing) {
        super(botToken);
        this.username = username;
        this.commandContainer = commandContainer;
        this.inputContainer = inputContainer;
        this.courseInfoCallbackHandler = courseInfoCallbackHandler;
        this.callbackContainer = callbackContainer;
        this.adminContainer = adminContainer;
        this.startCommandHandler = startCommandHandler;
        this.processImage = processImage;
        this.adminCallbackContainer = adminCallbackContainer;
        this.processMailing = processMailing;
    }

    @PostConstruct
    public void init() {
        inputContainer.initializeUserStates();
    }

    @Override
    @Async("telegramExecutor")
    public void onUpdateReceived(Update update) {
        Long chatId = update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();

        if (String.valueOf(chatId).equals(adminChatId)) {
            processAdminUpdate(update);
        } else {
            if (update.hasMessage()) {
                if (update.getMessage().hasText()) {
                    processUpdate(update);
                } else if (update.getMessage().hasContact()) {
                    inputContainer.handleInput(update);
                } else if (update.getMessage().hasPhoto()) {
                    processImages(update);
                }
            } else if (update.hasCallbackQuery()) {
                if (InputContainer.userStates.get(update.getCallbackQuery().getMessage().getChatId()).equals(UserStates.BACK_TO_COURSE_LIST)
                        || InputContainer.userStates.get(update.getCallbackQuery().getMessage().getChatId()).equals(UserStates.BACK_TO_COURSE_PURCHASE))
                    callbackContainer.handleCallback(update);
                else
                    courseInfoCallbackHandler.execute(update);
            }
        }
    }

    @Async
    protected void processUpdate(Update update) {
        String message = update.getMessage().getText().trim();
        Long chatId = update.getMessage().getChatId();
        if (message.startsWith(CommandName.START.getCommandName()))
            startCommandHandler.execute(update);
        else {
            if (message.startsWith(COMMAND_PREFIX) || InputContainer.userStates.get(chatId) != UserStates.WAITING_FOR_NAME && InputContainer.userStates.get(chatId) != UserStates.WAITING_FOR_PHONE_NUMBER) {
                commandContainer.handleCommand(update);
            } else {
                inputContainer.handleInput(update);
            }
        }
    }


    @Async
    protected void processImages(Update update) {
        Message message = update.getMessage();
        // Получаем список всех фото, отправленных пользователем
        List<PhotoSize> photos = message.getPhoto();
        // Выбираем максимальное фото (обычно последнее в списке)
        PhotoSize photo = photos.getLast();

        // Получаем ID файла фото
        String fileId = photo.getFileId();


        // Загружаем фото с помощью fileId
        GetFile getFile = new GetFile(fileId);
        File file = downloadFile(getFile);
        String filePath = file.getFilePath();
        // Загружаем фото по URL
        String photoUrl = "https://api.telegram.org/file/bot" + "7849932150:AAFu0PFxUvV5Lyiaibya9OERwVxFi3YTqSI" + "/" + filePath;
        // Теперь вы можете использовать URL для загрузки изображения
        processImage.processImages(update, photoUrl);
    }


    @Async
    protected void processAdminUpdate(Update update) {
        if (update.hasCallbackQuery())
            adminCallbackContainer.handleCallback(update);
        else if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            if (message.getText().startsWith("Рассылка:")) {
                processMailing.execute(update);
            } else
                adminContainer.handleCommand(update);
        }
    }

        @Override
        public String getBotUsername () {
            return username;
        }

        @Override
        @Async
        public void sendMessage (SendMessage sendMessage){
            try {
                super.execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error("Error sending message: {}", e.getMessage());
            }
        }

        @Override
        @Async
        public void sendFile (SendDocument document){
            try {
                super.execute(document);
            } catch (Exception e) {
                log.error("Error sending file: {}", e.getMessage());
            }
        }

        @Override
        @Async
        public void editMessage (EditMessageText messageText){
            try {
                super.execute(messageText);
            } catch (TelegramApiException e) {
                log.error("Error editing message: {}", e.getMessage());
            }
        }

        @Override
        @Async
        public void sendMessage (ForwardMessage message){
            try {
                super.execute(message);
            } catch (TelegramApiException e) {
                log.error("Error sending forwarded message: {}", e.getMessage());
            }
        }

        @Override
        @Async
        public void deleteMessage (DeleteMessage message){
            try {
                super.execute(message);
            } catch (TelegramApiException e) {
                log.error("Error deleting message: {}", e.getMessage());
            }
        }

        @Override
        @Async
        public void sendPhoto (SendPhoto sendPhoto){
            try {
                super.execute(sendPhoto);
            } catch (TelegramApiException e) {
                log.error("Error sending photo: {}", e.getMessage());
            }
        }

        @Override
        @Async
        public String createChatInviteLink (CreateChatInviteLink link){
            try {
                return super.execute(link).getInviteLink();
            } catch (TelegramApiException e) {
                log.error("Error creating link: {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }

        @Override
        public File downloadFile (GetFile getFile){
            try {
                return super.execute(getFile);
            } catch (TelegramApiException e) {
                log.error("Error downloading file: {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }


    }

