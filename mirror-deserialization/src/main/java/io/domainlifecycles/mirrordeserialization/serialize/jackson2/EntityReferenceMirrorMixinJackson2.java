package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.model.BoundedContextModel;
import io.domainlifecycles.mirror.model.EntityReferenceModel;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EntityReferenceModel.class),
})
public interface EntityReferenceMirrorMixinJackson2 {

    @JsonCreator
    EntityReferenceMirrorMixinJackson2 init(
        @JsonProperty("name") String name,
        @JsonProperty("type") AssertedContainableTypeMirror type,
        @JsonProperty("accessLevel") AccessLevel accessLevel,
        @JsonProperty("declaredByTypeName") String declaredByTypeName,
        @JsonProperty("modifiable") boolean modifiable,
        @JsonProperty("publicReadable") boolean publicReadable,
        @JsonProperty("publicWriteable") boolean publicWriteable,
        @JsonProperty("static") boolean isStatic,
        @JsonProperty("hidden") boolean hidden
    );

    @JsonIgnore
    EntityMirror getEntity();
}