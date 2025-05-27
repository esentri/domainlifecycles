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

package io.domainlifecycles.diagram.domain.mapper;

import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.nomnoml.NomnomlClass;
import io.domainlifecycles.diagram.nomnoml.NomnomlField;
import io.domainlifecycles.diagram.nomnoml.NomnomlMethod;
import io.domainlifecycles.diagram.nomnoml.NomnomlParameter;
import io.domainlifecycles.diagram.nomnoml.NomnomlStereotype;
import io.domainlifecycles.diagram.nomnoml.NomnomlType;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.AssertionMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.ParamMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;
import io.domainlifecycles.mirror.model.AssertionType;
import io.domainlifecycles.mirror.visitor.ContextDomainObjectVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Derives the {@link NomnomlClass} representations from the given domain structures delivered as mirrors.
 *
 * @author Mario Herb
 */
public class DomainClassMapper {

    private final DomainDiagramConfig domainDiagramConfig;

    /**
     * Initializes the DomainClassMapper with a given {@link DomainDiagramConfig}
     *
     * @param domainDiagramConfig diagram configuration
     */
    public DomainClassMapper(DomainDiagramConfig domainDiagramConfig) {
        this.domainDiagramConfig = domainDiagramConfig;
    }

    /**
     * Maps DomainEvent structure to a {@link NomnomlClass} representation.
     *
     * @param domainEventMirror mirrored domain event
     * @return mapped domain event
     */
    public NomnomlClass mapDomainEventClass(DomainEventMirror domainEventMirror) {
        return mapToNomnomlClass(domainEventMirror,
            domainDiagramConfig.getGeneralVisualSettings().isShowDomainEventFields() && domainDiagramConfig.getGeneralVisualSettings().isShowFields(),
            domainDiagramConfig.getGeneralVisualSettings().isShowDomainEventMethods() && domainDiagramConfig.getGeneralVisualSettings().isShowMethods()
        );
    }

    /**
     * Maps DomainCommand structure to a {@link NomnomlClass} representation.
     *
     * @param domainCommandMirror mirrored domain command
     * @return mapped domain command
     */
    public NomnomlClass mapDomainCommandClass(DomainCommandMirror domainCommandMirror) {
        return mapToNomnomlClass(domainCommandMirror,
            domainDiagramConfig.getGeneralVisualSettings().isShowDomainCommandFields() && domainDiagramConfig.getGeneralVisualSettings().isShowFields(),
            domainDiagramConfig.getGeneralVisualSettings().isShowDomainCommandMethods() && domainDiagramConfig.getGeneralVisualSettings().isShowMethods()
        );
    }

    /**
     * Maps DomainService structure to a {@link NomnomlClass} representation.
     *
     * @param domainServiceMirror mirrored domain service
     * @return mapped domain service
     */
    public NomnomlClass mapDomainServiceClass(DomainServiceMirror domainServiceMirror) {
        return mapToNomnomlClass(domainServiceMirror,
            domainDiagramConfig.getGeneralVisualSettings().isShowDomainServiceFields() && domainDiagramConfig.getGeneralVisualSettings().isShowFields(),
            domainDiagramConfig.getGeneralVisualSettings().isShowDomainServiceMethods() && domainDiagramConfig.getGeneralVisualSettings().isShowMethods()
        );
    }

    /**
     * Maps ApplicationService structure to a {@link NomnomlClass} representation.
     *
     * @param applicationServiceMirror mirrored application service
     * @return mapped application service
     */
    public NomnomlClass mapApplicationServiceClass(ApplicationServiceMirror applicationServiceMirror) {
        return mapToNomnomlClass(applicationServiceMirror,
            domainDiagramConfig.getGeneralVisualSettings().isShowDomainServiceFields() && domainDiagramConfig.getGeneralVisualSettings().isShowFields(),
            domainDiagramConfig.getGeneralVisualSettings().isShowDomainServiceMethods() && domainDiagramConfig.getGeneralVisualSettings().isShowMethods()
        );
    }

