package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.MethodMirror;


import java.util.List;
import java.util.Map;
import java.util.Set;

public class DomainCalls {

    private final Map<MethodCall, CalledMethods> domainCalls;

    DomainCalls(Map<MethodCall, CalledMethods> domainCalls) {
        this.domainCalls = domainCalls;
    }

    public CalledMethods callsFor(MethodCall methodCall){
        return domainCalls.get(methodCall);
    }

    public Set<MethodCall> callers() {
        return this.domainCalls.keySet();
    }

    public record CalledMethods(List<MethodCall> methods) {
        public String toString(){
            if(methods != null) {
                var sb = new StringBuilder();
                for (var method : methods) {
                    sb.append(method).append("\n");
                }
                return sb.toString();
            }
            return "null";
        }
    }

    public record MethodCall(String calledTypeName, MethodMirror method) {

        public String toString(){
            var sb = new StringBuilder();
            if(calledTypeName != null){
                sb.append(calledTypeName).append(".");
            }
            if(method != null) {
                if(method.getReturnType().getContainerTypeName().isPresent()) {
                    sb.append(method.getReturnType().getContainerTypeName()).append("<");
                }
                sb.append(method.getReturnType().getTypeName());
                if(method.getReturnType().getContainerTypeName().isPresent()) {
                    sb.append(">");
                }
                sb.append(" ");
                sb.append(method.getName()).append("(");
                var start = true;
                for (var param : method.getParameters()) {
                    if(!start) {
                        sb.append(", ");
                    }else {
                        start = false;
                    }
                    if(param.getType().getContainerTypeName().isPresent()) {
                        sb.append(param.getType().getContainerTypeName()).append("<");
                    }
                    sb.append(param.getType().getTypeName());
                    if(param.getType().getContainerTypeName().isPresent()) {
                        sb.append(">");
                    }
                }

                return sb.toString();
            }
            return "null";
        }
    }
}
