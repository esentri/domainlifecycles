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

package io.domainlifecycles.domain.types.companions;

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.FieldMirror;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Companion class which gives us static access to several typical ValueObject related utility functions
 * implemented in a general way based on meta informations kept in the domain mirror.
 *
 * @author Mario Herb
 */
public class ValueObjects {

    /**
     * Generic equals implementation for ValueObjects depending on domain mirror.
     * In DDD and DLC entities are considered to be equal, if all of their contained values are equal (deep equality).
     *
     * @param thisValueObject First ValueObject to be checked for equality
     * @param thatObject Second Object to be checked for equality
     * @return true, if thisValueObject is equal to thatObject
     */
    public static boolean equals(ValueObject thisValueObject, Object thatObject) {
        Objects.requireNonNull(thisValueObject, "thisValueObject is required to be not null, when calling 'equals'!");
        if (thatObject == null) {
            return false;
        }
        if (!thisValueObject.getClass().equals(thatObject.getClass())) {
            return false;
        }
        ValueObject thatValueObject = (ValueObject) thatObject;
        var vm = Domain.valueObjectMirrorFor(thisValueObject);
        boolean isEqual = nullSafeObjectEquals(vm.getBasicFields().stream().filter(f -> !f.isStatic()), thisValueObject, thatValueObject);
        if (!isEqual) {
            return false;
        } else {
            return nullSafeObjectEquals(vm.getValueReferences().stream().filter(vrm -> !vrm.isStatic()), thisValueObject, thatValueObject);
        }
    }

    private static boolean nullSafeObjectEquals(Stream<? extends FieldMirror> fmStream, DomainObject thisObject, DomainObject thatObject) {
        var thisValueAccessor = DlcAccess.accessorFor(thisObject);
        var thatValueAccessor = DlcAccess.accessorFor(thatObject);
        boolean anyFalse = fmStream.map(fm -> {
            var thisValue = thisValueAccessor.peek(fm.getName());
            var thatValue = thatValueAccessor.peek(fm.getName());
            if (thisValue == null && thatValue == null) {
                return true;
            }
            if (thisValue != null) {
                return thisValue.equals(thatValue);
            }
            return false;
        }).anyMatch(b -> !b);
        return !anyFalse;
    }

    /**
     * Generic hashCode implementation for ValueObjects depending on domain mirror.
     *
     * @param thisValueObject the Value Object which will be hashed
     * @return hashcode of given ValueObject instance
     */
    public static int hashCode(ValueObject thisValueObject) {
        Objects.requireNonNull(thisValueObject, "thisValueObject is required to be not null, when calling 'hashCode'!");
        var vm = Domain.valueObjectMirrorFor(thisValueObject);
        var accessor = DlcAccess.accessorFor(thisValueObject);
        var propertyValues = vm.getBasicFields()
            .stream()
            .filter(fm -> !fm.isStatic())
            .map(fm -> accessor.peek(fm.getName())).toList();
        var valueObjectValues = vm.getValueReferences()
            .stream()
            .filter(fm -> !fm.isStatic())
            .map(fm -> accessor.peek(fm.getName()))
            .flatMap(val -> {
                if (val instanceof Collection) {
                    return ((Collection<?>) val).stream();
                } else {
                    return Stream.of(val);
                }
            }).toList();
        var allValues = new ArrayList<>();
        allValues.addAll(propertyValues);
        allValues.addAll(valueObjectValues);
        return Objects.hash(allValues.toArray());
    }

    /**
     * Generic toString implementation for ValueObjects depending on domain mirror.
     *
     * @param thisValueObject the ValueObject which will be represented as a String
     * @return String-representation of the given ValueObject instance
     */
    public static String toString(ValueObject thisValueObject) {
        Objects.requireNonNull(thisValueObject, "thisValueObject is required to be not null, when calling 'toString'!");

        var vm = Domain.valueObjectMirrorFor(thisValueObject);
        StringBuilder returnVal = new StringBuilder(thisValueObject.getClass().getName());
        returnVal.append("@");
        returnVal.append(System.identityHashCode(thisValueObject));
        returnVal.append("(");
        var accessor = DlcAccess.accessorFor(thisValueObject);
        String propVals = vm.getBasicFields()
            .stream()
            .filter(fm -> !fm.isStatic())
            .map(fm -> fm.getName() + "=" + accessor.peek(fm.getName()))
            .collect(Collectors.joining(", "));
        returnVal.append(propVals);
        String voVals = vm.getValueReferences()
            .stream()
            .filter(vrm -> !vrm.isStatic())
            .map(vrm -> {
                String voVal = vrm.getName() + "=";
                if (vrm.getType().hasCollectionContainer()) {
                    voVal += "[";
                    Collection<?> col = accessor.peek(vrm.getName());
                    if (col != null) {
                        voVal += col.stream().map(ValueObjects::toString).collect(Collectors.joining(", "));
                        voVal += "]";
                    } else {
                        voVal += "null";
                    }
                } else {
                    Object voObject = accessor.peek(vrm.getName());
                    if (voObject == null) {
                        voVal += "null";
                    } else if (voObject instanceof Optional<?> voOptional) {
                        if (voOptional.isPresent()) {
                            voObject = voOptional.get();
                            voVal += toString(voObject);
                        } else {
                            voVal += "null";
                        }
                    } else {
                        voVal += toString(voObject);
                    }
                }
                return voVal;
            }
        ).collect(Collectors.joining(", "));
        if (!voVals.isEmpty()) {
            returnVal.append(", ");
            returnVal.append(voVals);
        }
        returnVal.append(")");
        return returnVal.toString();
    }

    private static String toString(Object value){
        return value.toString();
    }
}
