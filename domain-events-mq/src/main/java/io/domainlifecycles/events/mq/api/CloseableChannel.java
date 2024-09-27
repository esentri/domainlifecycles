package io.domainlifecycles.events.mq.api;

import io.domainlifecycles.events.api.Channel;

public interface CloseableChannel extends Channel {
    void close();
}
