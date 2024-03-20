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

package nitrox.dlc.jooq.persistence.tests.manyToManyWithJoinEntity;


import nitrox.dlc.jooq.imp.JooqAggregateRepository;
import nitrox.dlc.jooq.imp.provider.JooqDomainPersistenceProvider;
import nitrox.dlc.persistence.repository.PersistenceEventPublisher;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToManyId;

public class ManyToManyAggregateRootRepository extends JooqAggregateRepository<TestRootManyToMany, TestRootManyToManyId> {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ManyToManyAggregateRootRepository.class);

    public ManyToManyAggregateRootRepository(DSLContext dslContext,
                                             PersistenceEventPublisher persistenceEventPublisher,
                                             JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            TestRootManyToMany.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher);

    }


}
