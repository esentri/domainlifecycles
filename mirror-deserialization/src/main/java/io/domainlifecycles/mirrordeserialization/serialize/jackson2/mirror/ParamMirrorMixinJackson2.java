package io.domainlifecycles.mirrordeserialization.serialize.jackson2.mirror;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.model.ParamModel;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.api.ParamMirror}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ParamModel.class),
})
public interface ParamMirrorMixinJackson2 { }