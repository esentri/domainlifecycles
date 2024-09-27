package io.domainlifecycles.events.gruelbox.api;

import java.time.Duration;

public class PublishingSchedulerConfiguration {

    private static final Duration PUBLISHING_SCHEDULING_DELAY_DEFAULT = Duration.ZERO;
    private static final boolean PUBLISHING_ORDERED_BY_DOMAIN_EVENT_TYPE_DEFAULT = false;

    private final Duration schedulingDelay;
    private final boolean orderedByDomainEventType;

    public PublishingSchedulerConfiguration(Duration schedulingDelay, boolean orderedByDomainEventType) {
        this.schedulingDelay = schedulingDelay;
        this.orderedByDomainEventType = orderedByDomainEventType;
    }

    public PublishingSchedulerConfiguration(){
        this(PUBLISHING_SCHEDULING_DELAY_DEFAULT, PUBLISHING_ORDERED_BY_DOMAIN_EVENT_TYPE_DEFAULT);
    }

    public Duration getSchedulingDelay() {
        return schedulingDelay;
    }

    public boolean isOrderedByDomainEventType() {
        return orderedByDomainEventType;
    }
}
