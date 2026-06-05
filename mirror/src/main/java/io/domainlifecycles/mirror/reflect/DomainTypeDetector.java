package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.mirror.api.DomainType;

import java.lang.reflect.Type;

public interface DomainTypeDetector {
    DomainType detectDomainType(Type type);
}
