package net.hydra.jojomod.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FloatOption {
    String group() default "default";

    float value();

    float min() default 0f;

    float max() default 100f;
}