/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.swagger.v3;

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * For all special DLC domain object types (entities, valueObject, identities) some Open API extensions are
 * performed, to make the
 * Open API description of controller interfaces match the default mapping behaviour of the DLC Jackson extension.
 * <p>
 * Identity types which are referenced from entities or value objects are "folded" in their mapping behaviour by the DLC
 * Jackson extension. Jackson default behaviour would read and write only structures like:
 * </p>
 * <p>
 * {
 * "identityRef": {
 * "value": "idValue"
 * }
 * }
 * </p>
 * <p>
 * This is simplified by {@see io.domainlifecycles.jackson.module.DlcJacksonModule} to:
 * {
 * "identityRef": "idValue"
 * }
 * </p>
 * <p>
 * In the same manner so called "singleValued" value object references are folded.
 * This Open API extension, makes the api doc fit to that behaviour accordingly.
 * </p>
 * <p>
 * Also, id value fields are added to the API documentation of identity types. Regular Spring docs behaviour ignores
 * those
 * fields and gives Open API consumers wrong information. Primary identity properties are not required (for creating
 * endpoints POST and PUT), that's
 * why they are marked as not required.
 * </p>
 * The folding is also applied by {@see io.domainlifecycles.jackson.module.DlcJacksonModule} to path and query
 * parameters on API
 * endpoints and hereby is corrected in the API documentation.
 *
 * @author Mario Herb
 */
public class MirrorBasedOpenApiExtension {

    private static final Logger log = LoggerFactory.getLogger(MirrorBasedOpenApiExtension.class);


    /**
     * This extension modifies and extends the Open API description for all DLC specific domain object classes, which
     * are
     * managed by the given {@link Domain}.
     */
    public MirrorBasedOpenApiExtension() {
    }

    /**
     * Starting point for all Open API specific extensions and corrections by this class.
     *
     * @param openAPI                           {@link OpenAPI} instance to be extended
     * @param entitiesWithExternallyProvidedIds array of full qualified class names of entities which require primary
     *                                         identities
     */
    public void extendOpenAPISchemaForDLCTypes(OpenAPI openAPI, String... entitiesWithExternallyProvidedIds) {
        if (openAPI.getComponents() != null && openAPI.getComponents().getSchemas() != null) {
            openAPI.getComponents().getSchemas().forEach((typeName, typeSchema) -> {
                var tm = Domain.typeMirror(typeName);
                if (tm.isPresent() && DomainType.IDENTITY.equals(tm.get().getDomainType())) {
                    var idm = (IdentityMirror) tm.get();
                    extendSchemaOfIdentityType(idm, typeSchema);
                }

            });
            modifyEntitySchemata(openAPI, entitiesWithExternallyProvidedIds);
            modifyValueObjectSchemata(openAPI);
            modifyParameters(openAPI);
            modifyExternalDomainObjectReferences(openAPI);
        }

    }

    private void extendSchemaOfIdentityType(IdentityMirror identityMirror, Schema<?> domainObjectSchema) {
        try {

            if (identityMirror.getValueTypeName().isPresent()) {
                var mcInstance = ModelConverters.getInstance();
                var valueTypeName = identityMirror.getValueTypeName().get();
                var valueTypeClass = DlcAccess.getClassForName(valueTypeName);
                var idValueSchema = mcInstance.resolveAsResolvedSchema(new AnnotatedType().type(valueTypeClass));
                @SuppressWarnings("rawtypes") var propertiesMap = new HashMap<String, Schema>();
                var fieldName = identityMirror.getAllFields().get(0).getName();
                propertiesMap.put(fieldName, idValueSchema.schema.name(fieldName));
                domainObjectSchema.setProperties(propertiesMap);
                domainObjectSchema.setRequired(List.of(fieldName));
            } else {
                log.warn("Wasn't able to extend Identity Open API schema of '{}'! Identity value field not found!",
                    identityMirror.getTypeName());
            }
        } catch (Throwable t) {
            log.warn("Extending Open API schema of identity type '{}' failed", identityMirror.getTypeName(), t);
        }
    }

