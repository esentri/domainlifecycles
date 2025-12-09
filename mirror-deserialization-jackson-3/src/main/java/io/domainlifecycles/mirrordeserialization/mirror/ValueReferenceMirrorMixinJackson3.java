package io.domainlifecycles.mirrordeserialization.mirror;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.model.ValueReferenceModel;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.api.ValueReferenceMirror}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ValueReferenceModel.class),
})
public interface ValueReferenceMirrorMixinJackson3 { }