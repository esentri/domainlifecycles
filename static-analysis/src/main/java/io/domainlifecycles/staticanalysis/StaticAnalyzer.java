package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.DomainMirror;

public interface StaticAnalyzer {

    DomainCalls analyze(DomainMirror domainMirror, String classpath);
}
