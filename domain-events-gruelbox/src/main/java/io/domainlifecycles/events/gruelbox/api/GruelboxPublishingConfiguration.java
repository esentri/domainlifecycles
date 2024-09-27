package io.domainlifecycles.events.gruelbox.api;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.events.api.PublishingConfiguration;
import io.domainlifecycles.events.gruelbox.publish.GruelboxDomainEventPublisher;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import java.util.Objects;

class GruelboxPublishingConfiguration implements PublishingConfiguration {

    private final TransactionOutbox transactionOutbox;
    private final GruelboxDomainEventPublisher gruelboxDomainEventPublisher;

    GruelboxPublishingConfiguration(
        TransactionOutbox transactionOutbox,
        PublishingSchedulerConfiguration publishingSchedulerConfiguration
    ) {
        this.transactionOutbox = Objects.requireNonNull(transactionOutbox, "A TransactionOutbox is required!");
        this.gruelboxDomainEventPublisher = new GruelboxDomainEventPublisher(
            this.transactionOutbox,
            publishingSchedulerConfiguration
        );
    }

    @Override
    public DomainEventPublisher domainEventPublisher() {
        return gruelboxDomainEventPublisher;
    }

    public TransactionOutbox getTransactionOutbox() {
        return transactionOutbox;
    }
}
