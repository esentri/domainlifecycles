package io.domainlifecycles.validation.extend.jakarta;

import io.domainlifecycles.assertion.DomainAssertionException;
import io.domainlifecycles.validation.extend.ValidationDomainClassExtender;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.shared.validation.jakarta.ValidatedAggregateRoot;
import tests.shared.validation.jakarta.ValidatedAggregateRootId;
import tests.shared.validation.jakarta.ValidatedValueObject;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class ValidationExtensionTest {

    @BeforeEach
    public void init() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @BeforeAll
    static void beforeAll() {
        Locale.setDefault(Locale.ENGLISH);
        ValidationDomainClassExtender.extend("tests", "io.domainlifecycles.validation.extend");
    }

    @Test
    public void testInstrumentStatic() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            VOStaticMethod root = new VOStaticMethod(null);
        });
        var msg = ex.getLocalizedMessage();
        log.info(msg);
        Assertions.assertThat(ex).hasMessageContaining("'value'");
    }

    @Test
    public void testInstrumentationBeanValidationAggregate() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "", null, 1L);
        });
        var msg = ex.getLocalizedMessage();
        log.info(msg);
        Assertions.assertThat(ex).hasMessageContaining("'text'");
    }

    @Test
    public void testInstrumentationBeanValidationValueObject() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedValueObject vo = new ValidatedValueObject("");
        });
        Assertions.assertThat(ex).hasMessageContaining("'text'");
    }

    @Test
    public void testInstrumentationValidateCallAggregate() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "WRONG", null,
                1L);
        });
        Assertions.assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationBeanValidateCallValueObject() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedValueObject vo = new ValidatedValueObject("WRONG");
        });
        Assertions.assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationAnnotatedMethod() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "OK", null,
                1L);
            root.komischeBerechnungMitValidationError();
        });
        Assertions.assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationValidateCallSetter() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.setText("WRONG");
        });
        Assertions.assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationBeanValidationSetter() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.setText(null);
        });
        Assertions.assertThat(ex).hasMessageContaining("'text'");
    }

    @Test
    public void testInstrumentationBeanValidationOptional() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.setText("test");
            root.setOptionalText("12345678901");
        });
        Assertions.assertThat(ex).hasMessageContaining("'optionalText'");
    }

    @Test
    public void testInstrumentationTextSetzenmitReturnOk() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null, 1L);
        ValidatedAggregateRoot returned = root.textSetzenMitReturn("OK");
        Assertions.assertThat(returned).isNotNull();
        Assertions.assertThat(returned.getText()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationTextSetzenmitReturnWrong() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.textSetzenMitReturn("WRONG");
        });
        Assertions.assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationTextSetzenPreconditionOk() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null, 1L);
        root.textSetzenPrecondition("OK");
        Assertions.assertThat(root).isNotNull();
        Assertions.assertThat(root.getText()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationTextSetzenPreconditionFail() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.textSetzenPrecondition(" ");
        });
        Assertions.assertThat(ex).hasMessageContaining("textSetzenPrecondition");
    }

    @Test
    public void testInstrumentationTextSetzenReturnValOk() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null, 1L);
        root.textSetzenMitReturn("OK");
        Assertions.assertThat(root).isNotNull();
        Assertions.assertThat(root.getText()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationTextSetzenReturnValFail() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.textSetzenReturnVal(" ");
        });
        Assertions.assertThat(ex).hasMessageContaining("textSetzenReturnVal");
    }

    @Test
    public void testInstrumentationTextSetzenPreconditionOkFailAfter() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.textSetzenPrecondition("WRONG");
        });
        Assertions.assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationOptionalTextSetzenFailLength() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null, 1L);
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            root.optionalTextSetzenMitReturn(Optional.of("1223344556677876555"));
        });
        Assertions.assertThat(ex).hasMessageContaining("optionalText");
    }

    @Test
    public void testInstrumentationOptionalTextSetzenFailLengthIntialization() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test",
                "1223344556677876555", 1L);
        });
        Assertions.assertThat(ex).hasMessageContaining("optionalText");
    }

    @Test
    public void testInstrumentationTextSetzenReturnValOkFailOnValidate() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.textSetzenReturnVal("WRONG");
        });
        Assertions.assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }


}
