/*
 *
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.mirror.api;

import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.resolver.DefaultEmptyGenericTypeResolver;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;
import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.domain.types.ValueObject;

import java.util.List;
import java.util.Optional;

/**
 * Central static access point for the mirrored Domain (or subdomain).
 *
 * @author Mario Herb
 */
public class Domain {

    private static boolean initialized = false;

    private static  InitializedDomain initializedDomain;

    private static GenericTypeResolver genericTypeResolver = new DefaultEmptyGenericTypeResolver();

    /**
     * Get a mirror instance for a given full qualified type name.
     */
    public static <V extends DomainTypeMirror> Optional<V> typeMirror(String fullQualifiedTypeName){
        if(!initialized){
            throw MirrorException.fail("Domain was not initialized!");
        }
        var mirror = initializedDomain.allTypeMirrors().get(fullQualifiedTypeName);
        return Optional.ofNullable((V) mirror);
    }

    /**
     * Returns the {@link AggregateRootMirror} for the given AggregateRoot instance.
     */
    public static <A extends AggregateRootMirror>  A aggregateRootMirrorFor(AggregateRoot<?> aggregateRoot){
        return (A)typeMirror(aggregateRoot.getClass().getName())
            .orElseThrow(()-> MirrorException.fail("No AggregateRootMirror found for %s", aggregateRoot.getClass().getName()));
    }

    /**
     * Returns the {@link AggregateRootMirror} for the given full qualified AggregateRoot type name.
     */
    public static <A extends AggregateRootMirror>  A aggregateRootMirrorFor(String aggregateRootTypeName){
        return (A)typeMirror(aggregateRootTypeName)
            .orElseThrow(()-> MirrorException.fail("No AggregateRootMirror found for %s", aggregateRootTypeName));
    }

    /**
     * Returns the {@link EntityMirror} for the given Entity instance.
     */
    public static <E extends EntityMirror>  E entityMirrorFor(Entity<?> entity){
        return (E)typeMirror(entity.getClass().getName())
            .orElseThrow(()-> MirrorException.fail("No EntityMirror found for %s", entity.getClass().getName()));
    }

    /**
     * Returns the {@link EntityMirror} for the given full qualified Entity type name.
     */
    public static <E extends EntityMirror>  E entityMirrorFor(String entityTypeName){
        return (E)typeMirror(entityTypeName)
            .orElseThrow(()-> MirrorException.fail("No EntityMirror found for %s", entityTypeName));
    }

    /**
     * Returns the {@link DomainEventMirror} for the given DomainEvent instance.
     */
    public static <DE extends DomainEventMirror>  DE domainEventMirrorFor(DomainEvent domainEvent){
        return (DE)typeMirror(domainEvent.getClass().getName())
            .orElseThrow(()-> MirrorException.fail("No DomainEventMirror found for %s", domainEvent.getClass().getName()));
    }

    /**
     * Returns the {@link DomainEventMirror} for the given full qualified DomainEvent type name.
     */
    public static <DE extends DomainEventMirror>  DE domainEventMirrorFor(String domainEventTypeName){
        return (DE)typeMirror(domainEventTypeName)
            .orElseThrow(()-> MirrorException.fail("No DomainEventMirror found for %s", domainEventTypeName));
    }

    /**
     * Returns the {@link DomainServiceMirror} for the given DomainService instance.
     */
    public static <DS extends DomainServiceMirror>  DS domainServiceMirrorFor(DomainService domainService){
        return (DS)typeMirror(domainService.getClass().getName())
            .orElseThrow(()-> MirrorException.fail("No DomainServiceMirror found for %s", domainService.getClass().getName()));
    }

    /**
     * Returns the {@link DomainServiceMirror} for the given full qualified DomainService type name.
     */
    public static <DS extends DomainServiceMirror>  DS domainServiceMirrorFor(String domainServiceTypeName){
        return (DS)typeMirror(domainServiceTypeName)
            .orElseThrow(()-> MirrorException.fail("No DomainServiceMirror found for %s", domainServiceTypeName));
    }

    /**
     * Returns the {@link ApplicationServiceMirror} for the given ApplicationService instance.
     */
    public static <AS extends ApplicationServiceMirror>  AS domainServiceMirrorFor(ApplicationService applicationService){
        return (AS)typeMirror(applicationService.getClass().getName())
            .orElseThrow(()-> MirrorException.fail("No ApplicationServiceMirror found for %s", applicationService.getClass().getName()));
    }

    /**
     * Returns the {@link ApplicationServiceMirror} for the given full qualified ApplicationService type name.
     */
    public static <AS extends ApplicationServiceMirror>  AS applicationServiceMirrorFor(String applicationServiceTypeName){
        return (AS)typeMirror(applicationServiceTypeName)
            .orElseThrow(()-> MirrorException.fail("No ApplicationServiceMirror found for %s", applicationServiceTypeName));
    }

