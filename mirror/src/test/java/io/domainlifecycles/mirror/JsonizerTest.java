package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.DomainMirrorFactory;
import io.domainlifecycles.mirror.model.EntityModel;
import io.domainlifecycles.mirror.model.FieldModel;
import io.domainlifecycles.mirror.model.MethodModel;
import io.domainlifecycles.mirror.model.ProvidedDomain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import io.domainlifecycles.mirror.serialize.api.JacksonDomainSerializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class JsonizerTest {

    @Test
    public void testJsonizeWithoutTypeMeta() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
        testJsonize(factory);
    }

    @Test
    public void testJsonizeWithTypeMeta() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory( "tests");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        testJsonize(factory);
    }

    private  void testJsonize(DomainMirrorFactory factory) {
        var serializer = new JacksonDomainSerializer(true);
        var dm = factory.initializeDomainMirror();
        var result = serializer.serialize(dm);
        var init = serializer.deserialize(result);
        assertThat(init.getAllBoundedContextMirrors()).isEqualTo(dm.getAllBoundedContextMirrors());
        assertThat(init.getAllDomainTypeMirrors().size()).isEqualTo(dm.getAllDomainTypeMirrors().size());
        assertThat(init.getAllDomainTypeMirrors()).isEqualTo(dm.getAllDomainTypeMirrors());


        assertThat(init).isEqualTo(dm);
        assertThat(init.getAllDomainTypeMirrors()
                .stream()
                .map(t -> (ProvidedDomain) t)
                .allMatch(ProvidedDomain::domainMirrorSet)).isTrue();
        assertThat(init.getAllDomainTypeMirrors()
                .stream()
                .flatMap(dt-> dt.getAllFields().stream())
                .allMatch(t-> ((FieldModel)t).domainMirrorSet())).isTrue();
        assertThat(init.getAllDomainTypeMirrors()
            .stream()
            .flatMap(dt-> dt.getMethods().stream())
            .allMatch(t-> ((MethodModel)t).domainMirrorSet())).isTrue();
        assertThat(init.getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt instanceof EntityModel)
            .map(dt ->(EntityModel) dt)
            .map(dt-> (FieldModel)dt.getIdentityField().orElse(null))
            .filter(Objects::nonNull)
            .allMatch(FieldModel::domainMirrorSet)).isTrue();
        var result2 = serializer.serialize(init);
        assertThat(result).isEqualTo(result2);
    }

    @Test
    public void testDeserializeConcreteDomainTypeMirror(){
        var serializer = new JacksonDomainSerializer(true);
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
        assertThat(ar).isNotNull();
    }


}
