package uz.giza.bot.service.input;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uz.giza.bot.service.input.handlers.InputHandler;
import uz.giza.bot.service.input.handlers.InputMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InputFactory {
    private final Map<UserStates, InputHandler> inputMap;

    public InputFactory(List<InputHandler> inputHandlers) {
        this.inputMap = inputHandlers.stream()
                .collect(Collectors.toMap(
                        inputHandler -> inputHandler.getClass().getAnnotation(InputMapping.class).value(),
                        Function.identity()
                ));
    }

    public InputHandler getInputHandler(UserStates userState) {
        return Optional.ofNullable(inputMap.get(userState))
                .orElseThrow(() -> new IllegalArgumentException("Unknown user state"));
    }
}
