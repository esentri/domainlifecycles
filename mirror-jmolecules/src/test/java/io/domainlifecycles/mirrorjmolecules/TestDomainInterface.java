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
 *  Copyright 2019-2025 the original author or authors.
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

package io.domainlifecycles.mirrorjmolecules;

import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;
import io.domainlifecycles.mirrorjmolecules.reflect.JMoleculesDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.mirror.interfaces.AggregateRootJMoleculesInterface;
import tests.mirror.interfaces.DomainEventJMoleculesInterface;
import tests.mirror.interfaces.EntityJMoleculesInterface;
import tests.mirror.interfaces.RepositoryJMoleculesInterface;
import tests.mirror.interfaces.ValueObjectJMoleculesInterface;
import static org.assertj.core.api.Assertions.assertThat;

public class TestDomainInterface {

    @BeforeAll
    public static void init() {
        JMoleculesDomainMirrorFactory factory = new JMoleculesDomainMirrorFactory("tests.mirror.interfaces");
        Domain.initialize(factory);
    }

    @Test
    void testDomainInitJMoleculesInterface() {
        DomainMirror dm = Domain.getDomainMirror();
        assertThat(dm.getAllDomainTypeMirrors()).hasSize(5);
    }

    @Test
    void testAggregateRootInterface() {
        AggregateRootMirror aggregateRootMirror = (AggregateRootMirror) Domain.typeMirror(
            AggregateRootJMoleculesInterface.class.getName()).get();

        assertThat(aggregateRootMirror.getDomainType()).isEqualTo(DomainType.AGGREGATE_ROOT);
        assertThat(aggregateRootMirror.getTypeName()).isEqualTo(AggregateRootJMoleculesInterface.class.getName());
        assertThat(aggregateRootMirror.getIdentityField()).isPresent();
        assertThat(aggregateRootMirror.getAllFields().get(0).getName()).isEqualTo("id");
        assertThat(aggregateRootMirror.getAllFields().get(1).getName()).isEqualTo("someField");
    }

    @Test
    void testDomainDomainEventInterface() {
        DomainEventMirror domainEventMirror = (DomainEventMirror) Domain.typeMirror(
            DomainEventJMoleculesInterface.class.getName()).get();

        assertThat(domainEventMirror.getDomainType()).isEqualTo(DomainType.DOMAIN_EVENT);
        assertThat(domainEventMirror.getTypeName()).isEqualTo(DomainEventJMoleculesInterface.class.getName());
    }

    @Test
    void testEntityInterface() {
        EntityMirror entityMirror = (EntityMirror) Domain.typeMirror(
            EntityJMoleculesInterface.class.getName()).get();

        assertThat(entityMirror.getDomainType()).isEqualTo(DomainType.ENTITY);
        assertThat(entityMirror.getTypeName()).isEqualTo(EntityJMoleculesInterface.class.getName());
        assertThat(entityMirror.getIdentityField()).isPresent();
        assertThat(entityMirror.getAllFields().get(0).getName()).isEqualTo("id");
        assertThat(entityMirror.getAllFields().get(1).getName()).isEqualTo("someField");
    }

    @Test
    void testRepositoryInterface() {
        RepositoryMirror repositoryMirror = (RepositoryMirror) Domain.typeMirror(
            RepositoryJMoleculesInterface.class.getName()).get();

        assertThat(repositoryMirror.getDomainType()).isEqualTo(DomainType.REPOSITORY);
        assertThat(repositoryMirror.getTypeName()).isEqualTo(RepositoryJMoleculesInterface.class.getName());
        assertThat(repositoryMirror.getManagedAggregate()).isPresent();
        assertThat(repositoryMirror.getManagedAggregate().get().getTypeName()).isEqualTo(AggregateRootJMoleculesInterface.class.getName());
    }

    @Test
    void testValueObjectInterface() {
        ValueObjectMirror valueObjectMirror = (ValueObjectMirror) Domain.typeMirror(
            ValueObjectJMoleculesInterface.class.getName()).get();

        assertThat(valueObjectMirror.getDomainType()).isEqualTo(DomainType.VALUE_OBJECT);
        assertThat(valueObjectMirror.getTypeName()).isEqualTo(ValueObjectJMoleculesInterface.class.getName());
        assertThat(valueObjectMirror.getAllFields().get(0).getName()).isEqualTo("someValue");
    }
}
