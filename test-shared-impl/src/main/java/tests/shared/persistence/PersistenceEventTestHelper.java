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

package tests.shared.persistence;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import org.assertj.core.api.Assertions;
import org.objenesis.strategy.StdInstantiatorStrategy;
import tests.shared.events.PersistenceEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class PersistenceEventTestHelper {


    public TestEventPublisher testEventPublisher = new TestEventPublisher();

    public static Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
    }

    public <T extends Entity> void assertFoundWithResult(Optional<T> found, T result) {
        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(result)
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .ignoringFieldsOfTypes(UUID.class)
            .withStrictTypeChecking()
            .isEqualTo(found.get());
    }

    public void assertEvents() {
        Assertions.assertThat(eventsCaught.size()).isEqualTo(expectedEvents.size());
        for (int i = 0; i < eventsCaught.size(); i++) {
            log.info("assert event number " + i + ": "
                + eventsCaught.get(i).getEventType() + " "
                + eventsCaught.get(i).getInstance().domainObject().getClass().getName() + " "
                + eventsCaught.get(i).getInstance().domainObject().toString());
        }

        Assertions.assertThat(eventsCaught.stream().map(
                e -> new DomainObjectEventType(e.getInstance().domainObject(), e.getEventType())).collect(
                Collectors.toList()))
            .usingRecursiveComparison()
            .ignoringOverriddenEqualsForTypes(Entity.class)
            .ignoringFieldsOfTypes(UUID.class)
            .ignoringCollectionOrder()
            .isEqualTo(expectedEvents.stream().map(e -> new DomainObjectEventType(e.domainObject, e.eventType)).collect(
                Collectors.toList()));

    }


    public void addExpectedEvent(PersistenceEvent.PersistenceEventType eventType, Entity entity) {
        expectedEvents.add(new ExpectedEvent(entity, eventType, null));
    }

    public void addExpectedEvent(PersistenceEvent.PersistenceEventType eventType, ValueObject valueObject,
                                 Entity containingEntity) {
        expectedEvents.add(new ExpectedEvent(valueObject, eventType, containingEntity));
    }

    public List<PersistenceEvent> eventsCaught;
    public List<ExpectedEvent> expectedEvents;

    public void resetEventsCaught() {
        eventsCaught = new ArrayList<>();
        expectedEvents = new ArrayList<>();
    }

    private class TestEventPublisher implements PersistenceEventPublisher {

        @Override
        public void publish(PersistenceAction<?> action) {
            final PersistenceEvent event = assembleEvent(action);
            log.debug("Publish persistence event: {}", event);
            eventsCaught.add(event);
        }

        private PersistenceEvent assembleEvent(final PersistenceAction persistenceAction) {
            PersistenceEvent.PersistenceEventType eventType = PersistenceEvent.PersistenceEventType.INSERTED;
            if (persistenceAction.actionType.equals(
                PersistenceAction.ActionType.UPDATE) || persistenceAction.actionType.equals(
                PersistenceAction.ActionType.DELETE_UPDATE)) {
                eventType = PersistenceEvent.PersistenceEventType.UPDATED;
            } else if (persistenceAction.actionType.equals(PersistenceAction.ActionType.DELETE)) {
                eventType = PersistenceEvent.PersistenceEventType.DELETED;
            }

            return new PersistenceEvent(persistenceAction.instanceAccessModel, eventType);
        }
    }

    public class ExpectedEvent {
        public final DomainObject domainObject;
        public final PersistenceEvent.PersistenceEventType eventType;
        public final Entity containingEntity;

        public ExpectedEvent(
            DomainObject domainObject,
            PersistenceEvent.PersistenceEventType eventType,
            Entity containingEntity
        ) {
            this.domainObject = domainObject;
            this.eventType = eventType;
            this.containingEntity = containingEntity;
        }
    }

    public record DomainObjectEventType(DomainObject domainObject, PersistenceEvent.PersistenceEventType eventType) {
    }

    ;

}
