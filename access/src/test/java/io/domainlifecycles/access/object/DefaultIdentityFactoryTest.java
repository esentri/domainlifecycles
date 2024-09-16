package io.domainlifecycles.access.object;

import io.domainlifecycles.access.classes.DefaultClassProvider;
import io.domainlifecycles.access.exception.DLCAccessException;
import io.domainlifecycles.domain.types.Identity;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultIdentityFactoryTest {

    @Test
    void testIdentityFactorySuccess() {
        IdentityFactory identityFactory = new DefaultIdentityFactory(new DefaultClassProvider());
        Identity<Long> id = identityFactory.newInstance(1L, "tests.shared.persistence.domain.oneToManyIdentityEnum.MyId");

        assertThat(id).isNotNull();
        assertThat(id.value()).isEqualTo(1L);
    }

    @Test
    void testIdentityFactoryFailNotInstantiableAbstract() {
        IdentityFactory identityFactory = new DefaultIdentityFactory(new DefaultClassProvider());

        assertThatThrownBy(identityFactory.newInstance("", "tests.shared.persistence.domain.inheritance.Vehicle"))
            .isInstanceOf(DLCAccessException.class)
            .hasMessageContaining("Failed to instantiate Identity");
    }
}
