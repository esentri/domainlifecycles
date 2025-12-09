package io.domainlifecycles.mirrordeserialization.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.util.List;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.ServiceKindModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class ServiceKindModelMixinJackson3 extends DomainTypeModelMixinJackson3 {

    @JsonCreator
    public ServiceKindModelMixinJackson3(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    @JsonIgnore
    public abstract DomainType getDomainType();

    @JsonIgnore
    public abstract List<ServiceKindMirror> getReferencedServiceKinds();

    @JsonIgnore
    public abstract List<RepositoryMirror> getReferencedRepositories();

    @JsonIgnore
    public abstract List<DomainServiceMirror> getReferencedDomainServices();

    @JsonIgnore
    public abstract List<OutboundServiceMirror> getReferencedOutboundServices();

    @JsonIgnore
    public abstract List<QueryHandlerMirror> getReferencedQueryHandlers();

    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getReferencedApplicationServices();

    @JsonIgnore
    public abstract List<DomainCommandMirror> processedDomainCommands();

    @JsonIgnore
    public abstract List<DomainEventMirror> publishedDomainEvents();

    @JsonIgnore
    public abstract List<DomainEventMirror> listenedDomainEvents();
}
