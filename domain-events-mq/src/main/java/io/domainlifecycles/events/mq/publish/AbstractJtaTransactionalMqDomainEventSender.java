package io.domainlifecycles.events.mq.publish;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.jta.publish.AbstractJtaTransactionalDomainEventPublisher;
import io.domainlifecycles.events.jta.publish.JtaDomainEventSender;
import jakarta.transaction.TransactionManager;

import java.util.Objects;

public abstract class AbstractJtaTransactionalMqDomainEventSender<TOPIC> extends AbstractJtaTransactionalDomainEventPublisher {

    private final AbstractMqDomainEventPublisher<TOPIC> abstractMqDomainEventPublisher;

    public AbstractJtaTransactionalMqDomainEventSender(
        AbstractMqDomainEventPublisher<TOPIC> abstractMqDomainEventPublisher,
        TransactionManager transactionManager,
        boolean afterCommit
    ) {
        super(new MqJtaDomainEventSender(abstractMqDomainEventPublisher), transactionManager, afterCommit);
        this.abstractMqDomainEventPublisher = abstractMqDomainEventPublisher;
    }

    @Override
    protected void send(DomainEvent domainEvent) {
        abstractMqDomainEventPublisher.publish(domainEvent);
    }

    private static class MqJtaDomainEventSender implements JtaDomainEventSender{

        private final AbstractMqDomainEventPublisher abstractMqDomainEventPublisher;

        private MqJtaDomainEventSender(AbstractMqDomainEventPublisher abstractMqDomainEventPublisher) {
            this.abstractMqDomainEventPublisher = Objects.requireNonNull(abstractMqDomainEventPublisher, "AbstractMqDomainEventPublisher is required!");
        }

        @Override
        public void send(DomainEvent domainEvent) {
            abstractMqDomainEventPublisher.publish(domainEvent);
        }
    }

}
