package io.domainlifecycles.mirror;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.validate.CompletenessChecker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import tests.mirror.completeness.domain.ServiceUsingOutside;
import tests.mirror.completeness.nondomain.NonDomainMapper;
import tests.mirror.completeness.outside.OutsideValue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CompletenessCheckerTest {

    private final ListAppender<ILoggingEvent> log = new ListAppender<>();

    @BeforeEach
    void attachLog() {
        log.start();
        logger().addAppender(log);
    }

    @AfterEach
    void detachLog() {
        logger().detachAppender(log);
    }

    @Test
    void Should_OnlyWarn_When_ANonDomainClassReferencesAnUnknownDomainType() {
        var domainMirror = new ReflectiveDomainMirrorFactory("tests.mirror.completeness.nondomain")
            .initializeDomainMirror();

        assertThat(domainMirror.getDomainTypeMirror(NonDomainMapper.class.getName())).isPresent();
        assertThat(log.list)
            .filteredOn(e -> e.getLevel() == Level.WARN)
            .singleElement()
            .extracting(ILoggingEvent::getFormattedMessage)
            .satisfies(message -> assertThat(message)
                .contains(NonDomainMapper.class.getName())
                .contains(OutsideValue.class.getName())
                .contains("field declaration 'last'")
                .contains("method return type declaration of method 'map'"));
    }

    @Test
    void Should_Fail_When_ADomainTypeReferencesAnUnknownDomainType() {
        var factory = new ReflectiveDomainMirrorFactory("tests.mirror.completeness.domain");

        assertThatThrownBy(factory::initializeDomainMirror)
            .isInstanceOf(MirrorException.class)
            .hasMessageContaining(ServiceUsingOutside.class.getName())
            .hasMessageContaining(OutsideValue.class.getName());
        assertThat(log.list).noneMatch(e -> e.getLevel() == Level.WARN);
    }

    private static Logger logger() {
        return (Logger) LoggerFactory.getLogger(CompletenessChecker.class);
    }
}