    private void modifyEntitySchemata(OpenAPI openAPI, String... entitiesWithExternallyProvidedIds) {
        Domain.getInitializedDomain()
            .allTypeMirrors()
            .values()
            .stream()
            .filter(dtm -> DomainType.ENTITY.equals(dtm.getDomainType()) || DomainType.AGGREGATE_ROOT.equals(
                dtm.getDomainType()))
            .map(dtm -> (EntityMirror) dtm)
            .forEach(
                em -> {
                    try {
                        String entityFqn = em.getTypeName();
                        var schemas = openAPI.getComponents().getSchemas();
                        var entitySchema = schemas.get(entityFqn);
                        if (entitySchema != null) {
                            if (em.getIdentityField().isPresent()) {
                                var idField = em.getIdentityField().get();
                                if (entitySchema.getRequired() != null && Arrays.stream(
                                    entitiesWithExternallyProvidedIds).noneMatch(fqn -> fqn.equals(em.getTypeName()))) {
                                    entitySchema.getRequired().remove(idField.getName());
                                }
                                modifyIdentitySchemaReference(openAPI, idField.getName(),
                                    idField.getType().getTypeName(), entitySchema);
                            }
                            em.getValueReferences()
                                .stream()
                                .filter(vrm -> !vrm.isStatic())
                                .filter(vrm -> vrm.getValue().isSingledValued() && !vrm.getValue().isEnum())
                                .forEach(vrm -> {
                                        var vm = vrm.getValue();
                                        if (vm.isValueObject()) {
                                            modifySingleValuedValueObjectSchemaReference(openAPI, vrm.getName(),
                                                vrm.getType().getTypeName(), entitySchema);
                                        } else {
                                            modifyIdentitySchemaReference(openAPI, vrm.getName(),
                                                vrm.getType().getTypeName(), entitySchema);
                                        }
                                    }
                                );
                        }
                    } catch (Throwable t) {
                        log.warn("Extending Open API schema of entity type '{}' failed", em.getTypeName(), t);
                    }
                }
            );
    }

    private void modifyValueObjectSchemata(OpenAPI openAPI) {
        Domain.getInitializedDomain()
            .allTypeMirrors()
            .values()
            .stream()
            .filter(dtm -> DomainType.VALUE_OBJECT.equals(dtm.getDomainType()))
            .map(dtm -> (ValueObjectMirror) dtm)
            .forEach(
                vo -> {
                    try {
                        String voFqn = vo.getTypeName();
                        var schemas = openAPI.getComponents().getSchemas();
                        var voSchema = schemas.get(voFqn);
                        if (voSchema != null) {
                            vo.getValueReferences()
                                .stream()
                                .filter(vrm -> !vrm.isStatic())
                                .filter(vrm -> vrm.getValue().isValueObject() || vrm.getValue().isIdentity())
                                .forEach(vrm -> {
                                    if (vrm.getValue().isValueObject()) {
                                        var vm = vrm.getValue();
                                        if (vm.isSingledValued()) {
                                            modifySingleValuedValueObjectSchemaReference(openAPI, vrm.getName(),
                                                vrm.getType().getTypeName(), voSchema);
                                        }
                                    } else {
                                        modifyIdentitySchemaReference(openAPI, vrm.getName(),
                                            vrm.getType().getTypeName(), voSchema);
                                    }

                                });
                        }
                    } catch (Throwable t) {
                        log.warn("Extending Open API schema of value object type '{}' failed", vo.getTypeName(), t);
                    }
                }
            );
    }

