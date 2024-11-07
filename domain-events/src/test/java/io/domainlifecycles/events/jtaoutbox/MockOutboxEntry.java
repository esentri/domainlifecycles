package io.domainlifecycles.events.jtaoutbox;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.publish.outbox.api.ProcessingResult;

import java.time.LocalDateTime;
import java.util.UUID;

public class MockOutboxEntry {

    public final DomainEvent domainEvent;
    public LocalDateTime inserted;
    public UUID batchId;
    public ProcessingResult processingResult;

    public MockOutboxEntry(DomainEvent domainEvent) {
        this.domainEvent = domainEvent;
        this.inserted = LocalDateTime.now();
    }
}
