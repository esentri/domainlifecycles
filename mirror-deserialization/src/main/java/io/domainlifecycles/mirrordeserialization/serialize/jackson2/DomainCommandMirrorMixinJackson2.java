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
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.model.BoundedContextModel;
import io.domainlifecycles.mirror.model.DomainCommandModel;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DomainCommandModel.class),
})
public interface DomainCommandMirrorMixinJackson2 {

    @JsonProperty
    Optional<String> getAggregateTargetIdentityTypeName();

    @JsonProperty
    Optional<String> getDomainServiceTargetTypeName();

    @JsonCreator
    DomainCommandMirrorMixinJackson2 init(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("aggregateTargetIdentityTypeName") Optional<String> aggregateTargetIdentityTypeName,
        @JsonProperty("domainServiceTargetTypeName") Optional<String> domainServiceTargetTypeName,
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
    Optional<AggregateRootMirror> getAggregateTarget();

    @JsonIgnore
    Optional<DomainServiceMirror> getDomainServiceTarget();

    @JsonIgnore
    List<ServiceKindMirror> getProcessingServiceKinds();

    @JsonIgnore
    List<ApplicationServiceMirror> getProcessingApplicationServices();

    @JsonIgnore
    List<DomainServiceMirror> getProcessingDomainServices();

    @JsonIgnore
    List<RepositoryMirror> getProcessingRepositories();

    @JsonIgnore
    List<OutboundServiceMirror> getProcessingOutboundServices();

    @JsonIgnore
    List<QueryHandlerMirror> getProcessingQueryHandlers();

    @JsonIgnore
    DomainType getDomainType();
}