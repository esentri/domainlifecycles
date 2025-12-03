package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.api.AssertionMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;

import io.domainlifecycles.mirror.model.AssertedContainableTypeModel;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AssertedContainableTypeModel.class),
})
public interface AssertedContainableTypeMirrorMixinJackson2 {

    @JsonProperty
    boolean hasOptionalContainer();

    @JsonProperty
    boolean hasCollectionContainer();

    @JsonProperty
    boolean hasSetContainer();

    @JsonProperty
    boolean hasListContainer();

    @JsonProperty
    boolean isArray();

    @JsonProperty
    boolean hasStreamContainer();

    @JsonCreator
    AssertedContainableTypeMirrorMixinJackson2 init(
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
    );
}