    /**
     * Maps Repository structure to a {@link NomnomlClass} representation.
     *
     * @param repositoryMirror mirrored repository
     * @return mapped repository
     */
    public NomnomlClass mapRepositoryClass(RepositoryMirror repositoryMirror) {
        return mapToNomnomlClass(repositoryMirror,
            domainDiagramConfig.getGeneralVisualSettings().isShowRepositoryFields() && domainDiagramConfig.getGeneralVisualSettings().isShowFields(),
            domainDiagramConfig.getGeneralVisualSettings().isShowRepositoryMethods() && domainDiagramConfig.getGeneralVisualSettings().isShowMethods()
        );
    }

    /**
     * Maps ReadModel structure to a {@link NomnomlClass} representation.
     *
     * @param readModelMirror mirrored read model
     * @return mapped read model
     */
    public NomnomlClass mapReadModelClass(ReadModelMirror readModelMirror) {
        return mapToNomnomlClass(readModelMirror,
            domainDiagramConfig.getGeneralVisualSettings().isShowReadModelFields() && domainDiagramConfig.getGeneralVisualSettings().isShowFields(),
            domainDiagramConfig.getGeneralVisualSettings().isShowReadModelMethods() && domainDiagramConfig.getGeneralVisualSettings().isShowMethods()
        );
    }

    /**
     * Maps QueryHandler structure to a {@link NomnomlClass} representation.
     *
     * @param queryHandlerMirror mirrored query handler
     * @return mapped query handler
     */
    public NomnomlClass mapQueryHandlerClass(QueryHandlerMirror queryHandlerMirror) {
        return mapToNomnomlClass(queryHandlerMirror,
            domainDiagramConfig.getGeneralVisualSettings().isShowQueryHandlerFields() && domainDiagramConfig.getGeneralVisualSettings().isShowFields(),
            domainDiagramConfig.getGeneralVisualSettings().isShowQueryHandlerMethods() && domainDiagramConfig.getGeneralVisualSettings().isShowMethods()
        );
    }

    /**
     * Maps OutboundService structure to a {@link NomnomlClass} representation.
     *
     * @param outboundServiceMirror mirrored outbound service
     * @return mapped outbound service
     */
    public NomnomlClass mapOutboundServiceClass(OutboundServiceMirror outboundServiceMirror) {
        return mapToNomnomlClass(outboundServiceMirror,
            domainDiagramConfig.getGeneralVisualSettings().isShowOutboundServiceFields() && domainDiagramConfig.getGeneralVisualSettings().isShowFields(),
            domainDiagramConfig.getGeneralVisualSettings().isShowOutboundServiceMethods() && domainDiagramConfig.getGeneralVisualSettings().isShowMethods()
        );
    }

    /**
     * Maps ServiceKind structure to a {@link NomnomlClass} representation.
     *
     * @param serviceKindMirror mirrored service kind
     * @return mapped service kind
     */
    public NomnomlClass mapUnspecifiedServiceKindClass(ServiceKindMirror serviceKindMirror) {
        return mapToNomnomlClass(serviceKindMirror,
            domainDiagramConfig.getGeneralVisualSettings().isShowUnspecifiedServiceKindFields() && domainDiagramConfig.getGeneralVisualSettings().isShowFields(),
            domainDiagramConfig.getGeneralVisualSettings().isShowUnspecifiedServiceKindMethods() && domainDiagramConfig.getGeneralVisualSettings().isShowMethods()
        );
    }

