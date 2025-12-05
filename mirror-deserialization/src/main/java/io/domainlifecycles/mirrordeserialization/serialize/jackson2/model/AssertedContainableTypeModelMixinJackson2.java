package io.domainlifecycles.mirrordeserialization.serialize.jackson2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AssertionMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;
import java.util.List;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.AssertedContainableTypeModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class AssertedContainableTypeModelMixinJackson2 {

    @JsonProperty
    public boolean hasOptionalContainer;

    @JsonProperty
    public boolean hasCollectionContainer;

    @JsonProperty
    public boolean hasSetContainer;

    @JsonProperty
    public boolean hasListContainer;

    @JsonProperty
    public boolean isArray;

    @JsonProperty
    public boolean hasStreamContainer;

    @JsonCreator
    public AssertedContainableTypeModelMixinJackson2(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("domainType") DomainType domainType,
        @JsonProperty("assertions") List<AssertionMirror> assertions,
        @JsonProperty("hasOptionalContainer") boolean hasOptionalContainer,
        @JsonProperty("hasCollectionContainer") boolean hasCollectionContainer,
        @JsonProperty("hasListContainer") boolean hasListContainer,
        @JsonProperty("hasSetContainer") boolean hasSetContainer,
        @JsonProperty("hasStreamContainer") boolean hasStreamContainer,
        @JsonProperty("isArray") boolean isArray,
        @JsonProperty("containerTypeName") String containerTypeName,
        @JsonProperty("containerAssertions") List<AssertionMirror> containerAssertions,
        @JsonProperty("resolvedGenericType") ResolvedGenericTypeMirror resolvedGenericType
    ) {}
}
