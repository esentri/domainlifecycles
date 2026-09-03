package tests.mirror;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.base.EntityBase;

/**
 * Test entity covering all array flavours relevant for the type name resolution
 * of {@link io.domainlifecycles.mirror.api.AssertedContainableTypeMirror}.
 */
public class ArrayFieldsEntity extends EntityBase<ArrayFieldsEntity.ArrayFieldsEntityId> {

    public record ArrayFieldsEntityId(Long value) implements Identity<Long> {
    }

    private ArrayFieldsEntityId id;

    private byte[] primitiveByteArray;

    private Byte[] boxedByteArray;

    private int[] primitiveIntArray;

    private long[] primitiveLongArray;

    private short[] primitiveShortArray;

    private char[] primitiveCharArray;

    private boolean[] primitiveBooleanArray;

    private float[] primitiveFloatArray;

    private double[] primitiveDoubleArray;

    private String[] stringArray;

    private byte[][] twoDimensionalByteArray;

    private String noArray;

    private byte noArrayPrimitive;

    public ArrayFieldsEntity(ArrayFieldsEntityId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }
}
