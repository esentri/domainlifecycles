package io.domainlifecycles.mirror;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.FactoryMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.reflect.DomainServiceMirrorBuilder;
import io.domainlifecycles.mirror.reflect.FactoryMirrorBuilder;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import tests.mirror.factory.Calendar;
import tests.mirror.factory.CalendarCreatingService;
import tests.mirror.factory.CalendarFactory;
import tests.mirror.factory.CalendarFactoryWithFurtherResponsibilities;
import tests.mirror.factory.CalendarService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FactoryMirrorTest {

    private static final ListAppender<ILoggingEvent> LOG = new ListAppender<>();

    private static DomainMirror domainMirror;

    @BeforeAll
    static void init() {
        LOG.start();
        logger(FactoryMirrorBuilder.class).addAppender(LOG);
        logger(DomainServiceMirrorBuilder.class).addAppender(LOG);
        domainMirror = new ReflectiveDomainMirrorFactory("tests.mirror.factory").initializeDomainMirror();
    }

    @AfterAll
    static void detachLog() {
        logger(FactoryMirrorBuilder.class).detachAppender(LOG);
        logger(DomainServiceMirrorBuilder.class).detachAppender(LOG);
    }

    @Test
    void Should_MirrorAFactory_When_TheClassImplementsFactory() {
        var factory = mirror(CalendarFactory.class);

        assertThat(factory.getDomainType()).isEqualTo(DomainType.FACTORY);
        assertThat(factory).isInstanceOf(FactoryMirror.class);
        assertThat(domainMirror.getAllFactoryMirrors())
            .extracting(FactoryMirror::getTypeName)
            .containsExactlyInAnyOrder(CalendarFactory.class.getName(),
                CalendarFactoryWithFurtherResponsibilities.class.getName());
        assertThat(domainMirror.getAllServiceKindMirrors())
            .extracting(ServiceKindMirror::getTypeName)
            .contains(CalendarFactory.class.getName());
        assertThat(domainMirror.getAllDomainServiceMirrors())
            .extracting(ServiceKindMirror::getTypeName)
            .doesNotContain(CalendarFactory.class.getName());
    }

    @Test
    void Should_TreatThePublicMethodsCreatingDomainObjects_AsFactoryMethods_When_TheyAreDeclaredInAFactory() {
        var factory = mirror(CalendarFactory.class);

        assertThat(factoryMethodNames(factory)).containsExactly("create");
        assertThat(factory.methodByName("newId").isFactoryMethod()).isFalse();
    }

    @Test
    void Should_NotTreatABuilder_AsFactoryMethod() {
        var calendar = mirror(Calendar.class);

        assertThat(calendar.methodByName("builder").isFactoryMethod()).isFalse();
    }

    @Test
    void Should_TreatOnlyTheAnnotatedMethods_AsFactoryMethods_When_TheyAreDeclaredInAnAggregate() {
        var calendar = mirror(Calendar.class);

        assertThat(factoryMethodNames(calendar)).containsExactlyInAnyOrder("open", "planAppointment");
        assertThat(calendar.methodByName("firstAppointment").isFactoryMethod()).isFalse();
    }

    @Test
    void Should_TreatOnlyTheAnnotatedMethods_AsFactoryMethods_When_TheyAreDeclaredInADomainService() {
        var service = mirror(CalendarService.class);

        assertThat(service.getDomainType()).isEqualTo(DomainType.DOMAIN_SERVICE);
        assertThat(factoryMethodNames(service)).containsExactly("openFor");
        assertThat(service.methodByName("rescheduledCopy").isFactoryMethod()).isFalse();
    }

    @Test
    void Should_WarnAboutAFactory_When_ItHasPublicMethodsCreatingNoDomainObject() {
        assertThat(warnings())
            .anyMatch(message -> message.contains(CalendarFactoryWithFurtherResponsibilities.class.getName())
                && message.contains("archive"));
    }

    @Test
    void Should_WarnAboutADomainService_When_AllItsPublicMethodsAreFactoryMethods() {
        assertThat(warnings())
            .anyMatch(message -> message.contains(CalendarCreatingService.class.getName()));
    }

    @Test
    void Should_NotWarn_When_FactoriesAndDomainServicesAreModeledConsistently() {
        assertThat(warnings())
            .noneMatch(message -> message.contains(CalendarFactory.class.getName() + " ")
                || message.contains(CalendarService.class.getName() + " "));
    }

    private static DomainTypeMirror mirror(Class<?> type) {
        return domainMirror.getDomainTypeMirror(type.getName()).orElseThrow();
    }

    private static List<String> factoryMethodNames(DomainTypeMirror mirror) {
        return mirror.getFactoryMethods().stream().map(MethodMirror::getName).toList();
    }

    private static List<String> warnings() {
        return LOG.list.stream()
            .filter(event -> event.getLevel() == Level.WARN)
            .map(ILoggingEvent::getFormattedMessage)
            .toList();
    }

    private static Logger logger(Class<?> type) {
        return (Logger) LoggerFactory.getLogger(type);
    }
}
