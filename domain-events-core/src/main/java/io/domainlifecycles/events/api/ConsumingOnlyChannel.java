package io.domainlifecycles.events.api;

import java.util.Objects;

public class ConsumingOnlyChannel implements ConsumingChannel, NamedChannel{

    private final String name;
    private final ConsumingConfiguration consumingConfiguration;

    public ConsumingOnlyChannel(String name, ConsumingConfiguration consumingConfiguration) {
        this.name = Objects.requireNonNull(name, "A name is required!");
        this.consumingConfiguration = Objects.requireNonNull(consumingConfiguration, "A ConsumingConfiguration is required!");

    }

    @Override
    public ConsumingConfiguration getConsumingConfiguration() {
        return consumingConfiguration;
    }

    @Override
    public String getName() {
        return name;
    }
}
