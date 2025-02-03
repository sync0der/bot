package uz.giza.bot.service.callback;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CallbackName {
    INFO("course"),
    PAYMENT_CARD("To'lash"),
    BUY_COURSE("buy");
    private final String commandName;
}
