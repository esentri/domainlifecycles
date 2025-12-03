package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import io.domainlifecycles.mirror.model.QueryHandlerModel;
import io.domainlifecycles.mirror.model.RepositoryModel;
import java.util.List;
import java.util.Optional;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = RepositoryModel.class),
})
public interface RepositoryMirrorMixinJackson2 {

    @JsonProperty
    String getManagedAggregateTypeName();

    @JsonProperty
    List<String> getRepositoryInterfaceTypeNames();

    @JsonCreator
    RepositoryMirrorMixinJackson2 init(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("managedAggregateTypeName") String managedAggregateTypeName,
        @JsonProperty("repositoryInterfaceTypeNames") List<String> repositoryInterfaceTypeNames,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    );

    @JsonIgnore
    Optional<AggregateRootMirror> getManagedAggregate();

    @JsonIgnore
    DomainType getDomainType();
}