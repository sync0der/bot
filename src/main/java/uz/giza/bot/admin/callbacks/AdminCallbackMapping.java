package uz.giza.bot.admin.callbacks;

import uz.giza.bot.service.callback.CallbackName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminCallbackMapping {
    AdminCallbackName value();
}
