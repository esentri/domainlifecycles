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

package tests.shared.complete.onlinehandel.bestellung;

import jakarta.validation.constraints.NotNull;
import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.domain.types.Repository;
import tests.shared.persistence.domain.bestellung.bv2.BestellStatusCodeEnum;

import java.util.Optional;
import java.util.stream.Stream;


public class BestellungRepository implements Repository<BestellungIdBv3, BestellungBv3> {


    @Override
    public Optional<BestellungBv3> findById(@NotNull BestellungIdBv3 id) {
        return Optional.empty();
    }

    @Override
    public BestellungBv3 insert(BestellungBv3 aggregateRoot) {
        return null;
    }

    @Override
    public BestellungBv3 update(BestellungBv3 aggregateRoot) {
        return null;
    }

    @Override
    public Optional<BestellungBv3> deleteById(BestellungIdBv3 bestellungIdBv3) {
        return Optional.empty();
    }

    @Publishes(domainEventTypes = NeueBestellung.class)
    public BestellungIdBv3 create(@NotNull BestellungBv3 bestellung) {
        return bestellung.getId();
    }

    public Stream<BestellungBv3> findByStatus(@NotNull BestellStatusCodeEnum status) {
        return Stream.empty();
    }

}
