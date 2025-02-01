package uz.giza.bot.service.command;

import org.springframework.stereotype.Component;
import uz.giza.bot.service.command.handlers.CommandHandler;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CommandFactory {
    private final Map<String, CommandHandler> commandMap;

    public CommandFactory(List<CommandHandler> handlers) {
        this.commandMap = handlers.stream()
                .flatMap(handler ->
                        Arrays.stream(handler.getClass().getAnnotation(CommandMapping.class).values())
                                .map(cmd ->
                                        new AbstractMap.SimpleEntry<>(cmd.getCommandName(), handler)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Optional<CommandHandler> getCommand(String commandName) {
        return Optional.ofNullable(commandMap.get(commandName));
    }
}


/*
 @PostConstruct
    public void init() {
        commandMap = Map.of(
                CommandName.START.getCommandName(), startCommandHandlerHandler,
                CommandName.MAIN.getCommandName(), mainCommandHandler,
                CommandName.SUPPORT.getCommandName(), supportInputHandler,
                CommandName.SETTINGS.getCommandName(), settingsInputHandler,
                CommandName.MENU.getCommandName(), mainCommandHandler,
                CommandName.CHANGE_NAME.getCommandName(), nameChangerCommandHandler,
                CommandName.CHANGE_PHONE_NUMBER.getCommandName(), phoneNumberChangerCommandHandler,
                CommandName.BACK_TO_SETTINGS.getCommandName(), settingsInputHandler
        );
    }
* */