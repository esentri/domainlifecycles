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

package io.domainlifecycles.events.spring.outbox.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.spring.outbox.api.OutboxBatch;
import io.domainlifecycles.events.spring.outbox.api.ProcessingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A subclass of AbstractCleaningOutbox that uses Spring JDBC to interact with a database holding the outbox table.
 *
 * This class provides methods to insert domain events into the outbox, fetch a batch of events
 * for sending, mark a batch as sent successfully, mark a domain event as failed, perform cleanup
 * of processed events, and check for delivery timeouts on the outbox table.
 *
 * The class requires a DataSource, ObjectMapper, and PlatformTransactionManager to be provided
 * in order to function properly. The outbox table name can also be configured.
 *
 * The fetch timeout, in seconds, can be set using the setFetchTimeoutSeconds method.
 *
 * @author MArio Herb
 */
public class SpringJdbcOutbox extends AbstractCleaningOutbox{

    private static final Logger log = LoggerFactory.getLogger(SpringJdbcOutbox.class);
    private final JdbcTemplate jdbcTemplate;
    private final PlatformTransactionManager platformTransactionManager;
    private final ObjectMapper objectMapper;

    private String outboxTableName = "outbox";
    private String insertStatementTemplate = "INSERT INTO $OUTBOX_TABLE$(id, domain_event, inserted) VALUES (?, ?, ?);";
    private String updateStatementSetBatchIdTemplate = "UPDATE $OUTBOX_TABLE$ SET batch_id = ?, delivery_started = ? WHERE id = ?;";
    private String updateStatementBatchSuccessfulTemplate = "UPDATE $OUTBOX_TABLE$ SET processing_result = 'OK' WHERE batch_id = ?;";
    private String updateStatementDomainEventFailedTemplate = "UPDATE $OUTBOX_TABLE$ SET processing_result = ? WHERE domain_event = ?;";
    private String fetchForBatchStatementTemplate = "SELECT id, domain_event, inserted, batch_id, processing_result FROM $OUTBOX_TABLE$ WHERE processing_result IS NULL ORDER BY inserted ASC LIMIT ? OFFSET 0  FOR UPDATE NOWAIT;";
    private String fetchForBatchStatementTemplateNonStrict = "SELECT id, domain_event, inserted, batch_id, processing_result FROM $OUTBOX_TABLE$ WHERE processing_result IS NULL and batch_id IS NULL ORDER BY inserted ASC LIMIT ? OFFSET 0  FOR UPDATE NOWAIT;";
    private String cleanupStatementTemplate = "DELETE FROM $OUTBOX_TABLE$ WHERE processing_result = 'OK' AND inserted < ?;";
    private String updateStatementDeliverTimeoutTemplate = "UPDATE $OUTBOX_TABLE$ SET processing_result = 'DELIVERY_TIMED_OUT' WHERE batch_id IS NOT NULL AND delivery_started < ?;";
    private String insertStatement;
    private String updateStatementSetBatchId;
    private String updateStatementBatchSuccessful;
    private String updateStatementDomainEventFailed;
    private String fetchForBatchStatement;
    private String cleanupStatement;
    private String updateStatementDeliverTimeout;
    private int fetchTimeoutSeconds = 60;
    private boolean strictBatchOrder = true;

