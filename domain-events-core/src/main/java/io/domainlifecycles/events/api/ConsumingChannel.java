package io.domainlifecycles.events.api;


public interface ConsumingChannel extends NamedChannel{

    ConsumingConfiguration getConsumingConfiguration();
}
