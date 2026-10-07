package tests.mirror.selfboundgenerics;

/**
 * Hand-written equivalent of a Lombok {@code @SuperBuilder}-generated builder base class: a
 * self-referential (F-bounded) generic pair {@code <C, B>}, with a method taking the type variable
 * bounded by the built domain command as its parameter. Not a domain type itself, so it is mirrored
 * as a non-domain class - only its {@code fillValuesFrom} parameter carries the {@code DOMAIN_COMMAND}
 * classification.
 */
public abstract class TestSuperCommandBuilder<C extends TestSuperCommand, B extends TestSuperCommandBuilder<C, B>> {

    public B fillValuesFrom(C instance) {
        return self();
    }

    protected abstract B self();
}
