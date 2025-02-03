package uz.giza.bot.admin.callbacks;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminCallbackName {
    DECLINE("Отклонить");
    private final String commandName;
}
