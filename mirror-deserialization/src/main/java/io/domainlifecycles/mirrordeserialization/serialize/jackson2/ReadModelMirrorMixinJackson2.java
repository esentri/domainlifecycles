package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import io.domainlifecycles.mirror.model.QueryHandlerModel;
import io.domainlifecycles.mirror.model.ReadModelModel;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ReadModelModel.class),
})
public interface ReadModelMirrorMixinJackson2 {

    @JsonCreator
    ReadModelMirrorMixinJackson2 init(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    );

    @JsonIgnore
    List<FieldMirror> getBasicFields();

    @JsonIgnore
    List<ValueReferenceMirror> getValueReferences();

    @JsonIgnore
    List<EntityReferenceMirror> getEntityReferences();

    @JsonIgnore
    List<AggregateRootReferenceMirror> getAggregateRootReferences();

    @JsonIgnore
    DomainType getDomainType();
}