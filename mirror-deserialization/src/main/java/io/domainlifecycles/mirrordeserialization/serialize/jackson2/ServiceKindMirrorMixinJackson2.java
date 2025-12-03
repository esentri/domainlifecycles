package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.model.RepositoryModel;
import io.domainlifecycles.mirror.model.ServiceKindModel;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ServiceKindModel.class),
})
public interface ServiceKindMirrorMixinJackson2 {

    @JsonCreator
    ServiceKindMirrorMixinJackson2 init(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    );

    @JsonIgnore
    DomainType getDomainType();

    @JsonIgnore
    List<ServiceKindMirror> getReferencedServiceKinds();

    @JsonIgnore
    List<RepositoryMirror> getReferencedRepositories();

    @JsonIgnore
    List<DomainServiceMirror> getReferencedDomainServices();

    @JsonIgnore
    List<OutboundServiceMirror> getReferencedOutboundServices();

    @JsonIgnore
    List<QueryHandlerMirror> getReferencedQueryHandlers();

    @JsonIgnore
    List<ApplicationServiceMirror> getReferencedApplicationServices();

    @JsonIgnore
    List<DomainCommandMirror> processedDomainCommands();

    @JsonIgnore
    List<DomainEventMirror> publishedDomainEvents();

    @JsonIgnore
    List<DomainEventMirror> listenedDomainEvents();
}