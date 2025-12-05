package io.domainlifecycles.mirrordeserialization.serialize.jackson2.model;

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
        @JsonProperty("name") String name,
        @JsonProperty("type") AssertedContainableTypeMirror type,
        @JsonProperty("accessLevel") AccessLevel accessLevel,
        @JsonProperty("declaredByTypeName") String declaredByTypeName,
        @JsonProperty("modifiable") boolean modifiable,
        @JsonProperty("publicReadable") boolean publicReadable,
        @JsonProperty("publicWriteable") boolean publicWriteable,
        @JsonProperty("static") boolean isStatic,
        @JsonProperty("hidden") boolean hidden
    ){
        super(name, type, accessLevel, declaredByTypeName, modifiable, publicReadable, publicWriteable, isStatic, hidden);
    }

    @JsonIgnore
    public abstract ValueMirror getValue();
}
