package nitrox.dlc.mirror.resolver;

import com.github.vladislavsevruk.resolver.resolver.executable.ExecutableTypeMetaResolver;
import com.github.vladislavsevruk.resolver.resolver.executable.ExecutableTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.field.FieldTypeMetaResolver;
import com.github.vladislavsevruk.resolver.resolver.field.FieldTypeResolver;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import nitrox.dlc.mirror.api.ResolvedGenericTypeMirror;
import nitrox.dlc.mirror.api.WildcardBound;
import nitrox.dlc.mirror.model.ResolvedGenericTypeModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * TypeMetaResolver is a concrete implementation of the GenericTypeResolver interface.
 * It provides methods for resolving generic types in fields and executable elements (methods, constructors).
 *
 * <p>
 * To enable generic type resolving in the mirror, set the TypeMetaResolver before initializing the domain:
 * <pre>{@code
 *  Domain.setGenericTypeResolver(new TypeMetaResolver());
 *  ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
 *  Domain.initialize(factory);
 * }</pre>
 *
 * A current use case is the DomainDiagrammer {@see nitrox.dlc.diagram.domain.DomainDiagramGenerator}.
 *
 * @author Mario Herb
 */
public class TypeMetaResolver implements GenericTypeResolver{

    private final FieldTypeResolver<TypeMeta<?>> fieldTypeResolver = new FieldTypeMetaResolver();

    ExecutableTypeResolver<TypeMeta<?>> executableTypeResolver = new ExecutableTypeMetaResolver();
    /**
     * {@inheritDoc}
     */
    @Override
    public ResolvedGenericTypeMirror resolveFieldType(Field f, Class<?> contextClass) {
        TypeMeta<?> fieldTypeMeta = fieldTypeResolver.resolveField(contextClass, f);
        return map(fieldTypeMeta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResolvedGenericTypeMirror> resolveExecutableParameters(Method m, Class<?> contextClass) {
        return executableTypeResolver.getParameterTypes(contextClass, m)
            .stream()
            .map(this::map)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResolvedGenericTypeMirror resolveExecutableReturnType(Method m, Class<?> contextClass) {
        TypeMeta<?> methodReturnTypeMeta = executableTypeResolver.getReturnType(contextClass, m);
        return map(methodReturnTypeMeta);
    }

    private ResolvedGenericTypeMirror map(TypeMeta<?> typeMeta){
        var typeName = typeMeta.getType().getName();
        if(typeMeta.getType().isArray()){
            typeName = typeMeta.getType().getComponentType().getName();
        }
        return new ResolvedGenericTypeModel(typeName, typeMeta.getType().isArray(), Arrays.stream(typeMeta.getGenericTypes()).map(tm -> map(tm)).toList(), map(typeMeta.getWildcardBound()));
    }

    private Optional<WildcardBound> map(com.github.vladislavsevruk.resolver.type.WildcardBound metaWildcardBound){
        if(metaWildcardBound == null){
            return Optional.empty();
        }
        return metaWildcardBound.equals(com.github.vladislavsevruk.resolver.type.WildcardBound.UPPER) ? Optional.of(WildcardBound.UPPER) : Optional.of(WildcardBound.LOWER);
    }
}
