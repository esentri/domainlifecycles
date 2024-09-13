/*
 *
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.events.spring.outbox.poll;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.spring.outbox.api.ProcessingResult;
import io.domainlifecycles.events.spring.outbox.api.TransactionalOutbox;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * The AbstractOutboxPoller class is an abstract base class that is reponsible for polling the outbox and sending the fetched domain event
 * to its corresponding consumers.
 * Subclasses of AbstractOutboxPoller must implement the send method for sending individual domain events.
 * The AbstractOutboxPoller uses a TransactionalOutbox to fetch a batch of domain events and send them periodically using a Timer.
 * The timer can be configured with a delay and a period.
 * It also provides methods to set the delay, period, and maximum batch size for sending the domain events.
 *
 * Example usage:
 *
 * AbstractOutboxPoller outboxPoller = new DirectOutboxPoller(transactionalOutbox, receivingDomainEventHandler);
 * outboxSender.setDelay(5000);
 * outboxSender.setPeriod(1000);
 * outboxSender.setMaxBatchSize(10);
 *
 * @see TransactionalOutbox
 * @see DirectOutboxPoller
 *
 * @author Mario Herb
 */
public abstract class AbstractOutboxPoller {

    private final TransactionalOutbox transactionalOutbox;
    private long pollingDelayMilliseconds = 10000;
    private long pollingPeriodMilliseconds = 1000;
    private volatile int maxBatchSize = 100;
    private final ScheduledExecutorService senderExecutorService;

    private ScheduledFuture<?> sendFuture;


    public AbstractOutboxPoller(TransactionalOutbox transactionalOutbox) {
        this.transactionalOutbox = Objects.requireNonNull(transactionalOutbox, "The OutboxSender need a non-null outbox!");
        this.senderExecutorService = Executors.newScheduledThreadPool(1);
        resetSendSchedule();
    }

    private void resetSendSchedule(){
        if(sendFuture != null){
            sendFuture.cancel(false);
        }
        sendFuture = senderExecutorService.scheduleAtFixedRate(
            ()->sendEvents(),
            pollingDelayMilliseconds,
            pollingPeriodMilliseconds,
            TimeUnit.MILLISECONDS
        );
    }

    /**
     * Sends a batch of domain events.
     *
     * The method retrieves a batch of domain events from the transactional outbox and sends each event using the send method.
     * If the send method returns true for all events in the batch, the method marks the batch as sent successfully.
     * Otherwise, it marks any failed events and does not mark the batch as sent.
     *
     * If the batch is empty, no events are sent.
     */
    protected void sendEvents(){
        var batch = transactionalOutbox.fetchBatchForSending(maxBatchSize);
        if(!batch.getDomainEvents().isEmpty()) {
            var batchSuccessfull = true;
            for (DomainEvent event : batch.getDomainEvents()){
                var res = send(event);
                if(!ProcessingResult.OK.equals(res)){
                    batchSuccessfull = false;
                    transactionalOutbox.markFailed(event, res);
                }
            }
            if(batchSuccessfull){
                transactionalOutbox.sentSuccessfully(batch);
            }
        }
    }

    /**
     * Sends a domain event and returns the processing result.
     *
     * @param domainEvent the domain event to be sent
     * @return the processing result of sending the domain event
     */
    protected abstract ProcessingResult send(DomainEvent domainEvent);

    /**
     * Sets the polling delay in milliseconds for the Outbox Poller.
     *
     * @param pollingDelayMilliseconds the polling delay in milliseconds
     */
    public void setPollingDelayMilliseconds(long pollingDelayMilliseconds) {
        this.pollingDelayMilliseconds = pollingDelayMilliseconds;
        resetSendSchedule();
    }

    /**
     * Sets the polling period in milliseconds for the Outbox Poller.
     *
     * @param pollingPeriodMilliseconds the polling period in milliseconds
     */
    public void setPollingPeriodMilliseconds(long pollingPeriodMilliseconds) {
        this.pollingPeriodMilliseconds = pollingPeriodMilliseconds;
        resetSendSchedule();
    }

    /**
     * Sets the maximum batch size for sending domain events.
     *
     * The maximum batch size determines the number of domain events that can be sent in a single batch.
     * When sending domain events, they are fetched from the outbox in batches.
     * The method sets the maximum number of events that can be included in each batch.
     * The actual number of events sent in each batch may be less than or equal to the maximum batch size.
     * If the maximum batch size is set to 0 or a negative value, no events will be sent in batches.
     *
     * @param maxBatchSize the maximum batch size for sending domain events
     */
    public void setMaxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }
}
