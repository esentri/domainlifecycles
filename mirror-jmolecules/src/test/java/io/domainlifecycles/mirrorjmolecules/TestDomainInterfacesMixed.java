package io.domainlifecycles.mirrorjmolecules;

import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;
import io.domainlifecycles.mirrorjmolecules.reflect.ExtendedJMoleculesDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.mirror.mixed.ACallout;
import tests.mirror.mixed.ADomainService;
import tests.mirror.mixed.ARepository;
import tests.mirror.mixed.AValueObject;
import tests.mirror.mixed.AnAggregateRoot;
import tests.mirror.mixed.AnApplicationService;
import tests.mirror.mixed.AnEntity;
import tests.mirror.mixed.DidIt;
import tests.mirror.mixed.DoneSomething;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class TestDomainInterfacesMixed {

    @BeforeAll
    public static void init() {
        ExtendedJMoleculesDomainMirrorFactory factory = new ExtendedJMoleculesDomainMirrorFactory("tests.mirror.mixed");
        Domain.initialize(factory);
    }

    @Test
    void testDomainInitMixed() {
        DomainMirror dm = Domain.getDomainMirror();
        assertThat(dm.getAllDomainTypeMirrors().stream().filter(m -> !m.getTypeName().startsWith("org.jmolecules") && !m.getTypeName().startsWith("io.domainlifecycles")).collect(Collectors.toSet())).hasSize(11);
    }

    @Test
    void testAggregateRootInterface() {
        AggregateRootMirror aggregateRootMirror = (AggregateRootMirror) Domain.typeMirror(AnAggregateRoot.class.getName()).get();

        assertThat(aggregateRootMirror.getDomainType()).isEqualTo(DomainType.AGGREGATE_ROOT);
        assertThat(aggregateRootMirror.getTypeName()).isEqualTo(AnAggregateRoot.class.getName());
        assertThat(aggregateRootMirror.getIdentityField()).isPresent();
        assertThat(aggregateRootMirror.getAllFields().get(0).getName()).isEqualTo("id");
        assertThat(aggregateRootMirror.getAllFields().get(1).getName()).isEqualTo("someField");
        assertThat(aggregateRootMirror.getEntityReferences().get(0).getName()).isEqualTo("entity");
        assertThat(aggregateRootMirror.getValueReferences().get(0).getName()).isEqualTo("valueObject");
        var m = aggregateRootMirror.getMethods().stream().filter(mm -> mm.getName().equals("onDidIt")).findFirst();
        assertThat(m).isPresent();
        assertThat(m.get().getListenedEvent()).isPresent();
        assertThat(m.get().getListenedEvent().get().getTypeName()).isEqualTo(DidIt.class.getName());
    }

    @Test
    void testDomainService() {
        DomainServiceMirror domainServiceMirror = (DomainServiceMirror) Domain.typeMirror(
            ADomainService.class.getName()).get();

        assertThat(domainServiceMirror.getDomainType()).isEqualTo(DomainType.DOMAIN_SERVICE);
        assertThat(domainServiceMirror.getTypeName()).isEqualTo(ADomainService.class.getName());
        assertThat(domainServiceMirror.getAllFields().get(0).getName()).isEqualTo("repository");
        assertThat(domainServiceMirror.getAllFields().get(1).getName()).isEqualTo("callout");
        assertThat(domainServiceMirror.getReferencedOutboundServices().stream().map(DomainTypeMirror::getTypeName).toList()).containsExactly(ACallout.class.getName());

        var m = domainServiceMirror.getMethods().stream().filter(mm -> mm.getName().equals("onDoneSomething")).findFirst();
        assertThat(m).isPresent();
        assertThat(m.get().getListenedEvent()).isPresent();
        assertThat(m.get().getListenedEvent().get().getTypeName()).isEqualTo(DoneSomething.class.getName());
        assertThat(m.get().getPublishedEvents().stream().map(DomainTypeMirror::getTypeName).toList()).containsExactly(DidIt.class.getName());
    }

    @Test
    void testDomainDomainEventDidIt() {
        DomainEventMirror domainEventMirror = (DomainEventMirror) Domain.typeMirror(
            DidIt.class.getName()).get();

        assertThat(domainEventMirror.getDomainType()).isEqualTo(DomainType.DOMAIN_EVENT);
        assertThat(domainEventMirror.getTypeName()).isEqualTo(DidIt.class.getName());
    }

    @Test
    void testDomainDomainEventDoneSomething() {
        DomainEventMirror domainEventMirror = (DomainEventMirror) Domain.typeMirror(
            DoneSomething.class.getName()).get();

        assertThat(domainEventMirror.getDomainType()).isEqualTo(DomainType.DOMAIN_EVENT);
        assertThat(domainEventMirror.getTypeName()).isEqualTo(DoneSomething.class.getName());
    }

    @Test
    void testEntityInterface() {
        EntityMirror entityMirror = (EntityMirror) Domain.typeMirror(
            AnEntity.class.getName()).get();

        assertThat(entityMirror.getDomainType()).isEqualTo(DomainType.ENTITY);
        assertThat(entityMirror.getTypeName()).isEqualTo(AnEntity.class.getName());
        assertThat(entityMirror.getIdentityField()).isPresent();
        assertThat(entityMirror.getAllFields().get(0).getName()).isEqualTo("id");
        assertThat(entityMirror.getAllFields().get(1).getName()).isEqualTo("someField");
    }

    @Test
    void testRepositoryInterface() {
        RepositoryMirror repositoryMirror = (RepositoryMirror) Domain.typeMirror(
            ARepository.class.getName()).get();

        assertThat(repositoryMirror.getDomainType()).isEqualTo(DomainType.REPOSITORY);
        assertThat(repositoryMirror.getTypeName()).isEqualTo(ARepository.class.getName());
        assertThat(repositoryMirror.getManagedAggregate()).isPresent();
        assertThat(repositoryMirror.getManagedAggregate().get().getTypeName()).isEqualTo(AnAggregateRoot.class.getName());
    }

    @Test
    void testValueObjectInterface() {
        ValueObjectMirror valueObjectMirror = (ValueObjectMirror) Domain.typeMirror(
            AValueObject.class.getName()).get();

        assertThat(valueObjectMirror.getDomainType()).isEqualTo(DomainType.VALUE_OBJECT);
        assertThat(valueObjectMirror.getTypeName()).isEqualTo(AValueObject.class.getName());
        assertThat(valueObjectMirror.getAllFields().get(0).getName()).isEqualTo("someValue");
    }

    @Test
    void testApplicationService() {
        ApplicationServiceMirror applicationServiceMirror = (ApplicationServiceMirror) Domain.typeMirror(
            AnApplicationService.class.getName()).get();

        assertThat(applicationServiceMirror.getDomainType()).isEqualTo(DomainType.APPLICATION_SERVICE);
        assertThat(applicationServiceMirror.getTypeName()).isEqualTo(AnApplicationService.class.getName());
        assertThat(applicationServiceMirror.getAllFields().get(0).getName()).isEqualTo("aRepository");
        assertThat(applicationServiceMirror.getReferencedDomainServices().stream().map(DomainTypeMirror::getTypeName).toList()).containsExactly(ADomainService.class.getName());

        var m = applicationServiceMirror.getMethods().stream().filter(mm -> mm.getName().equals("doSomeThing")).findFirst();
        assertThat(m).isPresent();
        assertThat(m.get().getPublishedEvents().stream().map(DomainTypeMirror::getTypeName).toList()).containsExactly(DoneSomething.class.getName());
    }
}
