package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import org.junit.jupiter.api.Test;
import tests.mirror.selfboundgenerics.TestSuperCommand;
import tests.mirror.selfboundgenerics.TestSuperCommandBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A Lombok {@code @SuperBuilder}-generated builder declares a self-referential (F-bounded) type
 * parameter pair {@code <C extends X, B extends Builder<C, B>>}. Mirrored as a non-domain class (see
 * {@link NonDomainTypeScanTest}), a method taking the {@code C}-typed parameter has no concrete usage
 * context for a generic type resolver to bind {@code C} against, so it can only fall back to
 * {@link Object} for the type name - while reflection alone already classifies the parameter's domain
 * type as {@code DOMAIN_COMMAND} from its erased bound. Without a fix, that mismatch (a domain type
 * that disagrees with its own type name) breaks {@code MethodMirror#getProcessedCommands()} for every
 * method in the domain model, not just the offending one.
 */
public class SelfBoundGenericCommandParamTest {

    @Test
    public void selfReferentialBuilderParameterKeepsConsistentDomainTypeAndTypeName() {
        var factory = new ReflectiveDomainMirrorFactory("tests.mirror.selfboundgenerics");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        var domainMirror = factory.initializeDomainMirror();

        var builderMirror = domainMirror.getDomainTypeMirror(TestSuperCommandBuilder.class.getName())
            .orElseThrow();
        var fillValuesFrom = builderMirror.getMethods().stream()
            .filter(m -> m.getName().equals("fillValuesFrom"))
            .findFirst()
            .orElseThrow();
        var instanceParam = fillValuesFrom.getParameters().get(0);

        assertThat(instanceParam.getType().getDomainType()).isEqualTo(DomainType.DOMAIN_COMMAND);
        assertThat(instanceParam.getType().getTypeName()).isEqualTo(TestSuperCommand.class.getName());

        assertThat(fillValuesFrom.getProcessedCommands())
            .extracting(c -> c.getTypeName())
            .containsExactly(TestSuperCommand.class.getName());
    }
}
