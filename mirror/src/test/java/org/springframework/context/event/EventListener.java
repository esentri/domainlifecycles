package org.springframework.context.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test stand-in for Spring's annotation of the same name: DLC recognizes framework event listeners by the name of their
 * annotation, without depending on Spring.
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {

    Class<?>[] value() default {};

    Class<?>[] classes() default {};
}
