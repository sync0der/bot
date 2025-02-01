package uz.giza.bot.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.giza.bot.BotMessageSender;
import uz.giza.bot.admin.AdminContainer;
import uz.giza.bot.service.callback.CallbackContainer;
import uz.giza.bot.service.callback.CourseInfoCallbackHandler;
import uz.giza.bot.service.command.CommandContainer;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.command.handlers.StartCommandHandler;
import uz.giza.bot.service.input.InputContainer;
import uz.giza.bot.service.input.UserStates;

import java.util.Collections;

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

    @Value("${admin.chat_id}")
    private String adminChatId;
    public TelegramBotController(@Value("${bot.token}") String botToken,
                                 @Value("${bot.username}") String username,
                                 CommandContainer commandContainer,
                                 InputContainer inputContainer,
                                 CourseInfoCallbackHandler courseInfoCallbackHandler,
                                 CallbackContainer callbackContainer,
                                 AdminContainer adminContainer, StartCommandHandler startCommandHandler) {
        super(botToken);
        this.username = username;
        this.commandContainer = commandContainer;
        this.inputContainer = inputContainer;
        this.courseInfoCallbackHandler = courseInfoCallbackHandler;
        this.callbackContainer = callbackContainer;
        this.adminContainer = adminContainer;
        this.startCommandHandler = startCommandHandler;
    }

    @PostConstruct
    public void init() {
        inputContainer.initializeUserStates();
    }

    @Override
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
                }
            } else if (update.hasCallbackQuery()) {
                if (InputContainer.userStates.get(update.getCallbackQuery().getMessage().getChatId()).equals(UserStates.BACK_TO_COURSE_LIST))
                    callbackContainer.handleCallback(update);
                else
                    courseInfoCallbackHandler.execute(update);
            }
        }
    }

    private void processUpdate(Update update) {
        String message = update.getMessage().getText().trim();
        Long chatId = update.getMessage().getChatId();
        if (message.startsWith(CommandName.START.getCommandName()))
            startCommandHandler.execute(update);
        else {
            if (message.startsWith(COMMAND_PREFIX) || InputContainer.userStates.get(chatId) != UserStates.WAITING_FOR_NAME && InputContainer.userStates.get(chatId) != UserStates.WAITING_FOR_PHONE_NUMBER) {
//            if (message.startsWith(CommandName.START.getCommandName()))
//                startCommandHandlerHandler.execute(update);
//            else
                commandContainer.handleCommand(update);
            } else {
                inputContainer.handleInput(update);
            }
        }
    }

    private void processAdminUpdate(Update update) {
        adminContainer.handleCommand(update);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            super.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error sending message: {}", e.getMessage());
        }
    }

    @Override
    public void sendFile(SendDocument document) {
        try {
            super.execute(document);
        } catch (Exception e) {
            log.error("Error sending file: {}", e.getMessage());
        }
    }

    @Override
    public void editMessage(EditMessageText messageText) {
        try {
            super.execute(messageText);
        } catch (TelegramApiException e) {
            log.error("Error editing message: {}", e.getMessage());
        }
    }

    @Override
    public void sendMessage(ForwardMessage message) {
        try {
            super.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending forwarded message: {}", e.getMessage());
        }
    }

    @Override
    public void deleteMessage(DeleteMessage message) {
        try {
            super.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error deleting message: {}", e.getMessage());
        }
    }



}

