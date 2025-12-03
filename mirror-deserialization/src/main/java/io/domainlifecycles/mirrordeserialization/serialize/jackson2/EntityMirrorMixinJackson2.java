package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;

import io.domainlifecycles.mirror.model.BoundedContextModel;
import io.domainlifecycles.mirror.model.EntityModel;
import java.util.List;
import java.util.Optional;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EntityModel.class),
})
public interface EntityMirrorMixinJackson2 {

    @JsonCreator
    EntityMirrorMixinJackson2 init(
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
    List<EntityReferenceMirror> getEntityReferences();

    @JsonIgnore
    List<AggregateRootReferenceMirror> getAggregateRootReferences();

    @JsonIgnore
    List<ValueReferenceMirror> getValueReferences();

    @JsonIgnore
    List<FieldMirror> getBasicFields();

    @JsonIgnore
    List<DomainCommandMirror> processedDomainCommands();

    @JsonIgnore
    List<DomainEventMirror> publishedDomainEvents();

    @JsonIgnore
    List<DomainEventMirror> listenedDomainEvents();

    @JsonIgnore
    DomainType getDomainType();
}