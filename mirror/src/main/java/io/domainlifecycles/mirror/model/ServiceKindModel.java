package io.domainlifecycles.mirror.model;

import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import java.util.List;

public class ServiceKindModel extends DomainTypeModel implements ServiceKindMirror {

    public ServiceKindModel(String typeName,
                            boolean isAbstract,
                            List<FieldMirror> allFields,
                            List<MethodMirror> methods,
                            List<String> inheritanceHierarchyTypeNames,
                            List<String> allInterfaceTypeNames) {

        super(typeName, isAbstract, allFields,
            methods, inheritanceHierarchyTypeNames,
            allInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean publishes(DomainEventMirror domainEvent) {
        return getMethods().stream()
            .anyMatch(m -> m.publishes(domainEvent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean listensTo(DomainEventMirror domainEvent) {
        return getMethods().stream()
            .anyMatch(m -> m.listensTo(domainEvent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean processes(DomainCommandMirror command) {
        return getMethods().stream()
            .anyMatch(m -> m.processes(command));
    }

    @Override
    public String getTypeName() {
        return null;
    }

    @Override
    public DomainType getDomainType() {
        return null;
    }

    @Override
    public List<FieldMirror> getAllFields() {
        return null;
    }

    @Override
    public List<MethodMirror> getMethods() {
        return null;
    }

    @Override
    public MethodMirror methodByName(String methodName) {
        return null;
    }

    @Override
    public FieldMirror fieldByName(String fieldName) {
        return null;
    }

    @Override
    public boolean isAbstract() {
        return false;
    }

    @Override
    public List<String> getInheritanceHierarchyTypeNames() {
        return null;
    }

    @Override
    public boolean isSubClassOf(String typeName) {
        return false;
    }

    @Override
    public List<String> getAllInterfaceTypeNames() {
        return null;
    }
}
