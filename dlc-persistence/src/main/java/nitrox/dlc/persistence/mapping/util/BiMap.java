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

package nitrox.dlc.persistence.mapping.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Simple implementation of a bidirectional map.
 *
 * @param <K>
 * @param <V>
 *
 * @author MArio Herb
 */
public class BiMap<K, V> {

    private final Map<K,V> keyToValue = new HashMap<>();
    private final Map<V,K> valueToKey = new HashMap<>();

    public void put(K key, V value){
        keyToValue.put(key, value);
        valueToKey.put(value, key);
    }

    public K getInverse(V value){
        return valueToKey.get(value);
    }

    public V get(K key){
        return keyToValue.get(key);
    }

    public Set<K> keySet() {
        return keyToValue.keySet();
    }

    public Set<V> valueSet() {
        return valueToKey.keySet();
    }
}