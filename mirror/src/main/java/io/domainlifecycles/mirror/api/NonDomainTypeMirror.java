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

package io.domainlifecycles.mirror.api;

import java.util.List;

/**
 * A NonDomainTypeMirror mirrors a class that is not classified as one of the recognized
 * {@link DomainType}s (its {@link #getDomainType()} is {@link DomainType#NON_DOMAIN}), but that
 * belongs to a scanned domain model package (e.g. a utility, mapper or helper class).
 * <p>
 * Such mirrors are only built when non-domain class scanning is enabled on the
 * {@code DomainMirrorFactory}.
 *
 * @author Mario Herb
 */
public interface NonDomainTypeMirror extends DomainTypeMirror {

    /**
     * @return the list of referenced {@link ServiceKindMirror} instances, i.e. service kinds
     * (domain service, application service, repository, query handler, outbound service or an
     * unspecified service kind) referenced by a field, method parameter or method return type of
     * this non-domain class. This is the inverse of
     * {@link ServiceKindMirror#getReferencedNonDomainTypes()} and typically identifies callers of a
     * service kind, e.g. a controller or a message listener.
     */
    List<ServiceKindMirror> getReferencedServiceKinds();

}
