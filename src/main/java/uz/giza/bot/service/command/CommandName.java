package uz.giza.bot.service.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandName {
    START("/start"),
    MAIN("/main"),
    SUPPORT("Aloqa \uD83D\uDCDE"),
    SETTINGS("Sozlamalar ⚙\uFE0F"),
    MENU("Asosiy menyu \uD83C\uDFE0"),
    CHANGE_NAME("Ismni o'zgartirish 👤"),
    CHANGE_PHONE_NUMBER("Telefon raqamni o'zgartirish 📞"),
    BACK("\uD83D\uDD19 Ortga"),
    BUY_COURSE("Kursga yozilish \uD83D\uDCDD"),
    CONFIRM("Tasdiqlayman"),
    CLICK_PAYMENT("Click"),
    PAYME_PAYMENT("Payme"),
    UZUM_PAYMENT("Uzum");
    private final String commandName;
}
