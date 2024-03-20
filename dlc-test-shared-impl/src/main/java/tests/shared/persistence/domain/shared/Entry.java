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

package tests.shared.persistence.domain.shared;

import nitrox.dlc.domain.types.Entity;
import nitrox.dlc.domain.types.Identity;

/*
Given with the sample of a shared context and the entrys for the tables
 */
public abstract class Entry<T extends Identity<Long>> implements Entity<T> {

    private final int x;
    private final int y;

    @ConcurrencyVersion
    private long concurrencyVersion;

    public Entry(long concurrencyVersion, int x, int y) {
        this.x = x;
        this.y = y;
        this.concurrencyVersion = concurrencyVersion;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public long concurrencyVersion() {
        return concurrencyVersion;
    }
}
