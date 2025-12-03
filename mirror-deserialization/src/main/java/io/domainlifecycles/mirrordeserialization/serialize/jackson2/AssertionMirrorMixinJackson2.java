package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.model.AssertionModel;
import io.domainlifecycles.mirror.model.AssertionType;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AssertionModel.class),
})
public interface AssertionMirrorMixinJackson2 {

    @JsonCreator
    AssertionMirrorMixinJackson2 init(
        @JsonProperty("assertionType") AssertionType assertionType,
        @JsonProperty("param1") String param1,
        @JsonProperty("param2") String param2,
        @JsonProperty("message") String message
    );
}