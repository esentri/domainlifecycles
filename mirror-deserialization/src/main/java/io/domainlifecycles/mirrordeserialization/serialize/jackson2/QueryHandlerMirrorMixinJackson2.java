package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import io.domainlifecycles.mirror.model.OutboundServiceModel;
import io.domainlifecycles.mirror.model.QueryHandlerModel;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = QueryHandlerModel.class),
})
public interface QueryHandlerMirrorMixinJackson2 {

    @JsonProperty
    String getProvidedReadModelTypeName();

    @JsonProperty
    List<String> getQueryHandlerInterfaceTypeNames();

    @JsonCreator
    QueryHandlerMirrorMixinJackson2 init(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("queryHandlerInterfaceTypeNames") List<String> queryHandlerInterfaceTypeNames,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames,
        @JsonProperty("providedReadModelTypeName") String providedReadModelTypeName
    );
}