    private void modifyIdentitySchemaReference(OpenAPI openAPI, String propertyName, String propertyIdentityTypeName,
                                               Schema<?> targetSchema) {

        Schema<?> identitySchema = openAPI.getComponents().getSchemas().get(propertyIdentityTypeName);
        if (identitySchema != null && identitySchema.getProperties() != null) {
            @SuppressWarnings("rawtypes") var idValueSchema =
                (Optional<Schema>) identitySchema.getProperties().values().stream().findFirst();
            if (idValueSchema.isPresent()) {
                Schema<?> newIdentityPropertySchema = copyValueSchema(idValueSchema.get(),
                    targetSchema.getName() + "." + propertyName);
                if (targetSchema.getProperties() != null) {
                    Schema<?> currentValueSchema = targetSchema.getProperties().get(propertyName);
                    if (Constants.TYPE_ARRAY.equals(currentValueSchema.getType())) {
                        currentValueSchema.setItems(newIdentityPropertySchema);
                    } else {
                        targetSchema.getProperties().put(propertyName, newIdentityPropertySchema);
                    }
                    return;
                }
            }
        }
        log.warn(
            "Wasn't able to set correct value type for property '" + propertyName + "' on schema'" + targetSchema.getName() + "'!");
    }

    private void modifySingleValuedValueObjectSchemaReference(OpenAPI openAPI, String voReferencePropertyName,
                                                              String voTypeName, Schema<?> targetSchema) {
        Schema<?> valueObjectSchema = openAPI.getComponents().getSchemas().get(voTypeName);
        if (valueObjectSchema != null && valueObjectSchema.getProperties() != null) {
            @SuppressWarnings("rawtypes") var valueSchema =
                (Optional<Schema>) valueObjectSchema.getProperties().values().stream().findFirst();
            if (valueSchema.isPresent()) {
                Schema<?> newValueSchema = copyValueSchema(valueSchema.get(),
                    targetSchema.getName() + "." + voReferencePropertyName);
                if (targetSchema.getProperties() != null) {
                    Schema<?> currentValueSchema = targetSchema.getProperties().get(voReferencePropertyName);
                    if (currentValueSchema != null) {
                        if (Constants.TYPE_ARRAY.equals(currentValueSchema.getType())) {
                            currentValueSchema.setItems(newValueSchema);
                        } else {
                            targetSchema.getProperties().put(voReferencePropertyName, newValueSchema);
                        }
                    }
                    return;
                }
            }
        }
        log.warn(
            "Wasn't able to set correct value type for property '" + voReferencePropertyName + "' on '" + voTypeName + "'!");
    }

    private Schema<?> copyValueSchema(Schema<?> source, String newName) {
        Schema<?> newSchema = new Schema<>();
        newSchema.setName(newName);
        newSchema.setType(source.getType());
        newSchema.setPattern(source.getPattern());
        newSchema.setDescription(source.getDescription());
        newSchema.setFormat(source.getFormat());
        if (source.getExample() != null) {
            newSchema.setExample(source.getExample());
        }
        newSchema.setExclusiveMinimum(source.getExclusiveMinimum());
        newSchema.setExclusiveMaximum(source.getExclusiveMaximum());
        newSchema.setMinimum(source.getMinimum());
        newSchema.setMaximum(source.getMaximum());
        newSchema.setDefault(source.getDefault());
        newSchema.setMaxLength(source.getMaxLength());
        newSchema.setMinLength(source.getMinLength());
        newSchema.setJsonSchema(source.getJsonSchema());
        return newSchema;
    }

    private void modifyParameters(OpenAPI openAPI) {
        openAPI.getPaths().values()
            .forEach(pathItem -> {
                List<Operation> ops = pathItem.readOperations();
                if (ops != null) {
                    ops.forEach(op -> {
                        var parameters = op.getParameters();
                        if (parameters != null) {
                            parameters
                                .forEach(param -> modifyParam(param, openAPI));
                        }
                    });
                }
            });
    }

