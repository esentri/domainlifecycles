package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.resolver.DefaultEmptyGenericTypeResolver;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;
import java.util.regex.Pattern;

public abstract class DomainMirrorFactoryUtils {

    private static final Pattern packagePattern = Pattern.compile("^[a-z]+(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$");

    protected final String[] domainModelPackages;
    protected String[] boundedContextPackages;
    protected ClassGraphDomainTypesScanner classGraphDomainTypesScanner;
    private GenericTypeResolver genericTypeResolver;
    private ClassLoader externalClassLoader;

    public DomainMirrorFactoryUtils(String... domainModelPackages) {
        this.domainModelPackages = domainModelPackages;
        if (domainModelPackages == null || domainModelPackages.length == 0){
            throw MirrorException.fail("No domain model package defined!");
        }
        validatePackages(domainModelPackages);
    }

    protected void initializeForScanning(){
        if(this.genericTypeResolver == null){
            this.genericTypeResolver = new DefaultEmptyGenericTypeResolver();
        }
        if(this.externalClassLoader == null){
            this.classGraphDomainTypesScanner = new ClassGraphDomainTypesScanner(genericTypeResolver);
        }else{
            this.classGraphDomainTypesScanner = new ClassGraphDomainTypesScanner(externalClassLoader, genericTypeResolver);
        }
        if(boundedContextPackages == null){
            this.boundedContextPackages = domainModelPackages;
        }
        validatePackages(boundedContextPackages);
    }

    private static void validatePackages(final String... packageNames) {
        for (String packageName : packageNames) {
            if(!packagePattern.matcher(packageName).matches()){
                throw MirrorException.fail("Invalid package name: " + packageName);
            }
        }
    }

    /**
     * Sets the bounded context packages.
     *
     * @param boundedContextPackages an array of package names representing the bounded context boundaries int he domain model
     */
    public void setBoundedContextPackages(String[] boundedContextPackages) {
        this.boundedContextPackages = boundedContextPackages;
    }

    /**
     * Sets the GenericTypeResolver to be used for resolving generic types
     * within the context of this factory.
     *
     * @param genericTypeResolver an implementation of the GenericTypeResolver interface,
     *                            responsible for resolving generic type information
     *                            for fields, methods, and constructors.
     */
    public void setGenericTypeResolver(GenericTypeResolver genericTypeResolver) {
        this.genericTypeResolver = genericTypeResolver;
    }

    /**
     * Sets the external class loader to be used by this factory. This allows
     * the factory to utilize a custom class loader for loading and scanning
     * classes, typically useful for dynamically loaded classes or isolated
     * class loading environments.
     *
     * @param externalClassLoader the custom class loader to be used by the factory
     */
    public void setExternalClassLoader(ClassLoader externalClassLoader) {
        this.externalClassLoader = externalClassLoader;
    }
}
