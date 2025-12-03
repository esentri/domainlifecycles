package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.model.ParamModel;

/**
 * Jackson mixin interface for proper serialization of {@link ParamModel}.
 *
 * @author Mario Herb
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ParamModel.class),
})
public interface ParamMirrorMixinJackson2 {

    @JsonCreator
    ParamMirrorMixinJackson2 init(
        @JsonProperty("name") String name,
        @JsonProperty("type") AssertedContainableTypeMirror type
    );
}