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

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.staticanalysis.Diagnostic;
import io.domainlifecycles.staticanalysis.DomainCalls;
import io.domainlifecycles.staticanalysis.DomainMethod;
import io.domainlifecycles.staticanalysis.serialize.DomainCallsSerializationException;
import io.domainlifecycles.staticanalysis.serialize.DomainCallsSerializer;
import tools.jackson.core.JacksonException;
import tools.jackson.core.StreamReadFeature;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

/**
 * Jackson 3 based implementation of a {@link DomainCallsSerializer}.
 * <p>
 * A {@link DomainMethod} is not serialized by embedding its {@link MethodMirror}: a mirror is only
 * meaningful wired into a fully initialized {@link DomainMirror}, so embedding it would either
 * duplicate large parts of that mirror's JSON representation or produce a half-initialized mirror
 * on the reading side. Instead, a {@link DomainMethod} is written as a compact reference - its
 * owner type name, method name and parameter type names, i.e. the information
 * {@link DomainMethod#signature()} is built from - and resolved back against a {@link DomainMirror}
 * supplied at deserialization time. That {@link DomainMirror} must therefore be the one the
 * serialized {@link DomainCalls} was analyzed against (or an equally built one), otherwise
 * deserialization fails with a {@link DomainCallsSerializationException}.
 *
 * @author Mario Herb
 */
public class JacksonDomainCallsSerializer implements DomainCallsSerializer {

    private final ObjectMapper objectMapper;

    /**
     * Constructs a new instance without pretty printing.
     */
    public JacksonDomainCallsSerializer() {
        this(false);
    }

    /**
     * Constructs a new instance.
     *
     * @param prettyPrint whether the serialized JSON should be indented
     */
    public JacksonDomainCallsSerializer(boolean prettyPrint) {
        JsonMapper.Builder jsonMapperBuilder = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (prettyPrint) {
            jsonMapperBuilder.enable(SerializationFeature.INDENT_OUTPUT);
        }

        // the stream based serialize/deserialize overloads document that they do not close the given
        // stream, leaving that to the caller - Jackson's default is to auto-close it, so that default
        // has to be turned off here to make good on that promise
        jsonMapperBuilder.configure(StreamWriteFeature.AUTO_CLOSE_TARGET, false);
        jsonMapperBuilder.configure(StreamReadFeature.AUTO_CLOSE_SOURCE, false);

        this.objectMapper = jsonMapperBuilder.build();
    }

    @Override
    public String serialize(DomainCalls domainCalls) {
        Objects.requireNonNull(domainCalls, "A DomainCalls instance must be given!");
        try {
            return objectMapper.writeValueAsString(toDto(domainCalls));
        } catch (JacksonException e) {
            throw DomainCallsSerializationException.fail("Jackson serialization of DomainCalls failed!", e);
        }
    }

    @Override
    public void serialize(DomainCalls domainCalls, OutputStream outputStream) {
        Objects.requireNonNull(domainCalls, "A DomainCalls instance must be given!");
        Objects.requireNonNull(outputStream, "An OutputStream to write to must be given!");
        try {
            objectMapper.writeValue(outputStream, toDto(domainCalls));
        } catch (JacksonException e) {
            throw DomainCallsSerializationException.fail("Jackson serialization of DomainCalls failed!", e);
        }
    }

    @Override
    public DomainCalls deserialize(String serializedDomainCalls, DomainMirror domainMirror) {
        Objects.requireNonNull(serializedDomainCalls, "The serialized DomainCalls string must be given!");
        Objects.requireNonNull(domainMirror,
            "A DomainMirror to resolve the DomainCalls' methods against must be given!");
        try {
            DomainCallsDto dto = objectMapper.readValue(serializedDomainCalls, DomainCallsDto.class);
            return fromDto(dto, domainMirror);
        } catch (JacksonException e) {
            throw DomainCallsSerializationException.fail("Jackson deserialization of DomainCalls failed!", e);
        }
    }

    @Override
    public DomainCalls deserialize(InputStream serializedDomainCalls, DomainMirror domainMirror) {
        Objects.requireNonNull(serializedDomainCalls, "The serialized DomainCalls input stream must be given!");
        Objects.requireNonNull(domainMirror,
            "A DomainMirror to resolve the DomainCalls' methods against must be given!");
        try {
            DomainCallsDto dto = objectMapper.readValue(serializedDomainCalls, DomainCallsDto.class);
            return fromDto(dto, domainMirror);
        } catch (JacksonException e) {
            throw DomainCallsSerializationException.fail("Jackson deserialization of DomainCalls failed!", e);
        }
    }

