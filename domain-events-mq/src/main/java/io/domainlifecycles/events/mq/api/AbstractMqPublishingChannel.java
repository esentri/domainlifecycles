package io.domainlifecycles.events.mq.api;

import io.domainlifecycles.events.api.PublishingOnlyChannel;

public class AbstractMqPublishingChannel extends PublishingOnlyChannel implements CloseableChannel {

    private final AbstractMqPublishingConfiguration abstractMqPublishingConfiguration;

    public AbstractMqPublishingChannel(String name, AbstractMqPublishingConfiguration abstractMqPublishingConfiguration) {
        super(name, abstractMqPublishingConfiguration);
        this.abstractMqPublishingConfiguration = abstractMqPublishingConfiguration;
    }

    @Override
    public void close() {
        abstractMqPublishingConfiguration.close();
    }
}
