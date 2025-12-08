package io.domainlifecycles.mirrordeserialization.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import java.util.List;
import java.util.Optional;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.EntityModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class EntityModelMixinJackson2 extends DomainObjectModelMixinJackson2{

    @JsonCreator
    public EntityModelMixinJackson2(
        String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        List<FieldMirror> allFields,
        List<MethodMirror> methods,
        Optional<FieldMirror> identityField,
        Optional<FieldMirror> concurrencyVersionField,
        List<String> inheritanceHierarchyTypeNames,
        List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    @JsonIgnore
    public abstract List<EntityReferenceMirror> getEntityReferences();

    @JsonIgnore
    public abstract List<AggregateRootReferenceMirror> getAggregateRootReferences();

    @JsonIgnore
    public abstract List<ValueReferenceMirror> getValueReferences();

    @JsonIgnore
    public abstract List<FieldMirror> getBasicFields();

    @JsonIgnore
    public abstract List<DomainCommandMirror> processedDomainCommands();

    @JsonIgnore
    public abstract List<DomainEventMirror> publishedDomainEvents();

    @JsonIgnore
    public abstract List<DomainEventMirror> listenedDomainEvents();

    @JsonIgnore
    public abstract DomainType getDomainType();
}