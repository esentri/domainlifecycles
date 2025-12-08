package io.domainlifecycles.mirrordeserialization.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.ValueMirror;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.ValueReferenceModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class ValueReferenceModelMixinJackson2 extends FieldModelMixinJackson2{

    @JsonCreator
    public ValueReferenceModelMixinJackson2(
        String name,
        AssertedContainableTypeMirror type,
        AccessLevel accessLevel,
        String declaredByTypeName,
        boolean modifiable,
        boolean publicReadable,
        boolean publicWriteable,
        @JsonProperty("static") boolean isStatic,
        boolean hidden
    ){
        super(name, type, accessLevel, declaredByTypeName, modifiable, publicReadable, publicWriteable, isStatic, hidden);
    }

    @JsonIgnore
    public abstract ValueMirror getValue();
}