    /**
     * Maps Aggregate structure to a list of {@link NomnomlClass} representations.
     *
     * @param aggregateRootMirror mirrored AggregateRoot
     * @return mapped aggregate classes
     */
    public List<NomnomlClass> mapAllAggregateClasses(AggregateRootMirror aggregateRootMirror) {
        var aggregateClasses = new ArrayList<NomnomlClass>();

        var visitor = new ContextDomainObjectVisitor(aggregateRootMirror) {
            @Override
            public void visitEnterAnyDomainType(DomainTypeMirror domainTypeMirror) {
                if (List.of(
                    DomainType.AGGREGATE_ROOT,
                    DomainType.ENTITY,
                    DomainType.VALUE_OBJECT
                ).contains(domainTypeMirror.getDomainType())) {
                    if (!domainDiagramConfig.getDiagramTrimSettings().getClassesBlacklist().contains(domainTypeMirror.getTypeName())) {
                        if (DomainType.VALUE_OBJECT.equals(domainTypeMirror.getDomainType())) {
                            var voMirror = (ValueObjectMirror) domainTypeMirror;
                            if (!voMirror.isSingledValued()) {
                                aggregateClasses.add(mapToNomnomlClass(domainTypeMirror,
                                    domainDiagramConfig.getGeneralVisualSettings().isShowAggregateFields() && domainDiagramConfig.getGeneralVisualSettings().isShowFields(),
                                    domainDiagramConfig.getGeneralVisualSettings().isShowAggregateMethods() && domainDiagramConfig.getGeneralVisualSettings().isShowMethods()));
                            }
                        } else {
                            aggregateClasses.add(mapToNomnomlClass(domainTypeMirror,
                                domainDiagramConfig.getGeneralVisualSettings().isShowAggregateFields() && domainDiagramConfig.getGeneralVisualSettings().isShowFields(),
                                domainDiagramConfig.getGeneralVisualSettings().isShowAggregateMethods() && domainDiagramConfig.getGeneralVisualSettings().isShowMethods()));
                        }
                    }
                }
            }
        };
        visitor.start();
        return aggregateClasses;
    }

    /**
     * Maps a generic domain type to a {@link NomnomlClass} representation.
     */
    private NomnomlClass mapToNomnomlClass(DomainTypeMirror domainTypeMirror, boolean showFields, boolean showMethods) {
        var className = DomainMapperUtils.domainTypeName(domainTypeMirror, domainDiagramConfig);
        var nomnomlClassBuilder = NomnomlClass
            .builder()
            .styleClassifier(DomainMapperUtils.styleClassifier(domainTypeMirror))
            .stereotypes(List.of(NomnomlStereotype.builder().name(
                DomainMapperUtils.stereotype(domainTypeMirror, domainDiagramConfig)).build()))
            .name(className)
            .comment("!!! " + domainTypeMirror.getTypeName() + " !!!")
            .showFields(showFields)
            .showMethods(showMethods);
        if (showFields) {
            var inheritedIdentityName = "";
            if (domainTypeMirror.getDomainType().equals(DomainType.ENTITY) || domainTypeMirror.getDomainType().equals(
                DomainType.AGGREGATE_ROOT)) {
                var entityMirror = (EntityMirror) domainTypeMirror;
                if (entityMirror.getIdentityField().isPresent()) {
                    inheritedIdentityName = entityMirror.getIdentityField().get().getName();
                } else {
                    inheritedIdentityName = Identity.class.getName();
                }
            }
            final var inheritedIdentityNameFinal = inheritedIdentityName;
            var allShownFields = new ArrayList<NomnomlField>();
            var basicFields = domainTypeMirror.getAllFields()
                .stream()
                .filter(p -> !p.isIdentityField())
                .filter(p -> !p.isHidden())
                .filter(p -> !p.isStatic())
                .filter(p -> !p.getName().equals(inheritedIdentityNameFinal))
                .filter(p -> !domainDiagramConfig.getGeneralVisualSettings().getFieldBlacklist().contains(p.getName()))
                .filter(p -> DomainMapperUtils.showPropertyInline(p, domainTypeMirror, domainDiagramConfig))
                .filter(p -> {
                    if (!domainDiagramConfig.getGeneralVisualSettings().isShowInheritedMembersInClasses()) {
                        return p.getDeclaredByTypeName().equals(domainTypeMirror.getTypeName());
                    }
                    return true;
                })
                .filter(p -> {
                    if (!domainDiagramConfig.getGeneralVisualSettings().isShowObjectMembersInClasses()) {
                        return !p.getDeclaredByTypeName().equals(Object.class.getName());
                    }
                    return true;
                })
                .sorted(new PropertyComparator())
                .map(this::mapToNomnomlField)
                .distinct()
                .toList();
            if (DomainType.ENTITY.equals(domainTypeMirror.getDomainType())
                || DomainType.AGGREGATE_ROOT.equals(domainTypeMirror.getDomainType())) {
                EntityMirror entityMirror = (EntityMirror) domainTypeMirror;
                if (entityMirror.getIdentityField().isPresent()) {
                    allShownFields.add(mapToNomnomlField(entityMirror.getIdentityField().get()));
                }
            }
            allShownFields.addAll(basicFields);
            nomnomlClassBuilder.fields(
                allShownFields
            );
        } else {
            nomnomlClassBuilder.fields(Collections.emptyList());
        }
        if (showMethods) {
            nomnomlClassBuilder.methods(
                domainTypeMirror.getMethods()
                    .stream()
                    .filter(m -> !domainDiagramConfig.getGeneralVisualSettings().isShowOnlyPublicMethods()
                        || m.getAccessLevel().equals(AccessLevel.PUBLIC)
                    )
                    .filter(m -> !m.isOverridden())
                    .filter(m -> !domainDiagramConfig.getGeneralVisualSettings().getMethodBlacklist().contains(m.getName()))
                    .filter(m -> {
                        if (!domainDiagramConfig.getGeneralVisualSettings().isShowInheritedMembersInClasses()) {
                            return m.getDeclaredByTypeName().equals(domainTypeMirror.getTypeName());
                        }
                        return true;
                    })
                    .filter(m -> {
                        if (!domainDiagramConfig.getGeneralVisualSettings().isShowObjectMembersInClasses()) {
                            return !m.getDeclaredByTypeName().equals(Object.class.getName());
                        }
                        return true;
                    })
                    .filter(m -> !m.isGetter() && !m.isSetter())
                    .sorted(new MethodComparator())
                    .map(this::mapToNomnomlMethod)
                    .distinct()
                    .collect(Collectors.toList())
            );
        } else {
            nomnomlClassBuilder.methods(Collections.emptyList());
        }
        return nomnomlClassBuilder.build();
    }

