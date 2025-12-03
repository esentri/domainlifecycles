package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
import io.domainlifecycles.mirror.model.BoundedContextModel;
import java.util.List;

/**
 * Jackson mixin interface for proper serialization of {@link BoundedContextModel}.
 *
 * @author Mario Herb
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BoundedContextModel.class),
})
public interface BoundedContextMirrorMixinJackson2 {

    @JsonCreator
    BoundedContextMirrorMixinJackson2 init(@JsonProperty("packageName") String packageName);

    @JsonIgnore
    List<AggregateRootMirror> getAggregateRoots();

    @JsonIgnore
    List<DomainServiceMirror> getDomainServices();

    @JsonIgnore
    List<RepositoryMirror> getRepositories();

    @JsonIgnore
    List<ReadModelMirror> getReadModels();

    @JsonIgnore
    List<DomainCommandMirror> getDomainCommands();

    @JsonIgnore
    List<DomainEventMirror> getDomainEvents();

    @JsonIgnore
    List<ApplicationServiceMirror> getApplicationServices();

    @JsonIgnore
    List<QueryHandlerMirror> getQueryHandlers();

    @JsonIgnore
    List<OutboundServiceMirror> getOutboundServices();

    @JsonIgnore
    List<ServiceKindMirror> getServiceKinds();
}