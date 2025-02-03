package uz.giza.bot.admin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminCommands {
    START("/start"),
    MAIN("/main"),
    MENU("Главное меню"),
    MAILING("Рассылка"),
    COURSES("Курсы"),
    USERS("Пользователи"),
    ALL_USERS("Все пользователи"),
    SORT_USERS("Сортировать"),
    SORT_USERS_BY_UTM("Сортировать по UTM"),
    SORT_USERS_BY_COURSE("Сортировать по курсам"),
    ALL_PAYMENTS("Оплатившие"),
    PROCESS_PHONE_NUMBER("Обработать номера"),
    SORT_USERS_BY_PURCHASE("Сортировать по опталам");
    private final String commandName;
}

