package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ToStringTest {

    @BeforeAll
    public static void init() {
        ReflectiveDomainModelFactory factory = new ReflectiveDomainModelFactory("tests");
        Domain.initialize(factory);
    }

    @Test
    public void domainToString() {
        assertThat(Domain.getDomainModel().toString()).isNotEmpty();
    }
}
