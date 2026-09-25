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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.jdbc.imp;

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.util.NamingUtil;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Plain JDBC based implementation of a {@link EntityIdentityProvider}.
 * <p>
 * Mirrors the jOOQ based implementation's conventions: a {@code UUID}-valued identity is generated in memory
 * (no database round-trip); any other identity value is generated from a database sequence named after the
 * identity type's simple name, e.g. {@code TestRootSimpleId} -&gt; sequence {@code TEST_ROOT_SIMPLE_ID_SEQ}.
 * This is a different naming convention than {@link JdbcValueObjectIdProvider} uses for value object ids
 * (which are keyed by table name, since value objects have no {@link Identity} type of their own).
 *
 * @author Mario Herb
 */
public class JdbcEntityIdentityProvider implements EntityIdentityProvider {

    private static final String SEQUENCE_SUFFIX = "_SEQ";

    private final JdbcConnectionProvider connectionProvider;
    private final JdbcDialect dialect;
    private final Map<String, String> sequenceNameCache = new ConcurrentHashMap<>();

    /**
     * Constructs a new instance of {@code JdbcEntityIdentityProvider}.
     *
     * @param connectionProvider supplies the connection used to query sequences
     * @param dialect            the dialect providing the "next sequence value" SQL syntax
     */
    public JdbcEntityIdentityProvider(JdbcConnectionProvider connectionProvider, JdbcDialect dialect) {
        this.connectionProvider = Objects.requireNonNull(connectionProvider);
        this.dialect = Objects.requireNonNull(dialect);
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
            .orElseThrow(
                () -> DLCPersistenceException.fail("Identity type not found for entity '%s'", entityTypeName));
        var im = Domain.identityMirrorFor(identityTypeName);
        if (im.getValueTypeName().isPresent() && im.getValueTypeName().get().equals(UUID.class.getName())) {
            return DlcAccess.newIdentityInstance(UUID.randomUUID(), identityTypeName);
        }
        var sequenceName = sequenceNameCache.computeIfAbsent(identityTypeName, JdbcEntityIdentityProvider::sequenceNameFor);
        var idValue = JdbcSequenceIdGenerator.nextValue(connectionProvider.getConnection(), dialect, sequenceName);
        return DlcAccess.newIdentityInstance(idValue, identityTypeName);
    }

    private static String sequenceNameFor(String identityTypeName) {
        var simpleName = identityTypeName.substring(identityTypeName.lastIndexOf(".") + 1);
        return NamingUtil.camelCaseToSnakeCase(simpleName).toUpperCase() + SEQUENCE_SUFFIX;
    }
}
