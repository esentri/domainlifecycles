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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.staticanalysis.serialize.jackson3;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import io.domainlifecycles.staticanalysis.Diagnostic;
import io.domainlifecycles.staticanalysis.DomainCalls;
import io.domainlifecycles.staticanalysis.DomainMethod;
import io.domainlifecycles.staticanalysis.serialize.DomainCallsSerializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Round-trips a hand-built {@link DomainCalls} - built the same way {@code FlowFilteredDiagramTest}
 * in the domain-diagrammer module does, without a real static analysis run - through JSON, resolving
 * the deserialized result back against the very {@link io.domainlifecycles.mirror.api.DomainMirror}
 * it was built against.
 */
public class JacksonDomainCallsSerializerTest {

    private static final String ZUSTELLUNGS_SERVICE =
        "tests.shared.complete.onlinehandel.zustellung.ZustellungsService";
    private static final String REPOSITORY =
        "tests.shared.complete.onlinehandel.bestellung.BestellungRepository";
    private static final String AGGREGATE =
        "tests.shared.complete.onlinehandel.bestellung.BestellungBv3";

    private final JacksonDomainCallsSerializer serializer = new JacksonDomainCallsSerializer(true);

    @BeforeEach
    void initializeMirror() {
        var factory = new ReflectiveDomainMirrorFactory("tests.shared");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
    }

    @Test
    void roundTripsCallsAndDiagnostics() {
        var caller = domainMethod(ZUSTELLUNGS_SERVICE, "liefereAus");
        var original = DomainCalls.builder()
            .add(caller, List.of(
                new DomainCalls.CallSite(domainMethod(REPOSITORY, "findById"), ZUSTELLUNGS_SERVICE, 42),
                new DomainCalls.CallSite(domainMethod(AGGREGATE, "starteLieferung"), ZUSTELLUNGS_SERVICE, 44),
                new DomainCalls.CallSite(domainMethod(REPOSITORY, "update"), ZUSTELLUNGS_SERVICE, 45)))
            .add(Diagnostic.typeNotOnClasspath("does.not.Exist"))
            .build();

        var json = serializer.serialize(original);
        var deserialized = serializer.deserialize(json, Domain.getDomainMirror());

        assertThat(deserialized.callers()).isEqualTo(original.callers());
        for (DomainMethod domainMethod : original.callers()) {
            assertThat(deserialized.callsFor(domainMethod).callSites())
                .isEqualTo(original.callsFor(domainMethod).callSites());
        }
        assertThat(deserialized.callersOf(domainMethod(REPOSITORY, "findById")))
            .isEqualTo(original.callersOf(domainMethod(REPOSITORY, "findById")));
        assertThat(deserialized.diagnostics()).isEqualTo(original.diagnostics());
        assertThat(deserialized.isComplete()).isEqualTo(original.isComplete()).isFalse();
        assertThat(deserialized.size()).isEqualTo(original.size());
    }

    @Test
    void roundTripsAnEmptyResult() {
        var original = DomainCalls.builder().build();

        var deserialized = serializer.deserialize(serializer.serialize(original), Domain.getDomainMirror());

        assertThat(deserialized.callers()).isEmpty();
        assertThat(deserialized.diagnostics()).isEmpty();
        assertThat(deserialized.isComplete()).isTrue();
    }

    @Test
    void doesNotSerializeTheDerivedCallersIndex() {
        var caller = domainMethod(ZUSTELLUNGS_SERVICE, "liefereAus");
        var domainCalls = DomainCalls.builder()
            .add(caller, List.of(
                new DomainCalls.CallSite(domainMethod(REPOSITORY, "findById"), ZUSTELLUNGS_SERVICE, 42)))
            .build();

        var json = serializer.serialize(domainCalls);

        assertThat(json).doesNotContain("callersByCalled");
    }

    @Test
    void deserializationFailsForAnUnresolvableTypeReference() {
        var json = """
            {"callsByCaller":[{"caller":{"typeName":"does.not.Exist","methodName":"foo","parameterTypeNames":[]},"callSites":[]}],"diagnostics":[]}""";

        assertThatThrownBy(() -> serializer.deserialize(json, Domain.getDomainMirror()))
            .isInstanceOf(DomainCallsSerializationException.class)
            .hasMessageContaining("does.not.Exist");
    }

    @Test
    void deserializationFailsForAnUnresolvableMethodReference() {
        var json = """
            {"callsByCaller":[{"caller":{"typeName":"%s","methodName":"doesNotExist","parameterTypeNames":[]},"callSites":[]}],"diagnostics":[]}"""
            .formatted(ZUSTELLUNGS_SERVICE);

        assertThatThrownBy(() -> serializer.deserialize(json, Domain.getDomainMirror()))
            .isInstanceOf(DomainCallsSerializationException.class)
            .hasMessageContaining("doesNotExist")
            .hasMessageContaining(ZUSTELLUNGS_SERVICE);
    }

    private static DomainMethod domainMethod(String typeName, String methodName) {
        var typeMirror = Domain.getDomainMirror().getDomainTypeMirror(typeName).orElseThrow();
        var method = typeMirror.getMethods().stream()
            .filter(m -> m.getName().equals(methodName))
            .findFirst()
            .orElseThrow();
        return new DomainMethod(typeMirror.getTypeName(), method);
    }
}
