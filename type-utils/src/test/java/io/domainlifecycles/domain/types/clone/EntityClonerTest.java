package io.domainlifecycles.domain.types.clone;

import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Regression tests for the mutable basic-typed field bug: {@code cloneEntityProperties} used to
 * reference-copy a {@link List}/array field's container instance wholesale, so mutating the original
 * entity's list/array after cloning silently mutated the "clone" too.
 */
public class EntityClonerTest {

    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests", "io.domainlifecycles"));
    }

    private final EntityCloner entityCloner = new EntityCloner(new InnerClassDomainObjectBuilderProvider());

    @Test
    public void clonedListFieldIsNotSharedWithTheOriginal() {
        var original = CloneTestEntity.builder()
            .setId(new CloneTestEntityId(1L))
            .setConcurrencyVersion(0)
            .setTags(new ArrayList<>(List.of("a", "b")))
            .build();

        var cloned = (CloneTestEntity) entityCloner.clone(original);

        assertThat(cloned.tags).containsExactly("a", "b");
        assertThat(cloned.tags).isNotSameAs(original.tags);

        //when the original's list is mutated in place after cloning
        original.tags.add("c");

        //then the clone must not see the mutation
        assertThat(cloned.tags).containsExactly("a", "b");
        assertThat(original.tags).containsExactly("a", "b", "c");
    }

    @Test
    public void clonedArrayFieldIsNotSharedWithTheOriginal() {
        var original = CloneTestEntity.builder()
            .setId(new CloneTestEntityId(2L))
            .setConcurrencyVersion(0)
            .setPayload(new byte[]{1, 2, 3})
            .build();

        var cloned = (CloneTestEntity) entityCloner.clone(original);

        assertThat(cloned.payload).containsExactly(1, 2, 3);
        assertThat(cloned.payload).isNotSameAs(original.payload);

        //when the original's array is mutated in place after cloning
        original.payload[0] = 42;

        //then the clone must not see the mutation
        assertThat(cloned.payload).containsExactly(1, 2, 3);
        assertThat(original.payload).containsExactly(42, 2, 3);
    }

    @Test
    public void nullListAndArrayFieldsCloneAsNull() {
        var original = CloneTestEntity.builder()
            .setId(new CloneTestEntityId(3L))
            .setConcurrencyVersion(0)
            .build();

        var cloned = (CloneTestEntity) entityCloner.clone(original);

        assertThat(cloned.tags).isNull();
        assertThat(cloned.payload).isNull();
    }
}
