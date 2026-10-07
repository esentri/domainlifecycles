package org.springframework.modulith.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Test stand-in for Spring Modulith's annotation of the same name - like the original, composed of
 * {@link TransactionalEventListener}.
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@TransactionalEventListener
public @interface ApplicationModuleListener {

    String id() default "";
}
