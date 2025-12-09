package io.domainlifecycles.mirrordeserialization.mirror;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.model.AggregateRootModel;
import io.domainlifecycles.mirror.model.ApplicationServiceModel;
import io.domainlifecycles.mirror.model.DomainCommandModel;
import io.domainlifecycles.mirror.model.DomainEventModel;
import io.domainlifecycles.mirror.model.DomainServiceModel;
import io.domainlifecycles.mirror.model.DomainTypeModel;
import io.domainlifecycles.mirror.model.EntityModel;
import io.domainlifecycles.mirror.model.EnumModel;
import io.domainlifecycles.mirror.model.IdentityModel;
import io.domainlifecycles.mirror.model.OutboundServiceModel;
import io.domainlifecycles.mirror.model.QueryHandlerModel;
import io.domainlifecycles.mirror.model.RepositoryModel;
import io.domainlifecycles.mirror.model.ServiceKindModel;
import io.domainlifecycles.mirror.model.ValueObjectModel;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.api.DomainTypeMirror}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EntityModel.class),
    @JsonSubTypes.Type(value = IdentityModel.class),
    @JsonSubTypes.Type(value = DomainServiceModel.class),
    @JsonSubTypes.Type(value = RepositoryModel.class),
    @JsonSubTypes.Type(value = DomainEventModel.class),
    @JsonSubTypes.Type(value = ValueObjectModel.class),
    @JsonSubTypes.Type(value = AggregateRootModel.class),
    @JsonSubTypes.Type(value = EnumModel.class),
    @JsonSubTypes.Type(value = DomainCommandModel.class),
    @JsonSubTypes.Type(value = ApplicationServiceModel.class),
    @JsonSubTypes.Type(value = QueryHandlerModel.class),
    @JsonSubTypes.Type(value = OutboundServiceModel.class),
    @JsonSubTypes.Type(value = ServiceKindModel.class),
    @JsonSubTypes.Type(value = DomainTypeModel.class),
})
public interface DomainTypeMirrorMixinJackson3 {
}