package uz.giza.bot.service.input;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.UserService;
import uz.giza.bot.service.input.handlers.InputHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InputContainer {

    private final InputFactory inputFactory;
    private final UserService userService;
    public final static Map<Long, UserStates> userStates = new ConcurrentHashMap<>();

    public static void setUserState(Long chatId, UserStates state) {
        userStates.put(chatId, state);
    }

    public void handleInput(Update update) {
        Long chatId = update.getMessage().getChatId();
        UserStates userState = userStates.get(chatId);
        InputHandler inputHandler = inputFactory.getInputHandler(userState);
        inputHandler.execute(update);
    }

    public void initializeUserStates() {
        userStates.putAll(
                userService.getAll()
                        .stream()
                        .filter(user -> user.getChatId() != null && user.getUserState() != null)
                        .collect(Collectors.toMap(User::getChatId, User::getUserState))
        );
    }
}
