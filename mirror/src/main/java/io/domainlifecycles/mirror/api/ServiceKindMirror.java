package io.domainlifecycles.mirror.api;

import java.util.List;

public interface ServiceKindMirror extends DomainTypeMirror, DomainCommandProcessingMirror, DomainEventProcessingMirror {

    /**
     * @return the list of referenced {@link ServiceKindMirror} instances.
     */
    List<ServiceKindMirror> getReferencedServiceKinds();

    /**
     * @return the list of referenced {@link RepositoryMirror} instances.
     */
    List<RepositoryMirror> getReferencedRepositories();

    /**
     * @return the list of referenced {@link DomainServiceMirror} instances.
     */
    List<DomainServiceMirror> getReferencedDomainServices();

    /**
     * @return the list of referenced {@link OutboundServiceMirror} instances.
     */
    List<OutboundServiceMirror> getReferencedOutboundServices();

    /**
     * @return the list of referenced {@link QueryClientMirror} instances.
     */
    List<QueryClientMirror> getReferencedQueryClients();

    /**
     * @return the list of referenced {@link ApplicationServiceMirror} instances.
     */
    List<ApplicationServiceMirror> getReferencedApplicationServices();
}
