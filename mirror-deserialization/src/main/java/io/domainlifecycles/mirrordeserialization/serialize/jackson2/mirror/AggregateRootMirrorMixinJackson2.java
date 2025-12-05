package io.domainlifecycles.mirrordeserialization.serialize.jackson2.mirror;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.model.AssertedContainableTypeModel;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.api.AggregateRootMirror}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AssertedContainableTypeModel.class),
})
public interface AggregateRootMirrorMixinJackson2 { }
