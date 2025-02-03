package uz.giza.bot.admin.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.admin.AdminCommands;
import uz.giza.bot.admin.AdminHandler;
import uz.giza.bot.admin.AdminMapping;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@AdminMapping(values = {AdminCommands.ALL_USERS})
public class ExportUsersHandler implements AdminHandler {
    private final UserReportHandler userReportHandler;
    private final UserService userService;

    @Override
    @Async
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        List<User> users = userService.getAll();
        byte[] data = userReportHandler.generateUserExcelReport(users, "users");
        userReportHandler.sendUserReport(chatId, "users", "Foydalanuvchilar ro'yhati", data);
    }


}
