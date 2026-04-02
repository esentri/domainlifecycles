package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.DomainMirror;

public interface FlowAnalyzer {

    FlowAnalysisResult analyze(DomainMirror domainMirror);

}
