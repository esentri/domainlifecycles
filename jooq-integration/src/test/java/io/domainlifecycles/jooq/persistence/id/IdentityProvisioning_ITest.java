package io.domainlifecycles.jooq.persistence.id;

import io.domainlifecycles.jooq.imp.JooqEntityIdentityProvider;
import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.persistence.domain.inheritance.Vehicle;
import tests.shared.persistence.domain.uuid.TestRootUuid;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IdentityProvisioning_ITest extends BasePersistence_ITest {

    private JooqEntityIdentityProvider identityProvider;

    @BeforeEach
    public void setup(){
        identityProvider = new JooqEntityIdentityProvider(persistenceConfiguration.dslContext);
    }

    @Test
    public void testIdentityProvisioningUuid() {
        var identity = identityProvider.provideFor(TestRootUuid.class.getName());
        assertThat(identity).isNotNull();
        assertInstanceOf(UUID.class, identity.value());
    }

    @Test
    public void testIdentityProvisioningLong() {
        var identity = identityProvider.provideFor(Vehicle.class.getName());
        assertThat(identity).isNotNull();
        assertInstanceOf(Long.class, identity.value());
    }
}
