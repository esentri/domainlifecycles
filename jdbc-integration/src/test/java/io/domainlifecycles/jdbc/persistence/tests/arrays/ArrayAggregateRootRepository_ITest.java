package io.domainlifecycles.jdbc.persistence.tests.arrays;

import io.domainlifecycles.jdbc.persistence.JdbcBasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.persistence.domain.arrays.CryptoVo;
import tests.shared.persistence.domain.arrays.TestRootArray;
import tests.shared.persistence.domain.arrays.TestRootArrayId;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArrayAggregateRootRepository_ITest extends JdbcBasePersistence_ITest {

    private static final byte[] PAYLOAD = new byte[]{1, 2, 3, 4, 5};
    private static final byte[] CHIFFRAT = new byte[]{10, 20, 30};
    private static final byte[] SALT = new byte[]{-1, 0, 1};

    private ArrayAggregateRootRepository arrayAggregateRootRepository;

    @BeforeAll
    public void init() {
        arrayAggregateRootRepository = new ArrayAggregateRootRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
    }

    private static TestRootArray build() {
        return TestRootArray.builder()
            .setId(new TestRootArrayId(1L))
            .setName("array root")
            .setPayload(PAYLOAD.clone())
            .setCryptoVo(CryptoVo.builder()
                .setChiffrat(CHIFFRAT.clone())
                .setSalt(SALT.clone())
                .setKeyVersion(7L)
                .build())
            .build();
    }

    @Test
    public void testInsertAndReadArrayFields() {
        //given
        TestRootArray root = build();
        //when
        TestRootArray inserted = arrayAggregateRootRepository.insert(root);
        //then
        Optional<TestRootArray> found = arrayAggregateRootRepository
            .findResultById(inserted.getId()).resultValue();

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getPayload()).isEqualTo(PAYLOAD);
        Assertions.assertThat(found.get().getCryptoVo()).isNotNull();
        Assertions.assertThat(found.get().getCryptoVo().getChiffrat()).isEqualTo(CHIFFRAT);
        Assertions.assertThat(found.get().getCryptoVo().getSalt()).isEqualTo(SALT);
        Assertions.assertThat(found.get().getCryptoVo().getKeyVersion()).isEqualTo(7L);
        Assertions.assertThat(inserted.getPayload()).isEqualTo(PAYLOAD);
    }

    @Test
    public void testUpdateArrayFields() {
        //given
        TestRootArray inserted = arrayAggregateRootRepository.insert(build());
        TestRootArray insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        byte[] updatedPayload = new byte[]{9, 8, 7};
        insertedCopy.setPayload(updatedPayload);
        insertedCopy.setCryptoVo(CryptoVo.builder()
            .setChiffrat(new byte[]{42})
            .setSalt(new byte[]{43})
            .setKeyVersion(8L)
            .build());
        //when
        TestRootArray updated = arrayAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootArray> found = arrayAggregateRootRepository
            .findResultById(inserted.getId()).resultValue();

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getPayload()).isEqualTo(updatedPayload);
        Assertions.assertThat(found.get().getCryptoVo().getChiffrat()).isEqualTo(new byte[]{42});
        Assertions.assertThat(found.get().getCryptoVo().getKeyVersion()).isEqualTo(8L);
        Assertions.assertThat(updated.getPayload()).isEqualTo(updatedPayload);
    }

    @Test
    public void testNullArrayFields() {
        //given
        TestRootArray root = TestRootArray.builder()
            .setId(new TestRootArrayId(1L))
            .setName("no arrays")
            .build();
        //when
        TestRootArray inserted = arrayAggregateRootRepository.insert(root);
        //then
        Optional<TestRootArray> found = arrayAggregateRootRepository
            .findResultById(inserted.getId()).resultValue();

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getPayload()).isNull();
        Assertions.assertThat(found.get().getCryptoVo()).isNull();
    }

    @Test
    public void testEmptyArrayIsPreserved() {
        //given
        TestRootArray root = TestRootArray.builder()
            .setId(new TestRootArrayId(1L))
            .setName("empty")
            .setPayload(new byte[0])
            .build();
        //when
        TestRootArray inserted = arrayAggregateRootRepository.insert(root);
        //then
        Optional<TestRootArray> found = arrayAggregateRootRepository
            .findResultById(inserted.getId()).resultValue();

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getPayload()).isEmpty();
    }

}
