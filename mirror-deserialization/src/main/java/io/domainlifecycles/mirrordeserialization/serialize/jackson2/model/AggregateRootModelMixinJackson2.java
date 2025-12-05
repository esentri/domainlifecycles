package io.domainlifecycles.mirrordeserialization.serialize.jackson2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import java.util.List;
import java.util.Optional;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.AggregateRootModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class AggregateRootModelMixinJackson2 extends EntityModelMixinJackson2 {

    @JsonCreator
    public AggregateRootModelMixinJackson2(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("identityField") Optional<FieldMirror> identityField,
        @JsonProperty("concurrencyVersionField") Optional<FieldMirror> concurrencyVersionField,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, identityField, concurrencyVersionField, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }
    @JsonIgnore
    public abstract DomainType getDomainType();
}
