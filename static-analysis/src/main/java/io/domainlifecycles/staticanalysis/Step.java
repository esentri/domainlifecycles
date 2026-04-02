package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class Step {

    private final DomainEventMirror domainEventMirror;

    private final DomainCommandMirror domainCommandMirror;

    private final MethodMirror methodMirror;

    protected Step(DomainEventMirror domainEventMirror, DomainCommandMirror domainCommandMirror, MethodMirror methodMirror) {
        this.domainEventMirror = domainEventMirror;
        this.domainCommandMirror = domainCommandMirror;
        this.methodMirror = methodMirror;
    }

    public static Step of(DomainEventMirror domainEventMirror) {
        return new Step(domainEventMirror, null, null);
    }

    public static Step of(DomainCommandMirror domainCommandMirror) {
        return new Step(null, domainCommandMirror, null);
    }

    public static Step of(MethodMirror methodMirror) {
        return new Step(null, null, methodMirror);
    }

    public String stepClassName(){
        if(domainEventMirror != null){
            return domainEventMirror.getTypeName();
        } else if(domainCommandMirror != null){
            return domainCommandMirror.getTypeName();
        } else if(methodMirror != null){
            return methodMirror.getDeclaredByTypeName();
        } else {
            return "Unknown";
        }
    }

    public String stepMethodName(){
        if(methodMirror != null){
            return methodMirror.getName();
        } else {
            return "";
        }
    }

    public Optional<DomainEventMirror> listenedEvent(){
        if(methodMirror != null){
            return methodMirror.getListenedEvent();
        } else {
            return Optional.empty();
        }
    }

    public Collection<DomainEventMirror> publishedEvents(){
        if(methodMirror != null){
            return methodMirror.getPublishedEvents();
        } else {
            return Collections.emptyList();
        }
    }

    public Collection<DomainCommandMirror>  processedCommands(){
        if(methodMirror != null){
            return methodMirror.getProcessedCommands();
        } else {
            return Collections.emptyList();
        }
    }

}
