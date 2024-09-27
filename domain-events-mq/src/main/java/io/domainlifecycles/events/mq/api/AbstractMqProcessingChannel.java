package io.domainlifecycles.events.mq.api;

import io.domainlifecycles.events.api.ProcessingChannel;

public class AbstractMqProcessingChannel extends ProcessingChannel implements CloseableChannel {

    private final AbstractMqConsumingConfiguration consumingConfiguration;
    private final AbstractMqPublishingConfiguration publishingConfiguration;

    public AbstractMqProcessingChannel(
        String name,
        AbstractMqConsumingConfiguration consumingConfiguration,
        AbstractMqPublishingConfiguration publishingConfiguration
    ) {
        super(name, publishingConfiguration, consumingConfiguration);
        this.consumingConfiguration = consumingConfiguration;
        this.publishingConfiguration = publishingConfiguration;
    }


    @Override
    public void close() {
        consumingConfiguration.close();
        publishingConfiguration.close();
    }

    @Override
    public AbstractMqConsumingConfiguration getConsumingConfiguration() {
        return consumingConfiguration;
    }

    @Override
    public AbstractMqPublishingConfiguration getPublishingConfiguration() {
        return publishingConfiguration;
    }
}
