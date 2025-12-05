package io.domainlifecycles.mirrordeserialization.serialize.jackson2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import java.util.List;
import java.util.Optional;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.RepositoryModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class RepositoryModelMixinJackson2 {

    @JsonProperty
    public abstract String getManagedAggregateTypeName();

    @JsonProperty
    public abstract List<String> getRepositoryInterfaceTypeNames();

    @JsonCreator
    public RepositoryModelMixinJackson2(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("managedAggregateTypeName") String managedAggregateTypeName,
        @JsonProperty("repositoryInterfaceTypeNames") List<String> repositoryInterfaceTypeNames,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {}

    @JsonIgnore
    public abstract Optional<AggregateRootMirror> getManagedAggregate();

    @JsonIgnore
    public abstract DomainType getDomainType();
}