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

package io.domainlifecycles.springboot3.validation;

import io.domainlifecycles.assertion.DomainAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tests.shared.validation.jakarta.ValidatedAggregateRoot2;
import tests.shared.validation.jakarta.ValidatedAggregateRoot2Id;
import tests.shared.validation.jakarta.ValidatedRecordId2;
import tests.shared.validation.jakarta.ValidatedReordVo2;
import tests.shared.validation.jakarta.ValidatedValueObject2;

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles({ "test" })
public class ValidationExtensionSpringBoot3IntegrationTest {

    @BeforeEach
    public void init(){
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    public void testInstrumentationBeanValidationAggregate(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "", null,  1l);
        });
        assertThat(ex).hasMessageContaining("'text'");
    }

    @Test
    public void testInstrumentationBeanValidationRecordId(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            new ValidatedRecordId2(null);
        });
        assertThat(ex).hasMessageContaining("null");
    }

    @Test
    public void testInstrumentationBeanValidationRecordVo(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            var vo = new ValidatedReordVo2(null);
            System.out.println("Name:"+vo.name());
        });
        assertThat(ex).hasMessageContaining("'name'");
    }
    @Test
    public void testInstrumentationBeanValidationValueObject(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedValueObject2 vo = new ValidatedValueObject2("");
        });
        assertThat(ex).hasMessageContaining("'text'");
    }

    @Test
    public void testInstrumentationValidateCallAggregate(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "WRONG", null, 1l);
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationBeanValidateCallValueObject(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedValueObject2 vo = new ValidatedValueObject2("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationAnnotatedMethod(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "OK", null, 1l);
            root.komischeBerechnungMitValidationError();
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationValidateCallSetter(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
            root.setText("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationBeanValidationSetter(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
            root.setText(null);
        });
        assertThat(ex).hasMessageContaining("'text'");
    }

    @Test
    public void testInstrumentationBeanValidationOptional(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
            root.setText("test");
            root.setOptionalText("12345678901");
        });
        assertThat(ex).hasMessageContaining("'optionalText'");
    }

    @Test
    public void testInstrumentationTextSetzenmitReturnOk(){
        ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
        ValidatedAggregateRoot2 returned = root.textSetzenMitReturn("OK");
        assertThat(returned).isNotNull();
        assertThat(returned.getText()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationTextSetzenmitReturnWrong(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
            root.textSetzenMitReturn("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationTextSetzenPreconditionOk(){
        ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
        root.textSetzenPrecondition("OK");
        assertThat(root).isNotNull();
        assertThat(root.getText()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationTextSetzenPreconditionFail(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
            root.textSetzenPrecondition(" ");
        });
        assertThat(ex).hasMessageContaining("textSetzenPrecondition");
    }

    @Test
    public void testInstrumentationTextSetzenReturnValOk(){
        ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
        root.textSetzenMitReturn("OK");
        assertThat(root).isNotNull();
        assertThat(root.getText()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationTextSetzenReturnValFail(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
            root.textSetzenReturnVal(" ");
        });
        assertThat(ex).hasMessageContaining("textSetzenReturnVal");
    }

    @Test
    public void testInstrumentationTextSetzenPreconditionOkFailAfter(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
            root.textSetzenPrecondition("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

    @Test
    public void testInstrumentationOptionalTextSetzenOk(){
        ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
        root.optionalTextSetzenMitReturn(Optional.of("OK"));
        assertThat(root).isNotNull();
        assertThat(root.getOptionalText()).isPresent();
        assertThat(root.getOptionalText().get()).isEqualTo("OK");
    }

    @Test
    public void testInstrumentationOptionalTextSetzenFailPrecon(){
        ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            root.optionalTextSetzenMitReturn(Optional.of(" "));
        });
        assertThat(ex).hasMessageContaining("optionalTextSetzenMitReturn");
    }

    @Test
    public void testInstrumentationOptionalTextSetzenFailLength(){
        ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            root.optionalTextSetzenMitReturn(Optional.of("1223344556677876555"));
        });
        assertThat(ex).hasMessageContaining("optionalText");
    }

    @Test
    public void testInstrumentationOptionalTextSetzenFailLengthIntialization(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", "1223344556677876555",  1l);
        });
        assertThat(ex).hasMessageContaining("optionalText");
    }

    @Test
    public void testInstrumentationTextSetzenReturnValOkFailOnValidate(){
        DomainAssertionException ex = assertThrows( DomainAssertionException.class, ()-> {
            ValidatedAggregateRoot2 root = new ValidatedAggregateRoot2(new ValidatedAggregateRoot2Id(1l), "test", null, 1l);
            root.textSetzenReturnVal("WRONG");
        });
        assertThat(ex).hasMessageContaining("text darf niemals 'WRONG' sein!");
    }

}
