package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Methods annotated with the event listener annotations of Spring or Spring Modulith listen to the domain event they
 * take - as if annotated with {@code @ListensTo}.
 */
public class FrameworkEventListenerTest {

    private static final String EVENT = "tests.mirror.frameworklisteners.OrderPlaced";

    private static DomainTypeMirror service;

    @BeforeAll
    static void initializeMirror() {
        DomainMirror domainMirror = new ReflectiveDomainMirrorFactory("tests.mirror.frameworklisteners").initializeDomainMirror();
        service = domainMirror.getDomainTypeMirror("tests.mirror.frameworklisteners.OrderNotificationService").orElseThrow();
    }

    @Test
    void springEventListenerListens() {
        assertThat(listenedEvent("onEvent")).contains(EVENT);
    }

    @Test
    void springTransactionalEventListenerListens() {
        assertThat(listenedEvent("afterCommit")).contains(EVENT);
    }

    @Test
    void springModulithApplicationModuleListenerListens() {
        assertThat(listenedEvent("inModule")).contains(EVENT);
    }

    @Test
    void ownAnnotationComposedOfASpringListenerListens() {
        assertThat(listenedEvent("composed")).contains(EVENT);
    }

    @Test
    void eventNamedByTheAnnotationInsteadOfAParameterIsListenedTo() {
        assertThat(listenedEvent("eventNamedByAnnotation")).contains(EVENT);
    }

    @Test
    void listenerOfANonDomainEventListensToNoDomainEvent() {
        assertThat(listenedEvent("onText")).isEmpty();
    }

    @Test
    void methodWithoutListenerAnnotationListensToNothing() {
        assertThat(listenedEvent("notify")).isEmpty();
    }

    private static Optional<String> listenedEvent(String methodName) {
        MethodMirror method = service.getMethods().stream()
            .filter(m -> m.getName().equals(methodName))
            .findFirst()
            .orElseThrow();
        return method.getListenedEvent().map(DomainTypeMirror::getTypeName);
    }
}
