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

    private static InitializedDomain initializedDomain;

    private static GenericTypeResolver genericTypeResolver = new DefaultEmptyGenericTypeResolver();

    /**
     * @param <V>                   type of DomainTypeMirror
     * @param fullQualifiedTypeName the full qualified type name to return a mirror instance for
     * @return a mirror instance for a given full qualified type name.
     */
    @SuppressWarnings("unchecked")
    public static <V extends DomainTypeMirror> Optional<V> typeMirror(String fullQualifiedTypeName) {
        if (!initialized) {
            throw MirrorException.fail("Domain was not initialized!");
        }
        var mirror = initializedDomain.allTypeMirrors().get(fullQualifiedTypeName);
        return Optional.ofNullable((V) mirror);
    }

    /**
     * @param <A>           type of AggregateRootMirror
     * @param aggregateRoot the AggregateRoot to return a mirror for
     * @return the {@link AggregateRootMirror} for the given AggregateRoot instance.
     */
    @SuppressWarnings("unchecked")
    public static <A extends AggregateRootMirror> A aggregateRootMirrorFor(AggregateRoot<?> aggregateRoot) {
        return (A) typeMirror(aggregateRoot.getClass().getName())
            .orElseThrow(
                () -> MirrorException.fail("No AggregateRootMirror found for %s", aggregateRoot.getClass().getName()));
    }

    /**
     * @param <E>    type of EntityMirror
     * @param entity the entity to return a mirror for
     * @return the {@link EntityMirror} for the given Entity instance.
     */
    @SuppressWarnings("unchecked")
    public static <E extends EntityMirror> E entityMirrorFor(Entity<?> entity) {
        return (E) typeMirror(entity.getClass().getName())
            .orElseThrow(() -> MirrorException.fail("No EntityMirror found for %s", entity.getClass().getName()));
    }

    /**
     * @param <E>            type of EntityMirror
     * @param entityTypeName full qualified name of the entity type
     * @return the {@link EntityMirror} for the given full qualified Entity type name.
     */
    @SuppressWarnings("unchecked")
    public static <E extends EntityMirror> E entityMirrorFor(String entityTypeName) {
        return (E) typeMirror(entityTypeName)
            .orElseThrow(() -> MirrorException.fail("No EntityMirror found for %s", entityTypeName));
    }

    /**
     * @param <DE>        type of DomainEventMirror
     * @param domainEvent the domain event to return a mirror for
     * @return the {@link DomainEventMirror} for the given DomainEvent instance.
     */
    @SuppressWarnings("unchecked")
    public static <DE extends DomainEventMirror> DE domainEventMirrorFor(DomainEvent domainEvent) {
        return (DE) typeMirror(domainEvent.getClass().getName())
            .orElseThrow(
                () -> MirrorException.fail("No DomainEventMirror found for %s", domainEvent.getClass().getName()));
    }

    /**
     * @param <DE>                type of DomainEventMirror
     * @param domainEventTypeName full qualified name of DomainEvent type
     * @return the {@link DomainEventMirror} for the given full qualified DomainEvent type name.
     */
    @SuppressWarnings("unchecked")
    public static <DE extends DomainEventMirror> DE domainEventMirrorFor(String domainEventTypeName) {
        return (DE) typeMirror(domainEventTypeName)
            .orElseThrow(() -> MirrorException.fail("No DomainEventMirror found for %s", domainEventTypeName));
    }

    /**
     * @param <DS>          type of DomainServiceMirror
     * @param domainService the DomainService to return a mirror for
     * @return the {@link DomainServiceMirror} for the given DomainService instance.
     */
    @SuppressWarnings("unchecked")
    public static <DS extends DomainServiceMirror> DS domainServiceMirrorFor(DomainService domainService) {
        return (DS) typeMirror(domainService.getClass().getName())
            .orElseThrow(
                () -> MirrorException.fail("No DomainServiceMirror found for %s", domainService.getClass().getName()));
    }

    /**
     * @param <DS>                  type of DomainServiceMirror
     * @param domainServiceTypeName full qualified name of domain service type
     * @return the {@link DomainServiceMirror} for the given full qualified DomainService type name.
     */
    @SuppressWarnings("unchecked")
    public static <DS extends DomainServiceMirror> DS domainServiceMirrorFor(String domainServiceTypeName) {
        return (DS) typeMirror(domainServiceTypeName)
            .orElseThrow(() -> MirrorException.fail("No DomainServiceMirror found for %s", domainServiceTypeName));
    }

    /**
     * @param <AS>               type of ApplicationServiceMirror
     * @param applicationService the ApplicationService to return the mirror for
     * @return the {@link ApplicationServiceMirror} for the given ApplicationService instance.
     */
    @SuppressWarnings("unchecked")
    public static <AS extends ApplicationServiceMirror> AS domainServiceMirrorFor(ApplicationService applicationService) {
        return (AS) typeMirror(applicationService.getClass().getName())
            .orElseThrow(() -> MirrorException.fail("No ApplicationServiceMirror found for %s",
                applicationService.getClass().getName()));
    }

    /**
     * @param <AS>                       type of ApplicationServiceMirror
     * @param applicationServiceTypeName full qualified name of the application service type
     * @return the {@link ApplicationServiceMirror} for the given full qualified ApplicationService type name.
     */
    @SuppressWarnings("unchecked")
    public static <AS extends ApplicationServiceMirror> AS applicationServiceMirrorFor(String applicationServiceTypeName) {
        return (AS) typeMirror(applicationServiceTypeName)
            .orElseThrow(
                () -> MirrorException.fail("No ApplicationServiceMirror found for %s", applicationServiceTypeName));
    }

    /**
     * @param <R> type of RepositoryMirror
     * @param arm AggregateRootMirror to return the mirror for
     * @return the {@link RepositoryMirror} for the given Repository instance for a given {@link AggregateRootMirror}.
     */
    @SuppressWarnings("unchecked")
    public static <R extends RepositoryMirror> R repositoryMirrorFor(AggregateRootMirror arm) {
        return (R) initializedDomain
            .allTypeMirrors()
            .values()
            .stream()
            .filter(tm -> tm.getDomainType().equals(DomainType.REPOSITORY))
            .map(tm -> (RepositoryMirror) tm)
            .filter(rm -> rm.getManagedAggregate().isPresent() && rm.getManagedAggregate().get().equals(arm))
            .findFirst()
            .orElseThrow(
                () -> MirrorException.fail("No RepositoryMirror found for AggregateRoot %s", arm.getTypeName()));
    }

    /**
     * @param <R>        type of RepositoryMirror
     * @param repository the repository to return the mirror for
     * @return the {@link RepositoryMirror} for the given Repository instance.
     */
    @SuppressWarnings("unchecked")
    public static <R extends RepositoryMirror> R repositoryMirrorFor(Repository<?, ?> repository) {
        return (R) typeMirror(repository.getClass().getName())
            .orElseThrow(
                () -> MirrorException.fail("No RepositoryMirror found for %s", repository.getClass().getName()));
    }

    /**
     * @param <R>                type of RepositoryMirror
     * @param repositoryTypeName full qualified Repository type name
     * @return the {@link RepositoryMirror} for the given full qualified Repository type name.
     */
    @SuppressWarnings("unchecked")
    public static <R extends RepositoryMirror> R repositoryMirrorFor(String repositoryTypeName) {
        return (R) typeMirror(repositoryTypeName)
            .orElseThrow(() -> MirrorException.fail("No RepositoryMirror found for %s", repositoryTypeName));
    }

    /**
     * @param <E>              type of EntityMirror
     * @param identityTypeName name of identity type
     * @return the {@link EntityMirror} for the given full qualified type name of the Identity type.
     */
    @SuppressWarnings("unchecked")
    public static <E extends EntityMirror> E entityMirrorForIdentityTypeName(String identityTypeName) {
        if (!initialized) {
            throw MirrorException.fail("Domain was not initialized!");
        }

        return (E) initializedDomain.allTypeMirrors()
            .values()
            .stream()
            .filter(dm -> {
                if (dm instanceof EntityMirror) {
                    var em = (EntityMirror) dm;
                    if (em.getIdentityField().isPresent()) {
                        return em.getIdentityField().get().getType().getTypeName().equals(identityTypeName);
                    }
                }
                return false;
            })
            .findFirst()
            .orElseThrow(() -> MirrorException.fail("No EntityMirror found for identity type %s", identityTypeName));
    }

    /**
     * @param <I>              type of IdentityMirror
     * @param identityTypeName name of the identity type
     * @return the {@link IdentityMirror} for the given full qualified Identity type name.
     */
    @SuppressWarnings("unchecked")
    public static <I extends IdentityMirror> I identityMirrorFor(String identityTypeName) {
        return (I) typeMirror(identityTypeName)
            .orElseThrow(() -> MirrorException.fail("No IdentityMirror found for %s", identityTypeName));
    }

    /**
     * @param <V>         type of ValueObjectMirror
     * @param valueObject the ValueObject to return the mirror for
     * @return the {@link ValueObjectMirror} for the given ValueObject instance.
     */
    @SuppressWarnings("unchecked")
    public static <V extends ValueObjectMirror> V valueObjectMirrorFor(ValueObject valueObject) {
        return (V) typeMirror(valueObject.getClass().getName())
            .orElseThrow(
                () -> MirrorException.fail("No ValueObjectMirror found for %s", valueObject.getClass().getName()));
    }

    /**
     * @param <V>                 type of ValueObjectMirror
     * @param valueObjectTypeName name of the ValueObject type
     * @return the {@link ValueObjectMirror} for the given full qualified ValueObject type name
     */
    @SuppressWarnings("unchecked")
    public static <V extends ValueObjectMirror> V valueObjectMirrorFor(String valueObjectTypeName) {
        return (V) typeMirror(valueObjectTypeName)
            .orElseThrow(() -> MirrorException.fail("No ValueObjectMirror found for %s", valueObjectTypeName));
    }

    /**
     * @return the list of {@link BoundedContextMirror} instances for the mirrored bounded contexts
     */
    public static List<BoundedContextMirror> getBoundedContexts() {
        if (!initialized) {
            throw MirrorException.fail("Domain was not initialized!");
        }
        return initializedDomain.boundedContextMirrors();
    }

    /**
     * @return the {@link InitializedDomain} containing all type mirrors created upon initialization
     */
    public static InitializedDomain getInitializedDomain() {
        return initializedDomain;
    }

    /**
     * Initializes the domain by a given factory.
     * The factory decides the source of Domain meta information (reflection or something else).
     *
     * @param domainMirrorFactory the factory
     */
    public static void initialize(DomainMirrorFactory domainMirrorFactory) {
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
     * @return the GenericTypeResolver for the Domain
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
