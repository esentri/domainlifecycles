package io.domainlifecycles.access.object;

import io.domainlifecycles.access.classes.DefaultClassProvider;
import io.domainlifecycles.access.exception.DLCAccessException;
import io.domainlifecycles.domain.types.Identity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.oneToManyIdentityEnum.MyId;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultIdentityFactoryTest {

    @Test
    void testIdentityFactorySuccess() {
        IdentityFactory identityFactory = new DefaultIdentityFactory(new DefaultClassProvider());
        Identity<Long> id = identityFactory.newInstance(1L, "tests.shared.persistence.domain.oneToManyIdentityEnum.MyId");

        Assertions.assertThat(id).isNotNull();
        Assertions.assertThat(id.value()).isEqualTo(1L);
    }

    @Test
    void testIdentityFactoryFailNotInstantiableAbstract() {
        IdentityFactory identityFactory = new DefaultIdentityFactory(new DefaultClassProvider());
        DLCAccessException exception = assertThrows(DLCAccessException.class, () -> identityFactory.newInstance("", "tests.shared.persistence.domain.inheritance.Vehicle"));

        Assertions.assertThat(exception).hasMessageContaining("Failed to instantiate Identity");
    }
}