    /**
     * Returns the {@link RepositoryMirror} for the given Repository instance for a given {@link AggregateRootMirror}.
     */
    public static <R extends RepositoryMirror>  R repositoryMirrorFor(AggregateRootMirror arm){
        return (R) initializedDomain
            .allTypeMirrors()
            .values()
            .stream()
            .filter(tm -> tm.getDomainType().equals(DomainType.REPOSITORY))
            .map(tm -> (RepositoryMirror) tm)
            .filter(rm -> rm.getManagedAggregate().isPresent() && rm.getManagedAggregate().get().equals(arm))
            .findFirst()
            .orElseThrow(()-> MirrorException.fail("No RepositoryMirror found for AggregateRoot %s", arm.getTypeName()));
    }

    /**
     * Returns the {@link RepositoryMirror} for the given Repository instance.
     */
    public static <R extends RepositoryMirror>  R repositoryMirrorFor(Repository<?, ?> repository){
        return (R)typeMirror(repository.getClass().getName())
            .orElseThrow(()-> MirrorException.fail("No RepositoryMirror found for %s", repository.getClass().getName()));
    }

    /**
     * Returns the {@link RepositoryMirror} for the given full qualified Repository type name.
     */
    public static <R extends RepositoryMirror>  R repositoryMirrorFor(String repositoryTypeName){
        return (R)typeMirror(repositoryTypeName)
            .orElseThrow(()-> MirrorException.fail("No RepositoryMirror found for %s", repositoryTypeName));
    }

    /**
     * Returns the {@link EntityMirror} for the given full qualified type name of the Identity type.
     */
    public static <E extends EntityMirror>  E entityMirrorForIdentityTypeName(String identityTypeName){
        if(!initialized){
            throw MirrorException.fail("Domain was not initialized!");
        }

        return (E)initializedDomain.allTypeMirrors()
            .values()
            .stream()
            .filter(dm -> {
                if(dm instanceof EntityMirror){
                    var em = (EntityMirror) dm;
                    if(em.getIdentityField().isPresent()){
                        return em.getIdentityField().get().getType().getTypeName().equals(identityTypeName);
                    }
                }
               return false;
            })
            .findFirst()
            .orElseThrow(()-> MirrorException.fail("No EntityMirror found for identity type %s", identityTypeName));
    }

    /**
     * Returns the {@link IdentityMirror} for the given full qualified Identity type name.
     */
    public static <I extends IdentityMirror>  I identityMirrorFor(String identityTypeName){
        return (I)typeMirror(identityTypeName)
            .orElseThrow(()-> MirrorException.fail("No IdentityMirror found for %s", identityTypeName));
    }

    /**
     * Returns the {@link ValueObjectMirror} for the given ValueObject instance.
     */
    public static <V extends ValueObjectMirror>  V valueObjectMirrorFor(ValueObject valueObject){
        return (V)typeMirror(valueObject.getClass().getName())
            .orElseThrow(()-> MirrorException.fail("No ValueObjectMirror found for %s", valueObject.getClass().getName()));
    }

    /**
     * Returns the {@link ValueObjectMirror} for the given full qualified ValueObject type name.
     */
    public static <V extends ValueObjectMirror>  V valueObjectMirrorFor(String valueObjectTypeName){
        return (V)typeMirror(valueObjectTypeName)
            .orElseThrow(()-> MirrorException.fail("No ValueObjectMirror found for %s", valueObjectTypeName));
    }

    /**
     * Returns the list of {@link BoundedContextMirror} instances for the mirrored bounded contexts.
     */
    public static List<BoundedContextMirror> getBoundedContexts(){
        if(!initialized){
            throw MirrorException.fail("Domain was not initialized!");
        }
        return initializedDomain.boundedContextMirrors();
    }

    /**
     * Returns the {@link InitializedDomain} containing all type mirrors created upon initialization.
     */
    public static InitializedDomain getInitializedDomain() {
        return initializedDomain;
    }

    /**
     * Initializes the domain by a given factory.
     * The factory decides the source of Domain meta information (reflection or something else).
     */
    public static void initialize(DomainMirrorFactory domainMirrorFactory){
        initializedDomain = domainMirrorFactory.initializeDomain(genericTypeResolver);
        initialized = true;
    }

    /**
     * Sets the GenericTypeResolver for the Domain to resolve generic types within domain classes.
     *
     * @param genericTypeResolver the type resolver to be set
     */
    public static void setGenericTypeResolver(GenericTypeResolver genericTypeResolver) {
        Domain.genericTypeResolver = genericTypeResolver;
    }

    /**
     * Returns the GenericTypeResolver for the Domain.
     *
     * @return the GenericTypeResolver instance
     */
    public static GenericTypeResolver getGenericTypeResolver() {
        return genericTypeResolver;
    }

    /**
     * Query the initialization state of the Domain.
     *
     * @return true, if the Domain was initialized
     */
    public static boolean isInitialized() {
        return initialized;
    }

}
