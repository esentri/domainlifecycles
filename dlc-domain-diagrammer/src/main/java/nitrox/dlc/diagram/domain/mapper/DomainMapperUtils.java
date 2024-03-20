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

package nitrox.dlc.diagram.domain.mapper;

import nitrox.dlc.diagram.domain.DomainDiagramGenerator;
import nitrox.dlc.diagram.domain.config.DomainDiagramConfig;
import nitrox.dlc.mirror.api.ApplicationServiceMirror;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.api.DomainServiceMirror;
import nitrox.dlc.mirror.api.DomainType;
import nitrox.dlc.mirror.api.DomainTypeMirror;
import nitrox.dlc.mirror.api.EntityReferenceMirror;
import nitrox.dlc.mirror.api.FieldMirror;
import nitrox.dlc.mirror.api.OutboundServiceMirror;
import nitrox.dlc.mirror.api.ReadModelProviderMirror;
import nitrox.dlc.mirror.api.RepositoryMirror;
import nitrox.dlc.mirror.api.ValueObjectMirror;
import nitrox.dlc.mirror.api.ValueReferenceMirror;


/**
 * Some general mapping utility methods used within the mappings.
 *
 * @author Mario Herb
 */
public class DomainMapperUtils {

    /**
     * Returns the type name representation of a Domain Type depending on the
     * used {@link DomainDiagramConfig}.
     */
    public static String domainTypeName(DomainTypeMirror domainTypeMirror, DomainDiagramConfig domainDiagramConfig){
        var name = DomainMapperUtils.mapTypeName(domainTypeMirror.getTypeName(), domainDiagramConfig);
        if(domainTypeMirror.getDomainType().equals(DomainType.REPOSITORY)){
            var repositoryMirror = (RepositoryMirror) domainTypeMirror;
            if(repositoryMirror.getRepositoryInterfaceTypeNames().size()>0 && !repositoryMirror.isAbstract()){
                name = DomainMapperUtils.mapTypeName(repositoryMirror.getRepositoryInterfaceTypeNames().get(0), domainDiagramConfig);
            }
        } else if(domainTypeMirror.getDomainType().equals(DomainType.DOMAIN_SERVICE)){
            var domainServiceMirror = (DomainServiceMirror) domainTypeMirror;
            if(domainServiceMirror.getDomainServiceInterfaceTypeNames().size()>0 && !domainServiceMirror.isAbstract()){
                name = DomainMapperUtils.mapTypeName(domainServiceMirror.getDomainServiceInterfaceTypeNames().get(0), domainDiagramConfig);
            }
        } else if(domainTypeMirror.getDomainType().equals(DomainType.APPLICATION_SERVICE)){
            var applicationServiceMirror = (ApplicationServiceMirror) domainTypeMirror;
            if(applicationServiceMirror.getApplicationServiceInterfaceTypeNames().size()>0 && !applicationServiceMirror.isAbstract()){
                name = DomainMapperUtils.mapTypeName(applicationServiceMirror.getApplicationServiceInterfaceTypeNames().get(0), domainDiagramConfig);
            }
        } else if(domainTypeMirror.getDomainType().equals(DomainType.OUTBOUND_SERVICE)){
            var outboundServiceMirror = (OutboundServiceMirror) domainTypeMirror;
            if(outboundServiceMirror.getOutboundServiceInterfaceTypeNames().size()>0 && !outboundServiceMirror.isAbstract()){
                name = DomainMapperUtils.mapTypeName(outboundServiceMirror.getOutboundServiceInterfaceTypeNames().get(0), domainDiagramConfig);
            }
        } else if(domainTypeMirror.getDomainType().equals(DomainType.READ_MODEL_PROVIDER)){
            var readModelProviderMirror = (ReadModelProviderMirror) domainTypeMirror;
            if(readModelProviderMirror.getReadModelProviderInterfaceTypeNames().size()>0 && !readModelProviderMirror.isAbstract()){
                name = DomainMapperUtils.mapTypeName(readModelProviderMirror.getReadModelProviderInterfaceTypeNames().get(0), domainDiagramConfig);
            }
        }
        return name;
    }

    /**
     * Returns the type name representation dreived from the full qualified name of a Domain Type depending on the
     * used {@link DomainDiagramConfig}.
     */
    public static String mapTypeName(String fullQualifiedName, DomainDiagramConfig domainDiagramConfig){
        return (domainDiagramConfig.isShowFullQualifiedClassNames()) ?
            fullQualifiedName : simpleTypeName(fullQualifiedName);
    }

