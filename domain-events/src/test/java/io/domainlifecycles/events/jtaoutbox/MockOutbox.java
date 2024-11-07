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
