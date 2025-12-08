package io.domainlifecycles.mirrordeserialization.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import java.util.List;
import java.util.Optional;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.DomainCommandModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class DomainCommandModelMixinJackson2 extends DomainTypeModelMixinJackson2{

    public Optional<String> aggregateTargetIdentityTypeName;

    public Optional<String> domainServiceTargetTypeName;

    @JsonCreator
    public DomainCommandModelMixinJackson2(
        String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        List<FieldMirror> allFields,
        List<MethodMirror> methods,
        Optional<String> aggregateTargetIdentityTypeName,
        Optional<String> domainServiceTargetTypeName,
        List<String> inheritanceHierarchyTypeNames,
        List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    @JsonIgnore
    public abstract List<FieldMirror> getBasicFields();

    @JsonIgnore
    public abstract List<ValueReferenceMirror> getValueReferences();

    @JsonIgnore
    public abstract List<EntityReferenceMirror> getEntityReferences();

    @JsonIgnore
    public abstract List<AggregateRootReferenceMirror> getAggregateRootReferences();

    @JsonIgnore
    public abstract Optional<AggregateRootMirror> getAggregateTarget();

    @JsonIgnore
    public abstract Optional<DomainServiceMirror> getDomainServiceTarget();

    @JsonIgnore
    public abstract List<ServiceKindMirror> getProcessingServiceKinds();

    @JsonIgnore
    public abstract List<ApplicationServiceMirror> getProcessingApplicationServices();

    @JsonIgnore
    public abstract List<DomainServiceMirror> getProcessingDomainServices();

    @JsonIgnore
    public abstract List<RepositoryMirror> getProcessingRepositories();

    @JsonIgnore
    public abstract List<OutboundServiceMirror> getProcessingOutboundServices();

    @JsonIgnore
    public abstract List<QueryHandlerMirror> getProcessingQueryHandlers();

    @JsonIgnore
    public abstract DomainType getDomainType();
}