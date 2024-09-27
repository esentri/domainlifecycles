package io.domainlifecycles.events.api;

public interface PublishingChannel extends NamedChannel{

    PublishingConfiguration getPublishingConfiguration();
}
