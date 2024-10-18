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

package io.domainlifecycles.events.jtaoutbox;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.publish.outbox.api.OutboxBatch;
import io.domainlifecycles.events.publish.outbox.api.ProcessingResult;
import io.domainlifecycles.events.publish.outbox.api.TransactionalOutbox;

import java.util.ArrayList;
import java.util.List;

public class MockOutbox implements TransactionalOutbox {

    public List<MockOutboxEntry> entries = new ArrayList<>();

    @Override
    public void insert(DomainEvent domainEvent) {
        MockOutboxEntry entry = new MockOutboxEntry(domainEvent);
        entries.add(entry);
    }

    @Override
    public OutboxBatch fetchBatchForSending(int batchSize) {
        var events = new ArrayList<DomainEvent>();
        var batch = new OutboxBatch(events);
        var eventsBatched = entries.stream()
            .filter(e -> e.batchId == null)
            .map(e -> {
                e.batchId = batch.getBatchId();
                return e.domainEvent;
            })
            .toList();
        events.addAll(eventsBatched);
        return batch;
    }

    @Override
    public void sentSuccessfully(OutboxBatch batch) {
        entries.stream()
            .filter(e -> batch.getBatchId().equals(e.batchId))
            .forEach(e -> e.processingResult = ProcessingResult.OK);
    }

    @Override
    public void markFailed(DomainEvent domainEvent, ProcessingResult processingResult) {
        entries.stream()
            .filter(e -> e.domainEvent.equals(domainEvent))
            .forEach(e -> e.processingResult = processingResult);
    }
}
