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

import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.NonDomainTypeMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Some general mapping utility methods used within the mappings.
 *
 * @author Mario Herb
 */
public class DomainMapperUtils {

    /**
     * @param domainTypeMirror    mirrored domain type
     * @param domainDiagramConfig diagram configuration
     * @return type name representation of a Domain Type depending on the
     * used {@link DomainDiagramConfig}.
     */
    public static String domainTypeName(DomainTypeMirror domainTypeMirror, DomainDiagramConfig domainDiagramConfig) {
        return DomainMapperUtils.mapTypeName(shownTypeName(domainTypeMirror, domainDiagramConfig), domainDiagramConfig);
    }

    /**
     * @param domainTypeMirror    mirrored domain type
     * @param domainDiagramConfig diagram configuration
     * @return the full qualified name a Domain Type is shown with: a service implementing an interface is shown as that
     * interface, unless the inheritance structures of services are shown
     */
    public static String shownTypeName(DomainTypeMirror domainTypeMirror, DomainDiagramConfig domainDiagramConfig) {
        var name = domainTypeMirror.getTypeName();
        if(!domainDiagramConfig.getGeneralVisualSettings().isShowAllInheritanceStructures()
            && !domainDiagramConfig.getGeneralVisualSettings().isShowInheritanceStructuresForServiceKinds()) {
            if (domainTypeMirror.getDomainType().equals(DomainType.REPOSITORY)) {
                var repositoryMirror = (RepositoryMirror) domainTypeMirror;
                if (!repositoryMirror.getRepositoryInterfaceTypeNames().isEmpty() && !repositoryMirror.isAbstract()) {
                    name = repositoryMirror.getRepositoryInterfaceTypeNames().get(0);
                }
            } else if (domainTypeMirror.getDomainType().equals(DomainType.DOMAIN_SERVICE)) {
                var domainServiceMirror = (DomainServiceMirror) domainTypeMirror;
                if (!domainServiceMirror.getDomainServiceInterfaceTypeNames().isEmpty() && !domainServiceMirror.isAbstract()) {
                    name = domainServiceMirror.getDomainServiceInterfaceTypeNames().get(0);
                }
            } else if (domainTypeMirror.getDomainType().equals(DomainType.APPLICATION_SERVICE)) {
                var applicationServiceMirror = (ApplicationServiceMirror) domainTypeMirror;
                if (!applicationServiceMirror.getApplicationServiceInterfaceTypeNames().isEmpty() && !applicationServiceMirror.isAbstract()) {
                    name = applicationServiceMirror.getApplicationServiceInterfaceTypeNames().get(0);
                }
            } else if (domainTypeMirror.getDomainType().equals(DomainType.OUTBOUND_SERVICE)) {
                var outboundServiceMirror = (OutboundServiceMirror) domainTypeMirror;
                if (!outboundServiceMirror.getOutboundServiceInterfaceTypeNames().isEmpty() && !outboundServiceMirror.isAbstract()) {
                    name = outboundServiceMirror.getOutboundServiceInterfaceTypeNames().get(0);
                }
            } else if (domainTypeMirror.getDomainType().equals(DomainType.QUERY_HANDLER)) {
                var queryHandlerMirror = (QueryHandlerMirror) domainTypeMirror;
                if (!queryHandlerMirror.getQueryHandlerInterfaceTypeNames().isEmpty() && !queryHandlerMirror.isAbstract()) {
                    name = queryHandlerMirror.getQueryHandlerInterfaceTypeNames().get(0);
                }
            }
        }
        return name;
    }

    /**
     * @param fullQualifiedName   full qualified domain type name
     * @param domainDiagramConfig diagram configuration
     * @return the type name representation derived from the full qualified name of a Domain Type depending on the
     * used {@link DomainDiagramConfig}.
     */
    public static String mapTypeName(String fullQualifiedName, DomainDiagramConfig domainDiagramConfig) {
        return (domainDiagramConfig.getGeneralVisualSettings().isShowFullQualifiedClassNames()) ?
            fullQualifiedName : simpleTypeName(fullQualifiedName);
    }

