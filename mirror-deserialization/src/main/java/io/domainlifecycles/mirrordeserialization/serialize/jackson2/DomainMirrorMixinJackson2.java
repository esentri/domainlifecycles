package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.BoundedContextMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
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
import io.domainlifecycles.mirror.model.DomainModel;
import java.util.List;
import java.util.Map;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DomainModel.class),
})
public interface DomainMirrorMixinJackson2 {

    @JsonCreator
    DomainMirror init(
        @JsonProperty("allTypeMirrors") Map<String, ? extends DomainTypeMirror> allTypeMirrors,
        @JsonProperty("boundedContextMirrors") List<BoundedContextMirror> boundedContextMirrors
    );

    @JsonIgnore
    List<DomainTypeMirror> getAllDomainTypeMirrors();

    @JsonIgnore
    List<BoundedContextMirror> getAllBoundedContextMirrors();

    @JsonIgnore
    List<AggregateRootMirror> getAllAggregateRootMirrors();

    @JsonIgnore
    List<EntityMirror> getAllEntityMirrors();

    @JsonIgnore
    List<ValueObjectMirror> getAllValueObjectMirrors();

    @JsonIgnore
    List<EnumMirror> getAllEnumMirrors();

    @JsonIgnore
    List<ValueMirror> getAllValueMirrors();

    @JsonIgnore
    List<DomainCommandMirror> getAllDomainCommandMirrors();

    @JsonIgnore
    List<DomainEventMirror> getAllDomainEventMirrors();

    @JsonIgnore
    List<ApplicationServiceMirror> getAllApplicationServiceMirrors();

    @JsonIgnore
    List<DomainServiceMirror> getAllDomainServiceMirrors();

    @JsonIgnore
    List<RepositoryMirror> getAllRepositoryMirrors();

    @JsonIgnore
    List<ReadModelMirror> getAllReadModelMirrors();

    @JsonIgnore
    List<QueryHandlerMirror> getAllQueryHandlerMirrors();

    @JsonIgnore
    List<OutboundServiceMirror> getAllOutboundServiceMirrors();

    @JsonIgnore
    List<IdentityMirror> getAllIdentityMirrors();

    @JsonIgnore
    List<ServiceKindMirror> getAllUnspecifiedServiceKindMirrors();

    @JsonIgnore
    List<ServiceKindMirror> getAllServiceKindMirrors();
}