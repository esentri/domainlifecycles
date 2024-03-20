package nitrox.dlc.mirror.reflect;

import nitrox.dlc.mirror.exception.MirrorException;
import nitrox.dlc.reflect.JavaReflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class GenericInterfaceTypeResolver {
    private final Class<?> subType;
    private final List<Class<?>> hierarchy;

    public GenericInterfaceTypeResolver(Class<?> subType) {
        this.subType = subType;
        this.hierarchy = JavaReflect.allSupertypes(subType);
    }

    public Class<?> resolveFor(Class<?> interfaceType, int parameterIndex){
        if(!interfaceType.isInterface()){
            throw MirrorException.fail("Tried to resolve generic type parameter for non interface type %s", interfaceType.getName());
        }
        if(!JavaReflect.isGeneric(interfaceType)){
            throw MirrorException.fail("Tried to resolve generic type parameter for non generic interface type %s", interfaceType.getName());
        }
        if(interfaceType.getTypeParameters().length <= parameterIndex) {
            throw MirrorException.fail("Generic interface type %s has less than %s parameters", interfaceType.getName(), (parameterIndex + 1));
        }
        var filteredTypeHierarchy = new LinkedList<>(hierarchy
            .stream()
            .filter(c -> interfaceType.isAssignableFrom(c))
            .toList());

        var variable = interfaceType.getTypeParameters()[parameterIndex];

        var hierarchyIterator = filteredTypeHierarchy.descendingIterator();
        var superElement = hierarchyIterator.next();
        while(!superElement.getName().equals(interfaceType.getName())&& hierarchyIterator.hasNext()){
            superElement = hierarchyIterator.next();
        }

        var subElement = subElement(hierarchyIterator);

        var resolved = resolveActualTypeByVariableName(subElement, variable, superElement);

        while (resolved instanceof TypeVariable<?>){
            superElement = subElement;
            subElement = subElement(hierarchyIterator);
            if(subElement.equals(superElement)){
                break;
            }
            resolved = resolveActualTypeByVariableName(subElement, ((TypeVariable<?>) resolved), superElement);
        }

        if(resolved instanceof Class<?>){
            return (Class<?>)resolved;
        }else if(resolved instanceof TypeVariable<?>){
            var tv = (TypeVariable<?>)resolved;
            if(tv.getBounds()[0] instanceof ParameterizedType){
                var param = (ParameterizedType)tv.getBounds()[0];
                return (Class<?>) param.getRawType();
            }
            return (Class<?>)tv.getBounds()[0];
        }
        return null;
    }

    private Class<?> subElement(Iterator<Class<?>> iterator){
        if(iterator.hasNext()){
            return iterator.next();
        }
        return subType;
    }

    private int implementsDirectlyIndex(Class<?> subType, Class<?> interfaceType){
        var implementingInterfaces = subType.getInterfaces();
        for (int i = 0; i < implementingInterfaces.length; i++){
            if(implementingInterfaces[i].getName().equals(interfaceType.getName())){
                return i;
            }
        }
        return -1;
    }

    private Type resolveActualTypeByVariableName(Class<?> subType, TypeVariable<?> variable, Class<?> superType){
        if(superType.isInterface()){
            var interfaceIndex = implementsDirectlyIndex(subType, superType);
            if(interfaceIndex<0){
                return variable;
            }
            var genericInterface = subType.getGenericInterfaces()[interfaceIndex];
            if(genericInterface instanceof ParameterizedType){
                var variableIndex = resolveVariableIndexByName(superType, variable.getName());
                return ((ParameterizedType) genericInterface).getActualTypeArguments()[variableIndex];
            }
        }else{
            var genericSuperClass = subType.getGenericSuperclass();
            if(genericSuperClass instanceof ParameterizedType){
                var variableIndex = resolveVariableIndexByName(superType, variable.getName());
                return ((ParameterizedType) genericSuperClass).getActualTypeArguments()[variableIndex];
            }
        }
        return variable;
    }

    private int resolveVariableIndexByName(Class<?> genericType, String variableName){
        var params = genericType.getTypeParameters();
        for(int i=0; i<params.length;i++){
            if(params[i].getName().equals(variableName)){
                return i;
            }
        }
        return -1;
    }
}

