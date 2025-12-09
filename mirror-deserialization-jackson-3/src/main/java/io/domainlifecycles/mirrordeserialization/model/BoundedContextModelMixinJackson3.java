package io.domainlifecycles.mirrordeserialization.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import java.util.List;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.BoundedContextModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class BoundedContextModelMixinJackson3 {

    @JsonCreator
    public BoundedContextModelMixinJackson3(@JsonProperty("packageName") String packageName) {}

    @JsonIgnore
    public abstract List<AggregateRootMirror> getAggregateRoots();

    @JsonIgnore
    public abstract List<DomainServiceMirror> getDomainServices();

    @JsonIgnore
    public abstract List<RepositoryMirror> getRepositories();

    @JsonIgnore
    public abstract List<ReadModelMirror> getReadModels();

    @JsonIgnore
    public abstract List<DomainCommandMirror> getDomainCommands();

    @JsonIgnore
    public abstract List<DomainEventMirror> getDomainEvents();

    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getApplicationServices();

    @JsonIgnore
    public abstract List<QueryHandlerMirror> getQueryHandlers();

    @JsonIgnore
    public abstract List<OutboundServiceMirror> getOutboundServices();

    @JsonIgnore
    public abstract List<ServiceKindMirror> getServiceKinds();
}