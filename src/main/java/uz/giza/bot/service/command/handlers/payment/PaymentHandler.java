package uz.giza.bot.service.command.handlers.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentHandler {

    private final UserService userService;

    public InlineKeyboardMarkup createInlineKeyboard(String url) {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        rowInLine.add(InlineKeyboardButton.builder()
                .text("To'lash")
                .url(url)
                .build()
        );
        rowsInLine.add(rowInLine);
        return new InlineKeyboardMarkup(rowsInLine);
    }

    public String getMessage(Long chatId) {
        User user = userService.get(chatId);
        return String.format("""
                <b>To'lov</b>
                
                To'lovchi haqida ma'lumotlar:
                - <b><i>%s</i></b>
                - <b><i>%s</i></b>
                
                Kurs nomi: <b><i>%s</i></b>
                
                Summa: <b><i>%d so'm</i></b>
                """, user.getFullName(), user.getPhoneNumber(), user.getTargetCourse().getName(),
                user.getTargetCourse().getPrice());
    }
}
