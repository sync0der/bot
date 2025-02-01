package uz.giza.bot.service.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.command.CommandName;
import uz.giza.bot.service.command.handlers.Handler;
import uz.giza.bot.service.input.UserStates;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;


@Service
@CallbackMapping(value = CallbackName.BUY_COURSE)
@Slf4j
@RequiredArgsConstructor
public class CoursePurchaseCallbackHandler implements CallbackHandler, Handler {
    private final SendMessageService sendMessageService;
    private final FileService fileService;
    private final UserService userService;


    @Override
    public void execute(Update update) {

        Long chatId = update.hasCallbackQuery()
                ? update.getCallbackQuery().getMessage().getChatId()
                : update.getMessage().getChatId();
        sendDocument(chatId);
        sendMessageService.sendMessageWithReplyKeyboard(chatId, createReplyKeyboardMarkup(), "Quyidagi oferta bilan tanishib chiqing va tasdiqlash tugmasini bosing.");
        userService.updateUserState(chatId, UserStates.BACK_TO_COURSE_LIST);

    }


    private ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(CommandName.CONFIRM.getCommandName());

        KeyboardRow row2 = new KeyboardRow();
        row2.add(CommandName.BACK.getCommandName());
        row2.add(CommandName.MENU.getCommandName());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(row1, row2));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }


    public void sendDocument(Long chatId) {
        InputStream fileStream = new ByteArrayInputStream(fileService.getFileData());
        SendDocument document = SendDocument.builder()
                .chatId(chatId)
                .document(new InputFile(fileStream, "offer.pdf"))
                .build();
        sendMessageService.sendFile(document);
    }


}


//


/*
*   long start = System.currentTimeMillis();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        ForwardMessage build = ForwardMessage.builder()
                .chatId(chatId)
                .fromChatId("-1002491249504")
                .messageId(3).build();
        sendMessageService.sendForwardMessage(build);
        long end = System.currentTimeMillis();
        System.out.println(end - start);*/

/*
*    try {
            long start = System.currentTimeMillis();
            File file = ResourceUtils.getFile("classpath:static/docs/offer.pdf");
            SendDocument document = SendDocument.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .caption("Quyidagi oferta bilan tanishib chiqing va tasdiqlash tugmasini bosing.")
                    .document(new InputFile(file, "offer.pdf"))
                    .build();
            sendMessageService.sendFile(document);
            long end = System.currentTimeMillis();
            log.warn(String.valueOf(end - start));
        } catch (Exception e) {
            log.error("Error sending file: {}", e.getMessage());
        }
* */


/*
*  try {
            long start = System.currentTimeMillis();
            File file = fileService.getFile("offer.pdf");
            SendDocument document = SendDocument.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .caption("Quyidagi oferta bilan tanishib chiqing va tasdiqlash tugmasini bosing.")
                    .document(new InputFile(file, "offer.pdf"))
                    .build();
            sendMessageService.sendFile(document);
            long end = System.currentTimeMillis();
            log.warn(String.valueOf(end - start));
        } catch (Exception e) {
            log.error("Error sending file: {}", e.getMessage());
        }
* */