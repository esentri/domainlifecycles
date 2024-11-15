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

package io.domainlifecycles.jooq.imp;

import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.persister.BasePersister;
import io.domainlifecycles.persistence.repository.persister.Persister;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;

/**
 * jOOQ specific implementation of a {@link Persister}.
 *
 * @author Mario Herb
 */
public class JooqPersister extends BasePersister<UpdatableRecord<?>> implements Persister<UpdatableRecord<?>> {

    private final DSLContext dslContext;

    public JooqPersister(DSLContext dslContext, JooqDomainPersistenceProvider domainPersistenceProvider) {
        super(domainPersistenceProvider,
            new JooqValueObjectIdProvider(domainPersistenceProvider, dslContext),
            new JooqEntityParentReferenceProvider(domainPersistenceProvider)
        );
        this.dslContext = dslContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doInsert(UpdatableRecord<?> record) {
        record.attach(dslContext.configuration());
        record.insert();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doUpdate(UpdatableRecord<?> record) {
        record.attach(dslContext.configuration());
        record.update();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doDelete(UpdatableRecord<?> record) {
        record.attach(dslContext.configuration());
        record.delete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doIncreaseVersion(UpdatableRecord<?> record) {
        record.attach(dslContext.configuration());
        record.changed(true);
        record.update();
    }

}