    private DomainCallsDto toDto(DomainCalls domainCalls) {
        List<CallsByCallerEntryDto> callsByCaller = domainCalls.callers().stream()
            .map(caller -> new CallsByCallerEntryDto(
                toDto(caller),
                domainCalls.callsFor(caller).callSites().stream().map(this::toDto).toList()))
            .toList();
        return new DomainCallsDto(callsByCaller, domainCalls.diagnostics());
    }

    private CallSiteDto toDto(DomainCalls.CallSite callSite) {
        return new CallSiteDto(toDto(callSite.called()), callSite.callSiteTypeName(), callSite.lineNumber());
    }

    private DomainMethodDto toDto(DomainMethod domainMethod) {
        return new DomainMethodDto(
            domainMethod.typeName(), domainMethod.mirror().getName(), parameterTypeNames(domainMethod.mirror()));
    }

    private DomainCalls fromDto(DomainCallsDto dto, DomainMirror domainMirror) {
        DomainCalls.Builder builder = DomainCalls.builder();
        for (CallsByCallerEntryDto entry : dto.callsByCaller()) {
            DomainMethod caller = resolve(entry.caller(), domainMirror);
            List<DomainCalls.CallSite> callSites = entry.callSites().stream()
                .map(callSiteDto -> new DomainCalls.CallSite(
                    resolve(callSiteDto.called(), domainMirror),
                    callSiteDto.callSiteTypeName(),
                    callSiteDto.lineNumber()))
                .toList();
            builder.add(caller, callSites);
        }
        builder.addAll(dto.diagnostics());
        return builder.build();
    }

    private DomainMethod resolve(DomainMethodDto dto, DomainMirror domainMirror) {
        DomainTypeMirror typeMirror = domainMirror.<DomainTypeMirror>getDomainTypeMirror(dto.typeName())
            .orElseThrow(() -> DomainCallsSerializationException.fail(
                "Could not resolve a DomainMethod: type '%s' is unknown to the given DomainMirror.",
                dto.typeName()));
        MethodMirror method = typeMirror.getMethods().stream()
            .filter(candidate -> candidate.getName().equals(dto.methodName()))
            .filter(candidate -> parameterTypeNames(candidate).equals(dto.parameterTypeNames()))
            .findFirst()
            .orElseThrow(() -> DomainCallsSerializationException.fail(
                "Could not resolve a DomainMethod: '%s(%s)' is unknown on type '%s'.",
                dto.methodName(), String.join(", ", dto.parameterTypeNames()), dto.typeName()));
        return new DomainMethod(dto.typeName(), method);
    }

    private List<String> parameterTypeNames(MethodMirror method) {
        return method.getParameters().stream().map(param -> param.getType().getTypeName()).toList();
    }

    /**
     * Wire format of a {@link DomainCalls} instance. {@code callsByCaller} corresponds to
     * {@link DomainCalls#callers()} together with {@link DomainCalls#callsFor(DomainMethod)}; the
     * reverse {@link DomainCalls#callersOf(DomainMethod)} index is derived and rebuilt by
     * {@link DomainCalls.Builder#build()}, so it is not part of the wire format.
     */
    private record DomainCallsDto(List<CallsByCallerEntryDto> callsByCaller, List<Diagnostic> diagnostics) {
    }

    /**
     * Wire format of one caller and the call sites found in it, i.e. one entry of
     * {@link DomainCalls#callers()}.
     */
    private record CallsByCallerEntryDto(DomainMethodDto caller, List<CallSiteDto> callSites) {
    }

    /**
     * Wire format of a {@link DomainCalls.CallSite}.
     */
    private record CallSiteDto(DomainMethodDto called, String callSiteTypeName, int lineNumber) {
    }

    /**
     * Wire format of a {@link DomainMethod}: a compact reference, resolved back against a
     * {@link DomainMirror} at deserialization time (see the class javadoc).
     */
    private record DomainMethodDto(String typeName, String methodName, List<String> parameterTypeNames) {
    }
}
