package io.domainlifecycles.persistence.repository.persister;

import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.provider.StructuralPosition;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import io.domainlifecycles.persistence.repository.actions.PersistenceContext;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Regression test for the ambiguity {@link BaseValueObjectIdProvider#resolveContainerTechId} used to have
 * between "genuinely inline value object ancestor" and "record-mapped value object ancestor whose INSERT
 * action just hasn't run yet" - both looked identical to {@link PersistenceContext#getNewValueObjectRecord}
 * (null), so the second case (an insertion-ordering bug) used to be silently misattributed to a more
 * distant ancestor instead of failing loudly.
 */
class BaseValueObjectIdProviderTest {

    private static class TestVo implements ValueObject {
    }

    @SuppressWarnings("unchecked")
    private static class TestValueObjectIdProvider extends BaseValueObjectIdProvider<Object> {
        TestValueObjectIdProvider() {
            super(mock(DomainPersistenceProvider.class));
        }

        @Override
        protected void setContainerIdInNewVoRecord(Object newVoRecord, Serializable containerTechId) {
        }

        @Override
        protected Serializable selectExistingTechIdOfValueObject(Object voContainerRecord) {
            return null;
        }

        @Override
        protected void provideNewTechIdForValueObjectRecord(Object newVoRecord) {
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    void throwsInsteadOfSkippingAnAncestorWhoseInsertActionHasNotRunYet() {
        var ancestorVo = new TestVo();
        var currentVo = new TestVo();

        var ancestorPosition = StructuralPosition.builder().withInstance(ancestorVo).build();
        DomainObjectInstanceAccessModel<Object> ancestorAccessModel = DomainObjectInstanceAccessModel.builder()
            .withStructuralPosition(ancestorPosition)
            .build();
        var pendingInsertAction = new PersistenceAction<>(
            ancestorAccessModel, PersistenceAction.ActionType.INSERT, null);

        var currentPosition = StructuralPosition.builder()
            .withInstance(currentVo)
            .withParentStructuralPosition(ancestorPosition)
            .withAccessorFromParent("someField")
            .build();
        DomainObjectInstanceAccessModel<Object> currentAccessModel = DomainObjectInstanceAccessModel.builder()
            .withStructuralPosition(currentPosition)
            .build();

        PersistenceContext<Object> pc = mock(PersistenceContext.class);
        when(pc.getNewValueObjectRecord(ancestorVo)).thenReturn(null);
        when(pc.getActionsPartitioned(eq(ancestorVo.getClass().getName()), eq(PersistenceAction.ActionType.INSERT)))
            .thenReturn(List.of(pendingInsertAction));

        var provider = new TestValueObjectIdProvider();

        assertThatThrownBy(() ->
            provider.provideTechnicalIdsForNewVoRecord(new Object(), currentAccessModel, pc))
            .isInstanceOf(DLCPersistenceException.class)
            .hasMessageContaining(TestVo.class.getName());
    }

    @SuppressWarnings("unchecked")
    @Test
    void walksPastAGenuinelyInlineAncestorWithNoInsertActionAtAll() {
        var ancestorVo = new TestVo();
        var currentVo = new TestVo();

        var ancestorPosition = StructuralPosition.builder().withInstance(ancestorVo).build();
        var currentPosition = StructuralPosition.builder()
            .withInstance(currentVo)
            .withParentStructuralPosition(ancestorPosition)
            .withAccessorFromParent("someField")
            .build();
        DomainObjectInstanceAccessModel<Object> currentAccessModel = DomainObjectInstanceAccessModel.builder()
            .withStructuralPosition(currentPosition)
            .build();

        PersistenceContext<Object> pc = mock(PersistenceContext.class);
        when(pc.getNewValueObjectRecord(ancestorVo)).thenReturn(null);
        when(pc.getActionsPartitioned(eq(ancestorVo.getClass().getName()), eq(PersistenceAction.ActionType.INSERT)))
            .thenReturn(List.of());

        var provider = new TestValueObjectIdProvider();

        // no Entity and no persisted VO anywhere in the (single-element) access path - this is the
        // pre-existing "could not determine the persisted container" failure, not the new ordering check,
        // confirming a genuinely inline ancestor is still skipped rather than rejected outright
        assertThatThrownBy(() ->
            provider.provideTechnicalIdsForNewVoRecord(new Object(), currentAccessModel, pc))
            .isInstanceOf(DLCPersistenceException.class)
            .hasMessageContaining("Could not determine the persisted container");
    }
}
