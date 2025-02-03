package uz.giza.bot.admin.callbacks;

import org.springframework.stereotype.Component;
import uz.giza.bot.service.callback.CallbackHandler;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AdminCallbackFactory {
    private final Map<String, AdminCallbackHandler> callbackMap;

    public AdminCallbackFactory(List<AdminCallbackHandler> callbackHandlers) {
        this.callbackMap = callbackHandlers.stream()
                .collect(Collectors.toMap(
                        callbackHandler -> callbackHandler.getClass().getAnnotation(AdminCallbackMapping.class).value().getCommandName(),
                        Function.identity()
                ));
    }

    public Optional<AdminCallbackHandler> getCallbackHandler(String callbackName) {
        return Optional.ofNullable(callbackMap.get(callbackName));
    }
}
