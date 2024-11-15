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

package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.ListensTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class TransactionalCounterService implements ApplicationService {

    private final JdbcTemplate jdbcTemplate;

    public TransactionalCounterService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        createCounterTableIfNotExists();
    }

    public void createCounterTableIfNotExists(){
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS CNT (VAL INTEGER)");
        jdbcTemplate.execute("DELETE FROM CNT");
        jdbcTemplate.execute("INSERT INTO CNT(VAL) VALUES (1)");
    }

    public void increaseCounterInTransaction(){
        if(TransactionSynchronizationManager.isActualTransactionActive()) {
            log.info("Increasing counter!");
            jdbcTemplate.execute("UPDATE CNT SET VAL = VAL + 1");
        }
    }

    public int getCurrentCounterValue(){
        int result = jdbcTemplate.queryForObject(
            "SELECT VAL FROM CNT", Integer.class);
        return result;
    }

    @ListensTo(domainEventType = CounterDomainEvent.class)
    public void counterEventSuccess(CounterDomainEvent event){
        increaseCounterInTransaction();
    }

    @ListensTo(domainEventType = CounterDomainEvent.class)
    public void counterEventFail(CounterDomainEvent event){
        increaseCounterInTransaction();
        throw new IllegalStateException("Forced failure!");
    }
}