    /**
     * Maps a AssertedContainableType to a {@link NomnomlType} representation.
     */
    private NomnomlType mapToNomnomlType(AssertedContainableTypeMirror assertedContainableTypeMirror) {
        String typeName;
        Optional<String> containerTypeName;
        if (assertedContainableTypeMirror.getResolvedGenericType() == null
            || assertedContainableTypeMirror.getResolvedGenericType().genericTypes() == null
            || assertedContainableTypeMirror.getResolvedGenericType().genericTypes().isEmpty()
        ) {
            typeName = DomainMapperUtils.mapTypeName(assertedContainableTypeMirror.getTypeName(), domainDiagramConfig);
            if (assertedContainableTypeMirror.isArray()) {
                typeName += "[]";
            }
            containerTypeName = assertedContainableTypeMirror.getContainerTypeName().map(
                cn -> DomainMapperUtils.mapTypeName(cn, domainDiagramConfig));
        } else {
            var genericType = assertedContainableTypeMirror.getResolvedGenericType();
            if (assertedContainableTypeMirror.hasContainer()) {
                typeName = DomainMapperUtils.mapTypeName(genericType.genericTypes().get(0).typeName(),
                    domainDiagramConfig);
                if (genericType.isArray()) {
                    typeName += "[]";
                }
                containerTypeName = assertedContainableTypeMirror.getContainerTypeName().map(
                    cn -> DomainMapperUtils.mapTypeName(cn, domainDiagramConfig));
            } else {
                if (genericType.isArray()) {
                    typeName = DomainMapperUtils.mapTypeName(genericType.typeName() + "[]", domainDiagramConfig);
                    containerTypeName = Optional.empty();
                } else if (genericType.genericTypes().isEmpty()) {
                    typeName = DomainMapperUtils.mapTypeName(genericType.typeName(), domainDiagramConfig);
                    containerTypeName = Optional.empty();
                } else {
                    containerTypeName = Optional.of(
                        DomainMapperUtils.mapTypeName(genericType.typeName(), domainDiagramConfig));
                    typeName = genericType.genericTypes().stream()
                        .map(tn -> tn.typeName() + (tn.isArray() ? "[]" : ""))
                        .map(tn -> DomainMapperUtils.mapTypeName(tn, domainDiagramConfig))
                        .collect(Collectors.joining(", "));
                }
            }
        }
        return NomnomlType.builder()
            .typeName(typeName)
            .typeAssertions(assertionList(assertedContainableTypeMirror.getAssertions()))
            .containerTypeAssertions(assertionList(assertedContainableTypeMirror.getContainerAssertions()))
            .containerTypeName(containerTypeName)
            .build();
    }

