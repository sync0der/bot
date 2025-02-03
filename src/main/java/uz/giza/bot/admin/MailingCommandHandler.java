package uz.giza.bot.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.service.SendMessageService;

@Service
@RequiredArgsConstructor
@AdminMapping(values = {AdminCommands.MAILING})
public class MailingCommandHandler implements AdminHandler {

    private final SendMessageService sendMessageService;

    @Override
    public void execute(Update update) {
        sendMessageService.sendMessage(update.getMessage().getChatId(), "Отправьте сообщение, которое начинается на \"Рассылка: ...\"");
    }
}
