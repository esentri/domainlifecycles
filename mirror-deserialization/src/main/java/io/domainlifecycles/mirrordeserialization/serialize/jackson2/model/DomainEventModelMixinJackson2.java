package io.domainlifecycles.mirrordeserialization.serialize.jackson2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import java.util.List;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.DomainEventModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class DomainEventModelMixinJackson2 {

    @JsonCreator
    public DomainEventModelMixinJackson2(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {}

    @JsonIgnore
    public abstract List<FieldMirror> getBasicFields();

    @JsonIgnore
    public abstract List<ValueReferenceMirror> getValueReferences();

    @JsonIgnore
    public abstract List<EntityReferenceMirror> getEntityReferences();

    @JsonIgnore
    public abstract List<AggregateRootReferenceMirror> getAggregateRootReferences();

    @JsonIgnore
    public abstract List<AggregateRootMirror> getPublishingAggregates();

    @JsonIgnore
    public abstract List<DomainServiceMirror> getPublishingDomainServices();

    @JsonIgnore
    public abstract List<RepositoryMirror> getPublishingRepositories();

    @JsonIgnore
    public abstract List<AggregateRootMirror> getListeningAggregates();

    @JsonIgnore
    public abstract List<DomainServiceMirror> getListeningDomainServices();

    @JsonIgnore
    public abstract List<RepositoryMirror> getListeningRepositories();

    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getListeningApplicationServices();

    @JsonIgnore
    public abstract List<OutboundServiceMirror> getListeningOutboundServices();

    @JsonIgnore
    public abstract List<QueryHandlerMirror> getListeningQueryHandlers();

    @JsonIgnore
    public abstract List<ServiceKindMirror> getListeningServiceKinds();

    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getPublishingApplicationServices();

    @JsonIgnore
    public abstract List<OutboundServiceMirror> getPublishingOutboundServices();

    @JsonIgnore
    public abstract List<QueryHandlerMirror> getPublishingQueryHandlers();

    @JsonIgnore
    public abstract List<ServiceKindMirror> getPublishingServiceKinds();

    @JsonIgnore
    public abstract DomainType getDomainType();
}