    public SpringJdbcOutbox(DataSource dataSource, ObjectMapper objectMapper, PlatformTransactionManager platformTransactionManager) {
        this.jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource, "A dataSource must be provided for a SpringJdbcOutbox!"));
        this.objectMapper = Objects.requireNonNull(objectMapper, "An objectMapper must be provided for a SpringJdbcOutbox!");
        this.platformTransactionManager = Objects.requireNonNull(platformTransactionManager, "A platformTransactionManager must be provided for a SpringJdbcOutbox!");
        setOutboxTableName(this.outboxTableName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(DomainEvent domainEvent) {
        Objects.requireNonNull(domainEvent, "the DomainEvent cannot be NULL!");
        String domainEventString = mapDomainEvent(domainEvent);
        jdbcTemplate.update(ps -> {
            var stmt = ps.prepareStatement(insertStatement);
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, domainEventString);
            stmt.setTimestamp(3, Timestamp.from(Instant.now()));
            return stmt;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutboxBatch fetchBatchForSending(int batchSize) {
        var def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        def.setTimeout(fetchTimeoutSeconds);
        var trans = platformTransactionManager.getTransaction(def);
        List<OutboxEntry> result;
        try {
            result = jdbcTemplate.queryForStream(ps ->
                {
                    var stmt = ps.prepareStatement(this.fetchForBatchStatement);
                    stmt.setInt(1, batchSize);
                    return stmt;
                },
                (row, rownum) -> new OutboxEntry(
                    UUID.fromString(row.getString(1)),
                    readDeserializableDomainEvent(row.getString(2)),
                    row.getTimestamp(3).toLocalDateTime(),
                    row.getString(4) != null ? UUID.fromString(row.getString(4)) : null,
                    row.getString(5)
                )
            ).toList();
        } catch (QueryTimeoutException t){
            log.debug("Concurrent outbox fetch!", t);
            platformTransactionManager.rollback(trans);
            return new OutboxBatch(Collections.emptyList());
        }
        if(strictBatchOrder) {
            var batchInProcess = result.stream().anyMatch(outboxEntry -> outboxEntry.batchId != null);
            if (batchInProcess) {
                log.debug("Another batch is processed at the moment!");
                platformTransactionManager.commit(trans);
                return new OutboxBatch(Collections.emptyList());
            }
        }
        var batch = new OutboxBatch(result.stream().map(entry -> entry.domainEvent).toList());
        var deliveryStarted = Timestamp.from(Instant.now());
        for(var entry : result){
            jdbcTemplate.update(this.updateStatementSetBatchId, batch.getBatchId().toString(), deliveryStarted, entry.id().toString());
        }
        platformTransactionManager.commit(trans);
        return batch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sentSuccessfully(OutboxBatch batch) {
        var def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var trans = platformTransactionManager.getTransaction(def);
        jdbcTemplate.update(ps -> {
            var  stmt = ps.prepareStatement(this.updateStatementBatchSuccessful);
            stmt.setString(1, batch.getBatchId().toString());
            return stmt;
        });
        platformTransactionManager.commit(trans);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markFailed(DomainEvent domainEvent, ProcessingResult result) {
        Objects.requireNonNull(result,"The result cannot be NULL when marking a DomainEvent as failed!");
        var def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var trans = platformTransactionManager.getTransaction(def);
        String domainEventString = mapDomainEvent(domainEvent);
        jdbcTemplate.update(ps -> {
            var  stmt = ps.prepareStatement(this.updateStatementDomainEventFailed);
            stmt.setString(1, result.name());
            stmt.setString(2, domainEventString);
            return stmt;
        });
        platformTransactionManager.commit(trans);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanup(int cleanUpAgeDays) {
        var def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var trans = platformTransactionManager.getTransaction(def);
        var ageBoundaryTimeStamp = Timestamp.from(Instant.now().minus(cleanUpAgeDays, ChronoUnit.DAYS));
        jdbcTemplate.update(this.cleanupStatement, ageBoundaryTimeStamp);
        platformTransactionManager.commit(trans);
    }

    @Override
    public void deliveryCheck(int batchDeliveryTimeoutSeconds) {
        var def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var trans = platformTransactionManager.getTransaction(def);
        var deliveryBoundaryTimeStamp = Timestamp.from(Instant.now().minus(batchDeliveryTimeoutSeconds, ChronoUnit.SECONDS));
        jdbcTemplate.update(this.updateStatementDeliverTimeout, deliveryBoundaryTimeStamp);
        platformTransactionManager.commit(trans);
    }

    private String mapDomainEvent(DomainEvent domainEvent){
        try {
            return objectMapper.writeValueAsString(new DeserializableDomainEvent(domainEvent));
        } catch (JsonProcessingException e) {
            throw DLCEventsException.fail("Mapping  the given DomainEvent '{}' to its JSON representation failed!", e, domainEvent);
        }
    }

    private DomainEvent readDeserializableDomainEvent(String val){
        DeserializableDomainEvent deserializable = null;
        try {
            deserializable = objectMapper.readValue(val, DeserializableDomainEvent.class);
        } catch (JsonProcessingException e) {
            throw DLCEventsException.fail("Deserializing {} failed", e, val);
        }
        return deserializable.event();
    }

    /**
     * This method retrieves the fetch timeout in seconds.
     *
     * @return the fetch timeout in seconds
     */
    public int getFetchTimeoutSeconds() {
        return fetchTimeoutSeconds;
    }

    /**
     * Sets the fetch timeout in seconds.
     *
     * This method sets the fetch timeout in seconds. It determines the maximum time
     * that the fetch operation will wait for data to be fetched before timing out.
     *
     * @param fetchTimeoutSeconds the fetch timeout in seconds
     */
    public void setFetchTimeoutSeconds(int fetchTimeoutSeconds) {
        this.fetchTimeoutSeconds = fetchTimeoutSeconds;
    }

    /**
     * This method retrieves the table name of the outbox.
     *
     * @return the table name of the outbox
     */
    public String getOutboxTableName() {
        return outboxTableName;
    }

    /**
     * Sets the table name of the outbox.
     *
     * This method sets the table name of the outbox to be used for storing domain events.
     *
     * @param outboxTableName the table name of the outbox
     */
    public void setOutboxTableName(String outboxTableName) {
        this.outboxTableName = Objects.requireNonNull(outboxTableName, "The outbox table name cannot be null!");
        this.insertStatement = initializeStatement(insertStatementTemplate);
        this.updateStatementSetBatchId = initializeStatement(updateStatementSetBatchIdTemplate);
        this.updateStatementBatchSuccessful = initializeStatement(updateStatementBatchSuccessfulTemplate);
        this.updateStatementDomainEventFailed = initializeStatement(updateStatementDomainEventFailedTemplate);
        if(this.strictBatchOrder){
            this.fetchForBatchStatement = initializeStatement(this.fetchForBatchStatementTemplate);
        }else{
            this.fetchForBatchStatement = initializeStatement(this.fetchForBatchStatementTemplateNonStrict);
        }
        this.cleanupStatement = initializeStatement(cleanupStatementTemplate);
        this.updateStatementDeliverTimeout = initializeStatement(updateStatementDeliverTimeoutTemplate);
    }

    /**
     * This method returns the insert statement template used by the SpringJdbcOutbox class.
     *
     * @return the insert statement template
     */
    public String getInsertStatementTemplate() {
        return insertStatementTemplate;
    }

    /**
     * Sets the insert statement template that will be used by the SpringJdbcOutbox class.
     * Refer to the configurable outbox table name via a '$OUTBOX_TABLE$' placeholder.
     *
     * @param insertStatementTemplate the insert statement template
     */
    public void setInsertStatementTemplate(String insertStatementTemplate) {
        this.insertStatementTemplate = Objects.requireNonNull(insertStatementTemplate, "A statement template cannot be null!");
        this.insertStatement = initializeStatement(insertStatementTemplate);
    }

    /**
     * Retrieves the update statement template for setting the batch ID.
     *
     * This method returns the update statement template used by the SpringJdbcOutbox class for setting the batch ID of a batch of domain events.
     * It is used in the process of marking a batch as successful or failed.
     *
     * @return the update statement template for setting the batch ID
     */
    public String getUpdateStatementSetBatchIdTemplate() {
        return updateStatementSetBatchIdTemplate;
    }

    /**
     * Sets the update statement template for setting the batch ID.
     *
     * This method sets the update statement template used by the SpringJdbcOutbox class for setting the batch ID of a batch of domain events.
     * It is used in the process of marking a batch as successful or failed. Refer to the configurable outbox table name via a '$OUTBOX_TABLE$' placeholder.
     *
     * @param updateStatementSetBatchIdTemplate the update statement template for setting the batch ID
     */
    public void setUpdateStatementSetBatchIdTemplate(String updateStatementSetBatchIdTemplate) {
        this.updateStatementSetBatchIdTemplate = Objects.requireNonNull(updateStatementSetBatchIdTemplate, "A statement template cannot be null!");
        this.updateStatementSetBatchId = initializeStatement(updateStatementSetBatchIdTemplate);
    }

    /**
     * This method retrieves the update statement template for marking a batch as successful in the SpringJdbcOutbox class.
     *
     * @return the update statement template for marking a batch as successful
     */
    public String getUpdateStatementBatchSuccessfulTemplate() {
        return updateStatementBatchSuccessfulTemplate;
    }

    /**
     * Sets the update statement template for marking a batch as successful in the SpringJdbcOutbox class.
     *
     * This method sets the update statement template used by the SpringJdbcOutbox class for marking a batch
     * as successful. Refer to the configurable outbox table name via a '$OUTBOX_TABLE$' placeholder.
     *
     * @param updateStatementBatchSuccessfulTemplate the update statement template for marking a batch as successful
     */
    public void setUpdateStatementBatchSuccessfulTemplate(String updateStatementBatchSuccessfulTemplate) {
        this.updateStatementBatchSuccessfulTemplate = Objects.requireNonNull(updateStatementBatchSuccessfulTemplate, "A statement template cannot be null!");
        this.updateStatementBatchSuccessful = initializeStatement(updateStatementBatchSuccessfulTemplate);
    }

    /**
     * Retrieves the update statement template for marking a domain event as failed in the SpringJdbcOutbox class.
     *
     * @return the update statement template for marking a domain event as failed
     */
    public String getUpdateStatementDomainEventFailedTemplate() {
        return updateStatementDomainEventFailedTemplate;
    }

    /**
     * Sets the update statement template for marking a domain event as failed in the SpringJdbcOutbox class.
     * Refer to the configurable outbox table name via a '$OUTBOX_TABLE$' placeholder.
     *
     * @param updateStatementDomainEventFailedTemplate the update statement template for marking a domain event as failed
     */
    public void setUpdateStatementDomainEventFailedTemplate(String updateStatementDomainEventFailedTemplate) {
        this.updateStatementDomainEventFailedTemplate = Objects.requireNonNull(updateStatementDomainEventFailedTemplate, "A statement template cannot be null!");
        this.updateStatementDomainEventFailed = initializeStatement(updateStatementDomainEventFailedTemplate);
    }

    /**
     * Retrieves the fetch statement template for fetching a batch of domain events from the outbox in the SpringJdbcOutbox class.
     *
     * @return the fetch statement template for fetching a batch of domain events
     */
    public String getFetchForBatchStatementTemplate() {
        return fetchForBatchStatementTemplate;
    }

    /**
     * Sets the fetch statement template for fetching a batch of domain events from the outbox.
     *
     * This method sets the fetch statement template used by the SpringJdbcOutbox class for fetching
     * a batch of domain events from the outbox table. The template can include a '$OUTBOX_TABLE$'
     * placeholder that will be replaced with the actual outbox table name.
     *
     * @param fetchForBatchStatementTemplate the fetch statement template for fetching a batch of domain events
     * @throws NullPointerException if fetchForBatchStatementTemplate is null
     */
    public void setFetchForBatchStatementTemplate(String fetchForBatchStatementTemplate) {
        this.fetchForBatchStatementTemplate = Objects.requireNonNull(fetchForBatchStatementTemplate, "A statement template cannot be null!");
        if(this.strictBatchOrder){
            this.fetchForBatchStatement = initializeStatement(this.fetchForBatchStatementTemplate);
        }else{
            this.fetchForBatchStatement = initializeStatement(this.fetchForBatchStatementTemplateNonStrict);
        }
    }

    /**
     * Retrieves the cleanup statement template for cleaning up expired domain events in the SpringJdbcOutbox class.
     *
     * @return the cleanup statement template for cleaning up expired domain events
     */
    public String getCleanupStatementTemplate() {
        return cleanupStatementTemplate;
    }

    /**
     * Sets the cleanup statement template used by the SpringJdbcOutbox class.
     *
     * The cleanup statement template is used to create the cleanup statement that will be executed to remove expired
     * domain events from the outbox table. The template can include a '$OUTBOX_TABLE$' placeholder that will be replaced
     * with the actual outbox table name.
     *
     * @param cleanupStatementTemplate the cleanup statement template to set
     * @throws NullPointerException if cleanupStatementTemplate is null
     */
    public void setCleanupStatementTemplate(String cleanupStatementTemplate) {
        this.cleanupStatementTemplate = Objects.requireNonNull(cleanupStatementTemplate, "A statement template cannot be null!");
        this.cleanupStatement = initializeStatement(cleanupStatementTemplate);
    }

    /**
     * Retrieves the update statement template for updating the delivery timeout of a domain event in the SpringJdbcOutbox class.
     *
     * @return the update statement template for updating the delivery timeout
     */
    public String getUpdateStatementDeliverTimeoutTemplate() {
        return updateStatementDeliverTimeoutTemplate;
    }

    /**
     * Sets the update statement template for updating the delivery timeout of a domain event in the SpringJdbcOutbox class.
     * The template can include a '$OUTBOX_TABLE$' placeholder that will be replaced with the actual outbox table name.
     *
     * @param updateStatementDeliverTimeoutTemplate the update statement template for updating the delivery timeout
     */
    public void setUpdateStatementDeliverTimeoutTemplate(String updateStatementDeliverTimeoutTemplate) {
        this.updateStatementDeliverTimeoutTemplate = Objects.requireNonNull(updateStatementDeliverTimeoutTemplate, "A statement template cannot be null!");
        this.updateStatementDeliverTimeout = initializeStatement(updateStatementDeliverTimeoutTemplate);
    }

    /**
     * Sets the fetch statement template for fetching a batch of domain events from the outbox in the SpringJdbcOutbox class in case of {@code strictBatchOrder = false}.
     *
     * This method sets the fetch statement template used by the SpringJdbcOutbox class for fetching a batch of domain events from the outbox table.
     * The template can include a '$OUTBOX_TABLE$' placeholder that will be replaced with the actual outbox table name.
     * This non-strict fetch allows batches to overtake each other, even if they were inserted into the outbox in a different order.
     *
     * @param fetchForBatchStatementTemplateNonStrict the fetch statement template for fetching a batch of domain events with non-strict ordering
     * @throws NullPointerException if fetchForBatchStatementTemplateNonStrict is null
     */
    public void setFetchForBatchStatementTemplateNonStrict(String fetchForBatchStatementTemplateNonStrict) {
        this.fetchForBatchStatementTemplateNonStrict = Objects.requireNonNull(fetchForBatchStatementTemplateNonStrict, "A statement template cannot be null!");
        if(this.strictBatchOrder){
            this.fetchForBatchStatement = initializeStatement(this.fetchForBatchStatementTemplate);
        }else{
            this.fetchForBatchStatement = initializeStatement(this.fetchForBatchStatementTemplateNonStrict);
        }
    }

    /**
     * Sets the strict batch order flag.
     *
     * This method sets the flag that determines if the batch order should be strictly enforced.
     * If strictBatchOrder is set to true, the order in which domain events are inserted into the outbox will be preserved, when they are polled.
     * If strictBatchOrder is set to false, the order of domain events in a batch may be different from the order they were inserted into the outbox,
     * as batches might overtake each other, if there are multiple polling processes.
     *
     * @param strictBatchOrder the value indicating whether the strict batch order should be enforced
     */
    public void setStrictBatchOrder(boolean strictBatchOrder) {
        this.strictBatchOrder = strictBatchOrder;
        if(this.strictBatchOrder){
            this.fetchForBatchStatement = initializeStatement(this.fetchForBatchStatementTemplate);
        }else{
            this.fetchForBatchStatement = initializeStatement(this.fetchForBatchStatementTemplateNonStrict);
        }
    }

    private String initializeStatement(String statementTemplate){
        String initialized = statementTemplate.replaceAll("\\$OUTBOX_TABLE\\$", this.outboxTableName);
        log.debug("Initialized statement: " + initialized);
        return initialized;
    }

    private record DeserializableDomainEvent(@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, property="@class")DomainEvent event){}
    private record OutboxEntry(UUID id, DomainEvent domainEvent, LocalDateTime inserted, UUID batchId, String processingResult){}

}
