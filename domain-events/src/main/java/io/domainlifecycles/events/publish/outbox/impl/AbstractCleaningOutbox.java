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

package io.domainlifecycles.events.publish.outbox.impl;

import io.domainlifecycles.events.publish.outbox.api.TransactionalOutbox;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The AbstractCleaningOutbox class is an abstract class that provides functionality for
 * scheduling tasks related to cleanup and delivery check in a transactional outbox.
 *
 * @author Mario Herb
 */
public abstract class AbstractCleaningOutbox implements TransactionalOutbox {

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
    private ScheduledFuture<?> cleanupFuture;
    private ScheduledFuture<?> deliveryCheckFuture;

    private int cleanUpPeriodDays = 1;
    private int deliveryCheckPeriodSeconds = 60;
    private int batchDeliveryTimeoutSeconds = 5 * 60;
    private int cleanUpAgeDays = 1;

    private boolean doCleanUp = true;

    private boolean doDeliveryCheck = true;

    public AbstractCleaningOutbox() {
        resetCleanUpSchedule();
        resetDeliveryCheckSchedule();
    }

    /**
     * The cleans up task is intended to remove older already processed DomainEvents from the transactional outbox.
     *
     * @param cleanUpAgeDays cleanup age specification in days
     */
    public abstract void cleanup(int cleanUpAgeDays);

    /**
     * The delivery check task is intended to check for batches of DomainEvents being proccessed, that are already
     * running too long
     * and that should be considered failed.
     *
     * @param batchDeliveryTimeoutSeconds timeout specification in seconds
     */
    public abstract void deliveryCheck(int batchDeliveryTimeoutSeconds);

    private void resetCleanUpSchedule() {
        if (cleanupFuture != null) {
            cleanupFuture.cancel(true);
        }
        scheduleCleanUp();
    }

    private void scheduleCleanUp() {
        if (doCleanUp) {
            cleanupFuture = this.scheduledExecutorService.scheduleAtFixedRate(
                () -> cleanup(cleanUpAgeDays),
                cleanUpPeriodDays,
                cleanUpPeriodDays,
                TimeUnit.DAYS
            );
        }
    }

    private void resetDeliveryCheckSchedule() {
        if (deliveryCheckFuture != null) {
            deliveryCheckFuture.cancel(true);
        }
        scheduleDeliveryCheck();
    }

    private void scheduleDeliveryCheck() {
        if (doDeliveryCheck) {
            deliveryCheckFuture = this.scheduledExecutorService.scheduleAtFixedRate(
                () -> deliveryCheck(batchDeliveryTimeoutSeconds),
                deliveryCheckPeriodSeconds,
                deliveryCheckPeriodSeconds,
                TimeUnit.SECONDS
            );
        }
    }

    /**
     * Sets the clean up period in days.
     * <p>
     * This method sets the clean up period in days for removing older already processed DomainEvents from the
     * transactional outbox.
     * The clean up period defines how often the clean up task will run to remove older already processed DomainEvents.
     *
     * @param cleanUpPeriodDays the clean up period in days
     */
    public void setCleanUpPeriodDays(int cleanUpPeriodDays) {
        this.cleanUpPeriodDays = cleanUpPeriodDays;
        resetCleanUpSchedule();
    }

    /**
     * Sets the delivery check period in seconds.
     * <p>
     * This method sets the delivery check period in seconds for checking batches of DomainEvents being
     * processed, that are already running too long and that should be considered failed.
     * The delivery check period defines how often the delivery check task will run to check for failed batches.
     *
     * @param deliveryCheckPeriodSeconds the delivery check period in seconds
     */
    public void setDeliveryCheckPeriodSeconds(int deliveryCheckPeriodSeconds) {
        this.deliveryCheckPeriodSeconds = deliveryCheckPeriodSeconds;
        resetDeliveryCheckSchedule();
    }

    /**
     * Sets the flag to control whether clean up should be performed on older processed DomainEvents in the
     * transactional outbox.
     *
     * @param doCleanUp the flag to indicate whether clean up should be performed
     */
    public void setDoCleanUp(boolean doCleanUp) {
        this.doCleanUp = doCleanUp;
        resetCleanUpSchedule();
    }

    /**
     * Sets the flag to control whether delivery check should be performed on batches of DomainEvents.
     *
     * @param doDeliveryCheck the flag to indicate whether delivery check should be performed
     */
    public void setDoDeliveryCheck(boolean doDeliveryCheck) {
        this.doDeliveryCheck = doDeliveryCheck;
        resetDeliveryCheckSchedule();
    }

    /**
     * Sets the batch delivery timeout in seconds.
     * <p>
     * This method sets the time period in seconds after which a batch of DomainEvents being processed should be
     * considered
     * failed. If a batch takes longer than the specified timeout to process, it will be marked as failed.
     *
     * @param batchDeliveryTimeoutSeconds the batch delivery timeout in seconds
     */
    public void setBatchDeliveryTimeoutSeconds(int batchDeliveryTimeoutSeconds) {
        this.batchDeliveryTimeoutSeconds = batchDeliveryTimeoutSeconds;
        resetDeliveryCheckSchedule();
    }

    /**
     * Sets the clean up age in days for removing older already processed DomainEvents from the transactional outbox.
     * This method updates the clean up age in days and resets the clean up schedule.
     *
     * @param cleanUpAgeDays the clean up age in days
     */
    public void setCleanUpAgeDays(int cleanUpAgeDays) {
        this.cleanUpAgeDays = cleanUpAgeDays;
        resetCleanUpSchedule();
    }

    public int getCleanUpPeriodDays() {
        return cleanUpPeriodDays;
    }

    public int getDeliveryCheckPeriodSeconds() {
        return deliveryCheckPeriodSeconds;
    }

    public int getBatchDeliveryTimeoutSeconds() {
        return batchDeliveryTimeoutSeconds;
    }

    public int getCleanUpAgeDays() {
        return cleanUpAgeDays;
    }

    public boolean isDoCleanUp() {
        return doCleanUp;
    }

    public boolean isDoDeliveryCheck() {
        return doDeliveryCheck;
    }
}
