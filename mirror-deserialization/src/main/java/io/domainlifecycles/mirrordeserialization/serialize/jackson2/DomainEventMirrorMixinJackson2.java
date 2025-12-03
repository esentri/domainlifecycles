package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
import io.domainlifecycles.mirror.model.BoundedContextModel;
import io.domainlifecycles.mirror.model.DomainEventModel;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DomainEventModel.class),
})
public interface DomainEventMirrorMixinJackson2 {

    @JsonCreator
    DomainEventMirrorMixinJackson2 init(
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
    List<AggregateRootMirror> getPublishingAggregates();

    @JsonIgnore
    List<DomainServiceMirror> getPublishingDomainServices();

    @JsonIgnore
    List<RepositoryMirror> getPublishingRepositories();

    @JsonIgnore
    List<AggregateRootMirror> getListeningAggregates();

    @JsonIgnore
    List<DomainServiceMirror> getListeningDomainServices();

    @JsonIgnore
    List<RepositoryMirror> getListeningRepositories();

    @JsonIgnore
    List<ApplicationServiceMirror> getListeningApplicationServices();

    @JsonIgnore
    List<OutboundServiceMirror> getListeningOutboundServices();

    @JsonIgnore
    List<QueryHandlerMirror> getListeningQueryHandlers();

    @JsonIgnore
    List<ServiceKindMirror> getListeningServiceKinds();

    @JsonIgnore
    List<ApplicationServiceMirror> getPublishingApplicationServices();

    @JsonIgnore
    List<OutboundServiceMirror> getPublishingOutboundServices();

    @JsonIgnore
    List<QueryHandlerMirror> getPublishingQueryHandlers();

    @JsonIgnore
    List<ServiceKindMirror> getPublishingServiceKinds();

    @JsonIgnore
    DomainType getDomainType();
}