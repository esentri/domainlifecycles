package io.domainlifecycles.mirrordeserialization;

import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.DomainMirrorFactory;
import io.domainlifecycles.mirror.model.EntityModel;
import io.domainlifecycles.mirror.model.FieldModel;
import io.domainlifecycles.mirror.model.MethodModel;
import io.domainlifecycles.mirror.model.ProvidedDomain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import io.domainlifecycles.mirrordeserialization.serialize.jackson2.Jackson2DomainSerializer;
import io.domainlifecycles.mirrordeserialization.serialize.jackson3.Jackson3DomainSerializer;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;


@Slf4j
public class JsonizerTest {

    @Test
    public void testJsonizeWithoutTypeMetaJackson2() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
        testJsonize(factory, false);
    }

    @Test
    public void testJsonizeWithTypeMetaJackson2() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory( "tests");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        testJsonize(factory, false);
    }

    @Test
    public void testJsonizeWithoutTypeMetaJackson3() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
        testJsonize(factory, true);
    }

    @Test
    public void testJsonizeWithTypeMetaJackson3() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory( "tests");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        testJsonize(factory, true);
    }

    private void testJsonize(DomainMirrorFactory factory, boolean useJackson3) {
        var serializer = useJackson3 ? new Jackson3DomainSerializer(true) : new Jackson2DomainSerializer(true);
        var dm = factory.initializeDomainMirror();
        var result = serializer.serialize(dm);
        var init = serializer.deserialize(result);
        Assertions.assertThat(init.getAllBoundedContextMirrors()).isEqualTo(dm.getAllBoundedContextMirrors());
        Assertions.assertThat(init.getAllDomainTypeMirrors().size()).isEqualTo(dm.getAllDomainTypeMirrors().size());
        Assertions.assertThat(init.getAllDomainTypeMirrors()).isEqualTo(dm.getAllDomainTypeMirrors());


        Assertions.assertThat(init).isEqualTo(dm);
        Assertions.assertThat(init.getAllDomainTypeMirrors()
                .stream()
                .map(t -> (ProvidedDomain) t)
                .allMatch(ProvidedDomain::domainMirrorSet)).isTrue();
        Assertions.assertThat(init.getAllDomainTypeMirrors()
                .stream()
                .flatMap(dt-> dt.getAllFields().stream())
                .allMatch(t-> ((FieldModel)t).domainMirrorSet())).isTrue();
        Assertions.assertThat(init.getAllDomainTypeMirrors()
            .stream()
            .flatMap(dt-> dt.getMethods().stream())
            .allMatch(t-> ((MethodModel)t).domainMirrorSet())).isTrue();
        Assertions.assertThat(init.getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt instanceof EntityModel)
            .map(dt ->(EntityModel) dt)
            .map(dt-> (FieldModel)dt.getIdentityField().orElse(null))
            .filter(Objects::nonNull)
            .allMatch(FieldModel::domainMirrorSet)).isTrue();
        var result2 = serializer.serialize(init);
        Assertions.assertThat(result).isEqualTo(result2);
    }

    @Test
    public void testDeserializeConcreteDomainTypeMirrorJackson2(){
        var serializer = new Jackson2DomainSerializer(true);
        AggregateRootMirror ar = serializer.deserializeTypeMirror(
            """
                {
                  "@class": "io.domainlifecycles.mirror.model.AggregateRootModel",
                  "methods": [
                    {
                      "name": "builder",
                      "@class": "io.domainlifecycles.mirror.model.MethodModel",
                      "getter": false,
                      "setter": false,
                      "overridden": false,
                      "parameters": [],
                      "returnType": {
                        "array": false,
                        "@class": "io.domainlifecycles.mirror.model.AssertedContainableTypeModel",
                        "isArray": false,
                        "typeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler$HaendlerBuilder",
                        "assertions": [],
                        "domainType": "NON_DOMAIN",
                        "hasSetContainer": false,
                        "hasListContainer": false,
                        "containerTypeName": null,
                        "hasStreamContainer": false,
                        "containerAssertions": [],
                        "resolvedGenericType": {
                          "@class": "io.domainlifecycles.mirror.model.ResolvedGenericTypeModel",
                          "isArray": false,
                          "typeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler$HaendlerBuilder",
                          "genericTypes": [],
                          "wildcardBound": null
                        },
                        "hasOptionalContainer": false,
                        "hasCollectionContainer": false
                      },
                      "accessLevel": "PUBLIC",
                      "declaredByTypeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler",
                      "listenedEventTypeName": null,
                      "publishedEventTypeNames": []
                    },
                    {
                      "name": "getId",
                      "@class": "io.domainlifecycles.mirror.model.MethodModel",
                      "getter": true,
                      "setter": false,
                      "overridden": false,
                      "parameters": [],
                      "returnType": {
                        "array": false,
                        "@class": "io.domainlifecycles.mirror.model.AssertedContainableTypeModel",
                        "isArray": false,
                        "typeName": "de.mercator.esprit.vendor.core.domain.haendler.HaendlerId",
                        "assertions": [],
                        "domainType": "IDENTITY",
                        "hasSetContainer": false,
                        "hasListContainer": false,
                        "containerTypeName": null,
                        "hasStreamContainer": false,
                        "containerAssertions": [],
                        "resolvedGenericType": {
                          "@class": "io.domainlifecycles.mirror.model.ResolvedGenericTypeModel",
                          "isArray": false,
                          "typeName": "de.mercator.esprit.vendor.core.domain.haendler.HaendlerId",
                          "genericTypes": [],
                          "wildcardBound": null
                        },
                        "hasOptionalContainer": false,
                        "hasCollectionContainer": false
                      },
                      "accessLevel": "PUBLIC",
                      "declaredByTypeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler",
                      "listenedEventTypeName": null,
                      "publishedEventTypeNames": []
                    }
                  ],
                  "abstract": false,
                  "typeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler",
                  "allFields": [
                    {
                      "name": "id",
                      "type": {
                        "array": false,
                        "@class": "io.domainlifecycles.mirror.model.AssertedContainableTypeModel",
                        "isArray": false,
                        "typeName": "de.mercator.esprit.vendor.core.domain.haendler.HaendlerId",
                        "assertions": [],
                        "domainType": "IDENTITY",
                        "hasSetContainer": false,
                        "hasListContainer": false,
                        "containerTypeName": null,
                        "hasStreamContainer": false,
                        "containerAssertions": [],
                        "resolvedGenericType": {
                          "@class": "io.domainlifecycles.mirror.model.ResolvedGenericTypeModel",
                          "isArray": false,
                          "typeName": "de.mercator.esprit.vendor.core.domain.haendler.HaendlerId",
                          "genericTypes": [],
                          "wildcardBound": null
                        },
                        "hasOptionalContainer": false,
                        "hasCollectionContainer": false
                      },
                      "@class": "io.domainlifecycles.mirror.model.ValueReferenceModel",
                      "hidden": false,
                      "static": false,
                      "modifiable": false,
                      "accessLevel": "PRIVATE",
                      "publicReadable": true,
                      "publicWriteable": false,
                      "declaredByTypeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler"
                    }
                  ],
                  "identityField": {
                    "name": "id",
                    "type": {
                      "array": false,
                      "@class": "io.domainlifecycles.mirror.model.AssertedContainableTypeModel",
                      "isArray": false,
                      "typeName": "de.mercator.esprit.vendor.core.domain.haendler.HaendlerId",
                      "assertions": [],
                      "domainType": "IDENTITY",
                      "hasSetContainer": false,
                      "hasListContainer": false,
                      "containerTypeName": null,
                      "hasStreamContainer": false,
                      "containerAssertions": [],
                      "resolvedGenericType": {
                        "@class": "io.domainlifecycles.mirror.model.ResolvedGenericTypeModel",
                        "isArray": false,
                        "typeName": "de.mercator.esprit.vendor.core.domain.haendler.HaendlerId",
                        "genericTypes": [],
                        "wildcardBound": null
                      },
                      "hasOptionalContainer": false,
                      "hasCollectionContainer": false
                    },
                    "@class": "io.domainlifecycles.mirror.model.ValueReferenceModel",
                    "hidden": false,
                    "static": false,
                    "modifiable": false,
                    "accessLevel": "PRIVATE",
                    "publicReadable": true,
                    "publicWriteable": false,
                    "declaredByTypeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler"
                  },
                  "allInterfaceTypeNames": [],
                  "inheritanceHierarchyTypeNames": ["io.domainlifecycles.domain.types.base.AggregateRootBase", "io.domainlifecycles.domain.types.base.EntityBase", "java.lang.Object"]
                }
                """

        );
        Assertions.assertThat(ar).isNotNull();
    }

    @Test
    public void testDeserializeConcreteDomainTypeMirrorJackson3(){
        var serializer = new Jackson3DomainSerializer(true);
        AggregateRootMirror ar = serializer.deserializeTypeMirror(
            """
                {
                  "@class": "io.domainlifecycles.mirror.model.AggregateRootModel",
                  "methods": [
                    {
                      "name": "builder",
                      "@class": "io.domainlifecycles.mirror.model.MethodModel",
                      "getter": false,
                      "setter": false,
                      "overridden": false,
                      "parameters": [],
                      "returnType": {
                        "array": false,
                        "@class": "io.domainlifecycles.mirror.model.AssertedContainableTypeModel",
                        "isArray": false,
                        "typeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler$HaendlerBuilder",
                        "assertions": [],
                        "domainType": "NON_DOMAIN",
                        "hasSetContainer": false,
                        "hasListContainer": false,
                        "containerTypeName": null,
                        "hasStreamContainer": false,
                        "containerAssertions": [],
                        "resolvedGenericType": {
                          "@class": "io.domainlifecycles.mirror.model.ResolvedGenericTypeModel",
                          "isArray": false,
                          "typeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler$HaendlerBuilder",
                          "genericTypes": [],
                          "wildcardBound": null
                        },
                        "hasOptionalContainer": false,
                        "hasCollectionContainer": false
                      },
                      "accessLevel": "PUBLIC",
                      "declaredByTypeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler",
                      "listenedEventTypeName": null,
                      "publishedEventTypeNames": []
                    },
                    {
                      "name": "getId",
                      "@class": "io.domainlifecycles.mirror.model.MethodModel",
                      "getter": true,
                      "setter": false,
                      "overridden": false,
                      "parameters": [],
                      "returnType": {
                        "array": false,
                        "@class": "io.domainlifecycles.mirror.model.AssertedContainableTypeModel",
                        "isArray": false,
                        "typeName": "de.mercator.esprit.vendor.core.domain.haendler.HaendlerId",
                        "assertions": [],
                        "domainType": "IDENTITY",
                        "hasSetContainer": false,
                        "hasListContainer": false,
                        "containerTypeName": null,
                        "hasStreamContainer": false,
                        "containerAssertions": [],
                        "resolvedGenericType": {
                          "@class": "io.domainlifecycles.mirror.model.ResolvedGenericTypeModel",
                          "isArray": false,
                          "typeName": "de.mercator.esprit.vendor.core.domain.haendler.HaendlerId",
                          "genericTypes": [],
                          "wildcardBound": null
                        },
                        "hasOptionalContainer": false,
                        "hasCollectionContainer": false
                      },
                      "accessLevel": "PUBLIC",
                      "declaredByTypeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler",
                      "listenedEventTypeName": null,
                      "publishedEventTypeNames": []
                    }
                  ],
                  "abstract": false,
                  "typeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler",
                  "allFields": [
                    {
                      "name": "id",
                      "type": {
                        "array": false,
                        "@class": "io.domainlifecycles.mirror.model.AssertedContainableTypeModel",
                        "isArray": false,
                        "typeName": "de.mercator.esprit.vendor.core.domain.haendler.HaendlerId",
                        "assertions": [],
                        "domainType": "IDENTITY",
                        "hasSetContainer": false,
                        "hasListContainer": false,
                        "containerTypeName": null,
                        "hasStreamContainer": false,
                        "containerAssertions": [],
                        "resolvedGenericType": {
                          "@class": "io.domainlifecycles.mirror.model.ResolvedGenericTypeModel",
                          "isArray": false,
                          "typeName": "de.mercator.esprit.vendor.core.domain.haendler.HaendlerId",
                          "genericTypes": [],
                          "wildcardBound": null
                        },
                        "hasOptionalContainer": false,
                        "hasCollectionContainer": false
                      },
                      "@class": "io.domainlifecycles.mirror.model.ValueReferenceModel",
                      "hidden": false,
                      "static": false,
                      "modifiable": false,
                      "accessLevel": "PRIVATE",
                      "publicReadable": true,
                      "publicWriteable": false,
                      "declaredByTypeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler"
                    }
                  ],
                  "identityField": {
                    "name": "id",
                    "type": {
                      "array": false,
                      "@class": "io.domainlifecycles.mirror.model.AssertedContainableTypeModel",
                      "isArray": false,
                      "typeName": "de.mercator.esprit.vendor.core.domain.haendler.HaendlerId",
                      "assertions": [],
                      "domainType": "IDENTITY",
                      "hasSetContainer": false,
                      "hasListContainer": false,
                      "containerTypeName": null,
                      "hasStreamContainer": false,
                      "containerAssertions": [],
                      "resolvedGenericType": {
                        "@class": "io.domainlifecycles.mirror.model.ResolvedGenericTypeModel",
                        "isArray": false,
                        "typeName": "de.mercator.esprit.vendor.core.domain.haendler.HaendlerId",
                        "genericTypes": [],
                        "wildcardBound": null
                      },
                      "hasOptionalContainer": false,
                      "hasCollectionContainer": false
                    },
                    "@class": "io.domainlifecycles.mirror.model.ValueReferenceModel",
                    "hidden": false,
                    "static": false,
                    "modifiable": false,
                    "accessLevel": "PRIVATE",
                    "publicReadable": true,
                    "publicWriteable": false,
                    "declaredByTypeName": "de.mercator.esprit.vendor.core.domain.haendler.Haendler"
                  },
                  "allInterfaceTypeNames": [],
                  "inheritanceHierarchyTypeNames": ["io.domainlifecycles.domain.types.base.AggregateRootBase", "io.domainlifecycles.domain.types.base.EntityBase", "java.lang.Object"]
                }
                """

        );
        Assertions.assertThat(ar).isNotNull();
    }
}
