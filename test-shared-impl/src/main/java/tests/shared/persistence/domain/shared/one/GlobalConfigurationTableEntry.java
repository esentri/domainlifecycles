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

package tests.shared.persistence.domain.shared.one;

import lombok.Builder;
import lombok.Getter;
import tests.shared.persistence.domain.shared.Entry;

@Getter
public class GlobalConfigurationTableEntry extends Entry<GlobalConfigurationTableEntryId> {

    private GlobalConfigurationTableEntryId id;
    private ConfigurationId configurationId;

    @Builder(setterPrefix = "set")
    public GlobalConfigurationTableEntry(
        ConfigurationId configurationId,
        GlobalConfigurationTableEntryId id,
        long concurrencyVersion,
        int x,
        int y) {
        super(concurrencyVersion, x, y);
        this.id = id;
        this.configurationId = configurationId;

    }

    @Override
    public GlobalConfigurationTableEntryId id() {
        return id;
    }
}
