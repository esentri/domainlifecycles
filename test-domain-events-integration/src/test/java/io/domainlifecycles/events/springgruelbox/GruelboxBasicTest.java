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

package io.domainlifecycles.events.springgruelbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.events.AComplexDomainEvent;
import io.domainlifecycles.events.ADomainEvent;
import io.domainlifecycles.events.ADomainEventCarryingAnId;
import io.domainlifecycles.events.AValueObject;
import io.domainlifecycles.events.AnApplicationService;
import io.domainlifecycles.events.AnIdentity;
import io.domainlifecycles.events.AnotherApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@SpringBootTest(classes = TestApplicationGruelbox.class)
@Slf4j
public class GruelboxBasicTest {

    @Autowired
    private TransactionOutbox outbox;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private AnApplicationService anApplicationService;

    @Autowired
    private AnotherApplicationService anotherApplicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext
    public void testTransactionOutbox(){
        var val = new ADomainEvent("GruelboxEvent");
        transactionTemplate.executeWithoutResult((status)->{
            outbox.with().ordered("topic1").delayForAtLeast(Duration.ZERO).schedule(AnApplicationService.class).onADomainEvent(val);
        });

        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(anApplicationService.received)
                    .contains(val)
            );
    }

    @Test
    @DirtiesContext
    public void testTransactionOutboxDeSerialization(){
        var val = new ADomainEventCarryingAnId(new AnIdentity(5l));
        transactionTemplate.executeWithoutResult((status)->{
            outbox.with().ordered("topicId").delayForAtLeast(Duration.ZERO).schedule(AnotherApplicationService.class).onADomainEvent(val);
        });

        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(anotherApplicationService.received)
                    .contains(val)
            );
    }

    @Test
    @DirtiesContext
    public void testTransactionOutboxDeSerializationComplexEvent() {
        var val = new AComplexDomainEvent(
            new AnIdentity(44l),
            new AValueObject("test", 6l, new AnIdentity(77l)),
            List.of(new AnIdentity(88l)),
            List.of(
                new AValueObject("inList1", 0l, new AnIdentity(456l)),
                new AValueObject("inList2", 1l, new AnIdentity(4567l))
            )
        );

        transactionTemplate.executeWithoutResult((status)->{
            outbox.with().ordered("topicComplex").delayForAtLeast(Duration.ZERO).schedule(AnotherApplicationService.class).processComplexEvent(val);
        });

        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(anotherApplicationService.received)
                    .contains(val)
            );
    }

    @Test
    @DirtiesContext
    public void testComplexDomainEventDeserialization() throws Exception{
        var val = new AComplexDomainEvent(
            new AnIdentity(44l),
            new AValueObject("test", 6l, new AnIdentity(77l)),
            List.of(new AnIdentity(88l)),
            List.of(
                new AValueObject("inList1", 0l, new AnIdentity(456l)),
                new AValueObject("inList2", 1l, new AnIdentity(4567l))
            )
        );

        var serialized = objectMapper.writeValueAsString(val);
        log.debug("Serialized= " + serialized);
        var deserial = objectMapper.readValue(serialized, AComplexDomainEvent.class);
        log.debug("Deserialized= " + deserial);
        assertThat(deserial).isEqualTo(val);
    }





}
