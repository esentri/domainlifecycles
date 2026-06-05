package io.domainlifecycles.mirrorjmolecules.reflect;

import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.reflect.DefaultDomainTypeDetector;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifiable;
import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.ddd.types.Repository;
import org.jmolecules.ddd.types.ValueObject;
import org.jmolecules.event.types.DomainEvent;

import java.lang.reflect.Type;

public class ExtendedJMoleculesDomainTypeDetector extends DefaultDomainTypeDetector {

    @Override
    public DomainType detectDomainType(Type type) {
        var dt = super.detectDomainType(type);
        if(DomainType.NON_DOMAIN.equals(dt)){
            if (type instanceof Class<?> c) {
                if (AggregateRoot.class.isAssignableFrom(c)) {
                    return DomainType.AGGREGATE_ROOT;
                } else if (Entity.class.isAssignableFrom(c)) {
                    return DomainType.ENTITY;
                } else if (ValueObject.class.isAssignableFrom(c)) {
                    return DomainType.VALUE_OBJECT;
                } else if (Identifier.class.isAssignableFrom(c)) {
                    return DomainType.IDENTITY;
                } else if (Repository.class.isAssignableFrom(c)) {
                    return DomainType.REPOSITORY;
                } else if (DomainEvent.class.isAssignableFrom(c)) {
                    return DomainType.DOMAIN_EVENT;
                } else if (c.isAnnotationPresent(org.jmolecules.ddd.annotation.AggregateRoot.class)){
                    return DomainType.AGGREGATE_ROOT;
                } else if (c.isAnnotationPresent(org.jmolecules.ddd.annotation.Entity.class)){
                    return DomainType.ENTITY;
                } else if (c.isAnnotationPresent(org.jmolecules.ddd.annotation.ValueObject.class)){
                    return DomainType.VALUE_OBJECT;
                } else if (c.isAnnotationPresent(org.jmolecules.ddd.annotation.Repository.class)){
                    return DomainType.REPOSITORY;
                } else if (c.isAnnotationPresent(org.jmolecules.ddd.annotation.Service.class)){
                    return DomainType.DOMAIN_SERVICE;
                }
            }
        }
        return dt;
    }

}
