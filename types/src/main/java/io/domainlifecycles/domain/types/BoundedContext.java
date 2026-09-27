/*
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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.domain.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a package (via its {@code package-info.java}) as the root of a Bounded Context, letting the
 * Domain Mirror derive Bounded Context boundaries directly from the code instead of requiring them to
 * be configured manually (see {@code AbstractDomainMirrorFactory#setBoundedContextPackages}, which
 * still takes precedence over this annotation when used).
 * <p>
 * {@code mirror-jmolecules} additionally recognizes jMolecules' own, structurally equivalent
 * {@code org.jmolecules.ddd.annotation.BoundedContext} package annotation.
 *
 * @author Mario Herb
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface BoundedContext {

    /**
     * An optional human-readable name for the Bounded Context, in addition to its package name.
     *
     * @return the Bounded Context's name, or an empty String if none is given
     */
    String value() default "";
}
