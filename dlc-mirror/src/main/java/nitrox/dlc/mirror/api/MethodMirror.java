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

package nitrox.dlc.mirror.api;

import java.util.List;
import java.util.Optional;

/**
 * A MethodMirror mirrors a Java method on a mirrored type.
 *
 * @author Mario Herb
 */
public interface MethodMirror {

    /**
     * Returns the method name of the mirrored method
     */
    String getName();

    /**
     * Returns the full qualified type name, of the type that declared the method.
     */
    String getDeclaredByTypeName();

    /**
     * Returns the {@link AccessLevel} of the mirrored method.
     */
    AccessLevel getAccessLevel();

    /**
     * Returns the list of {@link ParamMirror} instances for the parameters declared in the mirrored methods signature.
     */
    List<ParamMirror> getParameters();

    /**
     * Returns an {@link AssertedContainableTypeMirror} mirroring the return type of the method.
     * The {@literal void} return type is explicitly defined.
     */
    AssertedContainableTypeMirror getReturnType();

    /**
     * Returns a list of {@link DomainEventMirror} published by the mirrored method.
     * The method must be annotated with {@link nitrox.dlc.domain.types.Publishes}.
     */
    List<DomainEventMirror> getPublishedEvents();

    /**
     * Returns a {@link DomainEventMirror} if the method listens to an according {@link nitrox.dlc.domain.types.DomainEvent}
     * The method must be annotated with {@link nitrox.dlc.domain.types.ListensTo}
     */
    Optional<DomainEventMirror> getListenedEvent();

    /**
     * Query, if the mirrored method publishes {@link nitrox.dlc.domain.types.DomainEvent} instances for the given corresponding mirror.
     * To be more precise, if the method is annotated to do so {@link nitrox.dlc.domain.types.Publishes}.
     */
    boolean publishes(DomainEventMirror domainEvent);

    /**
     * Query, if the mirrored method listens to {@link nitrox.dlc.domain.types.DomainEvent} instances for the given corresponding mirror.
     * To be more precise, if the method is annotated to do so {@link nitrox.dlc.domain.types.ListensTo}.
     */
    boolean listensTo(DomainEventMirror domainEvent);

    /**
     * Query, if the mirrored method processes {@link nitrox.dlc.domain.types.DomainCommand} instances for the given corresponding mirror.
     * To be more precise, if the method has exactly one parameter containing a DomainCommand.
     */
    boolean processes(DomainCommandMirror command);

    /**
     * Returns true, if the method is a setter (follows the conventions of a setter in Java)
     */
    boolean isSetter();

    /**
     * Returns true, if the method is a getter (follows the conventions of a getter in Java)
     */
    boolean isGetter();

    /**
     * Checks if the method is overridden by a subclass.
     *
     * @return true if the method is overridden, false otherwise
     */
    boolean isOverridden();
}
