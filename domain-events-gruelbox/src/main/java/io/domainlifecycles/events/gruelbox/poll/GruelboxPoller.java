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

package io.domainlifecycles.events.gruelbox.poll;


import com.gruelbox.transactionoutbox.TransactionOutbox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The GruelboxPoller class represents a poller that flushes the transaction outbox at a fixed rate.
 *
 * @author Mario Herb
 */
public final class GruelboxPoller {

    private static final Logger log = LoggerFactory.getLogger(GruelboxPoller.class);

    private final TransactionOutbox transactionOutbox;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final long initialDelayMs;
    private final long periodMs;

    /**
     * The GruelboxPoller class represents a poller that flushes the transaction outbox at a fixed rate.
     */
    public GruelboxPoller(TransactionOutbox transactionOutbox, long initialDelayMs, long periodMs) {
        this.transactionOutbox = Objects.requireNonNull(transactionOutbox, "A TransactionOutbox is required!");
        this.initialDelayMs = initialDelayMs;
        this.periodMs = periodMs;
        scheduler.scheduleAtFixedRate(() ->{
                try {
                    do {
                        log.info("Flushing transaction outbox!");
                    } while (transactionOutbox.flush());
                } catch (Throwable t) {
                    log.error("Error flushing transaction outbox. Pausing", t);
                }
            },
            this.initialDelayMs, this.periodMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Retrieves the TransactionOutbox associated with this GruelboxPoller.
     *
     * @return The TransactionOutbox associated with this GruelboxPoller.
     */
    public TransactionOutbox getTransactionOutbox() {
        return transactionOutbox;
    }

    /**
     * Returns the scheduler used by the GruelboxPoller for scheduling tasks.
     *
     * @return The scheduled executor service used by the GruelboxPoller.
     */
    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    /**
     * Retrieves the initial delay in milliseconds before the GruelboxPoller starts flushing the transaction outbox.
     *
     * @return the initial delay in milliseconds
     */
    public long getInitialDelayMs() {
        return initialDelayMs;
    }

    /**
     * Retrieves the period in milliseconds between flushes of the transaction outbox.
     *
     * @return The period in milliseconds between flushes.
     */
    public long getPeriodMs() {
        return periodMs;
    }
}
