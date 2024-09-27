package io.domainlifecycles.events.mq.api;


import io.domainlifecycles.events.api.ConsumingOnlyChannel;

public class AbstractMqConsumingChannel extends ConsumingOnlyChannel implements CloseableChannel {

    private final AbstractMqConsumingConfiguration consumingConfiguration;

    public AbstractMqConsumingChannel(String name, AbstractMqConsumingConfiguration consumingConfiguration) {
        super(name, consumingConfiguration);
        this.consumingConfiguration = consumingConfiguration;
    }

    @Override
    public void close() {
        consumingConfiguration.close();
    }

    @Override
    public AbstractMqConsumingConfiguration getConsumingConfiguration() {
        return consumingConfiguration;
    }
}
