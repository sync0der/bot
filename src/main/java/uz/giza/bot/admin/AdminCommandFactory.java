package uz.giza.bot.admin;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AdminCommandFactory {
    private final Map<String, AdminHandler> commandMap;

    public AdminCommandFactory(List<AdminHandler> handlers) {
        this.commandMap = handlers.stream()
                .flatMap(handler ->
                        Arrays.stream(handler.getClass().getAnnotation(AdminMapping.class).values())
                                .map(cmd ->
                                        new AbstractMap.SimpleEntry<>(cmd.getCommandName(), handler)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Optional<AdminHandler> getCommand(String commandName) {
        return Optional.ofNullable(commandMap.get(commandName));
    }
}
