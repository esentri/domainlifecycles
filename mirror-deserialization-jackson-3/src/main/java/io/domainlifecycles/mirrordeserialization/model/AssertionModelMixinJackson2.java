package io.domainlifecycles.mirrordeserialization.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.domainlifecycles.mirror.model.AssertionType;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.AssertionModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class AssertionModelMixinJackson2 {

    @JsonCreator
    public AssertionModelMixinJackson2(
        AssertionType assertionType,
        String param1,
        String param2,
        String message
    ) {}
}