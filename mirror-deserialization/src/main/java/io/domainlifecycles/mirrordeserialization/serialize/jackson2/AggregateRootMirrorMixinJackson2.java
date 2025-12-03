package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.model.AssertedContainableTypeModel;
import java.util.List;
import java.util.Optional;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AssertedContainableTypeModel.class),
})
public interface AggregateRootMirrorMixinJackson2 {

    @JsonCreator
    AggregateRootMirrorMixinJackson2 init(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("identityField") Optional<FieldMirror> identityField,
        @JsonProperty("concurrencyVersionField") Optional<FieldMirror> concurrencyVersionField,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    );

    @JsonIgnore
    DomainType getDomainType();
}
