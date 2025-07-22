/*
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

package io.domainlifecycles.autoconfig.configurations.events;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.autoconfig.configurations.DlcDomainEventsAutoConfiguration;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Background processor for Gruelbox Transaction Outbox.
 * This class manages the scheduled processing of outbox transactions,
 * ensuring reliable processing of asynchronous operations.
 *
 * @author leonvoellinger
 *  @see TransactionOutbox
 *  @see DlcDomainEventsAutoConfiguration
 */
@Slf4j
public class GruelboxBackgroundProcessor {

    /**
     * The transaction outbox to be processed in the background.
     */
    private final TransactionOutbox transactionOutbox;

    /**
     * Creates a new background processor for the given transaction outbox.
     *
     * @param transactionOutbox the transaction outbox to process
     * @throws NullPointerException if transactionOutbox is null
     */
    public GruelboxBackgroundProcessor(TransactionOutbox transactionOutbox) {
        this.transactionOutbox = Objects.requireNonNull(transactionOutbox, "TransactionOutbox cannot be null");
    }

    /**
     * Scheduled method to periodically flush the transaction outbox.
     * Executes every second with an initial delay of 3 seconds.
     * Continues flushing until no more entries are available.
     */
    @Scheduled(initialDelayString = "PT3S", fixedRateString = "PT1S")
    void poll() {
        try {
            do {
                log.info("Flushing");
            } while (transactionOutbox.flush());
        } catch (Throwable t) {
            log.error("Error flushing transaction outbox. Pausing", t);
        }
    }

}
