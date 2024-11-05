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

package io.domainlifecycles.events.activemq.gruelboxpublish;

import com.gruelbox.transactionoutbox.TransactionOutboxEntry;
import com.gruelbox.transactionoutbox.TransactionOutboxListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class MyTransactionOutboxListener implements TransactionOutboxListener {

    public Queue<TransactionOutboxEntry> successfulEntries = new ConcurrentLinkedQueue<>();
    public Queue<TransactionOutboxEntry> blockedEntries = new ConcurrentLinkedQueue<>();

    @Override
    public void success(TransactionOutboxEntry entry) {
        log.info("Entry '{}' processed successfully!", entry);
        successfulEntries.add(entry);
    }

    @Override
    public void failure(TransactionOutboxEntry entry, Throwable cause) {
        log.error("Entry '{}' failed!", entry, cause);
    }

    @Override
    public void blocked(TransactionOutboxEntry entry, Throwable cause) {
        log.error("Entry '{}' blocked!", entry, cause);
        blockedEntries.add(entry);
    }
}
