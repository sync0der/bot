package uz.giza.bot.service.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CallbackFactory {
    private final Map<String, CallbackHandler> callbackMap;

    public CallbackFactory(List<CallbackHandler> callbackHandlers) {
        this.callbackMap = callbackHandlers.stream()
                .collect(Collectors.toMap(
                        callbackHandler -> callbackHandler.getClass().getAnnotation(CallbackMapping.class).value().getCommandName(),
                        Function.identity()
                ));
    }

    public Optional<CallbackHandler> getCallbackHandler(String callbackName) {
        return Optional.ofNullable(callbackMap.get(callbackName));
    }
}
