package io.domainlifecycles.mirror.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryClientMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ServiceKindModel extends DomainTypeModel implements ServiceKindMirror {

    public ServiceKindModel(@JsonProperty("typeName") String typeName,
                            @JsonProperty("isAbstract") boolean isAbstract,
                            @JsonProperty("allFields") List<FieldMirror> allFields,
                            @JsonProperty("methods") List<MethodMirror> methods,
                            @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
                            @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames) {

        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean publishes(DomainEventMirror domainEvent) {
        return getMethods().stream()
            .anyMatch(m -> m.publishes(domainEvent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean listensTo(DomainEventMirror domainEvent) {
        return getMethods().stream()
            .anyMatch(m -> m.listensTo(domainEvent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean processes(DomainCommandMirror command) {
        return getMethods().stream()
            .anyMatch(m -> m.processes(command));
    }

    @Override
    public abstract DomainType getDomainType();

    @Override
    public List<ServiceKindMirror> getReferencedServiceKinds() {
        return allFields.stream()
            .filter(fieldMirror -> DomainType.SERVICE_KIND.equals(fieldMirror.getType().getDomainType()))
            .map(this::mapToServiceKindMirror).collect(Collectors.toList());
    }

    @Override
    public List<RepositoryMirror> getReferencedRepositories() {
        return allFields.stream()
            .filter(fieldMirror -> DomainType.REPOSITORY.equals(fieldMirror.getType().getDomainType()))
            .map(this::mapToRepositoryMirror).collect(Collectors.toList());
    }

    @Override
    public List<DomainServiceMirror> getReferencedDomainServices() {
        return allFields.stream()
            .filter(fieldMirror -> DomainType.DOMAIN_SERVICE.equals(fieldMirror.getType().getDomainType()))
            .map(this::mapToDomainServiceMirror).collect(Collectors.toList());
    }

    @Override
    public List<OutboundServiceMirror> getReferencedOutboundServices() {
        return allFields.stream()
            .filter(fieldMirror -> DomainType.OUTBOUND_SERVICE.equals(fieldMirror.getType().getDomainType()))
            .map(this::mapToOutboundServiceMirror).collect(Collectors.toList());
    }

    @Override
    public List<QueryClientMirror> getReferencedQueryClients() {
        return allFields.stream()
            .filter(fieldMirror -> DomainType.QUERY_CLIENT.equals(fieldMirror.getType().getDomainType()))
            .map(this::mapToQueryClientMirror).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationServiceMirror> getReferencedApplicationServices() {
        return allFields.stream()
            .filter(fieldMirror -> DomainType.APPLICATION_SERVICE.equals(fieldMirror.getType().getDomainType()))
            .map(this::mapToApplicationServiceMirror).collect(Collectors.toList());
    }

    private ServiceKindMirror mapToServiceKindMirror(FieldMirror fieldMirror) {
        return (ServiceKindMirror) Domain.typeMirror(fieldMirror.getType().getTypeName()).orElseThrow();
    }

    private RepositoryMirror mapToRepositoryMirror(FieldMirror fieldMirror) {
        return (RepositoryMirror) Domain.typeMirror(fieldMirror.getType().getTypeName()).orElseThrow();
    }

    private DomainServiceMirror mapToDomainServiceMirror(FieldMirror fieldMirror) {
        return (DomainServiceMirror) Domain.typeMirror(fieldMirror.getType().getTypeName()).orElseThrow();
    }

    private OutboundServiceMirror mapToOutboundServiceMirror(FieldMirror fieldMirror) {
        return (OutboundServiceMirror) Domain.typeMirror(fieldMirror.getType().getTypeName()).orElseThrow();
    }

    private QueryClientMirror mapToQueryClientMirror(FieldMirror fieldMirror) {
        return (QueryClientMirror) Domain.typeMirror(fieldMirror.getType().getTypeName()).orElseThrow();
    }

    private ApplicationServiceMirror mapToApplicationServiceMirror(FieldMirror fieldMirror) {
        return (ApplicationServiceMirror) Domain.typeMirror(fieldMirror.getType().getTypeName()).orElseThrow();
    }
}
