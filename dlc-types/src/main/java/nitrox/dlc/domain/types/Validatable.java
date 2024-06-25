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

package nitrox.dlc.domain.types;

/**
 * A mixin interface that declares the primary type accessible for validation.
 *
 * @author Tobias Herb
 * @author Mario Herb
 */
public interface Validatable {

    /**
     * Activates validation mechanics.
     */
    default void validate(){}
}