    /**
     * Decides, if a property should be included "inline" in a class or
     * if it should be represented as a relationship.
     */
    public static boolean showPropertyInline(FieldMirror propertyMirror, DomainTypeMirror domainTypeMirror){
        if(
            DomainType.NON_DOMAIN.equals(domainTypeMirror.getDomainType())
                || DomainType.DOMAIN_EVENT.equals(domainTypeMirror.getDomainType())
                || DomainType.DOMAIN_COMMAND.equals(domainTypeMirror.getDomainType())
                || DomainType.READ_MODEL.equals(domainTypeMirror.getDomainType())
                || DomainType.READ_MODEL_PROVIDER.equals(domainTypeMirror.getDomainType())
                || DomainType.DOMAIN_SERVICE.equals(domainTypeMirror.getDomainType())
                || DomainType.OUTBOUND_SERVICE.equals(domainTypeMirror.getDomainType())
        ){
            //for commands and domain events or unknown types we show all properties "inline"
            return true;
        }else if(DomainType.AGGREGATE_ROOT.equals(domainTypeMirror.getDomainType())
            || DomainType.ENTITY.equals(domainTypeMirror.getDomainType())
            || DomainType.VALUE_OBJECT.equals(domainTypeMirror.getDomainType())){
            //for aggregate roots, entities or value objects we filter
            if(propertyMirror instanceof EntityReferenceMirror){
                return false;
            }else if(propertyMirror instanceof ValueReferenceMirror valueRef){
                if(valueRef.getValue().isEnum()  || valueRef.getValue().isIdentity()){
                    return true;
                }else{
                    var valueObjectMirror = (ValueObjectMirror)valueRef.getValue();
                    return valueObjectMirror.isSingledValued();
                }
            }
            return true;

        }
        //for everything else we show no properties at all
        return false;
    }

    /**
     * Derives the style classifier for a given Domain type.
     */
    public static String styleClassifier(DomainTypeMirror domainTypeMirror){
        return switch (domainTypeMirror.getDomainType()){
            case AGGREGATE_ROOT -> "<"+ DomainDiagramGenerator.AGGREGATE_ROOT_STYLE_TAG+">";
            case ENTITY -> "<"+DomainDiagramGenerator.ENTITY_STYLE_TAG+">";
            case VALUE_OBJECT -> "<"+DomainDiagramGenerator.VALUE_OBJECT_STYLE_TAG+">";
            case ENUM -> "<"+DomainDiagramGenerator.ENUM_STYLE_TAG+">";
            case DOMAIN_SERVICE -> "<"+DomainDiagramGenerator.DOMAIN_SERVICE_STYLE_TAG+">";
            case REPOSITORY -> "<"+DomainDiagramGenerator.REPOSITORY_STYLE_TAG+">";
            case DOMAIN_COMMAND -> "<"+DomainDiagramGenerator.DOMAIN_COMMAND_STYLE_TAG+">";
            case DOMAIN_EVENT -> "<"+DomainDiagramGenerator.DOMAIN_EVENT_STYLE_TAG+">";
            case READ_MODEL -> "<"+DomainDiagramGenerator.READ_MODEL_STYLE_TAG+">";
            case IDENTITY -> "<"+DomainDiagramGenerator.IDENTITY_STYLE_TAG+">";
            case APPLICATION_SERVICE -> "<"+DomainDiagramGenerator.APPLICATION_SERVICE_STYLE_TAG+">";
            case READ_MODEL_PROVIDER -> "<"+DomainDiagramGenerator.READ_MODEL_PROVIDER_STYLE_TAG+">";
            case OUTBOUND_SERVICE -> "<"+DomainDiagramGenerator.OUTBOUND_SERVICE_STYLE_TAG+">";
            default -> "";
        };
    }

    /**
     * Derives the style classifier for a given Domain type, by its full qualified type name.
     */
    public static String styleClassifier(String typeName){
        var domainTypeMirrorOptional = Domain.typeMirror(typeName);
        if(domainTypeMirrorOptional.isPresent()) {
            return styleClassifier(domainTypeMirrorOptional.get());
        }
        return "";
    }

    private static String simpleTypeName(String fullQualifiedTypeName){
        if(fullQualifiedTypeName.contains(".")){
            return fullQualifiedTypeName.substring(fullQualifiedTypeName.lastIndexOf(".")+1);
        }
        return fullQualifiedTypeName;
    }

    /**
     * Derives the stereotype for a given Domain type.
     */
    public static String stereotype(DomainTypeMirror domainTypeMirror, DomainDiagramConfig domainDiagramConfig){
        return switch (domainTypeMirror.getDomainType()){
            case AGGREGATE_ROOT -> "AggregateRoot";
            case ENTITY -> "Entity";
            case VALUE_OBJECT -> "ValueObject";
            case ENUM -> "Enum";
            case DOMAIN_SERVICE -> "DomainService";
            case APPLICATION_SERVICE -> (domainDiagramConfig.isCallApplicationServiceDriver() ? "Driver" : "ApplicationService");
            case REPOSITORY -> "Repository";
            case DOMAIN_COMMAND -> "DomainCommand";
            case DOMAIN_EVENT -> "DomainEvent";
            case IDENTITY -> "Identity";
            case READ_MODEL-> "ReadModel";
            case READ_MODEL_PROVIDER-> "ReadModelProvider";
            case OUTBOUND_SERVICE-> "OutboundService";
            default -> "";
        };
    }
}
