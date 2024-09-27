package io.domainlifecycles.events.api;

import java.util.Objects;

public class PublishingOnlyChannel implements PublishingChannel, NamedChannel{

    private final String name;
    private final PublishingConfiguration publishingConfiguration;

    public PublishingOnlyChannel(String name, PublishingConfiguration publishingConfiguration) {
        this.name = Objects.requireNonNull(name, "A name is required!");
        this.publishingConfiguration = Objects.requireNonNull(publishingConfiguration, "A PublishingConfiguration is required!");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PublishingConfiguration getPublishingConfiguration() {
        return publishingConfiguration;
    }
}
