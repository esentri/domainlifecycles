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

package tests.shared.complete.onlinehandel.zustellung;

import nitrox.dlc.domain.types.DomainService;
import tests.shared.complete.onlinehandel.bestellung.BestellungIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungRepository;

public class ZustellungsService implements DomainService {

    private final BestellungRepository bestellungRepository;

    public ZustellungsService(BestellungRepository bestellungRepository) {
        this.bestellungRepository = bestellungRepository;
    }

    public void liefereAus(StarteAuslieferung starteAuslieferung){
        var bestellung = bestellungRepository.findById(starteAuslieferung.bestellungId());
        if(bestellung.isPresent()){
            var b = bestellung.get();
            b.starteLieferung();
            bestellungRepository.update(b);
        }
    }

    private void tueWasPrivates(BestellungIdBv3 bestellungIdBv3){

    }
}
