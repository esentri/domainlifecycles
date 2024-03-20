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

//    ____                _
//   |    \ ___ _____ ___|_|___
//   |  |  | . |     | .'| |   |
//  _|____/|___|_|_|_|__,|_|_|_|_
// |  |  |_|  _|___ ___ _ _ ___| |___ ___
// |  |__| |  _| -_|  _| | |  _| | -_|_ -|
// |_____|_|_| |___|___|_  |___|_|___|___|
//                     |___|
// Copyright (C) esentri.NitroX - All Rights Reserved.
//
// Unauthorized copying of this file, via any medium
// is strictly prohibited. Proprietary and confidential.

package nitrox.dlc.domain.types.internal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A mixin interface that declares a persistable type to be managed by optimistic
 * concurrency model.
 *
 * @author Mario Herb
 */
public interface ConcurrencySafe {

    /**
     * Returns the "version" of this entity according to the underlying optimistic
     * concurrency model.
     * <p>
     * The version is tested against the connected persistent data store whenever
     * the aggregate is being written back. If the version is out of sync (i.e.
     * someone else has written to the store while this operation was processing),
     * then the store operation fails.
     *
     * @return current version of this entity
     */
    long concurrencyVersion();

    /**
     * Used to instruct the DLC runtime to interpret the annotated Java field
     * as 'concurrency version'. The field must be of type {@code long}.
     */
    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ConcurrencyVersion { }
}