    /**
     * Maps a field to a {@link NomnomlField} representation.
     */
    private NomnomlField mapToNomnomlField(FieldMirror fieldMirror) {
        return NomnomlField.builder()
            .name(fieldMirror.getName())
            .typePrefix(fieldStereoType(fieldMirror))
            .required(fieldMirror.isIdentityField() || isTypeRequired(fieldMirror.getType()))
            .type(mapToNomnomlType(fieldMirror.getType()))
            .visibility(fieldVisibility(fieldMirror))
            .build();
    }

    /**
     * Detects the stereotype description for a property.
     */
    private Optional<String> fieldStereoType(FieldMirror fieldMirror) {
        Optional<String> typePrefix = Optional.empty();
        if (domainDiagramConfig.getGeneralVisualSettings().isFieldStereotypes()) {
            if (fieldMirror.isIdentityField()) {
                return Optional.of("<ID>");
            } else {
                if (fieldMirror.getType().getDomainType().equals(DomainType.ENUM)) {
                    return Optional.of("<ENUM>");
                } else if (fieldMirror.getType().getDomainType().equals(DomainType.VALUE_OBJECT)) {
                    return Optional.of("<VO>");
                }  else if (fieldMirror.getType().getDomainType().equals(DomainType.ENTITY)) {
                    return Optional.of("<ENTITY>");
                } else if (fieldMirror.getType().getDomainType().equals(DomainType.AGGREGATE_ROOT)) {
                    return Optional.of("<AR>");
                } else if (fieldMirror.getType().getDomainType().equals(DomainType.IDENTITY)) {
                    return Optional.of("<IDREF>");
                }
            }
        }
        return typePrefix;
    }

    private boolean isTypeRequired(AssertedContainableTypeMirror assertedContainableTypeMirror) {
        if (!assertedContainableTypeMirror.hasContainer()) {
            return assertedContainableTypeMirror
                .getAssertions()
                .stream()
                .anyMatch(a -> AssertionType.isNotNull.equals(a.getAssertionType())
                    || AssertionType.isNotEmpty.equals(a.getAssertionType()));
        } else {
            if (!assertedContainableTypeMirror.hasOptionalContainer()) {
                return assertedContainableTypeMirror
                    .getAssertions()
                    .stream()
                    .anyMatch(a -> AssertionType.isNotNull.equals(a.getAssertionType()));
            } else {
                //Optional properties are not required!
                return false;
            }
        }
    }

    private String fieldVisibility(FieldMirror propertyMirror) {
        var visibility = "";
        if (propertyMirror.isPublicReadable()) {
            visibility += "+";
        } else {
            visibility += "-";
        }
        if (propertyMirror.isPublicWriteable()) {
            visibility += "+";
        } else {
            visibility += "-";
        }
        return visibility;
    }

