/*
 *
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

package nitrox.dlc.jackson.databind.context;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import nitrox.dlc.builder.DomainObjectBuilder;
import nitrox.dlc.builder.DomainObjectBuilderProvider;
import nitrox.dlc.jackson.exception.DLCJacksonException;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.api.DomainType;
import nitrox.dlc.mirror.api.EntityMirror;
import nitrox.dlc.mirror.api.ValueObjectMirror;

import java.util.HashMap;
import java.util.Map;

/**
 * A DomainObjectMappingContextHolder instance is created within the Jackson {@link DeserializationContext}.
 * As such it is created for every new DeserializationContext instance. It provides access to several key objects in the deserialition process kept
 * in {@link DomainObjectMappingContext} instances.
 *
 * @author Mario Herb
 */
public class DomainObjectMappingContextHolder {

    protected static final String KEY = "DOMAIN_OBJECT_MAPPING_CONTEXT_HOLDER";

    private final Map<DomainObjectMappingContextKey, DomainObjectMappingContext> contexts = new HashMap<>();

    /**
     * Returns the current {@link DomainObjectMappingContext} instance
     */
    public static DomainObjectMappingContext getContext(JsonNode jsonNode,
                                                        String domainObjectTypeName,
                                                        DeserializationContext deserializationContext,
                                                        DomainObjectBuilderProvider domainObjectBuilderProvider) {
        DomainObjectMappingContextHolder instance = (DomainObjectMappingContextHolder) deserializationContext.getAttribute(KEY);
        if (instance == null) {
            instance = new DomainObjectMappingContextHolder();
            deserializationContext.setAttribute(KEY, instance);
        }

        DomainObjectMappingContextKey key = new DomainObjectMappingContextKey(jsonNode, domainObjectTypeName);
        DomainObjectMappingContext mappingContext = instance.contexts.get(key);
        if (mappingContext == null) {
            instance.initContexts(jsonNode, domainObjectTypeName, null, instance, domainObjectBuilderProvider);
            mappingContext = instance.contexts.get(key);

        }
        return mappingContext;
    }

    private void initContexts(JsonNode node,
                              String domainObjectTypeName,
                              DomainObjectMappingContext parentContext,
                              DomainObjectMappingContextHolder instance,
                              DomainObjectBuilderProvider domainObjectBuilderProvider) {
        DomainObjectBuilder<?> domainObjectBuilder = domainObjectBuilderProvider.provide(domainObjectTypeName);
        DomainObjectMappingContext mappingContext = new DomainObjectMappingContext(node, domainObjectTypeName, domainObjectBuilder, parentContext);
        DomainObjectMappingContextKey key = new DomainObjectMappingContextKey(node, domainObjectTypeName);
        instance.contexts.put(key, mappingContext);

        var dtm = Domain.typeMirror(domainObjectTypeName)
            .orElseThrow(()-> DLCJacksonException.fail("DomainTypeMirror not found for '%s'", domainObjectTypeName));

        if (DomainType.ENTITY.equals(dtm.getDomainType()) || DomainType.AGGREGATE_ROOT.equals(dtm.getDomainType())) {
            EntityMirror em = (EntityMirror)dtm;
            em.getEntityReferences().forEach(
                erm -> {
                    var refNode = node.get(erm.getName());
                    if ( refNode != null && !refNode.isNull()) {
                        if (!erm.getType().hasCollectionContainer()) {
                            initContexts(refNode, erm.getType().getTypeName(), mappingContext, instance, domainObjectBuilderProvider);
                        } else {
                            ArrayNode an = (ArrayNode) refNode;
                            an.forEach(en -> initContexts(en, erm.getType().getTypeName(), mappingContext, instance, domainObjectBuilderProvider));
                        }
                    }
                }
            );

            em.getValueReferences()
                .stream()
                .filter(vrm -> vrm.getValue().isValueObject())
                .forEach(vrm -> {
                    var refNode = node.get(vrm.getName());
                    if (refNode != null && !refNode.isNull()) {
                        if (!vrm.getType().hasCollectionContainer()) {
                            initContexts(refNode, vrm.getType().getTypeName(), mappingContext, instance, domainObjectBuilderProvider);
                        } else {
                            ArrayNode an = (ArrayNode) refNode;
                            an.forEach(en -> initContexts(en, vrm.getType().getTypeName(), mappingContext, instance, domainObjectBuilderProvider));
                        }
                    }
                }
            );

        } else if (DomainType.VALUE_OBJECT.equals(dtm.getDomainType())) {
            ValueObjectMirror vm = (ValueObjectMirror) dtm;

            vm.getValueReferences().stream()
                .filter(vrm -> vrm.getValue().isValueObject())
                .forEach(vrm -> {
                    var refNode = node.get(vrm.getName());
                    if ( refNode != null && !refNode.isNull()) {
                        if (!vrm.getType().hasListContainer()) {
                            initContexts(refNode, vrm.getType().getTypeName(), mappingContext, instance, domainObjectBuilderProvider);
                        } else {
                            ArrayNode an = (ArrayNode) refNode;
                            an.forEach(en -> initContexts(en, vrm.getType().getTypeName(), mappingContext, instance, domainObjectBuilderProvider));
                        }
                    }
                }
            );
        }
    }


}
