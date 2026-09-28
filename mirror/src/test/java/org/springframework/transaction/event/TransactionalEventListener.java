package org.springframework.transaction.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.event.EventListener;

/**
 * Test stand-in for Spring's annotation of the same name - like the original, composed of {@link EventListener}.
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EventListener
public @interface TransactionalEventListener {

    Class<?>[] value() default {};

    Class<?>[] classes() default {};
}
