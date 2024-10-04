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

package io.domainlifecycles.events.springgruelboxintegrationidempotency;

import com.gruelbox.transactionoutbox.TransactionOutboxEntry;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.IdemProtectedDomainEvent;
import io.domainlifecycles.events.IdemProtectedListener;
import io.domainlifecycles.events.MyTransactionOutboxListener;
import io.domainlifecycles.events.api.DomainEvents;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = TestApplicationGruelboxIntegrationIdempotency.class)
@DirtiesContext
@Slf4j
public class GreulboxIntegrationIdempotencyTests {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private IdemProtectedListener idemProtectedListener;

    @Autowired
    private MyTransactionOutboxListener outboxListener;

    private boolean match(TransactionOutboxEntry entry, DomainEvent domainEvent){
        return domainEvent.equals(entry.getInvocation().getArgs()[0]);
    }

    @Test
    public void testProtectedListenerCommit(){
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new IdemProtectedDomainEvent("TestCommit"+ UUID.randomUUID().toString());
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);

        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(1)
            );
        assertThat(idemProtectedListener.received).contains(evt);
    }

    @Test
    public void testProtectedListenerRollback(){
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new IdemProtectedDomainEvent("TestRollback"+ UUID.randomUUID().toString());
        DomainEvents.publish(evt);

        platformTransactionManager.rollback(status);

        //then
        await()
            .pollDelay(5, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(0)
            );
        assertThat(idemProtectedListener.received).doesNotContain(evt);
    }

    @Test
    public void testProtectedListenerProtection(){
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var val = "Protected"+ UUID.randomUUID().toString();
        //when
        var evt = new IdemProtectedDomainEvent(val);
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);

        status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt2 = new IdemProtectedDomainEvent(val);
        DomainEvents.publish(evt2);

        platformTransactionManager.commit(status);

        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isGreaterThan(1)
            );
        assertThat(idemProtectedListener.received).containsOnlyOnceElementsOf(List.of(evt));
    }
}
