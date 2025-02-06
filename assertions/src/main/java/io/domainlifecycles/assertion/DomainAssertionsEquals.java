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

package io.domainlifecycles.assertion;

import java.util.Optional;

/**
 * Assertions for equality of an object
 * Package private class for separation concerns.
 * API and javadoc is defined in {@link DomainAssertions}
 * <p>
 * If an assertion fails an either and passed RuntimeException or DomainAssertionException is thrown.
 *
 * @author Philipp Holz
 */
class DomainAssertionsEquals {

    private DomainAssertionsEquals() {

        throw new UnsupportedOperationException("This class should not be instantiated!");
    }

    static void equals(Object anObject1, Object anObject2, String aMessage) {

        equals(anObject1, anObject2, new DomainAssertionException(aMessage));
    }

    static void equals(Object anObject1, Object anObject2, RuntimeException runtimeException) {

        if ((anObject1 != null && !anObject1.equals(anObject2))
            || (anObject1 == null && anObject2 != null)) {
            throw runtimeException;
        }
    }

    static void optionalEquals(Optional anObjectOptional, Object anObject2, String aMessage) {

        if (anObjectOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (anObjectOptional.isPresent()) {
            Object anObject1 = anObjectOptional.get();
            equals(anObject1, anObject2, aMessage);
        } else if (anObjectOptional.isEmpty() && anObject2 != null) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void equals(int aNumber1, int aNumber2, String aMessage) {

        if (aNumber1 != aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void equals(long aNumber1, long aNumber2, String aMessage) {

        if (aNumber1 != aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void equals(byte aNumber1, byte aNumber2, String aMessage) {

        if (aNumber1 != aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void equals(short aNumber1, short aNumber2, String aMessage) {

        if (aNumber1 != aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void equals(double aNumber1, double aNumber2, String aMessage) {

        if (aNumber1 != aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void equals(float aNumber1, float aNumber2, String aMessage) {

        if (aNumber1 != aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void equals(char aChar1, char aChar2, String aMessage) {

        if (aChar1 != aChar2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void equals(boolean aBool1, boolean aBool2, String aMessage) {

        if (aBool1 != aBool2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void notEquals(Object anObject1, Object anObject2, String aMessage) {

        if ((anObject1 != null && anObject1.equals(anObject2))
            || (anObject1 == null && anObject2 == null)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void optionalNotEquals(Optional anOptionalObject1, Object anObject2, String aMessage) {

        if (anOptionalObject1 == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (anOptionalObject1.isPresent()) {
            Object anObject1 = anOptionalObject1.get();
            notEquals(anObject1, anObject2, aMessage);
        } else if (anOptionalObject1.isEmpty() && anObject2 == null) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void notEquals(int aNumber1, int aNumber2, String aMessage) {

        if (aNumber1 == aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void notEquals(long aNumber1, long aNumber2, String aMessage) {

        if (aNumber1 == aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void notEquals(byte aNumber1, byte aNumber2, String aMessage) {

        if (aNumber1 == aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void notEquals(short aNumber1, short aNumber2, String aMessage) {

        if (aNumber1 == aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void notEquals(double aNumber1, double aNumber2, String aMessage) {

        if (aNumber1 == aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void notEquals(float aNumber1, float aNumber2, String aMessage) {

        if (aNumber1 == aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void notEquals(char aChar1, char aChar2, String aMessage) {

        if (aChar1 == aChar2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    static void notEquals(boolean aBool1, boolean aBool2, String aMessage) {

        if (aBool1 == aBool2) {
            throw new DomainAssertionException(aMessage);
        }
    }
}
