package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainCommand;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryHandler;
import io.domainlifecycles.domain.types.ReadModel;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.mirror.api.DomainType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class DefaultDomainTypeDetector implements DomainTypeDetector {
    private static final Logger log = LoggerFactory.getLogger(DefaultDomainTypeDetector.class);


    @Override
    public DomainType detectDomainType(Type type) {
        if (type instanceof Class<?> c) {
            if (AggregateRoot.class.isAssignableFrom(c)) {
                return DomainType.AGGREGATE_ROOT;
            } else if (Entity.class.isAssignableFrom(c)) {
                return DomainType.ENTITY;
            } else if (ValueObject.class.isAssignableFrom(c)) {
                return DomainType.VALUE_OBJECT;
            } else if (Enum.class.isAssignableFrom(c)) {
                return DomainType.ENUM;
            } else if (Identity.class.isAssignableFrom(c)) {
                return DomainType.IDENTITY;
            } else if (DomainService.class.isAssignableFrom(c)) {
                return DomainType.DOMAIN_SERVICE;
            } else if (Repository.class.isAssignableFrom(c)) {
                return DomainType.REPOSITORY;
            } else if (DomainEvent.class.isAssignableFrom(c)) {
                return DomainType.DOMAIN_EVENT;
            } else if (DomainCommand.class.isAssignableFrom(c)) {
                return DomainType.DOMAIN_COMMAND;
            } else if (ReadModel.class.isAssignableFrom(c)) {
                return DomainType.READ_MODEL;
            } else if (ApplicationService.class.isAssignableFrom(c)) {
                return DomainType.APPLICATION_SERVICE;
            } else if (QueryHandler.class.isAssignableFrom(c)) {
                return DomainType.QUERY_HANDLER;
            } else if (OutboundService.class.isAssignableFrom(c)) {
                return DomainType.OUTBOUND_SERVICE;
            } else if (ServiceKind.class.isAssignableFrom(c)) {
                return DomainType.SERVICE_KIND;
            }
        }
        return DomainType.NON_DOMAIN;
    }
}
