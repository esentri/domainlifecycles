package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import io.domainlifecycles.mirror.model.ApplicationServiceModel;
import io.domainlifecycles.mirror.model.BoundedContextModel;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ApplicationServiceModel.class),
})
public interface ApplicationServiceMirrorMixinJackson2 {

    @JsonCreator
    ApplicationServiceMirrorMixinJackson2 init(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("applicationServiceInterfaceTypeNames") List<String> applicationServiceInterfaceTypeNames,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    );

    @JsonIgnore
    DomainType getDomainType();

    @JsonIgnore
    List<String> getApplicationServiceInterfaceTypeNames();
}