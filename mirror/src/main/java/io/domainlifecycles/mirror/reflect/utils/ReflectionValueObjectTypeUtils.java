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
 *  Copyright 2019-2025 the original author or authors.
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

package io.domainlifecycles.mirror.reflect.utils;

import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.reflect.MemberSelect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Utility class that contains general reflection cases used for analyzing
 * {@link ValueObject} classes.
 *
 * @author Mario Herb
 */
public class ReflectionValueObjectTypeUtils {

    /**
     * Returning a list of all field that are defined in the ValueObject or in the
     * corresponding class hierarchy (excluding static or synthetic fields)
     *
     * @param valueObjectClass the analyzed value object class
     * @return the list of fields defined
     */
    public static List<Field> valueObjectFields(Class<? extends ValueObject> valueObjectClass) {
        return JavaReflect.fields(valueObjectClass, MemberSelect.HIERARCHY)
            .stream()
            .filter(field ->
                !Modifier.isStatic(field.getModifiers())
                && !field.isSynthetic()
            )
            .toList();
    }


}
