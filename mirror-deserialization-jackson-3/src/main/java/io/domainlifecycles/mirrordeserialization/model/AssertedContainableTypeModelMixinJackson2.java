package io.domainlifecycles.mirrordeserialization.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.domainlifecycles.mirror.api.AssertionMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;
import java.util.List;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.AssertedContainableTypeModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class AssertedContainableTypeModelMixinJackson2 {

    public boolean hasOptionalContainer;

    public boolean hasCollectionContainer;

    public boolean hasSetContainer;

    public boolean hasListContainer;

    public boolean isArray;

    public boolean hasStreamContainer;

    @JsonCreator
    public AssertedContainableTypeModelMixinJackson2(
        String typeName,
        DomainType domainType,
        List<AssertionMirror> assertions,
        boolean hasOptionalContainer,
        boolean hasCollectionContainer,
        boolean hasListContainer,
        boolean hasSetContainer,
        boolean hasStreamContainer,
        boolean isArray,
        String containerTypeName,
        List<AssertionMirror> containerAssertions,
        ResolvedGenericTypeMirror resolvedGenericType
    ) {}
}