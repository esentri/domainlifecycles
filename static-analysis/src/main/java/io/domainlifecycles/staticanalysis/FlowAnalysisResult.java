package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.List;
import java.util.Map;

public class FlowAnalysisResult {





    public FlowAnalysisResult(Map<MethodMirror,  CalledMethods> calls) {
        this.calls = calls;
    }

    public Flow flowFor(Step startingStep){
        return null;
    }


}
