package io.domainlifecycles.mirrordeserialization.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.ParamModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class ParamModelMixinJackson3 {

    @JsonCreator
    public ParamModelMixinJackson3(
        @JsonProperty("name") String name,
        @JsonProperty("type") AssertedContainableTypeMirror type
    ) {}
}