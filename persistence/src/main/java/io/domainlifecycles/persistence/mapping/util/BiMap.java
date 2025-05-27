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

package io.domainlifecycles.persistence.mapping.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A bidirectional map which allows mapping of keys to values and values to keys.
 * This class maintains two internal mappings: one for key-to-value mapping and
 * another for value-to-key mapping. It ensures that each key maps to a unique value
 * and each value maps to a unique key.
 *
 * @param <K> the type of keys maintained by this bidirectional map
 * @param <V> the type of values maintained by this bidirectional map
 *
 * @author Mario Herb
 */
public class BiMap<K, V> {

    private final Map<K, V> keyToValue = new HashMap<>();
    private final Map<V, K> valueToKey = new HashMap<>();

    /**
     * Puts a key-value pair into the bidirectional map. Updates both the key-to-value and value-to-key mappings.
     *
     * @param key   the key to be added to the map
     * @param value the value to be associated with the given key
     */
    public void put(K key, V value) {
        keyToValue.put(key, value);
        valueToKey.put(value, key);
    }

    /**
     * Retrieves the key corresponding to the specified value in the bidirectional map.
     * If the value is not present in the map, returns {@code null}.
     *
     * @param value the value whose associated key is to be returned
     * @return the key associated with the specified value, or {@code null} if the value is not present
     */
    public K getInverse(V value) {
        return valueToKey.get(value);
    }

    /**
     * Retrieves the value associated with the given key in the bidirectional map.
     * If the key is not present, returns {@code null}.
     *
     * @param key the key whose associated value is to be returned
     * @return the value associated with the specified key, or {@code null} if the key is not present
     */
    public V get(K key) {
        return keyToValue.get(key);
    }

    /**
     * Returns a set view of the keys contained in this map. The set is backed by the map,
     * so changes to the map are reflected in the set, and vice versa.
     *
     * @return a set of keys contained in this map
     */
    public Set<K> keySet() {
        return keyToValue.keySet();
    }

    /**
     * Returns a set view of the values contained in this bidirectional map.
     * The set is backed by the map, so changes to the map are reflected in the set, and vice versa.
     *
     * @return a set of values contained in this map
     */
    public Set<V> valueSet() {
        return valueToKey.keySet();
    }
}
