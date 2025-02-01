package uz.giza.bot.service.input.handlers;

import uz.giza.bot.service.input.UserStates;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InputMapping {
    UserStates value();
}
