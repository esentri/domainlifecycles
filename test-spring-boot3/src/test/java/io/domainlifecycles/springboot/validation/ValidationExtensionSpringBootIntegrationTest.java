package io.domainlifecycles.springboot.validation;

import io.domainlifecycles.assertion.DomainAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tests.shared.validation.jakarta.ValidatedAggregateRoot;
import tests.shared.validation.jakarta.ValidatedAggregateRootId;
import tests.shared.validation.jakarta.ValidatedRecordId;
import tests.shared.validation.jakarta.ValidatedRecordVo;
import tests.shared.validation.jakarta.ValidatedValueObject;

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles({"test"})
public class ValidationExtensionSpringBootIntegrationTest {

    @BeforeEach
    public void init() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    public void testInstrumentationBeanValidationAggregate() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "", null, 1l);
        });
        assertThat(ex).hasMessageContaining("'text'");
    }

    @Test
    public void testInstrumentationBeanValidationRecordId() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            new ValidatedRecordId(null);
        });
        assertThat(ex).hasMessageContaining("null");
    }

    @Test
    public void testInstrumentationBeanValidationRecordVo() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            var vo = new ValidatedRecordVo(null);
            System.out.println("Name:" + vo.name());
        });
        assertThat(ex).hasMessageContaining("'name'");
    }

    @Test
    public void testInstrumentationBeanValidationValueObject() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedValueObject vo = new ValidatedValueObject("");
        });
        assertThat(ex).hasMessageContaining("'text'");
    }

    @Test
    public void testInstrumentationValidateCallAggregate() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "WRONG", null,
                1l);
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationBeanValidateCallValueObject() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedValueObject vo = new ValidatedValueObject("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationAnnotatedMethod() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "OK", null,
                1l);
            root.strangeCalculationWithValidationError();
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationValidateCallSetter() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null,
                1l);
            root.setText("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationBeanValidationSetter() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null,
                1l);
            root.setText(null);
        });
        assertThat(ex).hasMessageContaining("'text'");
    }

    @Test
    public void testInstrumentationBeanValidationOptional() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null,
                1l);
            root.setText("test");
            root.setOptionalText("12345678901");
        });
        assertThat(ex).hasMessageContaining("'optionalText'");
    }

    @Test
    public void testInstrumentationTextSetzenmitReturnOk() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null, 1l);
        ValidatedAggregateRoot returned = root.setTextWithReturn("OK");
        assertThat(returned).isNotNull();
        assertThat(returned.getText()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationTextSetzenmitReturnWrong() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null,
                1l);
            root.setTextWithReturn("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationTextSetzenPreconditionOk() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null, 1l);
        root.setTextPrecondition("OK");
        assertThat(root).isNotNull();
        assertThat(root.getText()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationTextSetzenPreconditionFail() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null,
                1l);
            root.setTextPrecondition(" ");
        });
        assertThat(ex).hasMessageContaining("setTextPrecondition");
    }

    @Test
    public void testInstrumentationTextSetzenReturnValOk() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null, 1l);
        root.setTextWithReturn("OK");
        assertThat(root).isNotNull();
        assertThat(root.getText()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationTextSetzenReturnValFail() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null,
                1l);
            root.setTextReturnVal(" ");
        });
        assertThat(ex).hasMessageContaining("setTextReturnVal");
    }

    @Test
    public void testInstrumentationTextSetzenPreconditionOkFailAfter() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null,
                1l);
            root.setTextPrecondition("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationOptionalTextSetzenOk() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null, 1l);
        root.setOptionalTextWithReturn(Optional.of("OK"));
        assertThat(root).isNotNull();
        assertThat(root.getOptionalText()).isPresent();
        assertThat(root.getOptionalText().get()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationOptionalTextSetzenFailPrecon() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null, 1l);
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            root.setOptionalTextWithReturn(Optional.of(" "));
        });
        assertThat(ex).hasMessageContaining("setOptionalTextWithReturn");
    }

    @Test
    public void testInstrumentationOptionalTextSetzenFailLength() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null, 1l);
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            root.setOptionalTextWithReturn(Optional.of("1223344556677876555"));
        });
        assertThat(ex).hasMessageContaining("optionalText");
    }

    @Test
    public void testInstrumentationOptionalTextSetzenFailLengthIntialization() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test",
                "1223344556677876555", 1l);
        });
        assertThat(ex).hasMessageContaining("optionalText");
    }

    @Test
    public void testInstrumentationTextSetzenReturnValOkFailOnValidate() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1l), "test", null,
                1l);
            root.setTextReturnVal("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

}