    private NomnomlMethod mapToNomnomlMethod(MethodMirror methodMirror) {
        return NomnomlMethod
            .builder()
            .name(methodMirror.getName())
            .returnType(mapToNomnomlType(methodMirror.getReturnType()))
            .visibility(methodVisibility(methodMirror))
            .parameters(
                methodMirror
                    .getParameters()
                    .stream()
                    .map(this::mapToNomnomlParameter)
                    .collect(Collectors.toList()))
            .build();
    }

    private NomnomlParameter mapToNomnomlParameter(ParamMirror paramMirror) {
        return NomnomlParameter.builder()
            .required(isTypeRequired(paramMirror.getType()))
            .type(mapToNomnomlType(paramMirror.getType()))
            .build();
    }

    private String methodVisibility(MethodMirror methodMirror) {
        if (AccessLevel.PUBLIC.equals(methodMirror.getAccessLevel())) {
            return "+";
        }
        return "-";
    }

    private List<String> assertionList(List<AssertionMirror> assertionMirrors) {
        if (assertionMirrors != null && domainDiagramConfig.getGeneralVisualSettings().isShowAssertions()) {
            return assertionMirrors
                .stream()
                .sorted(new AssertionComparator())
                .map(this::assertionStringAfterType)
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private static class PropertyComparator implements Comparator<FieldMirror> {

        @Override
        public int compare(FieldMirror o1, FieldMirror o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    private static class MethodComparator implements Comparator<MethodMirror> {

        /**
         * Compares two {@link MethodMirror} instances based on their method names, parameter count,
         * parameter types, and access level to determine their order.
         *
         * @param o1 the first {@link MethodMirror} to be compared
         * @param o2 the second {@link MethodMirror} to be compared
         * @return a negative integer, zero, or a positive integer as the first argument is less than,
         * equal to, or greater than the second, respectively
         */
        @Override
        public int compare(MethodMirror o1, MethodMirror o2) {
            // compare method names
            int result = o1.getName().compareTo(o2.getName());
            if (result == 0) {
                // if names are equal compare parameter count
                Integer size1 = o1.getParameters().size();
                Integer size2 = o2.getParameters().size();
                result = size1.compareTo(size2);
                if (result == 0 && size1 > 0) {
                    // if parameter count is equal compare parameter types
                    Collection<String> parameterTypes1 = o1.getParameters()
                        .stream()
                        .map(p -> p.getType().getTypeName()).toList();
                    Collection<String> parameterTypes2 = o2.getParameters()
                        .stream()
                        .map(p -> p.getType().getTypeName()).toList();
                    Iterator<String> it1 = parameterTypes1.iterator();
                    Iterator<String> it2 = parameterTypes2.iterator();
                    while (it1.hasNext() && it2.hasNext()) {
                        result = it1.next().compareTo(it2.next());
                        if (result != 0) {
                            return result;
                        }
                    }
                }
                // if names and parameter count and parameter types are equal compare access level
                if (result == 0) {
                    result = o1.getAccessLevel().compareTo(o2.getAccessLevel());
                }
            }
            return result;
        }
    }

    private static class AssertionComparator implements Comparator<AssertionMirror> {

        /**
         * Compares two AssertionMirror objects based on their predefined order index.
         * The order index is determined by the type of assertion they represent.
         *
         * @param o1 the first AssertionMirror object to be compared
         * @param o2 the second AssertionMirror object to be compared
         * @return a negative integer, zero, or a positive integer as the first argument is less than,
         *         equal to, or greater than the second, based on their order indices
         */
        @Override
        public int compare(AssertionMirror o1, AssertionMirror o2) {
            return assertionOrderIndex(o1) - assertionOrderIndex(o2);
        }

        private int assertionOrderIndex(AssertionMirror assertionMirror) {
            return switch (assertionMirror.getAssertionType()) {
                case regEx -> 1;
                case hasLength -> 0;
                case hasSize -> 4;
                case hasSizeMin -> 2;
                case hasSizeMax -> 3;
                case hasMaxDigits -> 7;
                case hasMaxDigitsFraction -> 6;
                case hasMaxDigitsInteger -> 5;
                case isAfter -> 12;
                case isBefore -> 14;
                case isAfterOrEqualTo -> 13;
                case isBeforeOrEqualTo -> 15;
                case isFutureOrPresent -> 9;
                case isFuture -> 8;
                case isPastOrPresent -> 10;
                case isPast -> 11;
                case equals -> 16;
                case notEquals -> 17;
                case isFalse -> 18;
                case isTrue -> 19;
                case hasLengthMax -> -4;
                case hasLengthMin -> -5;
                case isNegative -> 20;
                case isNegativeOrZero -> 21;
                case isPositive -> 22;
                case isPositiveOrZero -> 23;
                case isGreaterThan -> 24;
                case isGreaterOrEqual -> 25;
                case isLessThan -> 26;
                case isLessOrEqual -> 27;
                case isGreaterOrEqualNonDecimal -> 28;
                case isLessOrEqualNonDecimal -> 29;
                case isValidEmail -> 30;
                case isInRange -> 31;
                case isNotBlank -> 32;
                case isNotEmpty -> 33;
                case isNotEmptyIterable -> 34;
                case isNull -> 35;
                case isOneOf -> 36;
                default -> 99;
            };
        }
    }

    private String assertionStringAfterType(AssertionMirror assertionMirror) {
        return switch (assertionMirror.getAssertionType()) {
            case regEx -> "regEx{" + assertionMirror.getParam1() + "}";
            case hasLength, hasSize -> "\\[" + assertionMirror.getParam1() + "," + assertionMirror.getParam2() + "\\]";
            case hasSizeMin, hasLengthMin -> "\\[" + assertionMirror.getParam1() + ",*\\]";
            case hasSizeMax, hasLengthMax -> "\\[0," + assertionMirror.getParam2() + "\\]";
            case hasMaxDigits -> "(" + assertionMirror.getParam1() + "," + assertionMirror.getParam2() + ")";
            case hasMaxDigitsFraction -> "(*," + assertionMirror.getParam2() + ")";
            case hasMaxDigitsInteger -> "(" + assertionMirror.getParam1() + ",*)";
            case isAfter -> "{after(>):" + assertionMirror.getParam1() + "}";
            case isBefore -> "{before(<):" + assertionMirror.getParam1() + "}";
            case isAfterOrEqualTo -> "{after(>=):" + assertionMirror.getParam1() + "}";
            case isBeforeOrEqualTo -> "{before(<=):" + assertionMirror.getParam1() + "}";
            case isFutureOrPresent -> "{futureOrPresent}";
            case isFuture -> "{future}";
            case isPastOrPresent -> "{pastOrPresent}";
            case isPast -> "{past}";
            case equals -> "{=" + assertionMirror.getParam1() + "}";
            case notEquals -> "{<>" + assertionMirror.getParam1() + "}";
            case isFalse -> "{false}";
            case isTrue -> "{true}";
            case isNegative -> "{<0}";
            case isNegativeOrZero -> "{<=0}";
            case isPositive -> "{>0}";
            case isPositiveOrZero -> "{>=0}";
            case isGreaterThan -> "{>" + assertionMirror.getParam1() + "}";
            case isGreaterOrEqual, isGreaterOrEqualNonDecimal -> "{>=" + assertionMirror.getParam1() + "}";
            case isLessThan -> "{<" + assertionMirror.getParam2() + "}";
            case isLessOrEqual, isLessOrEqualNonDecimal -> "{<=" + assertionMirror.getParam2() + "}";
            case isValidEmail -> "{EMAIL}";
            case isInRange -> "{" + assertionMirror.getParam1() + "<x<" + assertionMirror.getParam2() + "}";
            case isNotBlank -> "{notBlank}";
            case isNotEmpty -> "{notEmpty}";
            case isNotEmptyIterable -> "\\[1,*\\]";
            case isNull -> "{null}";
            case isOneOf -> "{oneOf:" + assertionMirror.getParam1() + "}";
            default -> "";
        };
    }


}

