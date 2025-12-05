package io.domainlifecycles.mirrordeserialization.serialize.jackson2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import java.util.List;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.DomainServiceModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class DomainServiceModelMixinJackson2 extends ServiceKindModelMixinJackson2{

    @JsonCreator
    public DomainServiceModelMixinJackson2(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("domainServiceInterfaceTypeNames") List<String> domainServiceInterfaceTypeNames,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    @JsonIgnore
    public abstract DomainType getDomainType();

    @JsonIgnore
    public abstract List<String> getDomainServiceInterfaceTypeNames();
}