    private void modifyParam(Parameter param, OpenAPI openAPI) {
        String ref = param.getSchema().get$ref();
        if (Constants.TYPE_ARRAY.equals(param.getSchema().getType())) {
            ref = param.getSchema().getItems().get$ref();
        }
        if (ref != null) {
            String referencedTypeFqn = ref.substring(ref.lastIndexOf("/") + 1);
            Schema<?> refTypeSchema = openAPI.getComponents().getSchemas().get(referencedTypeFqn);
            var dtmOptional = Domain.typeMirror(referencedTypeFqn);
            if (dtmOptional.isPresent()) {
                var dtm = dtmOptional.get();
                if (DomainType.VALUE_OBJECT.equals(dtm.getDomainType())) {
                    var vo = (ValueObjectMirror) dtm;
                    if (vo.isSingledValued()) {
                        modifySingleValuedTypeSchema(param, refTypeSchema, referencedTypeFqn);
                    }
                } else if (DomainType.IDENTITY.equals(dtm.getDomainType())) {
                    modifySingleValuedTypeSchema(param, refTypeSchema, referencedTypeFqn);
                }
            } else {
                log.warn("Wasn't able to modify param schema for '{}'!", referencedTypeFqn);
            }
        }
    }

    private void modifySingleValuedTypeSchema(Parameter param, Schema<?> refTypeSchema, String referencedTypeFqn) {
        if (refTypeSchema.getProperties() != null) {
            @SuppressWarnings("rawtypes") var propSchema =
                (Optional<Schema>) refTypeSchema.getProperties().values().stream().findFirst();
            if (propSchema.isPresent()) {
                var newSchema = copyValueSchema(propSchema.get(), param.getName() + "." + refTypeSchema.getName());
                if (Constants.TYPE_ARRAY.equals(param.getSchema().getType())) {
                    //noinspection unchecked
                    param.getSchema().setItems(newSchema);
                } else {
                    param.schema(newSchema);
                }
                return;
            }
        }
        log.warn("Wasn't able to modify param schema for '{}'!", referencedTypeFqn);
    }

    private void modifyExternalDomainObjectReferences(OpenAPI openAPI) {
        openAPI.getComponents().getSchemas()
            .forEach(
                (typeName, typeSchema) -> {
                    var dtmOptional = Domain.typeMirror(typeName);
                    if (dtmOptional.isEmpty()) {
                        if (typeSchema.getProperties() != null) {
                            //noinspection unchecked
                            typeSchema.getProperties().forEach(
                                (n, p) -> {
                                    String pName = (String) n;
                                    Schema<?> pSchema = (Schema<?>) p;
                                    modifyExternalDomainReferenceSchema(openAPI, pName, pSchema, typeSchema);
                                });
                        }
                    }
                });
    }

    private void modifyExternalDomainReferenceSchema(OpenAPI openAPI, String propertyName, Schema<?> propertySchema,
                                                     Schema<?> containerSchema) {
        var pSchema = propertySchema;
        if (Constants.TYPE_ARRAY.equals(pSchema.getType())) {
            pSchema = propertySchema.getItems();
        }
        if (pSchema.get$ref() != null) {
            var propTypeName = pSchema.get$ref().substring("#/components/schemas/".length());
            var dtmOptional = Domain.typeMirror(propTypeName);
            if (dtmOptional.isPresent()) {
                var dtm = dtmOptional.get();
                if (DomainType.IDENTITY.equals(dtm.getDomainType())) {
                    modifyIdentitySchemaReference(openAPI, propertyName, propTypeName, containerSchema);
                } else if (DomainType.VALUE_OBJECT.equals(dtm.getDomainType())) {
                    var voMirror = Domain.valueObjectMirrorFor(propTypeName);
                    if (voMirror.isSingledValued()) {
                        modifySingleValuedValueObjectSchemaReference(openAPI, propertyName, propTypeName,
                            containerSchema);
                    }
                }
            }
        }
    }

}
