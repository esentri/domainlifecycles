package tests.mirror.annotation;

import org.jmolecules.ddd.annotation.Factory;

@Factory
public class FactoryJMoleculesAnnotation {

    public AggregateRootJMoleculesAnnotation create() {
        return new AggregateRootJMoleculesAnnotation();
    }

    private String describe() {
        return "factory";
    }
}
