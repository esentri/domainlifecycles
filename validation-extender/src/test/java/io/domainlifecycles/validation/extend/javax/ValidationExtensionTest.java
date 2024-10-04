/*
 *
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.validation.extend.javax;

import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.assertion.DomainAssertionException;
import io.domainlifecycles.validation.extend.ValidationDomainClassExtender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tests.shared.validation.javax.ValidatedAggregateRoot;
import tests.shared.validation.javax.ValidatedAggregateRootId;
import tests.shared.validation.javax.ValidatedValueObject;

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Disabled //depending on configured dependencies this could be switched on
//tested with spring boot 2 compatibility
public class ValidationExtensionTest {

    @BeforeEach
    public void init() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @BeforeAll
    static void beforeAll() {
        Locale.setDefault(Locale.ENGLISH);
        ValidationDomainClassExtender.extend("tests");
    }

    @Test
    public void testInstrumentationBeanValidationAggregate() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "", null, 1L);
        });
        var msg = ex.getLocalizedMessage();
        log.info(msg);
        assertThat(ex).hasMessageContaining("'text'");
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
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "WRONG", null,
                1L);
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
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "OK", null, 1L);
            root.komischeBerechnungMitValidationError();
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationValidateCallSetter() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.setText("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationBeanValidationSetter() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.setText(null);
        });
        assertThat(ex).hasMessageContaining("'text'");
    }

    @Test
    public void testInstrumentationBeanValidationOptional() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.setText("test");
            root.setOptionalText("12345678901");
        });
        assertThat(ex).hasMessageContaining("'optionalText'");
    }

    @Test
    public void testInstrumentationTextSetzenmitReturnOk() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null, 1L);
        ValidatedAggregateRoot returned = root.textSetzenMitReturn("OK");
        assertThat(returned).isNotNull();
        assertThat(returned.getText()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationTextSetzenmitReturnWrong() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.textSetzenMitReturn("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationTextSetzenPreconditionOk() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null, 1L);
        root.textSetzenPrecondition("OK");
        assertThat(root).isNotNull();
        assertThat(root.getText()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationTextSetzenPreconditionFail() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.textSetzenPrecondition(" ");
        });
        assertThat(ex).hasMessageContaining("textSetzenPrecondition");
    }

    @Test
    public void testInstrumentationTextSetzenReturnValOk() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null, 1L);
        root.textSetzenMitReturn("OK");
        assertThat(root).isNotNull();
        assertThat(root.getText()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationTextSetzenReturnValFail() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.textSetzenReturnVal(" ");
        });
        assertThat(ex).hasMessageContaining("textSetzenReturnVal");
    }

    @Test
    public void testInstrumentationTextSetzenPreconditionOkFailAfter() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.textSetzenPrecondition("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationOptionalTextSetzenFailLength() {
        ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null, 1L);
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            root.optionalTextSetzenMitReturn(Optional.of("1223344556677876555"));
        });
        assertThat(ex).hasMessageContaining("optionalText");
    }

    @Test
    public void testInstrumentationOptionalTextSetzenFailLengthIntialization() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test",
                "1223344556677876555", 1L);
        });
        assertThat(ex).hasMessageContaining("optionalText");
    }

    @Test
    public void testInstrumentationTextSetzenReturnValOkFailOnValidate() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, () -> {
            ValidatedAggregateRoot root = new ValidatedAggregateRoot(new ValidatedAggregateRootId(1L), "test", null,
                1L);
            root.textSetzenReturnVal("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }


}
