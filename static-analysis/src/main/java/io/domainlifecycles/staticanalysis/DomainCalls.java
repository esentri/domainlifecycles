package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.MethodMirror;


import java.util.List;
import java.util.Map;

public class DomainCalls {

    private final Map<MethodMirror, CalledMethods> domainCalls;

    DomainCalls(Map<MethodMirror, CalledMethods> domainCalls) {
        this.domainCalls = domainCalls;
    }

    public CalledMethods callsFor(MethodMirror methodMirror){
        return domainCalls.get(methodMirror);
    }

    public record CalledMethods(List<MethodMirror> methods) {}
}
