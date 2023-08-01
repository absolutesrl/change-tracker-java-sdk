package it.changetracker.sdk.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Tracker {
    public String name() default "";
    public String mapping() default "";
    public boolean ignore() default false;
    public String format() default "";
}
