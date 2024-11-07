/*
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

package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.DomainServiceCommand;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.domain.types.Validatable;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import io.domainlifecycles.domain.types.base.EntityBase;
import io.domainlifecycles.domain.types.base.ValueObjectBase;
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainObjectMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.EnumMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ParamMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import io.domainlifecycles.mirror.model.AssertionType;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tests.mirror.BaseEntityWithHidden;
import tests.mirror.SubEntityHiding;
import tests.shared.complete.onlinehandel.benachrichtigung.BenachrichtigungService;
import tests.shared.complete.onlinehandel.bestellung.AktionsCodeBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellKommentarBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellPositionBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellStatusBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellStatusCodeEnumBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungRepository;
import tests.shared.complete.onlinehandel.bestellung.KundennummerBv3;
import tests.shared.complete.onlinehandel.bestellung.LieferadresseBv3;
import tests.shared.complete.onlinehandel.bestellung.NeueBestellung;
import tests.shared.complete.onlinehandel.bestellung.PreisBv3;
import tests.shared.complete.onlinehandel.bestellung.WaehrungEnumBv3;
import tests.shared.complete.onlinehandel.zustellung.AuslieferungGestartet;
import tests.shared.complete.onlinehandel.zustellung.StarteAuslieferung;
import tests.shared.complete.onlinehandel.zustellung.ZustellungsService;
import tests.shared.openapi.TestId;
import tests.shared.openapi.TestIdExtended;
import tests.shared.openapi.TestIdInterface;
import tests.shared.openapi.TestIdInterfaceExtended;
import tests.shared.openapi.jakarta.TestVo2;
import tests.shared.persistence.domain.bestellung.bv2.BestellStatusCodeEnum;
import tests.shared.persistence.domain.bestellung.bv2.BestellungId;
import tests.shared.persistence.domain.bestellung.bv2.WaehrungEnum;
import tests.shared.persistence.domain.inheritanceExtended.CarWithEngine;
import tests.shared.persistence.domain.inheritanceExtended.VehicleExtended;
import tests.shared.persistence.domain.inheritanceGenericId.AbstractRoot;
import tests.shared.persistence.domain.inheritanceGenericId.ConcreteRoot;
import tests.shared.persistence.domain.valueobjects.ComplexVo;
import tests.shared.persistence.domain.valueobjects.SimpleVo;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany2;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany3;
import tests.shared.validation.jakarta.ValidatedAggregateRoot2;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ReflectiveDomainMirrorFactoryTest {

    @BeforeAll
    public static void init() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
        Domain.initialize(factory);
    }

    @ParameterizedTest
    @MethodSource(value = "identityValueTypes")
    public void testIdMirror(Class<?> identityClass, Class<?> valueTypeClass) {
        Optional<IdentityMirror> identity = Domain
            .typeMirror(identityClass.getName());
        assertThat(identity).isPresent();
        assertThat(identity.get().getValueTypeName().get()).isEqualTo(valueTypeClass.getName());
    }

    private static Stream<Arguments> identityValueTypes() {
        return Stream.of(
            arguments(TestId.class, UUID.class),
            arguments(TestIdInterface.class, UUID.class),
            arguments(TestIdExtended.class, UUID.class),
            arguments(TestIdInterfaceExtended.class, UUID.class),
            arguments(BestellungId.class, Long.class)
        );
    }

    @ParameterizedTest
    @MethodSource(value = "enumTypes")
    public void testEnumMirror(Class<? extends Enum<?>> enumClass) {
        Optional<EnumMirror> enumMirrorOptional = Domain.typeMirror(enumClass.getName());
        assertThat(enumMirrorOptional).isPresent();
        var enumMirror = enumMirrorOptional.get();
        assertThat(enumMirror.isEnum()).isTrue();
        assertThat(enumMirror
            .getEnumOptions()
            .stream()
            .map(om -> om.getValue())
            .collect(Collectors.toList())
        ).containsExactlyInAnyOrderElementsOf(
            Arrays.stream(enumClass.getEnumConstants()).map(c -> c.name()).collect(Collectors.toList()));
    }

    private static Stream<Arguments> enumTypes() {
        return Stream.of(
            arguments(BestellStatusCodeEnum.class),
            arguments(WaehrungEnum.class)
        );
    }

    @Test
    public void testHiddenField() {
        var mirror = Domain.typeMirror(SubEntityHiding.class.getName()).orElseThrow();

        var fieldMirrors = mirror.getAllFields()
            .stream()
            .filter(f -> f.getName().equals("hiddenField"))
            .toList();
        assertThat(fieldMirrors).hasSize(2);
        var hidden = fieldMirrors
            .stream()
            .filter(f -> !f.getDeclaredByTypeName().equals(SubEntityHiding.class.getName()))
            .findFirst().orElseThrow();
        assertThat(hidden.isHidden()).isTrue();
    }

    @Test
    public void testOverriddenMethods() {
        var mirror = Domain.typeMirror(SubEntityHiding.class.getName()).orElseThrow();

        var overridden = mirror.getMethods()
            .stream()
            .filter(m -> m.getName().equals("showTestOverridden"))
            .toList();
        assertThat(overridden).hasSize(2);
        var overriddenConcrete = overridden
            .stream()
            .filter(m -> m.getDeclaredByTypeName().equals(BaseEntityWithHidden.class.getName()))
            .findFirst()
            .orElseThrow();
        assertThat(overriddenConcrete.isOverridden()).isTrue();

        var overriddenConcreteNot = overridden
            .stream()
            .filter(m -> m.getDeclaredByTypeName().equals(SubEntityHiding.class.getName()))
            .findFirst()
            .orElseThrow();
        assertThat(overriddenConcreteNot.isOverridden()).isFalse();

        var validate = mirror.getMethods()
            .stream()
            .filter(m -> m.getName().equals("validate"))
            .toList();
        assertThat(validate).hasSize(2);
        var overriddenValidate = validate
            .stream()
            .filter(m -> m.getDeclaredByTypeName().equals(Validatable.class.getName()))
            .findFirst()
            .orElseThrow();
        assertThat(overriddenValidate.isOverridden()).isTrue();

        var overriddenValidateNot = validate
            .stream()
            .filter(m -> m.getDeclaredByTypeName().equals(BaseEntityWithHidden.class.getName()))
            .findFirst()
            .orElseThrow();
        assertThat(overriddenValidateNot.isOverridden()).isFalse();

        var notOverridden = mirror.getMethods()
            .stream()
            .filter(m -> m.getName().equals("showTestNotOverridden"))
            .toList();
        assertThat(notOverridden).hasSize(1);
        assertThat(notOverridden.get(0).isOverridden()).isFalse();

    }

    @Test
    public void testSimpleVo() {
        assertDomainObject(
            SimpleVo.class.getName(),
            ValueObjectBase.class.getName(),
            null,
            List.of(
                new ExpectedProperty(
                    String.class,
                    "value",
                    SimpleVo.class.getName(),
                    false,
                    false,
                    true,
                    false,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
                )
            ),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            List.of(ValueObjectBase.class.getName(), Object.class.getName())
        );
    }

    @Test
    public void testComplexVo() {
        assertDomainObject(
            ComplexVo.class.getName(),
            ValueObjectBase.class.getName(),
            null,
            List.of(
                new ExpectedProperty(
                    String.class,
                    "valueA",
                    ComplexVo.class.getName(),
                    false,
                    false,
                    true,
                    false,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
                )
            ),
            List.of(
                new ExpectedReference(
                    SimpleVo.class,
                    "valueB",
                    ComplexVo.class.getName(),
                    false,
                    false,
                    false,
                    true,
                    false,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
                )
            ),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            List.of(ValueObjectBase.class.getName(), Object.class.getName())
        );
    }

    @Test
    public void testSimpleVoOneToMany2() {
        assertDomainObject(
            SimpleVoOneToMany2.class.getName(),
            ValueObjectBase.class.getName(),
            null,
            List.of(
                new ExpectedProperty(
                    String.class,
                    "value",
                    SimpleVoOneToMany2.class.getName(),
                    false,
                    false,
                    true,
                    false,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
                )
            ),
            List.of(
                new ExpectedReference(
                    SimpleVoOneToMany3.class,
                    "oneToMany3Set",
                    SimpleVoOneToMany2.class.getName(),
                    true,
                    false,
                    false,
                    true,
                    false,
                    Set.class.getName(),
                    Collections.emptyList(),
                    Collections.emptyList()
                )
            ),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            List.of(ValueObjectBase.class.getName(), Object.class.getName())
        );
    }

    @Test
    public void testTestVo2() {
        assertDomainObject(
            TestVo2.class.getName(),
            Object.class.getName(),
            null,
            List.of(
                new ExpectedProperty(
                    String.class, "stringSized", TestVo2.class.getName(), false, false, true, false, null,
                    List.of(new ExpectedAssertion(AssertionType.hasLength, "0", "5",
                        "{jakarta.validation.constraints.Size.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    String.class, "optionalStringSized", TestVo2.class.getName(), false, true, true, false,
                    Optional.class.getName(),
                    List.of(new ExpectedAssertion(AssertionType.hasLength, "0", "5",
                        "{jakarta.validation.constraints.Size.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    String.class, "stringNotNull", TestVo2.class.getName(), false, false, true, false, null,
                    List.of(new ExpectedAssertion(AssertionType.isNotNull, null, null,
                        "{jakarta.validation.constraints.NotNull.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    String.class, "stringNotEmpty", TestVo2.class.getName(), false, false, true, false, null,
                    List.of(new ExpectedAssertion(AssertionType.isNotEmpty, null, null,
                        "{jakarta.validation.constraints.NotEmpty.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    String.class, "stringNotBlank", TestVo2.class.getName(), false, false, true, false, null,
                    List.of(new ExpectedAssertion(AssertionType.isNotBlank, null, null,
                        "{jakarta.validation.constraints.NotBlank.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    String.class, "stringPattern", TestVo2.class.getName(), false, false, true, false, null,
                    List.of(new ExpectedAssertion(AssertionType.regEx, "[0-9]", null,
                        "{jakarta.validation.constraints.Pattern.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    String.class, "stringEmail", TestVo2.class.getName(), false, false, true, false, null,
                    List.of(new ExpectedAssertion(AssertionType.isValidEmail, null, null,
                        "{jakarta.validation.constraints.Email.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    String.class, "optionalStringEmail", TestVo2.class.getName(), false, true, true, false,
                    Optional.class.getName(),
                    List.of(new ExpectedAssertion(AssertionType.isValidEmail, null, null,
                        "{jakarta.validation.constraints.Email.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    String.class, "optionalStringPattern", TestVo2.class.getName(), false, true, true, false,
                    Optional.class.getName(),
                    List.of(new ExpectedAssertion(AssertionType.regEx, "[0-9]", null,
                        "{jakarta.validation.constraints.Pattern.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    BigDecimal.class, "bigDecimalMin", TestVo2.class.getName(), false, false, true, false, null,
                    List.of(new ExpectedAssertion(AssertionType.isGreaterOrEqualNonDecimal, "5", null,
                        "{jakarta.validation.constraints.Min.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    int.class, "anIntMin", TestVo2.class.getName(), false, false, true, false, null,
                    List.of(new ExpectedAssertion(AssertionType.isGreaterOrEqualNonDecimal, "5", null,
                        "{jakarta.validation.constraints.Min.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    BigDecimal.class, "optionalBigDecimalMin", TestVo2.class.getName(), false, true, true, false,
                    Optional.class.getName(),
                    List.of(new ExpectedAssertion(AssertionType.isGreaterOrEqualNonDecimal, "5", null,
                        "{jakarta.validation.constraints.Min.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    BigDecimal.class, "bigDecimalDecimalMin", TestVo2.class.getName(), false, false, true, false, null,
                    List.of(new ExpectedAssertion(AssertionType.isGreaterOrEqual, "5.0", null,
                        "{jakarta.validation.constraints.DecimalMin.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    int.class, "anIntDecimalMin", TestVo2.class.getName(), false, false, true, false, null,
                    List.of(new ExpectedAssertion(AssertionType.isGreaterOrEqual, "5.0", null,
                        "{jakarta.validation.constraints.DecimalMin.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    BigDecimal.class, "optionalBigDecimalDecimalMin", TestVo2.class.getName(), false, true, true, false,
                    Optional.class.getName(),
                    List.of(new ExpectedAssertion(AssertionType.isGreaterOrEqual, "5.0", null,
                        "{jakarta.validation.constraints.DecimalMin.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    BigDecimal.class, "bigDecimalDecimalMaxExclusive", TestVo2.class.getName(), false, false, true,
                    false, null,
                    List.of(new ExpectedAssertion(AssertionType.isLessThan, null, "5.0",
                        "{jakarta.validation.constraints.DecimalMax.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    int.class, "anIntDecimalMaxExclusive", TestVo2.class.getName(), false, false, true, false, null,
                    List.of(new ExpectedAssertion(AssertionType.isLessThan, null, "5.0",
                        "{jakarta.validation.constraints.DecimalMax.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    BigDecimal.class, "optionalBigDecimalDecimalMaxExclusive", TestVo2.class.getName(), false, true,
                    true, false, Optional.class.getName(),
                    List.of(new ExpectedAssertion(AssertionType.isLessThan, null, "5.0",
                        "{jakarta.validation.constraints.DecimalMax.message}")),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    double.class, "aDoubleDigits", TestVo2.class.getName(), false, false, true, false, null,
                    List.of(new ExpectedAssertion(AssertionType.hasMaxDigits, "3", "2",
                        "{jakarta.validation.constraints.Digits.message}")),
                    Collections.emptyList()
                )
            ),
            List.of(
                new ExpectedReference(
                    TestId.class, "testId", TestVo2.class.getName(), false, false, false, true, false, null,
                    Collections.emptyList(),
                    Collections.emptyList()
                ),
                new ExpectedReference(
                    TestId.class, "optionalTestId", TestVo2.class.getName(), false, false, true, true, false,
                    Optional.class.getName(),
                    Collections.emptyList(),
                    Collections.emptyList()
                ),
                new ExpectedReference(
                    TestIdInterface.class, "testIdInterface", TestVo2.class.getName(), false, false, false, true, false,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
                ),
                new ExpectedReference(
                    TestId.class, "listTestIdSized", TestVo2.class.getName(), true, false, false, true, false,
                    List.class.getName(),
                    Collections.emptyList(),
                    List.of(new ExpectedAssertion(AssertionType.hasSize, "0", "2",
                        "{jakarta.validation.constraints.Size.message}"))
                )
            ),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            List.of(Object.class.getName())
        );
    }

    @Test
    public void testValidatedAggregateRoot2() {
        assertDomainObject(
            ValidatedAggregateRoot2.class.getName(),
            AggregateRootBase.class.getName(),
            "id",
            List.of(
                new ExpectedProperty(
                    String.class,
                    "text",
                    ValidatedAggregateRoot2.class.getName(),
                    true,
                    false,
                    true,
                    true,
                    null,
                    List.of(
                        new ExpectedAssertion(AssertionType.hasLength, "0", "100",
                            "{jakarta.validation.constraints.Size.message}"),
                        new ExpectedAssertion(AssertionType.isNotEmpty, null, null,
                            "{jakarta.validation.constraints.NotEmpty.message}")
                    ),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    String.class,
                    "optionalText",
                    ValidatedAggregateRoot2.class.getName(),
                    true,
                    true,
                    true,
                    true,
                    Optional.class.getName(),
                    List.of(
                        new ExpectedAssertion(AssertionType.hasLength, "0", "10",
                            "{jakarta.validation.constraints.Size.message}")
                    ),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    long.class,
                    "concurrencyVersion",
                    AggregateRootBase.class.getName(),
                    true,
                    false,
                    true,
                    false,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
                )
            ),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            List.of(AggregateRootBase.class.getName(), EntityBase.class.getName(), Object.class.getName())
        );
    }

    @Test
    public void testBestellung() {
        assertDomainObject(
            BestellungBv3.class.getName(),
            AggregateRootBase.class.getName(),
            "id",
            List.of(
                new ExpectedProperty(
                    Byte.class,
                    "prioritaet",
                    BestellungBv3.class.getName(),
                    true,
                    false,
                    true,
                    true,
                    null,
                    List.of(
                        new ExpectedAssertion(AssertionType.isNotNull, null, null,
                            "{jakarta.validation.constraints.NotNull.message}"),
                        new ExpectedAssertion(AssertionType.isLessOrEqual, null, "3.0",
                            "{jakarta.validation.constraints.DecimalMax.message}"),
                        new ExpectedAssertion(AssertionType.isGreaterOrEqual, "1.0", null,
                            "{jakarta.validation.constraints.DecimalMin.message}")
                    ),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    long.class,
                    "concurrencyVersion",
                    AggregateRootBase.class.getName(),
                    true,
                    false,
                    true,
                    false,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
                )
            ),
            List.of(
                new ExpectedReference(
                    KundennummerBv3.class,
                    "kundennummer",
                    BestellungBv3.class.getName(),
                    false,
                    true,
                    false,
                    true,
                    true,
                    null,
                    List.of(
                        new ExpectedAssertion(AssertionType.isNotNull, null, null,
                            "{jakarta.validation.constraints.NotNull.message}")
                    ),
                    Collections.emptyList()
                ),
                new ExpectedReference(
                    PreisBv3.class,
                    "gesamtPreis",
                    BestellungBv3.class.getName(),
                    false,
                    true,
                    false,
                    true,
                    false,
                    null,
                    List.of(
                        new ExpectedAssertion(AssertionType.isNotNull, null, null,
                            "{jakarta.validation.constraints.NotNull.message}")
                    ),
                    Collections.emptyList()
                ),
                new ExpectedReference(
                    AktionsCodeBv3.class,
                    "aktionsCodes",
                    BestellungBv3.class.getName(),
                    true,
                    true,
                    false,
                    true,
                    true,
                    List.class.getName(),
                    Collections.emptyList(),
                    Collections.emptyList()
                )
            ),
            List.of(
                new ExpectedReference(
                    LieferadresseBv3.class,
                    "lieferadresse",
                    BestellungBv3.class.getName(),
                    false,
                    true,
                    false,
                    true,
                    true,
                    null,
                    List.of(
                        new ExpectedAssertion(AssertionType.isNotNull, null, null,
                            "{jakarta.validation.constraints.NotNull.message}")
                    ),
                    Collections.emptyList()
                ),
                new ExpectedReference(
                    BestellPositionBv3.class,
                    "bestellPositionen",
                    BestellungBv3.class.getName(),
                    true,
                    false,
                    false,
                    true,
                    false,
                    List.class.getName(),
                    Collections.emptyList(),
                    List.of(
                        new ExpectedAssertion(AssertionType.isNotNull, null, null,
                            "{jakarta.validation.constraints.NotNull.message}"),
                        new ExpectedAssertion(AssertionType.hasSize, "1", String.valueOf(Integer.MAX_VALUE),
                            "{jakarta.validation.constraints.Size.message}")
                    )

                ),
                new ExpectedReference(
                    BestellStatusBv3.class,
                    "bestellStatus",
                    BestellungBv3.class.getName(),
                    false,
                    true,
                    false,
                    true,
                    true,
                    null,
                    List.of(
                        new ExpectedAssertion(AssertionType.isNotNull, null, null,
                            "{jakarta.validation.constraints.NotNull.message}")
                    ),
                    Collections.emptyList()
                ),
                new ExpectedReference(
                    BestellKommentarBv3.class,
                    "bestellKommentare",
                    BestellungBv3.class.getName(),
                    true,
                    false,
                    false,
                    true,
                    false,
                    List.class.getName(),
                    Collections.emptyList(),
                    List.of(
                        new ExpectedAssertion(AssertionType.isNotNull, null, null,
                            "{jakarta.validation.constraints.NotNull.message}")
                    )
                )
            ),
            Collections.emptyList(),
            List.of(AuslieferungGestartet.class.getName()),
            Collections.emptyList(),
            List.of(AggregateRootBase.class.getName(), EntityBase.class.getName(), Object.class.getName())
        );
        var aggMirror = (AggregateRootMirror) Domain.typeMirror(BestellungBv3.class.getName()).get();
        assertMethods(
            BestellungBv3.class,
            aggMirror.getMethods(),
            List.of(
                new ExpectedMethod("builder", BestellungBv3.class.getName(), AccessLevel.PUBLIC,
                    new ExpectedReturnType(
                        BestellungBv3.BestellungBv3Builder.class,
                        null,
                        false,
                        false,
                        false,
                        Collections.emptyList(),
                        Collections.emptyList()
                    ),
                    Collections.emptyList(), Collections.emptyList(), null
                ),
                new ExpectedMethod("starteLieferung", BestellungBv3.class.getName(), AccessLevel.PUBLIC,
                    new ExpectedReturnType(
                        void.class,
                        null,
                        false,
                        false,
                        false,
                        Collections.emptyList(),
                        Collections.emptyList()
                    ),
                    Collections.emptyList(), List.of(AuslieferungGestartet.class.getName()), null
                )
            )
        );
    }

    @Test
    public void testAktionsCode() {
        assertDomainObject(
            AktionsCodeBv3.class.getName(),
            java.lang.Record.class.getName(),
            null,
            List.of(
                new ExpectedProperty(
                    String.class,
                    "value",
                    AktionsCodeBv3.class.getName(),
                    false,
                    false,
                    true,
                    false,
                    null,
                    List.of(
                        new ExpectedAssertion(AssertionType.isNotEmpty, null, null,
                            "Ein Aktionscode darf nicht leer sein!"),
                        new ExpectedAssertion(AssertionType.hasLength, "0", "15",
                            "Ein Aktionscode darf aus maximal 15 Zeichen bestehen!")
                    ),
                    Collections.emptyList()
                )
            ),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            List.of(Record.class.getName(), Object.class.getName())
        );
    }

    @Test
    public void testBestellStatus() {
        assertDomainObject(
            BestellStatusBv3.class.getName(),
            EntityBase.class.getName(),
            "id",
            List.of(
                new ExpectedProperty(
                    LocalDateTime.class,
                    "statusAenderungAm",
                    BestellStatusBv3.class.getName(),
                    true,
                    false,
                    true,
                    true,
                    null,
                    List.of(
                        new ExpectedAssertion(AssertionType.isNotNull, null, null,
                            "{jakarta.validation.constraints.NotNull.message}")
                    ),
                    Collections.emptyList()
                ),
                new ExpectedProperty(
                    long.class,
                    "concurrencyVersion",
                    AggregateRootBase.class.getName(),
                    true,
                    false,
                    true,
                    false,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
                )
            ),
            List.of(
                new ExpectedReference(
                    BestellStatusCodeEnumBv3.class,
                    "statusCode",
                    BestellStatusBv3.class.getName(),
                    false,
                    true,
                    false,
                    true,
                    true,
                    null,
                    List.of(
                        new ExpectedAssertion(AssertionType.isNotNull, null, null,
                            "{jakarta.validation.constraints.NotNull.message}")
                    ),
                    Collections.emptyList()
                )
            ),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            List.of(EntityBase.class.getName(), Object.class.getName())
        );
    }

    @Test
    public void testPreis() {
        assertDomainObject(
            PreisBv3.class.getName(),
            java.lang.Record.class.getName(),
            null,
            List.of(
                new ExpectedProperty(
                    BigDecimal.class,
                    "betrag",
                    PreisBv3.class.getName(),
                    false,
                    false,
                    true,
                    false,
                    null,
                    List.of(
                        new ExpectedAssertion(AssertionType.isNotNull, null, null,
                            "{jakarta.validation.constraints.NotNull.message}"),
                        new ExpectedAssertion(AssertionType.isPositive, null, null,
                            "{jakarta.validation.constraints.Positive.message}")
                    ),
                    Collections.emptyList()
                )
            ),
            List.of(
                new ExpectedReference(
                    WaehrungEnumBv3.class,
                    "waehrung",
                    PreisBv3.class.getName(),
                    false,
                    false,
                    false,
                    true,
                    false,
                    null,
                    List.of(
                        new ExpectedAssertion(AssertionType.isNotNull, null, null,
                            "{jakarta.validation.constraints.NotNull.message}")
                    ),
                    Collections.emptyList()
                )
            ),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            List.of(Record.class.getName(), Object.class.getName())
        );
    }

    @Test
    public void testBenachrichtigungService() {
        var serviceMirrorOpt = Domain.typeMirror(BenachrichtigungService.class.getName()).map(
            s -> (DomainServiceMirror) s);
        assertThat(serviceMirrorOpt).isPresent();
        var serviceMirror = serviceMirrorOpt.get();
        assertThat(serviceMirror.getTypeName()).isEqualTo(BenachrichtigungService.class.getName());
        assertThat(serviceMirror.getInheritanceHierarchyTypeNames().get(0)).isEqualTo(Object.class.getName());
        assertThat(serviceMirror.getAllInterfaceTypeNames().get(0)).isEqualTo(DomainService.class.getName());
        var auslieferungGestartetEvent = Domain.typeMirror(AuslieferungGestartet.class.getName()).map(
            e -> (DomainEventMirror) e);
        assertThat(auslieferungGestartetEvent).isPresent();
        assertThat(serviceMirror.listensTo(auslieferungGestartetEvent.get())).isTrue();
        assertMethods(BenachrichtigungService.class, serviceMirror.getMethods(),
            List.of(
                new ExpectedMethod("benachrichtige", BenachrichtigungService.class.getName(), AccessLevel.PUBLIC,
                    new ExpectedReturnType(
                        void.class,
                        null,
                        false,
                        false,
                        false,
                        Collections.emptyList(),
                        Collections.emptyList()
                    ),
                    List.of(
                        new ExpectedParameter(
                            "arg0",
                            AuslieferungGestartet.class,
                            null,
                            false,
                            false,
                            List.of(new ExpectedAssertion(AssertionType.isNotNull, null, null,
                                "{jakarta.validation.constraints.NotNull.message}")),
                            Collections.emptyList()

                        )
                    ),
                    Collections.emptyList(),
                    AuslieferungGestartet.class.getName()
                )
            )
        );
        assertThat(Domain.getBoundedContexts().get(0).getDomainServices().contains(serviceMirror));
    }

    @Test
    public void testBestellungRepository() {
        var repositoryMirrorOpt = Domain.typeMirror(BestellungRepository.class.getName()).map(
            r -> (RepositoryMirror) r);
        assertThat(repositoryMirrorOpt).isPresent();
        var repMirror = repositoryMirrorOpt.get();
        assertThat(repMirror.getTypeName()).isEqualTo(BestellungRepository.class.getName());
        assertThat(repMirror.getInheritanceHierarchyTypeNames().get(0)).isEqualTo(Object.class.getName());
        assertThat(repMirror.getAllInterfaceTypeNames().get(0)).isEqualTo(Repository.class.getName());
        var neueBestellungEventOpt = Domain.typeMirror(NeueBestellung.class.getName()).map(e -> (DomainEventMirror) e);
        assertThat(neueBestellungEventOpt).isPresent();
        assertThat(repMirror.publishes(neueBestellungEventOpt.get())).isTrue();
        assertMethods(BestellungRepository.class, repMirror.getMethods(),
            List.of(
                new ExpectedMethod("findByStatus", BestellungRepository.class.getName(), AccessLevel.PUBLIC,
                    new ExpectedReturnType(
                        BestellungBv3.class,
                        Stream.class.getName(),
                        false,
                        false,
                        true,
                        Collections.emptyList(),
                        Collections.emptyList()
                    ),
                    List.of(
                        new ExpectedParameter(
                            "arg0",
                            BestellStatusCodeEnum.class,
                            null,
                            false,
                            false,
                            List.of(new ExpectedAssertion(AssertionType.isNotNull, null, null,
                                "{jakarta.validation.constraints.NotNull.message}")),
                            Collections.emptyList()

                        )
                    ),
                    Collections.emptyList(),
                    null
                ),
                new ExpectedMethod("findById", BestellungRepository.class.getName(), AccessLevel.PUBLIC,
                    new ExpectedReturnType(
                        BestellungBv3.class,
                        Optional.class.getName(),
                        true,
                        false,
                        false,
                        Collections.emptyList(),
                        Collections.emptyList()
                    ),
                    List.of(
                        new ExpectedParameter(
                            "arg0",
                            BestellungIdBv3.class,
                            null,
                            false,
                            false,
                            List.of(new ExpectedAssertion(AssertionType.isNotNull, null, null,
                                "{jakarta.validation.constraints.NotNull.message}")),
                            Collections.emptyList()

                        )
                    ),
                    Collections.emptyList(),
                    null
                ),
                new ExpectedMethod("create", BestellungRepository.class.getName(), AccessLevel.PUBLIC,
                    new ExpectedReturnType(
                        BestellungIdBv3.class,
                        null,
                        false,
                        false,
                        false,
                        Collections.emptyList(),
                        Collections.emptyList()
                    ),
                    List.of(
                        new ExpectedParameter(
                            "arg0",
                            BestellungBv3.class,
                            null,
                            false,
                            false,
                            List.of(new ExpectedAssertion(AssertionType.isNotNull, null, null,
                                "{jakarta.validation.constraints.NotNull.message}")),
                            Collections.emptyList()

                        )
                    ),
                    List.of(NeueBestellung.class.getName()),
                    null
                )
            )
        );

        assertThat(Domain.getBoundedContexts().get(0).getRepositories().contains(repMirror));
    }

    @Test
    public void testStarteAuslieferungCommand() {
        var commandMirrorOpt = Domain.typeMirror(StarteAuslieferung.class.getName()).map(r -> (DomainCommandMirror) r);
        assertThat(commandMirrorOpt).isPresent();
        var commandMirror = commandMirrorOpt.get();
        assertThat(commandMirror.getTypeName()).isEqualTo(StarteAuslieferung.class.getName());
        assertThat(commandMirror.getInheritanceHierarchyTypeNames().get(0)).isEqualTo(Record.class.getName());
        assertThat(commandMirror.getAllInterfaceTypeNames().get(0)).isEqualTo(DomainServiceCommand.class.getName());
        assertThat(commandMirror.getValueReferences().stream()
            .filter(vr -> vr.getName().equals("bestellungId")).findFirst()).isPresent();
        assertThat(commandMirror.getDomainServiceTarget()).isPresent();
        assertThat(commandMirror.getDomainServiceTarget().get().getTypeName()).isEqualTo(
            ZustellungsService.class.getName());
        var zustellungServiceOpt = Domain.typeMirror(ZustellungsService.class.getName()).map(
            s -> (DomainServiceMirror) s);
        assertThat(zustellungServiceOpt).isPresent();
        assertThat(zustellungServiceOpt.get().processes(commandMirror)).isTrue();
    }

    @Test
    public void testAuslieferungGestartetEvent() {
        var eventOpt = Domain.typeMirror(AuslieferungGestartet.class.getName()).map(e -> (DomainEventMirror) e);
        assertThat(eventOpt).isPresent();
        var event = eventOpt.get();
        assertThat(event.getTypeName()).isEqualTo(AuslieferungGestartet.class.getName());
        assertThat(event.getInheritanceHierarchyTypeNames().get(0)).isEqualTo(Record.class.getName());
        assertThat(event.getAllInterfaceTypeNames().get(0)).isEqualTo(DomainEvent.class.getName());
        var prop = event.getBasicFields().stream().filter(p -> p.getName().equals("premiumVersand")).findFirst();
        assertThat(prop).isPresent();
        assertThat(prop.get().isModifiable()).isFalse();
        var best = event.getAggregateRootReferences().stream()
            .filter(p -> p.getName().equals("bestellung")).findFirst();
        assertThat(best).isPresent();
        assertThat(best.get().isModifiable()).isFalse();
        assertThat(event.getListeningDomainServices().size()).isEqualTo(1);
        assertThat(event.getListeningDomainServices().get(0).getTypeName()).isEqualTo(
            BenachrichtigungService.class.getName());
        assertThat(event.getPublishingAggregates().size()).isEqualTo(1);
        assertThat(event.getPublishingAggregates().get(0).getTypeName()).isEqualTo(BestellungBv3.class.getName());
    }

    @Test
    public void testCarWithEngine() {
        var carWithEngineOpt = Domain.typeMirror(CarWithEngine.class.getName()).map(r -> (AggregateRootMirror) r);
        assertThat(carWithEngineOpt).isPresent();
        var carWithEngineMirror = carWithEngineOpt.get();
        assertThat(carWithEngineMirror.getTypeName()).isEqualTo(CarWithEngine.class.getName());
        assertThat(carWithEngineMirror.getInheritanceHierarchyTypeNames().get(0)).isEqualTo(
            VehicleExtended.class.getName());
        assertThat(carWithEngineMirror.getAllInterfaceTypeNames().size()).isEqualTo(0);
        assertThat(carWithEngineMirror.getInheritanceHierarchyTypeNames().size()).isEqualTo(4);
        var lengthCmPropOpt = carWithEngineMirror
            .getBasicFields()
            .stream()
            .filter(p -> p.getName().equals("lengthCm"))
            .findFirst();
        assertThat(lengthCmPropOpt).isPresent();
        assertThat(lengthCmPropOpt.get().getDeclaredByTypeName()).isEqualTo(VehicleExtended.class.getName());
        var lengthCmGetterOpt = carWithEngineMirror
            .getMethods()
            .stream()
            .filter(p -> p.getName().equals("getLengthCm"))
            .findFirst();
        assertThat(lengthCmGetterOpt).isPresent();
        assertThat(lengthCmGetterOpt.get().getDeclaredByTypeName()).isEqualTo(VehicleExtended.class.getName());
        var validateMethodList = carWithEngineMirror
            .getMethods()
            .stream()
            .filter(p -> p.getName().equals("validate"))
            .collect(Collectors.toList());
        assertThat(validateMethodList.size()).isEqualTo(2);
        var brandPropOpt = carWithEngineMirror
            .getValueReferences()
            .stream()
            .filter(p -> p.getName().equals("brand"))
            .findFirst();
        assertThat(brandPropOpt).isPresent();
        assertThat(brandPropOpt.get().getDeclaredByTypeName()).isEqualTo(CarWithEngine.class.getName());
    }

    @Test
    public void testExtendedEntity() throws NoSuchFieldException {
        var conreteRootOpt = Domain.typeMirror(ConcreteRoot.class.getName()).map(r -> (AggregateRootMirror) r);
        assertThat(conreteRootOpt).isPresent();
        var conreteRootMirror = conreteRootOpt.get();
        assertThat(conreteRootMirror.getTypeName()).isEqualTo(ConcreteRoot.class.getName());
        assertThat(conreteRootMirror.getInheritanceHierarchyTypeNames().get(0)).isEqualTo(AbstractRoot.class.getName());
        assertThat(conreteRootMirror.getInheritanceHierarchyTypeNames().size()).isEqualTo(4);
        assertThat(conreteRootMirror.getIdentityField()).isPresent();
        assertThat(conreteRootMirror.getIdentityField().get().getType().getTypeName()).isEqualTo(
            Identity.class.getName());
    }

    //TODO Command getAggregatetarget

    private void assertMethods(Class<?> clazz, List<MethodMirror> methodMirrors, List<ExpectedMethod> expectedMethods) {
        //assertThat(methodMirrors).hasSize(clazz.getMethods().length);
        for (var expectedMeth : expectedMethods) {
            asserMethod(methodMirrors, expectedMeth);
        }
    }

    private void assertDomainObject(String expectedTypeFullQualifiedName,
                                    String expectedExtendsTypeFullQualifiedName,
                                    String identityPropertyName,
                                    List<ExpectedProperty> expectedProperties,
                                    List<ExpectedReference> expectedValueReferences,
                                    List<ExpectedReference> expectedEntityReferences,
                                    List<String> listenedEvents,
                                    List<String> publishedEvents,
                                    List<String> processedCommands,
                                    List<String> inheritanceHierarchy

    ) {
        Optional<DomainObjectMirror> doMirrorOptional = Domain.typeMirror(expectedTypeFullQualifiedName);
        assertThat(doMirrorOptional).isPresent();
        var doMirror = doMirrorOptional.get();
        assertThat(doMirror.getInheritanceHierarchyTypeNames().get(0)).isEqualTo(expectedExtendsTypeFullQualifiedName);
        assertThat(doMirror.getBasicFields()).hasSize(expectedProperties.size());
        assertThat(doMirror.getValueReferences()).hasSize(expectedValueReferences.size());
        if (expectedEntityReferences.size() > 0) {
            var entityMirror = (EntityMirror) doMirror;
            assertThat(entityMirror.getEntityReferences()).hasSize(expectedEntityReferences.size());
        }
        for (var expectedProp : expectedProperties) {
            assertProperty(doMirror, expectedProp);
        }
        for (var expectedProp : expectedValueReferences) {
            assertReference(doMirror, expectedProp);
        }
        for (var expectedProp : expectedEntityReferences) {
            assertReference(doMirror, expectedProp);
        }
        assertThat(doMirror.getInheritanceHierarchyTypeNames()).containsExactly(
            inheritanceHierarchy.toArray(String[]::new));

        if (doMirror instanceof EntityMirror) {
            var entityMirror = (EntityMirror) doMirror;
            var identityProperty = entityMirror.getIdentityField();
            assertThat(identityProperty).isPresent();
            assertThat(identityProperty.get().getName()).isEqualTo(identityPropertyName);
            for (var event : listenedEvents) {
                var eventMirror = Domain.typeMirror(event).map(e -> (DomainEventMirror) e);
                assertThat(eventMirror).isPresent();
                assertThat(entityMirror.listensTo(eventMirror.get())).isTrue();
            }
            for (var event : publishedEvents) {
                var eventMirror = Domain.typeMirror(event).map(e -> (DomainEventMirror) e);
                assertThat(eventMirror).isPresent();
                assertThat(entityMirror.publishes(eventMirror.get())).isTrue();
            }
            for (var cmd : processedCommands) {
                var cmdMirror = Domain.typeMirror(cmd).map(c -> (DomainCommandMirror) c);
                assertThat(cmdMirror).isPresent();
                assertThat(entityMirror.processes(cmdMirror.get())).isTrue();
            }
        }
    }

    private void assertProperty(DomainObjectMirror domainObjectMirror, ExpectedProperty expectedProperty) {
        var propertyMirrorOptional = domainObjectMirror
            .getBasicFields()
            .stream()
            .filter(p -> expectedProperty.name.equals(p.getName()))
            .findFirst();
        assertThat(propertyMirrorOptional).describedAs(
            String.format("Expected Property with name '%s'", expectedProperty.name)).isPresent();
        var propertyMirror = propertyMirrorOptional.get();
        assertPropertyMirror(
            propertyMirror,
            expectedProperty.type,
            expectedProperty.name,
            expectedProperty.isModifiable,
            expectedProperty.isOptional,
            expectedProperty.containerType,
            expectedProperty.assertions,
            expectedProperty.containerAssertions);
    }

    private void assertReference(DomainObjectMirror domainObjectMirror, ExpectedReference expectedReference) {
        FieldMirror referenceMirror = null;
        if (ValueObject.class.isAssignableFrom(expectedReference.type) || Enum.class.isAssignableFrom(
            expectedReference.type) || Identity.class.isAssignableFrom(expectedReference.type)) {
            var valueReferenceMirrorOptional = domainObjectMirror
                .getValueReferences()
                .stream()
                .filter(p -> expectedReference.name.equals(p.getName()))
                .findFirst();
            assertThat(valueReferenceMirrorOptional).describedAs(
                String.format("Expected ValueObjectReference with name '%s'", expectedReference.name)).isPresent();
            referenceMirror = valueReferenceMirrorOptional.get();
        } else if (Entity.class.isAssignableFrom(expectedReference.type)) {
            var entityMirror = (EntityMirror) domainObjectMirror;
            var entityReferenceMirrorOptional = entityMirror
                .getEntityReferences()
                .stream()
                .filter(p -> expectedReference.name.equals(p.getName()))
                .findFirst();
            assertThat(entityReferenceMirrorOptional).describedAs(
                String.format("Expected EntityReference with name '%s'", expectedReference.name)).isPresent();
            referenceMirror = entityReferenceMirrorOptional.get();
        }

        assertPropertyMirror(
            referenceMirror,
            expectedReference.type,
            expectedReference.name,
            expectedReference.isModifiable,
            expectedReference.isOptional,
            expectedReference.containerType,
            expectedReference.assertions,
            expectedReference.containerAssertions);

        if (referenceMirror instanceof ValueReferenceMirror) {
            ValueReferenceMirror vrm = (ValueReferenceMirror) referenceMirror;
            assertThat(vrm.getValue()).isNotNull();
            assertThat(vrm.getType().hasCollectionContainer()).isEqualTo(expectedReference.isGroup);
        } else if (referenceMirror instanceof EntityReferenceMirror) {
            EntityReferenceMirror erm = (EntityReferenceMirror) referenceMirror;
            assertThat(erm.getEntity()).isNotNull();
            assertThat(erm.getType().hasCollectionContainer()).isEqualTo(expectedReference.isGroup);
        }
    }

    private void assertPropertyMirror(FieldMirror propertyMirror,
                                      Class<?> expectedType,
                                      String expectedName,
                                      boolean expectedIsModifiable,
                                      boolean expectedIsOptional,
                                      String expectedContainerType,
                                      List<ExpectedAssertion> expectedAssertions,
                                      List<ExpectedAssertion> expectedContainerAssertions) {
        assertThat(propertyMirror.getName()).describedAs(
            String.format("Expected property name '%s'", expectedName)).isEqualTo(expectedName);
        assertThat(propertyMirror.getType().getTypeName()).describedAs(
            String.format("Expected fullQualifiedTypeName '%s'", expectedName)).isEqualTo(expectedType.getName());
        assertThat(propertyMirror.getType().getContainerTypeName()).describedAs(
            String.format("Expected containerType '%s'", expectedContainerType)).isEqualTo(
            Optional.ofNullable(expectedContainerType));
        assertThat(propertyMirror.getType().hasOptionalContainer()).describedAs(
            String.format("Expected isOptional '%s'", expectedIsOptional)).isEqualTo(expectedIsOptional);
        assertThat(propertyMirror.isModifiable()).describedAs(
            String.format("Expected isModifiable '%s'", expectedIsModifiable)).isEqualTo(expectedIsModifiable);
        assertThat(propertyMirror.getType().getAssertions()).describedAs(
            String.format("Expected assertions size '%s'", expectedAssertions.size())).hasSize(
            expectedAssertions.size());
        assertThat(propertyMirror.getType().getContainerAssertions()).describedAs(
            String.format("Expected container assertions size '%s'", expectedContainerAssertions.size())).hasSize(
            expectedContainerAssertions.size());
        if (propertyMirror.getType().getAssertions().size() > 0) {
            for (var ea : expectedAssertions) {
                assertThat(
                    propertyMirror
                        .getType()
                        .getAssertions()
                        .stream()
                        .filter(am ->
                            am.getAssertionType().equals(ea.type)
                                && ((am.getParam1() == null && ea.param1 == null) || am.getParam1().equals(ea.param1))
                                && ((am.getParam2() == null && ea.param2 == null) || am.getParam2().equals(ea.param2))
                                && ((am.getMessage() == null && ea.message == null) || am.getMessage().equals(
                                ea.message))
                        )
                        .findFirst()
                ).describedAs(String.format(
                    "Expected assertion for property='%s' with type='%s', param1='%s', param2='%s', message='%s'",
                    expectedName, ea.type, ea.param1, ea.param2, ea.message)).isPresent();
            }
        }
        if (propertyMirror.getType().getContainerAssertions().size() > 0) {
            for (var ea : expectedContainerAssertions) {
                assertThat(
                    propertyMirror
                        .getType().getContainerAssertions()
                        .stream()
                        .filter(am ->
                            am.getAssertionType().equals(ea.type)
                                && ((am.getParam1() == null && ea.param1 == null) || am.getParam1().equals(ea.param1))
                                && ((am.getParam2() == null && ea.param2 == null) || am.getParam2().equals(ea.param2))
                                && ((am.getMessage() == null && ea.message == null) || am.getMessage().equals(
                                ea.message))
                        )
                        .findFirst()
                ).describedAs(String.format(
                    "Expected container assertion for property='%s' with type='%s', param1='%s', param2='%s', " +
                        "message='%s'",
                    expectedName, ea.type, ea.param1, ea.param2, ea.message)).isPresent();
            }
        }
    }

    private void asserMethod(List<MethodMirror> methodMirrors, ExpectedMethod method) {
        var methodMirrorOpt = methodMirrors
            .stream()
            .filter(m -> m.getName().equals(method.name))
            .findFirst();
        assertThat(methodMirrorOpt).describedAs(
            String.format("Expected Method with name '%s'", method.name)).isPresent();
        var methodMirror = methodMirrorOpt.get();
        assertThat(methodMirror.getName()).isEqualTo(method.name);
        assertThat(methodMirror.getDeclaredByTypeName()).isEqualTo(method.declaredByFullQualifiedName);
        assertThat(methodMirror.getAccessLevel()).isEqualTo(method.accessLevel);
        assertThat(methodMirror.getParameters().size()).describedAs(
            String.format("%s method parameters expected", String.valueOf(method.parameters.size()))).isEqualTo(
            method.parameters.size());
        if (methodMirror.getParameters().size() > 0) {
            for (int i = 0; i < methodMirror.getParameters().size(); i++) {
                assertParam(methodMirror.getParameters().get(i), method.parameters.get(i));
            }
        }
        assertReturnValue(methodMirror.getReturnType(), method.returnType);
        assertThat(methodMirror.getPublishedEvents().size()).describedAs(
            String.format("%s published event types expected",
                String.valueOf(method.publishedEventTypeNames.size()))).isEqualTo(
            method.publishedEventTypeNames.size());
        if (methodMirror.getPublishedEvents().size() > 0) {
            var eventNames = methodMirror.getPublishedEvents().stream()
                .map(de -> de.getTypeName())
                .collect(Collectors.toList())
                .toArray(String[]::new);
            assertThat(method.publishedEventTypeNames).containsExactly(eventNames);
        }
        assertThat(methodMirror.getListenedEvent().map(e -> e.getTypeName())).isEqualTo(
            Optional.ofNullable(method.listenedEventTypeName));
    }

    private void assertReturnValue(AssertedContainableTypeMirror returnMirror, ExpectedReturnType expectedReturnType) {
        assertThat(returnMirror.getTypeName()).describedAs(String.format("Expected fullQualifiedTypeName '%s'",
            expectedReturnType.expectedType().getName())).isEqualTo(expectedReturnType.expectedType().getName());
        assertThat(returnMirror.getContainerTypeName()).describedAs(
            String.format("Expected containerType '%s'", expectedReturnType.containerTypeName)).isEqualTo(
            Optional.ofNullable(expectedReturnType.containerTypeName));
        assertThat(returnMirror.hasOptionalContainer()).describedAs(
            String.format("Expected isOptional '%s'", expectedReturnType.optional)).isEqualTo(
            expectedReturnType.optional);
        assertThat(returnMirror.hasCollectionContainer()).describedAs(
            String.format("Expected isGroup '%s'", expectedReturnType.isGroup)).isEqualTo(expectedReturnType.isGroup);
        assertThat(returnMirror.getAssertions()).describedAs(
            String.format("Expected assertions size '%s'", expectedReturnType.assertions.size())).hasSize(
            expectedReturnType.assertions.size());
        assertThat(returnMirror.getContainerAssertions()).describedAs(
            String.format("Expected container assertions size '%s'",
                expectedReturnType.containerAssertions.size())).hasSize(expectedReturnType.containerAssertions.size());
        if (returnMirror.getAssertions().size() > 0) {
            for (var ea : expectedReturnType.assertions) {
                assertThat(
                    returnMirror
                        .getAssertions()
                        .stream()
                        .filter(am ->
                            am.getAssertionType().equals(ea.type)
                                && ((am.getParam1() == null && ea.param1 == null) || am.getParam1().equals(ea.param1))
                                && ((am.getParam2() == null && ea.param2 == null) || am.getParam2().equals(ea.param2))
                                && ((am.getMessage() == null && ea.message == null) || am.getMessage().equals(
                                ea.message))
                        )
                        .findFirst()
                ).describedAs(String.format(
                    "Expected assertion for return type with type='%s', param1='%s', param2='%s', message='%s'",
                    ea.type, ea.param1, ea.param2, ea.message)).isPresent();
            }
        }
        if (returnMirror.getContainerAssertions().size() > 0) {
            for (var ea : expectedReturnType.containerAssertions) {
                assertThat(
                    returnMirror.getContainerAssertions()
                        .stream()
                        .filter(am ->
                            am.getAssertionType().equals(ea.type)
                                && ((am.getParam1() == null && ea.param1 == null) || am.getParam1().equals(ea.param1))
                                && ((am.getParam2() == null && ea.param2 == null) || am.getParam2().equals(ea.param2))
                                && ((am.getMessage() == null && ea.message == null) || am.getMessage().equals(
                                ea.message))
                        )
                        .findFirst()
                ).describedAs(String.format(
                    "Expected container assertion for return type with type='%s', param1='%s', param2='%s', " +
                        "message='%s'",
                    ea.type, ea.param1, ea.param2, ea.message)).isPresent();
            }
        }
    }

    private void assertParam(ParamMirror paramMirror, ExpectedParameter parameter) {

        assertThat(paramMirror.getName()).describedAs(
            String.format("Expected parameter name '%s'", parameter.name)).isEqualTo(parameter.name);
        assertThat(paramMirror.getType().getTypeName()).describedAs(
            String.format("Expected fullQualifiedTypeName '%s'", parameter.expectedType.getName())).isEqualTo(
            parameter.expectedType.getName());
        assertThat(paramMirror.getType().getContainerTypeName()).describedAs(
            String.format("Expected containerType '%s'", parameter.containerTypeName)).isEqualTo(
            Optional.ofNullable(parameter.containerTypeName));
        assertThat(paramMirror.getType().hasOptionalContainer()).describedAs(
            String.format("Expected isOptional '%s'", parameter.optional)).isEqualTo(parameter.optional);
        assertThat(paramMirror.getType().hasCollectionContainer()).describedAs(
            String.format("Expected isGroup '%s'", parameter.isGroup)).isEqualTo(parameter.isGroup);
        assertThat(paramMirror.getType().getAssertions()).describedAs(
            String.format("Expected assertions size '%s'", parameter.assertions.size())).hasSize(
            parameter.assertions.size());
        assertThat(paramMirror.getType().getContainerAssertions()).describedAs(
            String.format("Expected container assertions size '%s'", parameter.containerAssertions.size())).hasSize(
            parameter.containerAssertions.size());
        if (paramMirror.getType().getAssertions().size() > 0) {
            for (var ea : parameter.assertions) {
                assertThat(
                    paramMirror
                        .getType()
                        .getAssertions()
                        .stream()
                        .filter(am ->
                            am.getAssertionType().equals(ea.type)
                                && ((am.getParam1() == null && ea.param1 == null) || am.getParam1().equals(ea.param1))
                                && ((am.getParam2() == null && ea.param2 == null) || am.getParam2().equals(ea.param2))
                                && ((am.getMessage() == null && ea.message == null) || am.getMessage().equals(
                                ea.message))
                        )
                        .findFirst()
                ).describedAs(String.format(
                    "Expected assertion for param='%s' with type='%s', param1='%s', param2='%s', message='%s'",
                    parameter.expectedType.getName(), ea.type, ea.param1, ea.param2, ea.message)).isPresent();
            }
        }
        if (paramMirror.getType().getContainerAssertions().size() > 0) {
            for (var ea : parameter.containerAssertions) {
                assertThat(
                    paramMirror
                        .getType().getContainerAssertions()
                        .stream()
                        .filter(am ->
                            am.getAssertionType().equals(ea.type)
                                && ((am.getParam1() == null && ea.param1 == null) || am.getParam1().equals(ea.param1))
                                && ((am.getParam2() == null && ea.param2 == null) || am.getParam2().equals(ea.param2))
                                && ((am.getMessage() == null && ea.message == null) || am.getMessage().equals(
                                ea.message))
                        )
                        .findFirst()
                ).describedAs(String.format(
                    "Expected container assertion for param='%s' with type='%s', param1='%s', param2='%s', " +
                        "message='%s'",
                    parameter.name, ea.type, ea.param1, ea.param2, ea.message)).isPresent();
            }
        }
    }

    private record ExpectedReturnType(
        Class<?> expectedType,
        String containerTypeName,
        boolean optional,
        boolean isGroup,
        boolean isStream,
        List<ExpectedAssertion> assertions,
        List<ExpectedAssertion> containerAssertions
    ) {
    }

    private record ExpectedParameter(String name,
                                     Class<?> expectedType,
                                     String containerTypeName,
                                     boolean optional,
                                     boolean isGroup,
                                     List<ExpectedAssertion> assertions,
                                     List<ExpectedAssertion> containerAssertions
    ) {
    }

    private record ExpectedMethod(String name,
                                  String declaredByFullQualifiedName,
                                  AccessLevel accessLevel,
                                  ExpectedReturnType returnType,
                                  List<ExpectedParameter> parameters,
                                  List<String> publishedEventTypeNames,
                                  String listenedEventTypeName) {
    }


    private record ExpectedReference(Class<?> type,
                                     String name,
                                     String declaredByFullQualifiedName,
                                     boolean isGroup,
                                     boolean isModifiable,
                                     boolean isOptional,
                                     boolean isPublicReadable,
                                     boolean isPublicWriteable,
                                     String containerType,
                                     List<ExpectedAssertion> assertions,
                                     List<ExpectedAssertion> containerAssertions) {
    }

    private record ExpectedProperty(Class<?> type,
                                    String name,

                                    String declaredByFullQualifiedName,
                                    boolean isModifiable,
                                    boolean isOptional,

                                    boolean isPublicReadable,
                                    boolean isPublicWriteable,
                                    String containerType,
                                    List<ExpectedAssertion> assertions,
                                    List<ExpectedAssertion> containerAssertions
    ) {
    }

    private record ExpectedAssertion(AssertionType type,
                                     String param1,
                                     String param2,
                                     String message) {
    }

}
