package io.domainlifecycles.events.gruelbox.api;



public class PollerConfiguration {

    private static final long CONSUMING_POLLER_DELAY_MS_DEFAULT = 3000;
    private static final long CONSUMING_POLLER_PERIOD_MS_DEFAULT = 1000;

    private final long pollerDelayMs;
    private final long pollerPeriodMs;

    public PollerConfiguration(
        long pollerDelayMs,
        long pollerPeriodMs
    ) {
        this.pollerDelayMs = pollerDelayMs;
        this.pollerPeriodMs = pollerPeriodMs;
    }

    public PollerConfiguration(){
        this(CONSUMING_POLLER_DELAY_MS_DEFAULT, CONSUMING_POLLER_PERIOD_MS_DEFAULT);
    }

    public long getPollerDelayMs() {
        return pollerDelayMs;
    }

    public long getPollerPeriodMs() {
        return pollerPeriodMs;
    }
}
