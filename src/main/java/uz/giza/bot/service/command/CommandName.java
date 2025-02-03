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
    MENU("\uD83C\uDFE0 Asosiy menyu"),
    CHANGE_NAME("\uD83D\uDC64 Ismni o'zgartirish"),
    CHANGE_PHONE_NUMBER("\uD83D\uDCDE Telefon raqamini o'zgartirish"),
    BACK("\uD83D\uDD19 Ortga"),
    BUY_COURSE("\uD83D\uDCDD Kursga yozilish"),
    CONFIRM("Tasdiqlayman"),
    CLICK_PAYMENT("Click"),
    PAYME_PAYMENT("Payme"),
    TRANSFER("Kartaga o'tkazma"),
    UZUM_PAYMENT("Uzum"),
    PURCHASE("Kursga yozilish");

    private final String commandName;
}
