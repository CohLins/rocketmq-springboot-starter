package cn.colins.rocketmqstarter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = ElementType.FIELD)
@Retention(RUNTIME)
public @interface RocketResource {
    String producerGroup() default "DEFAULT";
}