    /**
     * Determines whether a given property should be shown inline based on its type, domain type, and diagram configuration.
     *
     * @param propertyMirror the mirrored field representing the property to check
     * @param domainTypeMirror the mirrored type of the domain to which the property belongs
     * @param domainDiagramConfig the configuration of the domain diagram specifying certain rules and filters
     * @return true if the property should be shown inline; false otherwise
     */
    public static boolean showPropertyInline(FieldMirror propertyMirror, DomainTypeMirror domainTypeMirror, DomainDiagramConfig domainDiagramConfig) {
        if (
            DomainType.NON_DOMAIN.equals(domainTypeMirror.getDomainType())
                || DomainType.DOMAIN_EVENT.equals(domainTypeMirror.getDomainType())
                || DomainType.DOMAIN_COMMAND.equals(domainTypeMirror.getDomainType())
                || DomainType.READ_MODEL.equals(domainTypeMirror.getDomainType())
                || DomainType.QUERY_HANDLER.equals(domainTypeMirror.getDomainType())
                || DomainType.DOMAIN_SERVICE.equals(domainTypeMirror.getDomainType())
                || DomainType.OUTBOUND_SERVICE.equals(domainTypeMirror.getDomainType())
                || DomainType.SERVICE_KIND.equals(domainTypeMirror.getDomainType())
        ) {
            //for commands and domain events or unknown types we show all properties "inline"
            return true;
        } else if (DomainType.AGGREGATE_ROOT.equals(domainTypeMirror.getDomainType())
            || DomainType.ENTITY.equals(domainTypeMirror.getDomainType())
            || DomainType.VALUE_OBJECT.equals(domainTypeMirror.getDomainType())) {
            //for aggregate roots, entities or value objects we filter
            if (propertyMirror instanceof EntityReferenceMirror) {
                var erm = (EntityReferenceMirror) propertyMirror;
                return domainDiagramConfig.getDiagramTrimSettings().getClassesBlacklist().contains(erm.getType().getTypeName());
            } else if (propertyMirror instanceof ValueReferenceMirror valueRef) {
                if (valueRef.getValue().isEnum() || valueRef.getValue().isIdentity()) {
                    return true;
                } else {
                    var valueObjectMirror = (ValueObjectMirror) valueRef.getValue();
                    return isShownInline(valueObjectMirror, domainDiagramConfig) || domainDiagramConfig.getDiagramTrimSettings().getClassesBlacklist().contains(valueObjectMirror.getTypeName());
                }
            }
            return true;

        }
        //for everything else we show no properties at all
        return false;
    }

    /**
     * The read models a service kind or non-domain class provides, which no query handler provides: those returned by
     * its methods - directly, as {@code Optional} or as collection. A query handler provides its read model itself.
     *
     * @param domainTypeMirror a mirrored domain type
     * @param domainMirror     the domain it belongs to
     * @return the provided read models without query handler, empty for any other kind of type
     */
    public static List<ReadModelMirror> readModelsProvidedWithoutQueryHandler(DomainTypeMirror domainTypeMirror,
                                                                              DomainMirror domainMirror) {
        if (domainTypeMirror instanceof QueryHandlerMirror
            || !(domainTypeMirror instanceof ServiceKindMirror || domainTypeMirror instanceof NonDomainTypeMirror)) {
            return List.of();
        }
        return domainTypeMirror.getMethods().stream()
            .filter(method -> method.getReturnType() != null)
            .map(method -> domainMirror.getDomainTypeMirror(method.getReturnType().getTypeName()))
            .flatMap(Optional::stream)
            .filter(ReadModelMirror.class::isInstance)
            .map(ReadModelMirror.class::cast)
            .distinct()
            .filter(readModel -> !isProvidedByAQueryHandler(readModel, domainMirror))
            .toList();
    }

    /**
     * @param readModelMirror a mirrored read model
     * @param domainMirror    the domain it belongs to
     * @return whether a query handler provides the read model
     */
    public static boolean isProvidedByAQueryHandler(ReadModelMirror readModelMirror, DomainMirror domainMirror) {
        return domainMirror.getAllQueryHandlerMirrors().stream()
            .anyMatch(queryHandler -> queryHandler.getProvidedReadModel()
                .filter(provided -> provided.getTypeName().equals(readModelMirror.getTypeName()))
                .isPresent());
    }

    /**
     * Whether a value object is shown inline - as field of the class referencing it - instead of as class of its own,
     * connected by a composition: if it has at most {@code maxInlinedValueObjectFields} fields (see
     * {@link io.domainlifecycles.diagram.domain.config.GeneralVisualSettings#getMaxInlinedValueObjectFields()}), and
     * each of them is a basic value, an enum, an identity or a value object shown inline in turn.
     *
     * @param valueObjectMirror   mirrored value object
     * @param domainDiagramConfig diagram configuration
     * @return true if the value object is shown inline
     */
    public static boolean isShownInline(ValueObjectMirror valueObjectMirror, DomainDiagramConfig domainDiagramConfig) {
        return isInlinable(valueObjectMirror,
            domainDiagramConfig.getGeneralVisualSettings().getMaxInlinedValueObjectFields(), new HashSet<>());
    }

