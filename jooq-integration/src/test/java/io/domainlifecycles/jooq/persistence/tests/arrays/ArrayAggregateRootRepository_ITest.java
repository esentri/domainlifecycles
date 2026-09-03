package io.domainlifecycles.jooq.persistence.tests.arrays;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.persistence.domain.arrays.CryptoVo;
import tests.shared.persistence.domain.arrays.TestRootArray;
import tests.shared.persistence.domain.arrays.TestRootArrayId;

import java.util.Optional;

/**
 * Array typed fields used to fail the auto record mapping in both directions: the mirror reports the
 * component type ({@code byte}) for a {@code byte[]} field while the record property reports
 * {@code [B}, so the mapper considered the types different and looked up a converter
 * {@code [B -> java.lang.Byte} that no sensible converter could ever serve.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArrayAggregateRootRepository_ITest extends BasePersistence_ITest {

    private static final byte[] PAYLOAD = new byte[]{1, 2, 3, 4, 5};
    private static final byte[] CHIFFRAT = new byte[]{10, 20, 30};
    private static final byte[] SALT = new byte[]{-1, 0, 1};

    private ArrayAggregateRootRepository arrayAggregateRootRepository;

    @BeforeAll
    public void init() {
        arrayAggregateRootRepository = new ArrayAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
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
                .setSchluesselVersion(7L)
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
            .findResultById(new TestRootArrayId(1L)).resultValue();

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getPayload()).isEqualTo(PAYLOAD);
        Assertions.assertThat(found.get().getCryptoVo()).isNotNull();
        Assertions.assertThat(found.get().getCryptoVo().getChiffrat()).isEqualTo(CHIFFRAT);
        Assertions.assertThat(found.get().getCryptoVo().getSalt()).isEqualTo(SALT);
        Assertions.assertThat(found.get().getCryptoVo().getSchluesselVersion()).isEqualTo(7L);
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
            .setSchluesselVersion(8L)
            .build());
        //when
        TestRootArray updated = arrayAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootArray> found = arrayAggregateRootRepository
            .findResultById(new TestRootArrayId(1L)).resultValue();

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getPayload()).isEqualTo(updatedPayload);
        Assertions.assertThat(found.get().getCryptoVo().getChiffrat()).isEqualTo(new byte[]{42});
        Assertions.assertThat(found.get().getCryptoVo().getSchluesselVersion()).isEqualTo(8L);
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
        arrayAggregateRootRepository.insert(root);
        //then
        Optional<TestRootArray> found = arrayAggregateRootRepository
            .findResultById(new TestRootArrayId(1L)).resultValue();

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
        arrayAggregateRootRepository.insert(root);
        //then
        Optional<TestRootArray> found = arrayAggregateRootRepository
            .findResultById(new TestRootArrayId(1L)).resultValue();

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getPayload()).isEmpty();
    }

}
