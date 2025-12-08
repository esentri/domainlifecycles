package io.domainlifecycles.mirrordeserialization.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import java.util.List;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.DomainObjectModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class DomainObjectModelMixinJackson2 extends DomainTypeModelMixinJackson2{

    @JsonCreator
    public DomainObjectModelMixinJackson2(
        String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        List<FieldMirror> allFields,
        List<MethodMirror> methods,
        List<String> inheritanceHierarchyTypeNames,
        List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    @JsonIgnore
    public abstract List<FieldMirror> getBasicFields();

    @JsonIgnore
    public abstract List<ValueReferenceMirror> getValueReferences();
}