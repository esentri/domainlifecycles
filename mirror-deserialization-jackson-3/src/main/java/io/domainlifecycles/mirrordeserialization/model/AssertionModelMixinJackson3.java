package io.domainlifecycles.mirrordeserialization.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.model.AssertionType;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.AssertionModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class AssertionModelMixinJackson3 {

    @JsonCreator
    public AssertionModelMixinJackson3(
        @JsonProperty("assertionType") AssertionType assertionType,
        @JsonProperty("param1") String param1,
        @JsonProperty("param2") String param2,
        @JsonProperty("message") String message
    ) {}
}