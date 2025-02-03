package uz.giza.bot.admin.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.admin.AdminCommands;
import uz.giza.bot.admin.AdminHandler;
import uz.giza.bot.admin.AdminMapping;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.SendMessageService;
import uz.giza.bot.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@AdminMapping(values = {AdminCommands.SORT_USERS_BY_UTM})
public class ExportUsersByUtmHandler implements AdminHandler {
    private final UserService userService;
    private final UserReportHandler userReportHandler;

    @Value("${utm.tags}")
    private final List<String> utms;
    private final SendMessageService sendMessageService;

    @Override
    @Async
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        for (String utm : utms) {
            List<User> allByUtm = userService.getAllByUtm(utm);
            if (allByUtm.isEmpty())
                sendMessageService.sendMessage(chatId, "Пользователей по utm: " + utm + " не найдено!");
            else {
                byte[] data = userReportHandler.generateUserExcelReport(allByUtm, "users_by_utm_" + utm);
                userReportHandler.sendUserReport(chatId, "users_by_utm_" + utm, "Пользователи по utm: " + utm, data);
            }
        }

    }
}
