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

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.jooq.util.NamingUtil;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import org.jooq.DSLContext;
import org.jooq.Sequence;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * jOOQ specific implementation of a {@link EntityIdentityProvider}.
 *
 * @author Mario Herb
 */
public class JooqEntityIdentityProvider implements EntityIdentityProvider {

    private final DSLContext dslContext;

    private final String seqSuffix = "_SEQ";

    private final Map<String, Sequence<?>> sequenceCache = new ConcurrentHashMap<>();

    public JooqEntityIdentityProvider(DSLContext dslContext) {
        this.dslContext = Objects.requireNonNull(dslContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<?> provideFor(String entityTypeName) {
        var em = Domain.entityMirrorFor(entityTypeName);
        var identityTypeName = em
            .getIdentityField()
            .map(fm -> fm.getType().getTypeName())
            .orElseThrow(() -> DLCPersistenceException.fail("Identity type not found for entity '%s'", entityTypeName));
        var sequence = getSequence(identityTypeName);
        var idValue = dslContext.nextval(sequence);
        var id = DlcAccess.newIdentityInstance(idValue, identityTypeName);
        return id;
    }

    private Sequence<?> getSequence(String entityIdentityTypeName) {
        var seq = sequenceCache.get(entityIdentityTypeName);
        var simpleName = entityIdentityTypeName.substring(entityIdentityTypeName.lastIndexOf(".") + 1);
        if (seq == null) {
            var sequenceName = NamingUtil.camelCaseToSnakeCase(simpleName).toUpperCase() + seqSuffix;
            seq = dslContext
                .meta()
                .getSequences(sequenceName)
                .stream()
                .findFirst()
                .orElse(null);
            if (seq == null) {
                seq = dslContext
                    .meta()
                    .getSequences(sequenceName.toLowerCase())
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> DLCPersistenceException.fail("No sequence named '%s' found", sequenceName));
            }
            sequenceCache.put(entityIdentityTypeName, seq);
        }
        return seq;
    }
}