    private static boolean isInlinable(ValueObjectMirror valueObjectMirror, int maxFields, Set<String> enclosing) {
        if (!enclosing.add(valueObjectMirror.getTypeName())) {
            // containing itself
            return false;
        }
        try {
            List<FieldMirror> fields = valueObjectMirror.getAllFields().stream()
                .filter(field -> !field.isStatic() && !field.isHidden())
                .toList();
            if (fields.isEmpty() || fields.size() > maxFields) {
                return false;
            }
            for (FieldMirror field : fields) {
                if (field instanceof EntityReferenceMirror) {
                    return false;
                }
                if (field instanceof ValueReferenceMirror valueReference) {
                    var value = valueReference.getValue();
                    if (value.isEnum() || value.isIdentity()) {
                        continue;
                    }
                    if (!(value instanceof ValueObjectMirror nested) || !isInlinable(nested, maxFields, enclosing)) {
                        return false;
                    }
                }
            }
            return true;
        } finally {
            enclosing.remove(valueObjectMirror.getTypeName());
        }
    }

    /**
     * @param domainTypeMirror mirrored domain type
     * @return style classifier for given domain type
     */
    public static String styleClassifier(DomainTypeMirror domainTypeMirror) {
        if(domainTypeMirror==null){
            return "";
        }
        return switch (domainTypeMirror.getDomainType()) {
            case AGGREGATE_ROOT -> "<" + DomainDiagramGenerator.AGGREGATE_ROOT_STYLE_TAG + ">";
            case ENTITY -> "<" + DomainDiagramGenerator.ENTITY_STYLE_TAG + ">";
            case VALUE_OBJECT -> "<" + DomainDiagramGenerator.VALUE_OBJECT_STYLE_TAG + ">";
            case ENUM -> "<" + DomainDiagramGenerator.ENUM_STYLE_TAG + ">";
            case DOMAIN_SERVICE -> "<" + DomainDiagramGenerator.DOMAIN_SERVICE_STYLE_TAG + ">";
            case REPOSITORY -> "<" + DomainDiagramGenerator.REPOSITORY_STYLE_TAG + ">";
            case DOMAIN_COMMAND -> "<" + DomainDiagramGenerator.DOMAIN_COMMAND_STYLE_TAG + ">";
            case DOMAIN_EVENT -> "<" + DomainDiagramGenerator.DOMAIN_EVENT_STYLE_TAG + ">";
            case READ_MODEL -> "<" + DomainDiagramGenerator.READ_MODEL_STYLE_TAG + ">";
            case IDENTITY -> "<" + DomainDiagramGenerator.IDENTITY_STYLE_TAG + ">";
            case APPLICATION_SERVICE -> "<" + DomainDiagramGenerator.APPLICATION_SERVICE_STYLE_TAG + ">";
            case QUERY_HANDLER -> "<" + DomainDiagramGenerator.QUERY_HANDLER_STYLE_TAG + ">";
            case OUTBOUND_SERVICE -> "<" + DomainDiagramGenerator.OUTBOUND_SERVICE_STYLE_TAG + ">";
            case SERVICE_KIND -> "<" + DomainDiagramGenerator.SERVICE_KIND_STYLE_TAG + ">";
            case NON_DOMAIN -> "<" + DomainDiagramGenerator.NON_DOMAIN_CLASS_STYLE_TAG + ">";
            default -> "";
        };
    }

    private static String simpleTypeName(String fullQualifiedTypeName) {
        if (fullQualifiedTypeName.contains(".")) {
            return fullQualifiedTypeName.substring(fullQualifiedTypeName.lastIndexOf(".") + 1);
        }
        return fullQualifiedTypeName;
    }

    /**
     * @param domainTypeMirror    mirrored domain type
     * @param domainDiagramConfig diagram configuration
     * @return stereotype for given Domain type
     */
    public static String stereotype(DomainTypeMirror domainTypeMirror, DomainDiagramConfig domainDiagramConfig) {
        return switch (domainTypeMirror.getDomainType()) {
            case AGGREGATE_ROOT -> "AggregateRoot";
            case ENTITY -> "Entity";
            case VALUE_OBJECT -> "ValueObject";
            case ENUM -> "Enum";
            case DOMAIN_SERVICE -> "DomainService";
            case APPLICATION_SERVICE ->
                (domainDiagramConfig.getGeneralVisualSettings().isCallApplicationServiceDriver() ? "Driver" : "ApplicationService");
            case REPOSITORY -> "Repository";
            case DOMAIN_COMMAND -> "DomainCommand";
            case DOMAIN_EVENT -> "DomainEvent";
            case IDENTITY -> "Identity";
            case READ_MODEL -> "ReadModel";
            case QUERY_HANDLER -> "QueryHandler";
            case OUTBOUND_SERVICE -> "OutboundService";
            case SERVICE_KIND -> "ServiceKind";
            case NON_DOMAIN -> "NonDomain";
            default -> "";
        };
    }
}
