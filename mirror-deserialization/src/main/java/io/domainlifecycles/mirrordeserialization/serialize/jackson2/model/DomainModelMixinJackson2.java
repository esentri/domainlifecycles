package io.domainlifecycles.mirrordeserialization.serialize.jackson2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.BoundedContextMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.EnumMirror;
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;
import java.util.List;
import java.util.Map;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.DomainModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class DomainModelMixinJackson2 {

    @JsonCreator
    public DomainModelMixinJackson2(
        @JsonProperty("allDomainTypeMirrors") Map<String, ? extends DomainTypeMirror> allTypeMirrors,
        @JsonProperty("boundedContextMirrors") List<BoundedContextMirror> boundedContextMirrors) {}

    //@JsonIgnore
    public abstract List<DomainTypeMirror> getAllDomainTypeMirrors();

    //@JsonIgnore
    public abstract List<BoundedContextMirror> getAllBoundedContextMirrors();

    @JsonIgnore
    public abstract List<AggregateRootMirror> getAllAggregateRootMirrors();

    @JsonIgnore
    public abstract List<EntityMirror> getAllEntityMirrors();

    @JsonIgnore
    public abstract List<ValueObjectMirror> getAllValueObjectMirrors();

    @JsonIgnore
    public abstract List<EnumMirror> getAllEnumMirrors();

    @JsonIgnore
    public abstract List<ValueMirror> getAllValueMirrors();

    @JsonIgnore
    public abstract List<DomainCommandMirror> getAllDomainCommandMirrors();

    @JsonIgnore
    public abstract List<DomainEventMirror> getAllDomainEventMirrors();

    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getAllApplicationServiceMirrors();

    @JsonIgnore
    public abstract List<DomainServiceMirror> getAllDomainServiceMirrors();

    @JsonIgnore
    public abstract List<RepositoryMirror> getAllRepositoryMirrors();

    @JsonIgnore
    public abstract List<ReadModelMirror> getAllReadModelMirrors();

    @JsonIgnore
    public abstract List<QueryHandlerMirror> getAllQueryHandlerMirrors();

    @JsonIgnore
    public abstract List<OutboundServiceMirror> getAllOutboundServiceMirrors();

    @JsonIgnore
    public abstract List<IdentityMirror> getAllIdentityMirrors();

    @JsonIgnore
    public abstract List<ServiceKindMirror> getAllUnspecifiedServiceKindMirrors();

    @JsonIgnore
    public abstract List<ServiceKindMirror> getAllServiceKindMirrors();
}