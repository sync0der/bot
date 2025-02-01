package uz.giza.bot.service.command.handlers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uz.giza.bot.service.callback.CourseInfoCallbackHandler;
import uz.giza.bot.service.callback.CoursePurchaseCallbackHandler;
import uz.giza.bot.service.input.UserStates;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class BackCommandContainer {
    private final SettingsInputHandler settingsInputHandler;
    private final CourseCommandHandler courseCommandHandler;
    private final CoursePurchaseCallbackHandler coursePurchaseCallbackHandler;
    private Map<UserStates, Handler> handlerMap;
    private final MainCommandHandler mainCommandHandler;

    @PostConstruct
    public void init() {
        this.handlerMap = Map.of(
                UserStates.BACK_TO_MAIN, mainCommandHandler,
                UserStates.BACK_TO_SETTINGS, settingsInputHandler,
                UserStates.BACK_TO_COURSE_LIST, courseCommandHandler,
                UserStates.BACK_TO_COURSE_PURCHASE, coursePurchaseCallbackHandler
        );
    }

    public Optional<Handler> getBackCommandHandler(UserStates userState) {
        return Optional.ofNullable(handlerMap.get(userState));
    }


}
