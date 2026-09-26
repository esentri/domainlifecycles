package io.domainlifecycles.staticanalysis.fixture;

/**
 * Hand-written equivalent of a Lombok {@code @SuperBuilder}-generated builder base class: a
 * self-referential (F-bounded) generic pair {@code <C, B>}, with a method taking the type variable
 * bounded by {@link TestCommand} as its parameter. Mirrored as a non-domain class, this reproduces a
 * mirror data inconsistency that used to make
 * {@link io.domainlifecycles.staticanalysis.DomainCallFlowAnalyzer}'s construction fail for any domain
 * model containing such a builder, not just for flows reaching it.
 */
public abstract class TestCommandBuilder<C extends TestCommand, B extends TestCommandBuilder<C, B>> {

    public B fillValuesFrom(C instance) {
        return self();
    }

    protected abstract B self();
}
