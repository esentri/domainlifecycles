package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

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
import java.util.List;

/**
 * Generic Jackson mixin interface for proper serialization of domain type models.
 *
 * @author Mario Herb
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
public interface DomainTypeMirrorMixinJackson2 {

    @JsonCreator
    void init(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    );
}