package io.domainlifecycles.access.object;

import io.domainlifecycles.access.classes.DefaultClassProvider;
import io.domainlifecycles.access.exception.DLCAccessException;
import io.domainlifecycles.domain.types.Identity;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.oneToManyIdentityEnum.MyId;
import tests.shared.persistence.domain.valueobjects.SimpleVo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultIdentityFactoryTest {

    @Test
    void testIdentityFactorySuccess() {
        IdentityFactory identityFactory = new DefaultIdentityFactory(new DefaultClassProvider());
        Identity<Long> id = identityFactory.newInstance(1L,
            MyId.class.getName());

        assertThat(id).isNotNull();
        assertThat(id.value()).isEqualTo(1L);
    }

    @Test
    void testIdentityFactoryFailNotInstantiableValueObject() {
        IdentityFactory identityFactory = new DefaultIdentityFactory(new DefaultClassProvider());

        assertThatThrownBy(() -> identityFactory.newInstance("", SimpleVo.class.getName()))
            .isInstanceOf(DLCAccessException.class)
            .hasMessageContaining("Couldn't instantiate Identity");
    }
}
