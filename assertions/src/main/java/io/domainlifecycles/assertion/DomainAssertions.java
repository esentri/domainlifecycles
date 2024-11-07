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

import org.apache.commons.validator.routines.EmailValidator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Simple assertions for the correct state of an object.
 * <p>
 * If an assertion fails a DomainAssertionException is thrown.
 *
 * @author Mario Herb
 */
public class DomainAssertions {

    /**
     * Compares two objects for equality. Two null object references are considered to be equal.
     *
     * @param anObject1 checked
     * @param anObject2 compare value
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void equals(Object anObject1, Object anObject2, String aMessage) {
        if ((anObject1 != null && !anObject1.equals(anObject2))
            || (anObject1 == null && anObject2 != null)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares an Optional containing an object and an object for equality.
     * The Optional must not be {@code null}. An empty Optional and a null reference are considered to be equal.
     *
     * @param anObjectOptional checked
     * @param anObject2        compare value
     * @param aMessage         message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalEquals(Optional anObjectOptional, Object anObject2, String aMessage) {
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

    /**
     * Compares two {@code int} instances for equality.
     *
     * @param aNumber1 checked
     * @param aNumber2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void equals(int aNumber1, int aNumber2, String aMessage) {
        if (aNumber1 != aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code long} instances for equality.
     *
     * @param aNumber1 checked
     * @param aNumber2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void equals(long aNumber1, long aNumber2, String aMessage) {
        if (aNumber1 != aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code byte} instances for equality.
     *
     * @param aNumber1 checked
     * @param aNumber2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void equals(byte aNumber1, byte aNumber2, String aMessage) {
        if (aNumber1 != aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code short} instances for equality.
     *
     * @param aNumber1 checked
     * @param aNumber2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void equals(short aNumber1, short aNumber2, String aMessage) {
        if (aNumber1 != aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code double} instances for equality.
     *
     * @param aNumber1 checked
     * @param aNumber2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void equals(double aNumber1, double aNumber2, String aMessage) {
        if (aNumber1 != aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code float} instances for equality.
     *
     * @param aNumber1 checked
     * @param aNumber2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void equals(float aNumber1, float aNumber2, String aMessage) {
        if (aNumber1 != aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code char} instances for equality.
     *
     * @param aChar1   checked
     * @param aChar2   compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void equals(char aChar1, char aChar2, String aMessage) {
        if (aChar1 != aChar2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code boolean} instances for equality.
     *
     * @param aBool1   checked
     * @param aBool2   compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void equals(boolean aBool1, boolean aBool2, String aMessage) {
        if (aBool1 != aBool2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two objects for non-equality. Two null references of objects are considered to be equal.
     *
     * @param anObject1 checked
     * @param anObject2 compare value
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void notEquals(Object anObject1, Object anObject2, String aMessage) {
        if ((anObject1 != null && anObject1.equals(anObject2))
            || (anObject1 == null && anObject2 == null)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares an Optional containing an object and an object for non-equality.
     * The Optional must not be {@code null}. An empty Optional and a null reference are considered to be equal.
     *
     * @param anOptionalObject1 checked
     * @param anObject2         compare value
     * @param aMessage          message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalNotEquals(Optional anOptionalObject1, Object anObject2, String aMessage) {
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

    /**
     * Compares two {@code int} instances for non-equality.
     *
     * @param aNumber1 checked
     * @param aNumber2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void notEquals(int aNumber1, int aNumber2, String aMessage) {
        if (aNumber1 == aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code long} instances for non-equality.
     * Throws {@link DomainAssertionException}, if equal.
     *
     * @param aNumber1 checked
     * @param aNumber2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void notEquals(long aNumber1, long aNumber2, String aMessage) {
        if (aNumber1 == aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code byte} instances for non-equality.
     *
     * @param aNumber1 checked
     * @param aNumber2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void notEquals(byte aNumber1, byte aNumber2, String aMessage) {
        if (aNumber1 == aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code short} instances for non-equality.
     *
     * @param aNumber1 checked
     * @param aNumber2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void notEquals(short aNumber1, short aNumber2, String aMessage) {
        if (aNumber1 == aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code double} instances for non-equality.
     *
     * @param aNumber1 checked
     * @param aNumber2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void notEquals(double aNumber1, double aNumber2, String aMessage) {
        if (aNumber1 == aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code float} instances for non-equality.
     *
     * @param aNumber1 checked
     * @param aNumber2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void notEquals(float aNumber1, float aNumber2, String aMessage) {
        if (aNumber1 == aNumber2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code char} instances for non-equality.
     *
     * @param aChar1   checked
     * @param aChar2   compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void notEquals(char aChar1, char aChar2, String aMessage) {
        if (aChar1 == aChar2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Compares two {@code boolean} instances for non-equality.
     *
     * @param aBool1   checked
     * @param aBool2   compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void notEquals(boolean aBool1, boolean aBool2, String aMessage) {
        if (aBool1 == aBool2) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if a {@code boolean} is false.
     *
     * @param aBoolean checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFalse(boolean aBoolean, String aMessage) {
        if (aBoolean) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if a collection contains a given object. The Collection must not be {@code null}.
     *
     * @param object1          checked
     * @param objectCollection comparing collection
     * @param aMessage         message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isOneOf(Object object1, Collection<Object> objectCollection, String aMessage) {
        if (objectCollection == null) {
            throw new IllegalArgumentException("The second parameter (Collection) must not be null!");
        }
        if (object1 != null && !objectCollection.contains(object1)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if a collection contains a given object contained in an Optional.
     * The Collection must not be {@code null}.
     * The Optional must not be {@code null}.
     *
     * @param optionalObject1  to be contained
     * @param objectCollection checked
     * @param aMessage         message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsOneOf(Optional optionalObject1, Collection<Object> objectCollection, String aMessage) {
        if (optionalObject1 == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (objectCollection == null) {
            throw new IllegalArgumentException("The second parameter (Collection) must not be null!");
        }
        if (optionalObject1.isPresent()) {
            Object object1 = optionalObject1.get();
            isOneOf(object1, objectCollection, aMessage);
        }
    }

    /**
     * Checks a {@code String} for a given max length.
     *
     * @param aString  checked
     * @param aMaximum max length
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasLengthMax(String aString, int aMaximum, String aMessage) {
        if (aString != null) {
            int length = aString.length();
            if (length > aMaximum) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks an Optional containing a {@code String} for a given max length.
     * The Optional must not be {@code null}.
     *
     * @param optionalString checked
     * @param aMaximum       max length
     * @param aMessage       message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalHasLengthMax(Optional<String> optionalString, int aMaximum, String aMessage) {
        if (optionalString == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (optionalString.isPresent()) {
            String aString = optionalString.get();
            hasLengthMax(aString, aMaximum, aMessage);
        }
    }

    /**
     * Checks a {@code String} for a given min length.
     *
     * @param aString  checked
     * @param aMinimum min length
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasLengthMin(String aString, int aMinimum, String aMessage) {
        if (aString != null) {
            int length = aString.length();
            if (length < aMinimum) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks an Optional containing a {@code String} for a given min length.
     * The Optional must not be {@code null}.
     *
     * @param aStringOptional checked
     * @param aMinimum        min length
     * @param aMessage        message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalHasLengthMin(Optional<String> aStringOptional, int aMinimum, String aMessage) {
        if (aStringOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aStringOptional.isPresent()) {
            String aString = aStringOptional.get();
            hasLengthMin(aString, aMinimum, aMessage);
        }
    }

    /**
     * Checks a {@code String} for given length boundaries.
     *
     * @param aString  checked
     * @param aMinimum min length
     * @param aMaximum max length
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasLength(String aString, int aMinimum, int aMaximum, String aMessage) {
        if (aString != null) {
            int length = aString.length();
            if (length > aMaximum || length < aMinimum) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks an Optional containing a {@code String} for given length boundaries.
     * The Optional must not be {@code null}.
     *
     * @param aStringOptional checked
     * @param aMinimum        min length
     * @param aMaximum        max length
     * @param aMessage        message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalHasLength(Optional<String> aStringOptional, int aMinimum, int aMaximum,
                                         String aMessage) {
        if (aStringOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aStringOptional.isPresent()) {
            String aString = aStringOptional.get();
            hasLength(aString, aMinimum, aMaximum, aMessage);
        }
    }

    /**
     * Checks a {@code Collection} for given size boundaries.
     *
     * @param collection checked
     * @param aMinimum   min size
     * @param aMaximum   max size
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasSize(Collection collection, int aMinimum, int aMaximum, String aMessage) {
        if (collection != null) {
            int length = collection.size();
            if (length < aMinimum || length > aMaximum) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks a {@code Collection} for a given min size.
     *
     * @param collection checked
     * @param aMinimum   min size
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasSizeMin(Collection collection, int aMinimum, String aMessage) {
        if (collection != null) {
            int length = collection.size();
            if (length < aMinimum) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks a {@code Collection} for a given max size.
     *
     * @param collection checked
     * @param aMaximum   max size
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasSizeMax(Collection collection, int aMaximum, String aMessage) {
        if (collection != null) {
            int length = collection.size();
            if (length > aMaximum) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks a {@code Map} for given size boundaries.
     *
     * @param map      checked
     * @param aMinimum min size
     * @param aMaximum max size
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasSize(Map map, int aMinimum, int aMaximum, String aMessage) {
        if (map != null) {
            int length = map.size();
            if (length < aMinimum || length > aMaximum) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks a {@code Map} for a given min size.
     *
     * @param map      checked
     * @param aMinimum min size
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasSizeMin(Map map, int aMinimum, String aMessage) {
        if (map != null) {
            int length = map.size();
            if (length < aMinimum) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks a {@code MAp} for a given max size.
     *
     * @param map      checked
     * @param aMaximum max size
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasSizeMax(Map map, int aMaximum, String aMessage) {
        if (map != null) {
            int length = map.size();
            if (length > aMaximum) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks an array for given size boundaries.
     *
     * @param array    checked
     * @param aMinimum min length
     * @param aMaximum max length
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasSize(Object[] array, int aMinimum, int aMaximum, String aMessage) {
        if (array != null) {
            int length = array.length;
            if (length < aMinimum || length > aMaximum) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks an array for a given min size.
     *
     * @param array    checked
     * @param aMinimum min length
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasSizeMin(Object[] array, int aMinimum, String aMessage) {
        if (array != null) {
            int length = array.length;
            if (length < aMinimum) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks an array for a given max size.
     *
     * @param array    checked
     * @param aMaximum max length
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasSizeMax(Object[] array, int aMaximum, String aMessage) {
        if (array != null) {
            int length = array.length;
            if (length > aMaximum) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks an Optional containing an array for given size boundaries.
     * The Optional must not be {@code null}.
     *
     * @param arrayOptional checked
     * @param aMinimum      length
     * @param aMaximum      length
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalArrayHasSize(Optional<Object[]> arrayOptional, int aMinimum, int aMaximum,
     String aMessage) {
        if (arrayOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (arrayOptional.isPresent()) {
            hasSize(arrayOptional.get(), aMinimum, aMaximum, aMessage);
        }
    }

    /**
     * Checks an Optional containing an array for a given min length.
     * The Optional must not be {@code null}.
     *
     * @param arrayOptional checked
     * @param aMinimum      min length
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalArrayHasSizeMin(Optional<Object[]> arrayOptional, int aMinimum, String aMessage) {
        if (arrayOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (arrayOptional.isPresent()) {
            hasSizeMin(arrayOptional.get(), aMinimum, aMessage);
        }
    }

    /**
     * Checks an Optional containing an array for a given max length.
     * The Optional must not be {@code null}.
     *
     * @param arrayOptional checked
     * @param aMaximum      max length
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalArrayHasSizeMax(Optional<Object[]> arrayOptional, int aMaximum, String aMessage) {
        if (arrayOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (arrayOptional != null && arrayOptional.isPresent()) {
            hasSizeMax(arrayOptional.get(), aMaximum, aMessage);
        }
    }

    /**
     * Checks the max digits of a given {@code BigDecimal}.
     *
     * @param bd          value checked
     * @param maxInteger  max integer digits
     * @param maxFraction max fraction digits
     * @param aMessage    message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigits(BigDecimal bd, int maxInteger, int maxFraction, String aMessage) {
        if (bd != null) {
            int fraction = fractionDigits(bd);
            int integer = integerDigits(bd);
            if (maxFraction < fraction || maxInteger < integer) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks the max digits of an Optional containing {@link BigDecimal}
     * or a {@link Double} or a {@link Float}.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param maxInteger    max integer digits
     * @param maxFraction   max fraction digits
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalHasMaxDigits(Optional valueOptional, int maxInteger, int maxFraction, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            Object o = valueOptional.get();
            if (o instanceof BigDecimal) {
                hasMaxDigits((BigDecimal) o, maxInteger, maxFraction, aMessage);
            } else if (o instanceof Double) {
                hasMaxDigits((Double) o, maxInteger, maxFraction, aMessage);
            } else if (o instanceof Float) {
                hasMaxDigits((Float) o, maxInteger, maxFraction, aMessage);
            } else {
                throw new IllegalArgumentException("valueOptional parameter must contain " +
                    "BigDecimal, Double or Float. Other types are not supported!");
            }
        }
    }

    /**
     * Checks the max integer digits of a given {@code BigDecimal}.
     *
     * @param bd         value checked
     * @param maxInteger max integer digits
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(BigDecimal bd, int maxInteger, String aMessage) {
        if (bd != null) {
            int integer = integerDigits(bd);
            if (maxInteger < integer) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks the max integer digits of a given Optional containing either a
     * {@link BigDecimal}, {@link BigInteger}, {@link Double}, {@link Float}, {@link Integer}, {@link Long},
     * {@link Byte} or {@link Short}.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param maxInteger    max integer digits
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalHasMaxDigitsInteger(Optional valueOptional, int maxInteger, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            Object o = valueOptional.get();
            if (o instanceof BigDecimal) {
                hasMaxDigitsInteger((BigDecimal) o, maxInteger, aMessage);
            } else if (o instanceof Double) {
                hasMaxDigitsInteger((Double) o, maxInteger, aMessage);
            } else if (o instanceof Float) {
                hasMaxDigitsInteger((Float) o, maxInteger, aMessage);
            } else if (o instanceof Integer) {
                hasMaxDigitsInteger((Integer) o, maxInteger, aMessage);
            } else if (o instanceof Long) {
                hasMaxDigitsInteger((Long) o, maxInteger, aMessage);
            } else if (o instanceof BigInteger) {
                hasMaxDigitsInteger((BigInteger) o, maxInteger, aMessage);
            } else if (o instanceof Byte) {
                hasMaxDigitsInteger((Byte) o, maxInteger, aMessage);
            } else if (o instanceof Short) {
                hasMaxDigitsInteger((Short) o, maxInteger, aMessage);
            } else {
                throw new IllegalArgumentException("valueOptional parameter must contain " +
                    "BigDecimal, Double, Float, Integer, Long, BigInteger, Byte or Short. Other types are not " +
                     "supported!");
            }
        }
    }

    /**
     * Checks the max fraction digits of a given {@code BigDecimal}.
     *
     * @param bd          value checked
     * @param maxFraction max fraction digits
     * @param aMessage    message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsFraction(BigDecimal bd, int maxFraction, String aMessage) {
        if (bd != null) {
            int fraction = fractionDigits(bd);
            if (maxFraction < fraction) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks the max fraction digits of a given Optional containing either a
     * {@link BigDecimal}, {@link Double} or {@link Float}.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param maxFraction   max fraction digits
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalHasMaxDigitsFraction(Optional valueOptional, int maxFraction, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            Object o = valueOptional.get();
            if (o instanceof BigDecimal) {
                hasMaxDigitsFraction((BigDecimal) o, maxFraction, aMessage);
            } else if (o instanceof Double) {
                hasMaxDigitsFraction((Double) o, maxFraction, aMessage);
            } else if (o instanceof Float) {
                hasMaxDigitsFraction((Float) o, maxFraction, aMessage);
            } else {
                throw new IllegalArgumentException("valueOptional parameter must contain " +
                    "BigDecimal, Double or Float. Other types are not supported!");
            }
        }
    }

    /**
     * Checks the max integer digits of a given {@link BigInteger}.
     *
     * @param bi         checked value
     * @param maxInteger max integer digits
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(BigInteger bi, int maxInteger, String aMessage) {
        if (bi != null) {
            int integer = bi.toString().length();
            if (maxInteger < integer) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks the max digits of a given {@link double}.
     *
     * @param d           checked value
     * @param maxInteger  max integer digits
     * @param maxFraction max fraction digits
     * @param aMessage    message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigits(double d, int maxInteger, int maxFraction, String aMessage) {
        BigDecimal bd = BigDecimal.valueOf(d);
        hasMaxDigits(bd, maxInteger, maxFraction, aMessage);
    }

    /**
     * Checks the max integer digits of a given {@link double}.
     *
     * @param d          checked value
     * @param maxInteger max integer digits
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(double d, int maxInteger, String aMessage) {
        BigDecimal bd = BigDecimal.valueOf(d);
        hasMaxDigitsInteger(bd, maxInteger, aMessage);
    }

    /**
     * Checks the max fraction digits of a given {@link double}.
     *
     * @param d           checked value
     * @param maxFraction max fraction digits
     * @param aMessage    message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsFraction(double d, int maxFraction, String aMessage) {
        BigDecimal bd = BigDecimal.valueOf(d);
        hasMaxDigitsFraction(bd, maxFraction, aMessage);
    }

    /**
     * Checks the max digits of a given {@link Double}.
     *
     * @param d           checked value
     * @param maxInteger  max integer digits
     * @param maxFraction max fraction digits
     * @param aMessage    message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigits(Double d, int maxInteger, int maxFraction, String aMessage) {
        if (d != null) {
            BigDecimal bd = BigDecimal.valueOf(d);
            hasMaxDigits(bd, maxInteger, maxFraction, aMessage);
        }
    }

    /**
     * Checks the max integer digits of a given {@link Double}.
     *
     * @param d          checked value
     * @param maxInteger max integer digits
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(Double d, int maxInteger, String aMessage) {
        if (d != null) {
            BigDecimal bd = BigDecimal.valueOf(d);
            hasMaxDigitsInteger(bd, maxInteger, aMessage);
        }
    }

    /**
     * Checks the max fraction digits of a given {@link Double}.
     *
     * @param d           checked value
     * @param maxFraction max fraction digits
     * @param aMessage    message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsFraction(Double d, int maxFraction, String aMessage) {
        if (d != null) {
            BigDecimal bd = BigDecimal.valueOf(d);
            hasMaxDigitsFraction(bd, maxFraction, aMessage);
        }
    }

    /**
     * Checks the max digits of a given {@link Float}.
     *
     * @param f           checked value
     * @param maxInteger  max integer digits
     * @param maxFraction max fraction digits
     * @param aMessage    message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigits(Float f, int maxInteger, int maxFraction, String aMessage) {
        if (f != null) {
            hasMaxDigits(f.floatValue(), maxInteger, maxFraction, aMessage);
        }
    }

    /**
     * Checks the max integer digits of a given {@link Float}.
     *
     * @param f          checked value
     * @param maxInteger max integer digits
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(Float f, int maxInteger, String aMessage) {
        if (f != null) {
            hasMaxDigitsInteger(f.floatValue(), maxInteger, aMessage);
        }
    }

    /**
     * Checks the max fraction digits of a given {@link Float}.
     *
     * @param f           checked value
     * @param maxFraction max fraction digits
     * @param aMessage    message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsFraction(Float f, int maxFraction, String aMessage) {
        if (f != null) {
            hasMaxDigitsFraction(f.floatValue(), maxFraction, aMessage);
        }
    }

    /**
     * Checks the max digits of a given {@link float}.
     *
     * @param f           checked value
     * @param maxInteger  max integer digits
     * @param maxFraction max fraction digits
     * @param aMessage    message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigits(float f, int maxInteger, int maxFraction, String aMessage) {
        int integer = integerDigits(BigDecimal.valueOf(f));
        int fraction = fractionDigits(f);
        if (maxInteger < integer || maxFraction < fraction) {
            throw new DomainAssertionException(aMessage);
        }

    }

    /**
     * Checks the max integer digits of a given {@link float}.
     *
     * @param f          checked value
     * @param maxInteger max integer digits
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(float f, int maxInteger, String aMessage) {
        BigDecimal bd = BigDecimal.valueOf(f);
        hasMaxDigitsInteger(bd, maxInteger, aMessage);
    }

    /**
     * Checks the max fraction digits of a given {@link float}.
     *
     * @param f           checked value
     * @param maxFraction max fraction digits
     * @param aMessage    message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsFraction(float f, int maxFraction, String aMessage) {
        int fraction = fractionDigits(f);
        if (maxFraction < fraction) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks the max integer digits of a given {@link int}.
     *
     * @param i          checked value
     * @param maxInteger max integer digits
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(int i, int maxInteger, String aMessage) {
        int integer = Integer.valueOf(i).toString().length();
        if (maxInteger < integer) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks the max integer digits of a given {@link Integer}.
     *
     * @param i          checked value
     * @param maxInteger max integer digits
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(Integer i, int maxInteger, String aMessage) {
        if (i != null) {
            int integer = i.toString().length();
            if (maxInteger < integer) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks the max integer digits of a given {@link long}.
     *
     * @param l          checked value
     * @param maxInteger max integer digits
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(long l, int maxInteger, String aMessage) {
        int integer = Long.valueOf(l).toString().length();
        if (maxInteger < integer) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks the max integer digits of a given {@link Long}.
     *
     * @param l          checked value
     * @param maxInteger compare value
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(Long l, int maxInteger, String aMessage) {
        if (l != null) {
            int integer = l.toString().length();
            if (maxInteger < integer) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks the max integer digits of a given {@link short}.
     *
     * @param s          checked value
     * @param maxInteger max integer digits
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(short s, int maxInteger, String aMessage) {
        int integer = Short.valueOf(s).toString().length();
        if (maxInteger < integer) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks the max integer digits of a given {@link Short}.
     *
     * @param s          checked value
     * @param maxInteger max integer digits
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(Short s, int maxInteger, String aMessage) {
        if (s != null) {
            int integer = s.toString().length();
            if (maxInteger < integer) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * Checks the max integer digits of a given {@link byte}.
     *
     * @param b          checked value
     * @param maxInteger max integer digits
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(byte b, int maxInteger, String aMessage) {
        int integer = Byte.valueOf(b).toString().length();
        if (maxInteger < integer) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks the max integer digits of a given {@link Byte}.
     *
     * @param b          checked value
     * @param maxInteger max integer digits
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void hasMaxDigitsInteger(Byte b, int maxInteger, String aMessage) {
        if (b != null) {
            int integer = b.toString().length();
            if (maxInteger < integer) {
                throw new DomainAssertionException(aMessage);
            }
        }
    }

    /**
     * returns the integer digits of a given {@link BigDecimal}.
     *
     * @param number input value
     */
    private static int integerDigits(Number number) {
        if (number == null) {
            return 0;
        }
        BigDecimal bigNum;
        if (number instanceof BigDecimal) {
            bigNum = (BigDecimal) number;
        } else {
            bigNum = new BigDecimal(number.toString()).stripTrailingZeros();
        }
        int integerPartLength = bigNum.precision() - bigNum.scale();
        return integerPartLength;
    }

    /**
     * returns the fraction digits of a given {@link BigDecimal}.
     *
     * @param number input value
     */
    private static int fractionDigits(Number number) {
        if (number == null) {
            return 0;
        }
        BigDecimal bigNum;
        if (number instanceof BigDecimal) {
            bigNum = (BigDecimal) number;
        } else {
            bigNum = new BigDecimal(number.toString()).stripTrailingZeros();
        }
        int fractionPartLength = bigNum.scale() < 0 ? 0 : bigNum.scale();
        return fractionPartLength;
    }

    /**
     * Checks, if a given {@link LocalDate} is in the future.
     *
     * @param aDate    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFuture(LocalDate aDate, String aMessage) {
        LocalDate compare = LocalDate.now();
        if (aDate != null && (aDate.isBefore(compare) || aDate.isEqual(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if a given {@link LocalTime} is in the future.
     *
     * @param aTime    checked value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFuture(LocalTime aTime, String aMessage) {
        LocalTime compare = LocalTime.now();
        if (aTime != null && (aTime.isBefore(compare) || aTime.equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }


    /**
     * Checks an Optional containing a temporal value
     * ({@link LocalDateTime}, {@link LocalDate}, {@link LocalTime},
     * {@link Date}, {@link Calendar},
     * {@link Instant}, {@link Year},
     * {@link ZonedDateTime}, {@link YearMonth},
     * {@link MonthDay}, {@link OffsetDateTime}, {@link OffsetTime}), if the contained value
     * is in the future.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsFuture(Optional valueOptional, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            Object o = valueOptional.get();
            if (o instanceof LocalDateTime) {
                isFuture((LocalDateTime) o, aMessage);
            } else if (o instanceof LocalDate) {
                isFuture((LocalDate) o, aMessage);
            } else if (o instanceof LocalTime) {
                isFuture((LocalTime) o, aMessage);
            } else if (o instanceof Date) {
                isFuture((Date) o, aMessage);
            } else if (o instanceof Calendar) {
                isFuture((Calendar) o, aMessage);
            } else if (o instanceof Instant) {
                isFuture((Instant) o, aMessage);
            } else if (o instanceof Year) {
                isFuture((Year) o, aMessage);
            } else if (o instanceof ZonedDateTime) {
                isFuture((ZonedDateTime) o, aMessage);
            } else if (o instanceof YearMonth) {
                isFuture((YearMonth) o, aMessage);
            } else if (o instanceof MonthDay) {
                isFuture((MonthDay) o, aMessage);
            } else if (o instanceof OffsetDateTime) {
                isFuture((OffsetDateTime) o, aMessage);
            } else if (o instanceof OffsetTime) {
                isFuture((OffsetTime) o, aMessage);
            } else {
                throw new IllegalArgumentException("valueOptional parameter must contain " +
                    "LocalDateTime, LocalDate, LocalTime, Date, Calendar, Year, ZonedDateTime, YearMonth, MonthDay, " +
                     "OffsetDateTime or OffsetTime. Other types are not supported!");
            }
        }
    }

    /**
     * Checks, if a given {@link LocalDate} is in the future or is the present date.
     *
     * @param aDate    checked value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFutureOrPresent(LocalDate aDate, String aMessage) {
        if (aDate != null && aDate.isBefore(LocalDate.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if a given {@link LocalTime} is in the future or is the present date.
     *
     * @param aTime    checked value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFutureOrPresent(LocalTime aTime, String aMessage) {
        if (aTime != null && aTime.isBefore(LocalTime.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a temporal value
     * ({@link LocalDateTime}, {@link LocalDate}, {@link LocalTime},
     * {@link Date}, {@link Calendar},
     * {@link Instant}, {@link Year},
     * {@link ZonedDateTime}, {@link YearMonth},
     * {@link MonthDay}, {@link OffsetDateTime}, {@link OffsetTime}), if the contained value
     * is in the future or is the present.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsFutureOrPresent(Optional valueOptional, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            Object o = valueOptional.get();
            if (o instanceof LocalDateTime) {
                isFutureOrPresent((LocalDateTime) o, aMessage);
            } else if (o instanceof LocalDate) {
                isFutureOrPresent((LocalDate) o, aMessage);
            } else if (o instanceof LocalTime) {
                isFutureOrPresent((LocalTime) o, aMessage);
            } else if (o instanceof Date) {
                isFutureOrPresent((Date) o, aMessage);
            } else if (o instanceof Calendar) {
                isFutureOrPresent((Calendar) o, aMessage);
            } else if (o instanceof Instant) {
                isFutureOrPresent((Instant) o, aMessage);
            } else if (o instanceof Year) {
                isFutureOrPresent((Year) o, aMessage);
            } else if (o instanceof ZonedDateTime) {
                isFutureOrPresent((ZonedDateTime) o, aMessage);
            } else if (o instanceof YearMonth) {
                isFutureOrPresent((YearMonth) o, aMessage);
            } else if (o instanceof MonthDay) {
                isFutureOrPresent((MonthDay) o, aMessage);
            } else if (o instanceof OffsetDateTime) {
                isFutureOrPresent((OffsetDateTime) o, aMessage);
            } else if (o instanceof OffsetTime) {
                isFutureOrPresent((OffsetTime) o, aMessage);
            } else {
                throw new IllegalArgumentException("valueOptional parameter must contain " +
                    "LocalDateTime, LocalDate, LocalTime, Date, Calendar, Year, ZonedDateTime, YearMonth, MonthDay, " +
                     "OffsetDateTime or OffsetTime. Other types are not supported!");
            }
        }
    }

    /**
     * Checks, if of a given {@link LocalDate} is in the past.
     *
     * @param aDate    checked value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPast(LocalDate aDate, String aMessage) {
        LocalDate compare = LocalDate.now();
        if (aDate != null && (aDate.isAfter(compare) || aDate.isEqual(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link LocalTime} is in the past.
     *
     * @param aTime    checked value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPast(LocalTime aTime, String aMessage) {
        LocalTime compare = LocalTime.now();
        if (aTime != null && (aTime.isAfter(compare) || aTime.equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a temporal value
     * ({@link LocalDateTime}, {@link LocalDate}, {@link LocalTime},
     * {@link Date}, {@link Calendar},
     * {@link Instant}, {@link Year},
     * {@link ZonedDateTime}, {@link YearMonth},
     * {@link MonthDay}, {@link OffsetDateTime}, {@link OffsetTime}), if the contained value
     * is in the past.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsPast(Optional valueOptional, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            Object o = valueOptional.get();
            if (o instanceof LocalDateTime) {
                isPast((LocalDateTime) o, aMessage);
            } else if (o instanceof LocalDate) {
                isPast((LocalDate) o, aMessage);
            } else if (o instanceof LocalTime) {
                isPast((LocalTime) o, aMessage);
            } else if (o instanceof Date) {
                isPast((Date) o, aMessage);
            } else if (o instanceof Calendar) {
                isPast((Calendar) o, aMessage);
            } else if (o instanceof Instant) {
                isPast((Instant) o, aMessage);
            } else if (o instanceof Year) {
                isPast((Year) o, aMessage);
            } else if (o instanceof ZonedDateTime) {
                isPast((ZonedDateTime) o, aMessage);
            } else if (o instanceof YearMonth) {
                isPast((YearMonth) o, aMessage);
            } else if (o instanceof MonthDay) {
                isPast((MonthDay) o, aMessage);
            } else if (o instanceof OffsetDateTime) {
                isPast((OffsetDateTime) o, aMessage);
            } else if (o instanceof OffsetTime) {
                isPast((OffsetTime) o, aMessage);
            } else {
                throw new IllegalArgumentException("valueOptional parameter must contain " +
                    "LocalDateTime, LocalDate, LocalTime, Date, Calendar, Year, ZonedDateTime, YearMonth, MonthDay, " +
                     "OffsetDateTime or OffsetTime. Other types are not supported!");
            }
        }
    }

    /**
     * Checks, if of a given {@link LocalDate} is in the past or is the present.
     *
     * @param aDate    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPastOrPresent(LocalDate aDate, String aMessage) {
        if (aDate != null && aDate.isAfter(LocalDate.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link LocalTime} is in the past or is the present.
     *
     * @param aTime    checked value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPastOrPresent(LocalTime aTime, String aMessage) {
        if (aTime != null && aTime.isAfter(LocalTime.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, an Optional containing a temporal value
     * ({@link LocalDateTime}, {@link LocalDate}, {@link LocalTime},
     * {@link Date}, {@link Calendar},
     * {@link Instant}, {@link Year},
     * {@link ZonedDateTime}, {@link YearMonth},
     * {@link MonthDay}, {@link OffsetDateTime}, {@link OffsetTime}), if the contained value
     * is in the past or is the present.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsPastOrPresent(Optional valueOptional, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            Object o = valueOptional.get();
            if (o instanceof LocalDateTime) {
                isPastOrPresent((LocalDateTime) o, aMessage);
            } else if (o instanceof LocalDate) {
                isPastOrPresent((LocalDate) o, aMessage);
            } else if (o instanceof LocalTime) {
                isPastOrPresent((LocalTime) o, aMessage);
            } else if (o instanceof Date) {
                isPastOrPresent((Date) o, aMessage);
            } else if (o instanceof Calendar) {
                isPastOrPresent((Calendar) o, aMessage);
            } else if (o instanceof Instant) {
                isPastOrPresent((Instant) o, aMessage);
            } else if (o instanceof Year) {
                isPastOrPresent((Year) o, aMessage);
            } else if (o instanceof ZonedDateTime) {
                isPastOrPresent((ZonedDateTime) o, aMessage);
            } else if (o instanceof YearMonth) {
                isPastOrPresent((YearMonth) o, aMessage);
            } else if (o instanceof MonthDay) {
                isPastOrPresent((MonthDay) o, aMessage);
            } else if (o instanceof OffsetDateTime) {
                isPastOrPresent((OffsetDateTime) o, aMessage);
            } else if (o instanceof OffsetTime) {
                isPastOrPresent((OffsetTime) o, aMessage);
            } else {
                throw new IllegalArgumentException("valueOptional parameter must contain " +
                    "LocalDateTime, LocalDate, LocalTime, Date, Calendar, Year, ZonedDateTime, YearMonth, MonthDay, " +
                     "OffsetDateTime or OffsetTime. Other types are not supported!");
            }
        }
    }


    /**
     * Checks if {@code date1} is before {@code date2}.
     *
     * @param date1    checked value
     * @param date2    compared value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBefore(LocalDate date1, LocalDate date2, String aMessage) {
        if (date1 != null && date2 != null && !date1.isBefore(date2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code time1} is before {@code time2}.
     *
     * @param time1    checked value
     * @param time2    compared value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBefore(LocalTime time1, LocalTime time2, String aMessage) {
        if (time1 != null && time2 != null && !time1.isBefore(time2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link LocalDate}, if the contained value is before {@code date2}.
     *
     * @param date1Optional checked value
     * @param date2         compared value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBefore(Optional<LocalDate> date1Optional, LocalDate date2, String aMessage) {
        if (date1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (date1Optional.isPresent()) {
            isBefore(date1Optional.get(), date2, aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link LocalTime}, if the contained value is before {@code time2}.
     *
     * @param time1Optional checked value
     * @param time2         compared value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBefore(Optional<LocalTime> time1Optional, LocalTime time2, String aMessage) {
        if (time1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (time1Optional.isPresent()) {
            isBefore(time1Optional.get(), time2, aMessage);
        }
    }

    /**
     * Checks if {@code date1} is after {@code date2}.
     *
     * @param date1    checked value
     * @param date2    compared value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfter(LocalDate date1, LocalDate date2, String aMessage) {
        if (date1 != null && date2 != null && !date1.isAfter(date2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code time1} is after {@code time2}.
     *
     * @param time1    checked value
     * @param time2    compared value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfter(LocalTime time1, LocalTime time2, String aMessage) {
        if (time1 != null && time2 != null && !time1.isAfter(time2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link LocalDate}, if the contained value is after {@code date2}.
     *
     * @param date1Optional checked value
     * @param date2         compared value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfter(Optional<LocalDate> date1Optional, LocalDate date2, String aMessage) {
        if (date1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (date1Optional.isPresent()) {
            isAfter(date1Optional.get(), date2, aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link LocalTime}, if the contained value is after {@code time2}.
     *
     * @param time1Optional checked value
     * @param time2         compared value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfter(Optional<LocalTime> time1Optional, LocalTime time2, String aMessage) {
        if (time1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (time1Optional.isPresent()) {
            isAfter(time1Optional.get(), time2, aMessage);
        }
    }

    /**
     * Checks if {@code date1} is before or equal to{@code date2}.
     *
     * @param date1    checked value
     * @param date2    compared value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBeforeOrEqualTo(LocalDate date1, LocalDate date2, String aMessage) {
        if (date1 != null && date2 != null && date1.isAfter(date2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code time1} is before or equal to{@code time2}.
     *
     * @param time1    checked value
     * @param time2    compared value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBeforeOrEqualTo(LocalTime time1, LocalTime time2, String aMessage) {
        if (time1 != null && time2 != null && time1.isAfter(time2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link LocalDate}, if the contained value is before or equal to {@code date2}.
     *
     * @param date1Optional checked value
     * @param date2         compared value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBeforeOrEqualTo(Optional<LocalDate> date1Optional, LocalDate date2, String aMessage) {
        if (date1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (date1Optional.isPresent()) {
            isBeforeOrEqualTo(date1Optional.get(), date2, aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link LocalTime}, if the contained value is before or equal to {@code time2}.
     *
     * @param time1Optional checked value
     * @param time2         compared value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBeforeOrEqualTo(Optional<LocalTime> time1Optional, LocalTime time2, String aMessage) {
        if (time1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (time1Optional.isPresent()) {
            isBeforeOrEqualTo(time1Optional.get(), time2, aMessage);
        }
    }

    /**
     * Checks if {@code date1} is after or equal to{@code date2}.
     *
     * @param date1    checked value
     * @param date2    compared value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfterOrEqualTo(LocalDate date1, LocalDate date2, String aMessage) {
        if (date1 != null && date2 != null && date1.isBefore(date2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code time1} is after or equal to{@code time2}.
     *
     * @param time1    checked value
     * @param time2    compared value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfterOrEqualTo(LocalTime time1, LocalTime time2, String aMessage) {
        if (time1 != null && time2 != null && time1.isBefore(time2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link LocalDate}, if the contained value is before or equal to {@code date2}.
     *
     * @param date1Optional checked value
     * @param date2         compared value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfterOrEqualTo(Optional<LocalDate> date1Optional, LocalDate date2, String aMessage) {
        if (date1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (date1Optional.isPresent()) {
            isAfterOrEqualTo(date1Optional.get(), date2, aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link LocalTime}, if the contained value is before or equal to {@code time2}.
     *
     * @param time1Optional checked value
     * @param time2         compared value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfterOrEqualTo(Optional<LocalTime> time1Optional, LocalTime time2, String aMessage) {
        if (time1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (time1Optional.isPresent()) {
            isAfterOrEqualTo(time1Optional.get(), time2, aMessage);
        }
    }

    /**
     * Checks, if of a given {@link LocalDateTime} is in the future.
     *
     * @param aDateTime checked value
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFuture(LocalDateTime aDateTime, String aMessage) {
        LocalDateTime compare = LocalDateTime.now();
        if (aDateTime != null && (aDateTime.isBefore(compare) || aDateTime.isEqual(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link LocalDateTime} is in the future or is the present.
     *
     * @param aDateTime checked value
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFutureOrPresent(LocalDateTime aDateTime, String aMessage) {
        if (aDateTime != null && aDateTime.isBefore(LocalDateTime.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link LocalDateTime} is in the past.
     *
     * @param aDateTime checked value
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPast(LocalDateTime aDateTime, String aMessage) {
        LocalDateTime compare = LocalDateTime.now();
        if (aDateTime != null && (aDateTime.isAfter(compare) || aDateTime.isEqual(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link LocalDateTime} is in the past or is the present.
     *
     * @param aDateTime checked value
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPastOrPresent(LocalDateTime aDateTime, String aMessage) {
        if (aDateTime != null && aDateTime.isAfter(LocalDateTime.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code date1} is before to{@code date2}.
     *
     * @param date1    checked
     * @param date2    compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBefore(LocalDateTime date1, LocalDateTime date2, String aMessage) {
        if (date1 != null && date2 != null && !date1.isBefore(date2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link LocalDateTime}, if the contained value is before {@code date2}.
     *
     * @param date1Optional checked
     * @param date2         compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBefore(Optional<LocalDateTime> date1Optional, LocalDateTime date2, String aMessage) {
        if (date1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (date1Optional.isPresent()) {
            isBefore(date1Optional.get(), date2, aMessage);
        }
    }

    /**
     * Checks if {@code date1} is after to{@code date2}.
     *
     * @param date1    checked
     * @param date2    compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfter(LocalDateTime date1, LocalDateTime date2, String aMessage) {
        if (date1 != null && date2 != null && !date1.isAfter(date2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link LocalDateTime}, if the contained value is after {@code date2}.
     *
     * @param date1Optional checked
     * @param date2         compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfter(Optional<LocalDateTime> date1Optional, LocalDateTime date2, String aMessage) {
        if (date1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (date1Optional.isPresent()) {
            isAfter(date1Optional.get(), date2, aMessage);
        }
    }

    /**
     * Checks if {@code date1} is before or equal to to{@code date2}.
     *
     * @param date1    checked
     * @param date2    compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBeforeOrEqualTo(LocalDateTime date1, LocalDateTime date2, String aMessage) {
        if (date1 != null && date2 != null && date1.isAfter(date2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link LocalDateTime}, if the contained value is before or equal to {@code
      * date2}.
     *
     * @param date1Optional checked
     * @param date2         compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBeforeOrEqualTo(Optional<LocalDateTime> date1Optional, LocalDateTime date2,
 String aMessage) {
        if (date1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (date1Optional.isPresent()) {
            isBeforeOrEqualTo(date1Optional.get(), date2, aMessage);
        }
    }

    /**
     * Checks if {@code date1} is after or equal to to{@code date2}.
     *
     * @param date1    checked
     * @param date2    compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfterOrEqualTo(LocalDateTime date1, LocalDateTime date2, String aMessage) {
        if (date1 != null && date2 != null && date1.isBefore(date2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if an Optional containing a {@link LocalDateTime} is after or equal to {@code date2}.
     *
     * @param date1Optional checked
     * @param date2         compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfterOrEqualTo(Optional<LocalDateTime> date1Optional, LocalDateTime date2,
     String aMessage) {
        if (date1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (date1Optional.isPresent()) {
            isAfterOrEqualTo(date1Optional.get(), date2, aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Instant} is in the future.
     *
     * @param anInstant checked
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFuture(Instant anInstant, String aMessage) {
        Instant compare = Instant.now();
        if (anInstant != null && (anInstant.isBefore(compare) || anInstant.equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Instant} is in the future or is the present.
     *
     * @param anInstant checked
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFutureOrPresent(Instant anInstant, String aMessage) {
        if (anInstant != null && anInstant.isBefore(Instant.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Instant} is in the past.
     *
     * @param anInstant checked
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPast(Instant anInstant, String aMessage) {
        Instant compare = Instant.now();
        if (anInstant != null && (anInstant.isAfter(compare) || anInstant.equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Instant} is in the past or is the present.
     *
     * @param anInstant checked
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPastOrPresent(Instant anInstant, String aMessage) {
        if (anInstant != null && anInstant.isAfter(Instant.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code instant1} is before {@code instant2}.
     *
     * @param instant1 checked
     * @param instant2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBefore(Instant instant1, Instant instant2, String aMessage) {
        if (instant1 != null && instant2 != null && !instant1.isBefore(instant2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Instant}, if the contained value is before {@code instant2}.
     *
     * @param instant1Optional checked
     * @param instant2         compare value
     * @param aMessage         message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBefore(Optional<Instant> instant1Optional, Instant instant2, String aMessage) {
        if (instant1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (instant1Optional.isPresent()) {
            isBefore(instant1Optional.get(), instant2, aMessage);
        }
    }

    /**
     * Checks if {@code instant1} is after {@code instant2}.
     *
     * @param instant1 checked
     * @param instant2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfter(Instant instant1, Instant instant2, String aMessage) {
        if (instant1 != null && instant2 != null && !instant1.isAfter(instant2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Instant}, if the contained value is after {@code instant2}.
     *
     * @param instant1Optional checked
     * @param instant2         compare value
     * @param aMessage         message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfter(Optional<Instant> instant1Optional, Instant instant2, String aMessage) {
        if (instant1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (instant1Optional.isPresent()) {
            isAfter(instant1Optional.get(), instant2, aMessage);
        }
    }

    /**
     * Checks if {@code instant1} is before or equal to {@code instant2}.
     *
     * @param instant1 checked
     * @param instant2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBeforeOrEqualTo(Instant instant1, Instant instant2, String aMessage) {
        if (instant1 != null && instant2 != null && instant1.isAfter(instant2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Instant}, if the contained value is before or equal to {@code instant2}.
     *
     * @param instant1Optional checked
     * @param instant2         compare value
     * @param aMessage         message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBeforeOrEqualTo(Optional<Instant> instant1Optional, Instant instant2,
 String aMessage) {
        if (instant1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (instant1Optional.isPresent()) {
            isBeforeOrEqualTo(instant1Optional.get(), instant2, aMessage);
        }
    }

    /**
     * Checks if {@code instant1} is after or equal to {@code instant2}.
     *
     * @param instant1 checked
     * @param instant2 compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfterOrEqualTo(Instant instant1, Instant instant2, String aMessage) {
        if (instant1 != null && instant2 != null && instant1.isBefore(instant2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Instant}, if the contained value is after or equal to {@code instant2}.
     *
     * @param instant1Optional checked
     * @param instant2         compare value
     * @param aMessage         message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfterOrEqualTo(Optional<Instant> instant1Optional, Instant instant2, String aMessage) {
        if (instant1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (instant1Optional.isPresent()) {
            isAfterOrEqualTo(instant1Optional.get(), instant2, aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Date} is in the future.
     *
     * @param aDate    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFuture(Date aDate, String aMessage) {
        Instant compare = Instant.now();
        if (aDate != null && (aDate.toInstant().isBefore(compare) || aDate.toInstant().equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Date} is in the future or is the present.
     *
     * @param aDate    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFutureOrPresent(Date aDate, String aMessage) {
        if (aDate != null && aDate.toInstant().isBefore(Instant.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Date} is in the past.
     *
     * @param aDate    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPast(Date aDate, String aMessage) {
        Instant compare = Instant.now();
        if (aDate != null && (aDate.toInstant().isAfter(compare) || aDate.toInstant().equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Date} is in the past or is the present.
     *
     * @param aDate    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPastOrPresent(Date aDate, String aMessage) {
        if (aDate != null && aDate.toInstant().isAfter(Instant.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code date1} is before {@code date2}.
     *
     * @param date1    checked
     * @param date2    compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBefore(Date date1, Date date2, String aMessage) {
        if (date1 != null && date2 != null && !date1.toInstant().isBefore(date2.toInstant())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Date}, if the contained value is before {@code date2}.
     *
     * @param date1Optional checked
     * @param date2         compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBefore(Optional<Date> date1Optional, Date date2, String aMessage) {
        if (date1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (date1Optional.isPresent()) {
            isBefore(date1Optional.get(), date2, aMessage);
        }
    }

    /**
     * Checks if {@code date1} is after {@code date2}.
     *
     * @param date1    checked
     * @param date2    compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfter(Date date1, Date date2, String aMessage) {
        if (date1 != null && date2 != null && !date1.toInstant().isAfter(date2.toInstant())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Date}, if the contained value is after {@code date2}.
     *
     * @param date1Optional checked
     * @param date2         compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfter(Optional<Date> date1Optional, Date date2, String aMessage) {
        if (date1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (date1Optional.isPresent()) {
            isAfter(date1Optional.get(), date2, aMessage);
        }
    }

    /**
     * Checks if {@code date1} is before or equal to {@code date2}.
     *
     * @param date1    checked
     * @param date2    compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBeforeOrEqualTo(Date date1, Date date2, String aMessage) {
        if (date1 != null && date2 != null && date1.toInstant().isAfter(date2.toInstant())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if Optional containing an {@link Date}, if the contained value is before or equal to {@code date2}.
     *
     * @param date1Optional checked
     * @param date2         compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBeforeOrEqualTo(Optional<Date> date1Optional, Date date2, String aMessage) {
        if (date1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (date1Optional.isPresent()) {
            isBeforeOrEqualTo(date1Optional.get(), date2, aMessage);
        }
    }

    /**
     * Checks if {@code date1} is after or equal to {@code date2}.
     *
     * @param date1    checked
     * @param date2    compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfterOrEqualTo(Date date1, Date date2, String aMessage) {
        if (date1 != null && date2 != null && date1.toInstant().isBefore(date2.toInstant())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Date}, if the contained value is after or equal to {@code date2}.
     *
     * @param date1Optional checked
     * @param date2         compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfterOrEqualTo(Optional<Date> date1Optional, Date date2, String aMessage) {
        if (date1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (date1Optional.isPresent()) {
            isAfterOrEqualTo(date1Optional.get(), date2, aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Calendar} is in the future.
     *
     * @param aCal     checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFuture(Calendar aCal, String aMessage) {
        Instant compare = Instant.now();
        if (aCal != null && (aCal.toInstant().isBefore(compare) || aCal.toInstant().equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Calendar} is in the future or is the present.
     *
     * @param aCal     checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFutureOrPresent(Calendar aCal, String aMessage) {
        if (aCal != null && aCal.toInstant().isBefore(Instant.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Calendar} is in the past.
     *
     * @param aCal     checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPast(Calendar aCal, String aMessage) {
        Instant compare = Instant.now();
        if (aCal != null && (aCal.toInstant().isAfter(compare) || aCal.toInstant().equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Calendar} is in the past or is the present.
     *
     * @param aCal     checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPastOrPresent(Calendar aCal, String aMessage) {
        if (aCal != null && aCal.toInstant().isAfter(Instant.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code cal1} is before {@code cal2}.
     *
     * @param cal1     checked
     * @param cal2     compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBefore(Calendar cal1, Calendar cal2, String aMessage) {
        if (cal1 != null && cal2 != null && !cal1.toInstant().isBefore(cal2.toInstant())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Calendar}, if the contained value is before {@code cal2}.
     *
     * @param cal1Optional checked
     * @param cal2         compare value
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBefore(Optional<Calendar> cal1Optional, Calendar cal2, String aMessage) {
        if (cal1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (cal1Optional.isPresent()) {
            isBefore(cal1Optional.get(), cal2, aMessage);
        }
    }

    /**
     * Checks if {@code cal1} is after {@code cal2}.
     *
     * @param cal1     checked
     * @param cal2     compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfter(Calendar cal1, Calendar cal2, String aMessage) {
        if (cal1 != null && cal2 != null && !cal1.toInstant().isAfter(cal2.toInstant())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Calendar}, if the contained value is after {@code cal2}.
     *
     * @param cal1Optional checked
     * @param cal2         compare value
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfter(Optional<Calendar> cal1Optional, Calendar cal2, String aMessage) {
        if (cal1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (cal1Optional.isPresent()) {
            isAfter(cal1Optional.get(), cal2, aMessage);
        }
    }

    /**
     * Checks if {@code cal1} is before or equal to {@code cal2}.
     *
     * @param cal1     checked
     * @param cal2     compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBeforeOrEqualTo(Calendar cal1, Calendar cal2, String aMessage) {
        if (cal1 != null && cal2 != null && cal1.toInstant().isAfter(cal2.toInstant())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Calendar}, if the contained value is before or equal to {@code cal2}.
     *
     * @param cal1Optional checked
     * @param cal2         compare value
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBeforeOrEqualTo(Optional<Calendar> cal1Optional, Calendar cal2, String aMessage) {
        if (cal1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (cal1Optional.isPresent()) {
            isBeforeOrEqualTo(cal1Optional.get(), cal2, aMessage);
        }
    }

    /**
     * Checks if {@code cal1} is after or equal to {@code cal2}.
     *
     * @param cal1     checked
     * @param cal2     compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfterOrEqualTo(Calendar cal1, Calendar cal2, String aMessage) {
        if (cal1 != null && cal2 != null && cal1.toInstant().isBefore(cal2.toInstant())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Calendar}, if the contained value is after or equal to {@code cal2}.
     *
     * @param cal1Optional checked
     * @param cal2         compare value
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfterOrEqualTo(Optional<Calendar> cal1Optional, Calendar cal2, String aMessage) {
        if (cal1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (cal1Optional.isPresent()) {
            isAfterOrEqualTo(cal1Optional.get(), cal2, aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Year} is in the future.
     *
     * @param aYear    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFuture(Year aYear, String aMessage) {
        Year compare = Year.now();
        if (aYear != null && (aYear.isBefore(compare) || aYear.equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Year} is in the future and is not the present year.
     *
     * @param aYear    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFutureOrPresent(Year aYear, String aMessage) {
        if (aYear != null && aYear.isBefore(Year.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Year} is in the past.
     *
     * @param aYear    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPast(Year aYear, String aMessage) {
        Year compare = Year.now();
        if (aYear != null && (aYear.isAfter(compare) || aYear.equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link Year} is in the past and is not the present year.
     *
     * @param aYear    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPastOrPresent(Year aYear, String aMessage) {
        if (aYear != null && aYear.isAfter(Year.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code year1} is before {@code year2}.
     *
     * @param year1    checked
     * @param year2    compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBefore(Year year1, Year year2, String aMessage) {
        if (year1 != null && year2 != null && !year1.isBefore(year2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Year}, if the contained value is before {@code year2}.
     *
     * @param year1Optional checked
     * @param year2         compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBefore(Optional<Year> year1Optional, Year year2, String aMessage) {
        if (year1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (year1Optional.isPresent()) {
            isBefore(year1Optional.get(), year2, aMessage);
        }
    }

    /**
     * Checks if {@code year1} is after {@code year2}.
     *
     * @param year1    checked
     * @param year2    compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfter(Year year1, Year year2, String aMessage) {
        if (year1 != null && year2 != null && !year1.isAfter(year2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Year}, if the contained value is after {@code year2}.
     *
     * @param year1Optional checked
     * @param year2         compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfter(Optional<Year> year1Optional, Year year2, String aMessage) {
        if (year1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (year1Optional.isPresent()) {
            isAfter(year1Optional.get(), year2, aMessage);
        }
    }

    /**
     * Checks if {@code year1} is before or equal to {@code year2}.
     *
     * @param year1    checked
     * @param year2    compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBeforeOrEqualTo(Year year1, Year year2, String aMessage) {
        if (year1 != null && year2 != null && year1.isAfter(year2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Year}, if the contained value is before or equal to {@code year2}.
     *
     * @param year1Optional checked
     * @param year2         compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBeforeOrEqualTo(Optional<Year> year1Optional, Year year2, String aMessage) {
        if (year1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (year1Optional.isPresent()) {
            isBeforeOrEqualTo(year1Optional.get(), year2, aMessage);
        }
    }

    /**
     * Checks if {@code year1} is after or equal to {@code year2}.
     *
     * @param year1    checked
     * @param year2    compare value
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfterOrEqualTo(Year year1, Year year2, String aMessage) {
        if (year1 != null && year2 != null && year1.isBefore(year2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Year}, if the contained value is after or equal to {@code year2}.
     *
     * @param year1Optional checked
     * @param year2         compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfterOrEqualTo(Optional<Year> year1Optional, Year year2, String aMessage) {
        if (year1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (year1Optional.isPresent()) {
            isAfterOrEqualTo(year1Optional.get(), year2, aMessage);
        }
    }

    /**
     * Checks, if of a given {@link YearMonth} is in the future.
     *
     * @param aYearMonth checked
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFuture(YearMonth aYearMonth, String aMessage) {
        YearMonth compare = YearMonth.now();
        if (aYearMonth != null && (aYearMonth.isBefore(compare) || aYearMonth.equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link YearMonth} is in the future or is the present.
     *
     * @param aYearMonth checked
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFutureOrPresent(YearMonth aYearMonth, String aMessage) {
        if (aYearMonth != null && aYearMonth.isBefore(YearMonth.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link YearMonth} is in the past.
     *
     * @param aYearMonth checked
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPast(YearMonth aYearMonth, String aMessage) {
        YearMonth compare = YearMonth.now();
        if (aYearMonth != null && (aYearMonth.isAfter(compare) || aYearMonth.equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link YearMonth} is in the past or is the present.
     *
     * @param aYearMonth checked
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPastOrPresent(YearMonth aYearMonth, String aMessage) {
        if (aYearMonth != null && aYearMonth.isAfter(YearMonth.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code yearMonth1} is before {@code yearMonth2}.
     *
     * @param yearMonth1 checked
     * @param yearMonth2 compare value
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBefore(YearMonth yearMonth1, YearMonth yearMonth2, String aMessage) {
        if (yearMonth1 != null && yearMonth2 != null && !yearMonth1.isBefore(yearMonth2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link YearMonth}, if the contained value is before {@code yearMonth2}.
     *
     * @param yearMonth1Optional checked
     * @param yearMonth2         compare value
     * @param aMessage           message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBefore(Optional<YearMonth> yearMonth1Optional, YearMonth yearMonth2, String aMessage) {
        if (yearMonth1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (yearMonth1Optional.isPresent()) {
            isBefore(yearMonth1Optional.get(), yearMonth2, aMessage);
        }
    }

    /**
     * Checks if {@code yearMonth1} is after {@code yearMonth2}.
     *
     * @param yearMonth1 checked
     * @param yearMonth2 compare value
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfter(YearMonth yearMonth1, YearMonth yearMonth2, String aMessage) {
        if (yearMonth1 != null && yearMonth2 != null && !yearMonth1.isAfter(yearMonth2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link YearMonth}, if the contained value is after {@code yearMonth2}.
     *
     * @param yearMonth1Optional checked
     * @param yearMonth2         compare value
     * @param aMessage           message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfter(Optional<YearMonth> yearMonth1Optional, YearMonth yearMonth2, String aMessage) {
        if (yearMonth1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (yearMonth1Optional.isPresent()) {
            isAfter(yearMonth1Optional.get(), yearMonth2, aMessage);
        }
    }

    /**
     * Checks if {@code yearMonth1} is before or equal to {@code yearMonth2}.
     *
     * @param yearMonth1 checked
     * @param yearMonth2 compare value
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBeforeOrEqualTo(YearMonth yearMonth1, YearMonth yearMonth2, String aMessage) {
        if (yearMonth1 != null && yearMonth2 != null && yearMonth1.isAfter(yearMonth2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link YearMonth}, if the contained value is before or equal to {@code
      * yearMonth2}.
     *
     * @param yearMonth1Optional checked
     * @param yearMonth2         compare value
     * @param aMessage           message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBeforeOrEqualTo(Optional<YearMonth> yearMonth1Optional, YearMonth yearMonth2,
     String aMessage) {
        if (yearMonth1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (yearMonth1Optional.isPresent()) {
            isBeforeOrEqualTo(yearMonth1Optional.get(), yearMonth2, aMessage);
        }
    }

    /**
     * Checks if {@code yearMonth1} is after or equal to {@code yearMonth2}.
     *
     * @param yearMonth1 checked
     * @param yearMonth2 compare value
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfterOrEqualTo(YearMonth yearMonth1, YearMonth yearMonth2, String aMessage) {
        if (yearMonth1 != null && yearMonth2 != null && yearMonth1.isBefore(yearMonth2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link YearMonth}, if the contained value is after or equal to {@code
      * yearMonth2}.
     *
     * @param yearMonth1Optional checked
     * @param yearMonth2         compare value
     * @param aMessage           message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfterOrEqualTo(Optional<YearMonth> yearMonth1Optional, YearMonth yearMonth2,
     String aMessage) {
        if (yearMonth1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (yearMonth1Optional.isPresent()) {
            isAfterOrEqualTo(yearMonth1Optional.get(), yearMonth2, aMessage);
        }
    }

    /**
     * Checks, if of a given {@link ZonedDateTime} is in the future.
     *
     * @param aZonedDateTime checked
     * @param aMessage       message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFuture(ZonedDateTime aZonedDateTime, String aMessage) {
        ZonedDateTime compare = ZonedDateTime.now();
        if (aZonedDateTime != null && (aZonedDateTime.isBefore(compare) || aZonedDateTime.equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link ZonedDateTime} is in the future or is the present.
     *
     * @param aZonedDateTime checked
     * @param aMessage       message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFutureOrPresent(ZonedDateTime aZonedDateTime, String aMessage) {
        if (aZonedDateTime != null && aZonedDateTime.isBefore(ZonedDateTime.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link ZonedDateTime} is in the past.
     *
     * @param aZonedDateTime checked
     * @param aMessage       message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPast(ZonedDateTime aZonedDateTime, String aMessage) {
        ZonedDateTime compare = ZonedDateTime.now();
        if (aZonedDateTime != null && (aZonedDateTime.isAfter(compare) || aZonedDateTime.equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link ZonedDateTime} is in the past or is the present.
     *
     * @param aZonedDateTime checked
     * @param aMessage       message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPastOrPresent(ZonedDateTime aZonedDateTime, String aMessage) {
        if (aZonedDateTime != null && aZonedDateTime.isAfter(ZonedDateTime.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code aZonedDateTime1} is before {@code aZonedDateTime2}.
     *
     * @param aZonedDateTime1 checked
     * @param aZonedDateTime2 compare value
     * @param aMessage        message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBefore(ZonedDateTime aZonedDateTime1, ZonedDateTime aZonedDateTime2, String aMessage) {
        if (aZonedDateTime1 != null && aZonedDateTime2 != null && !aZonedDateTime1.isBefore(aZonedDateTime2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link ZonedDateTime}, if the contained value is before {@code aZonedDateTime2}.
     *
     * @param aZonedDateTime1Optional checked
     * @param aZonedDateTime2         compare value
     * @param aMessage                message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBefore(Optional<ZonedDateTime> aZonedDateTime1Optional,
     ZonedDateTime aZonedDateTime2, String aMessage) {
        if (aZonedDateTime1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aZonedDateTime1Optional.isPresent()) {
            isBefore(aZonedDateTime1Optional.get(), aZonedDateTime2, aMessage);
        }
    }

    /**
     * Checks if {@code aZonedDateTime1} is after {@code aZonedDateTime2}.
     *
     * @param aZonedDateTime1 checked
     * @param aZonedDateTime2 compare value
     * @param aMessage        message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfter(ZonedDateTime aZonedDateTime1, ZonedDateTime aZonedDateTime2, String aMessage) {
        if (aZonedDateTime1 != null && aZonedDateTime2 != null && !aZonedDateTime1.isAfter(aZonedDateTime2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link ZonedDateTime}, if the contained value is after {@code aZonedDateTime2}.
     *
     * @param aZonedDateTime1Optional checked
     * @param aZonedDateTime2         compare value
     * @param aMessage                message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfter(Optional<ZonedDateTime> aZonedDateTime1Optional, ZonedDateTime aZonedDateTime2
, String aMessage) {
        if (aZonedDateTime1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aZonedDateTime1Optional.isPresent()) {
            isAfter(aZonedDateTime1Optional.get(), aZonedDateTime2, aMessage);
        }
    }

    /**
     * Checks if {@code aZonedDateTime1} is before or equal to {@code aZonedDateTime2}.
     *
     * @param aZonedDateTime1 checked
     * @param aZonedDateTime2 compare value
     * @param aMessage        message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBeforeOrEqualTo(ZonedDateTime aZonedDateTime1, ZonedDateTime aZonedDateTime2,
     String aMessage) {
        if (aZonedDateTime1 != null && aZonedDateTime2 != null && aZonedDateTime1.isAfter(aZonedDateTime2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks Optional containing an {@link ZonedDateTime}, if the contained value is before or equal to {@code
      * aZonedDateTime2}.
     *
     * @param aZonedDateTime1Optional checked
     * @param aZonedDateTime2         compare value
     * @param aMessage                message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBeforeOrEqualTo(Optional<ZonedDateTime> aZonedDateTime1Optional,
     ZonedDateTime aZonedDateTime2, String aMessage) {
        if (aZonedDateTime1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aZonedDateTime1Optional.isPresent()) {
            isBeforeOrEqualTo(aZonedDateTime1Optional.get(), aZonedDateTime2, aMessage);
        }
    }

    /**
     * Checks if {@code aZonedDateTime1} is after or equal to {@code aZonedDateTime2}.
     *
     * @param aZonedDateTime1 checked
     * @param aZonedDateTime2 compare value
     * @param aMessage        message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfterOrEqualTo(ZonedDateTime aZonedDateTime1, ZonedDateTime aZonedDateTime2, String aMessage) {
        if (aZonedDateTime1 != null && aZonedDateTime2 != null && aZonedDateTime1.isBefore(aZonedDateTime2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link ZonedDateTime}, if the contained value is after or equal to {@code
      * aZonedDateTime2}.
     *
     * @param aZonedDateTime1Optional checked
     * @param aZonedDateTime2         compare value
     * @param aMessage                message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfterOrEqualTo(Optional<ZonedDateTime> aZonedDateTime1Optional,
     ZonedDateTime aZonedDateTime2, String aMessage) {
        if (aZonedDateTime1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aZonedDateTime1Optional.isPresent()) {
            isAfterOrEqualTo(aZonedDateTime1Optional.get(), aZonedDateTime2, aMessage);
        }
    }

    /**
     * Checks, if of a given {@link MonthDay} is in the future.
     *
     * @param aMonthDay checked
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFuture(MonthDay aMonthDay, String aMessage) {
        MonthDay compare = MonthDay.now();
        if (aMonthDay != null && (aMonthDay.isBefore(compare) || aMonthDay.equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link MonthDay} is in the future or is the present.
     *
     * @param aMonthDay checked
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFutureOrPresent(MonthDay aMonthDay, String aMessage) {
        if (aMonthDay != null && aMonthDay.isBefore(MonthDay.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link MonthDay} is in the past.
     *
     * @param aMonthDay checked
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPast(MonthDay aMonthDay, String aMessage) {
        MonthDay compare = MonthDay.now();
        if (aMonthDay != null && (aMonthDay.isAfter(compare) || aMonthDay.equals(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link MonthDay} is in the past or is the present.
     *
     * @param aMonthDay checked
     * @param aMessage  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPastOrPresent(MonthDay aMonthDay, String aMessage) {
        if (aMonthDay != null && aMonthDay.isAfter(MonthDay.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code aMonthDay1} is before {@code aMonthDay2}.
     *
     * @param aMonthDay1 checked
     * @param aMonthDay2 compare value
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBefore(MonthDay aMonthDay1, MonthDay aMonthDay2, String aMessage) {
        if (aMonthDay1 != null && aMonthDay2 != null && !aMonthDay1.isBefore(aMonthDay2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link MonthDay}, if the contained value is before {@code aMonthDay2}.
     *
     * @param aMonthDay1Optional checked
     * @param aMonthDay2         compare value
     * @param aMessage           message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBefore(Optional<MonthDay> aMonthDay1Optional, MonthDay aMonthDay2, String aMessage) {
        if (aMonthDay1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aMonthDay1Optional.isPresent()) {
            isBefore(aMonthDay1Optional.get(), aMonthDay2, aMessage);
        }
    }

    /**
     * Checks if {@code aMonthDay1} is after {@code aMonthDay2}.
     *
     * @param aMonthDay1 checked
     * @param aMonthDay2 compare value
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfter(MonthDay aMonthDay1, MonthDay aMonthDay2, String aMessage) {
        if (aMonthDay1 != null && aMonthDay2 != null && !aMonthDay1.isAfter(aMonthDay2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link MonthDay}, if the contained value is after {@code aMonthDay2}.
     *
     * @param aMonthDay1Optional checked
     * @param aMonthDay2         compare value
     * @param aMessage           message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfter(Optional<MonthDay> aMonthDay1Optional, MonthDay aMonthDay2, String aMessage) {
        if (aMonthDay1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aMonthDay1Optional.isPresent()) {
            isAfter(aMonthDay1Optional.get(), aMonthDay2, aMessage);
        }
    }

    /**
     * Checks if {@code aMonthDay1} is before or equal to {@code aMonthDay2}.
     *
     * @param aMonthDay1 checked
     * @param aMonthDay2 compare value
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBeforeOrEqualTo(MonthDay aMonthDay1, MonthDay aMonthDay2, String aMessage) {
        if (aMonthDay1 != null && aMonthDay2 != null && aMonthDay1.isAfter(aMonthDay2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link MonthDay}, if the contained value is before or equal to {@code
      * aMonthDay2}.
     *
     * @param aMonthDay1Optional checked
     * @param aMonthDay2         compare value
     * @param aMessage           message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBeforeOrEqualTo(Optional<MonthDay> aMonthDay1Optional, MonthDay aMonthDay2,
     String aMessage) {
        if (aMonthDay1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aMonthDay1Optional.isPresent()) {
            isBeforeOrEqualTo(aMonthDay1Optional.get(), aMonthDay2, aMessage);
        }
    }

    /**
     * Checks if {@code aMonthDay1} is after or equal to {@code aMonthDay2}.
     *
     * @param aMonthDay1 checked
     * @param aMonthDay2 compare value
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfterOrEqualTo(MonthDay aMonthDay1, MonthDay aMonthDay2, String aMessage) {
        if (aMonthDay1 != null && aMonthDay2 != null && aMonthDay1.isBefore(aMonthDay2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link MonthDay}, if the contained value is after or equal to {@code
      * aMonthDay2}.
     *
     * @param aMonthDay1Optional checked
     * @param aMonthDay2         compare value
     * @param aMessage           message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfterOrEqualTo(Optional<MonthDay> aMonthDay1Optional, MonthDay aMonthDay2,
     String aMessage) {
        if (aMonthDay1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aMonthDay1Optional.isPresent()) {
            isAfterOrEqualTo(aMonthDay1Optional.get(), aMonthDay2, aMessage);
        }
    }

    /**
     * Checks, if of a given {@link OffsetTime} is in the future.
     *
     * @param anOffsetTime checked
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFuture(OffsetTime anOffsetTime, String aMessage) {
        OffsetTime compare = OffsetTime.now();
        if (anOffsetTime != null && (anOffsetTime.isBefore(compare) || anOffsetTime.isEqual(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link OffsetTime} is in the future or is the present.
     *
     * @param anOffsetTime checked
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFutureOrPresent(OffsetTime anOffsetTime, String aMessage) {
        if (anOffsetTime != null && anOffsetTime.isBefore(OffsetTime.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link OffsetTime} is in the past.
     *
     * @param anOffsetTime checked
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPast(OffsetTime anOffsetTime, String aMessage) {
        OffsetTime compare = OffsetTime.now();
        if (anOffsetTime != null && (anOffsetTime.isAfter(compare) || anOffsetTime.isEqual(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link OffsetTime} is in the past or is the present.
     *
     * @param anOffsetTime checked
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPastOrPresent(OffsetTime anOffsetTime, String aMessage) {
        if (anOffsetTime != null && anOffsetTime.isAfter(OffsetTime.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code anOffsetTime1} is before {@code anOffsetTime2}.
     *
     * @param anOffsetTime1 checked
     * @param anOffsetTime2 compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBefore(OffsetTime anOffsetTime1, OffsetTime anOffsetTime2, String aMessage) {
        if (anOffsetTime1 != null && anOffsetTime2 != null && !anOffsetTime1.isBefore(anOffsetTime2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link OffsetTime}, if the contained value is before {@code anOffsetTime2}.
     *
     * @param anOffsetTime1Optional checked
     * @param anOffsetTime2         compare value
     * @param aMessage              message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBefore(Optional<OffsetTime> anOffsetTime1Optional, OffsetTime anOffsetTime2,
     String aMessage) {
        if (anOffsetTime1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (anOffsetTime1Optional.isPresent()) {
            isBefore(anOffsetTime1Optional.get(), anOffsetTime2, aMessage);
        }
    }

    /**
     * Checks if {@code anOffsetTime1} is after {@code anOffsetTime2}.
     *
     * @param anOffsetTime1 checked
     * @param anOffsetTime2 compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfter(OffsetTime anOffsetTime1, OffsetTime anOffsetTime2, String aMessage) {
        if (anOffsetTime1 != null && anOffsetTime2 != null && !anOffsetTime1.isAfter(anOffsetTime2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link OffsetTime}, if the contained value is after {@code anOffsetTime2}.
     *
     * @param anOffsetTime1Optional checked
     * @param anOffsetTime2         compare value
     * @param aMessage              message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfter(Optional<OffsetTime> anOffsetTime1Optional, OffsetTime anOffsetTime2,
String aMessage) {
        if (anOffsetTime1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (anOffsetTime1Optional.isPresent()) {
            isAfter(anOffsetTime1Optional.get(), anOffsetTime2, aMessage);
        }
    }

    /**
     * Checks if {@code anOffsetTime1} is before or equal to {@code anOffsetTime2}.
     *
     * @param anOffsetTime1 checked
     * @param anOffsetTime2 compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBeforeOrEqualTo(OffsetTime anOffsetTime1, OffsetTime anOffsetTime2, String aMessage) {
        if (anOffsetTime1 != null && anOffsetTime2 != null && anOffsetTime1.isAfter(anOffsetTime2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link OffsetTime}, if the contained value is before or equal to {@code
      * anOffsetTime2}.
     *
     * @param anOffsetTime1Optional checked
     * @param anOffsetTime2         compare value
     * @param aMessage              message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBeforeOrEqualTo(Optional<OffsetTime> anOffsetTime1Optional, OffsetTime anOffsetTime2
    , String aMessage) {
        if (anOffsetTime1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (anOffsetTime1Optional.isPresent()) {
            isBeforeOrEqualTo(anOffsetTime1Optional.get(), anOffsetTime2, aMessage);
        }
    }

    /**
     * Checks if {@code anOffsetTime1} is after or equal to {@code anOffsetTime2}.
     *
     * @param anOffsetTime1 checked
     * @param anOffsetTime2 compare value
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfterOrEqualTo(OffsetTime anOffsetTime1, OffsetTime anOffsetTime2, String aMessage) {
        if (anOffsetTime1 != null && anOffsetTime2 != null && anOffsetTime1.isBefore(anOffsetTime2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if an Optional containing an {@link OffsetTime} is after or equal to {@code anOffsetTime2}.
     *
     * @param anOffsetTime1Optional checked
     * @param anOffsetTime2         compare value
     * @param aMessage              message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfterOrEqualTo(Optional<OffsetTime> anOffsetTime1Optional, OffsetTime anOffsetTime2,
     String aMessage) {
        if (anOffsetTime1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (anOffsetTime1Optional.isPresent()) {
            isAfterOrEqualTo(anOffsetTime1Optional.get(), anOffsetTime2, aMessage);
        }
    }

    /**
     * Checks, if of a given {@link OffsetDateTime} is in the future.
     *
     * @param anOffsetDateTime checked
     * @param aMessage         message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFuture(OffsetDateTime anOffsetDateTime, String aMessage) {
        OffsetDateTime compare = OffsetDateTime.now();
        if (anOffsetDateTime != null && (anOffsetDateTime.isBefore(compare) || anOffsetDateTime.isEqual(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link OffsetDateTime} is in the future or is the present.
     *
     * @param anOffsetDateTime checked
     * @param aMessage         message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isFutureOrPresent(OffsetDateTime anOffsetDateTime, String aMessage) {
        if (anOffsetDateTime != null && anOffsetDateTime.isBefore(OffsetDateTime.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link OffsetDateTime} is in the past.
     *
     * @param anOffsetDateTime checked
     * @param aMessage         message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPast(OffsetDateTime anOffsetDateTime, String aMessage) {
        OffsetDateTime compare = OffsetDateTime.now();
        if (anOffsetDateTime != null && (anOffsetDateTime.isAfter(compare) || anOffsetDateTime.isEqual(compare))) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks, if of a given {@link OffsetDateTime} is in the past or is the present.
     *
     * @param anOffsetDateTime checked
     * @param aMessage         message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPastOrPresent(OffsetDateTime anOffsetDateTime, String aMessage) {
        if (anOffsetDateTime != null && anOffsetDateTime.isAfter(OffsetDateTime.now())) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks if {@code anOffsetDateTime1} is before {@code anOffsetDateTime2}.
     *
     * @param anOffsetDateTime1 checked
     * @param anOffsetDateTime2 compare value
     * @param aMessage          message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBefore(OffsetDateTime anOffsetDateTime1, OffsetDateTime anOffsetDateTime2, String aMessage) {
        if (anOffsetDateTime1 != null && anOffsetDateTime2 != null && !anOffsetDateTime1.isBefore(anOffsetDateTime2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link OffsetDateTime}, if the contained value is before {@code
      * anOffsetDateTime2}.
     *
     * @param anOffsetDateTime1Optional checked
     * @param anOffsetDateTime2         compare value
     * @param aMessage                  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBefore(Optional<OffsetDateTime> anOffsetDateTime1Optional,
     OffsetDateTime anOffsetDateTime2, String aMessage) {
        if (anOffsetDateTime1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (anOffsetDateTime1Optional.isPresent()) {
            isBefore(anOffsetDateTime1Optional.get(), anOffsetDateTime2, aMessage);
        }
    }

    /**
     * Checks if {@code anOffsetDateTime1} is after {@code anOffsetDateTime2}.
     *
     * @param anOffsetDateTime1 checked
     * @param anOffsetDateTime2 compare value
     * @param aMessage          message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfter(OffsetDateTime anOffsetDateTime1, OffsetDateTime anOffsetDateTime2, String aMessage) {
        if (anOffsetDateTime1 != null && anOffsetDateTime2 != null && !anOffsetDateTime1.isAfter(anOffsetDateTime2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link OffsetDateTime}, if the contained value is after {@code
      * anOffsetDateTime2}.
     *
     * @param anOffsetDateTime1Optional checked
     * @param anOffsetDateTime2         compare value
     * @param aMessage                  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfter(Optional<OffsetDateTime> anOffsetDateTime1Optional,
     OffsetDateTime anOffsetDateTime2, String aMessage) {
        if (anOffsetDateTime1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (anOffsetDateTime1Optional.isPresent()) {
            isAfter(anOffsetDateTime1Optional.get(), anOffsetDateTime2, aMessage);
        }
    }

    /**
     * Checks if {@code anOffsetDateTime1} is before or equal to {@code anOffsetDateTime2}.
     *
     * @param anOffsetDateTime1 checked
     * @param anOffsetDateTime2 compare value
     * @param aMessage          message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isBeforeOrEqualTo(OffsetDateTime anOffsetDateTime1, OffsetDateTime anOffsetDateTime2,
String aMessage) {
        if (anOffsetDateTime1 != null && anOffsetDateTime2 != null && anOffsetDateTime1.isAfter(anOffsetDateTime2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link OffsetDateTime}, if the contained value is before or equal to {@code
      * anOffsetDateTime2}.
     *
     * @param anOffsetDateTime1Optional checked
     * @param anOffsetDateTime2         compare value
     * @param aMessage                  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsBeforeOrEqualTo(Optional<OffsetDateTime> anOffsetDateTime1Optional,
     OffsetDateTime anOffsetDateTime2, String aMessage) {
        if (anOffsetDateTime1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (anOffsetDateTime1Optional.isPresent()) {
            isBeforeOrEqualTo(anOffsetDateTime1Optional.get(), anOffsetDateTime2, aMessage);
        }
    }

    /**
     * Checks if {@code anOffsetDateTime1} is after or equal to {@code anOffsetDateTime2}.
     *
     * @param anOffsetDateTime1 checked
     * @param anOffsetDateTime2 compare value
     * @param aMessage          message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isAfterOrEqualTo(OffsetDateTime anOffsetDateTime1, OffsetDateTime anOffsetDateTime2,
     String aMessage) {
        if (anOffsetDateTime1 != null && anOffsetDateTime2 != null && anOffsetDateTime1.isBefore(anOffsetDateTime2)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link OffsetDateTime}, if the contained value is after or equal to {@code
      * anOffsetDateTime2}.
     *
     * @param anOffsetDateTime1Optional checked
     * @param anOffsetDateTime2         compare value
     * @param aMessage                  message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsAfterOrEqualTo(Optional<OffsetDateTime> anOffsetDateTime1Optional,
 OffsetDateTime anOffsetDateTime2, String aMessage) {
        if (anOffsetDateTime1Optional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (anOffsetDateTime1Optional.isPresent()) {
            isAfterOrEqualTo(anOffsetDateTime1Optional.get(), anOffsetDateTime2, aMessage);
        }
    }

    /**
     * Check, if a given {@link String} is not empty. {@code null} is considered as empty in this case.
     *
     * @param aString  checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNotEmpty(String aString, String aMessage) {
        if (aString == null || aString.isEmpty()) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link String}, if the contained String is not empty.
     *
     * @param aStringOptional checked
     * @param aMessage        message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsNotEmpty(Optional<String> aStringOptional, String aMessage) {
        if (aStringOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aStringOptional.isPresent()) {
            isNotEmpty(aStringOptional.get(), aMessage);
        }
    }

    /**
     * Checks, if a given {@link String} is a valid email address.
     *
     * @param aString  checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isValidEmail(String aString, String aMessage) {
        if (aString != null && !EmailValidator.getInstance().isValid(aString)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link String}, if the contained String is a valid email address.
     *
     * @param aStringOptional checked
     * @param aMessage        message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsValidEmail(Optional<String> aStringOptional, String aMessage) {
        if (aStringOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aStringOptional.isPresent()) {
            isValidEmail(aStringOptional.get(), aMessage);
        }
    }

    /**
     * Checks, if a given {@link String} is not blank. {@code null} is considered to be blank.
     *
     * @param aString  checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNotBlank(String aString, String aMessage) {
        if (aString == null || aString.trim().isEmpty()) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link String}, if the contained String is not blank.
     *
     * @param aStringOptional checked
     * @param aMessage        message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsNotBlank(Optional<String> aStringOptional, String aMessage) {
        if (aStringOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aStringOptional.isPresent()) {
            isNotBlank(aStringOptional.get(), aMessage);
        }
    }

    /**
     * Checks an array, if it is not empty. {@code null} is considered to be empty.
     *
     * @param array    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNotEmptyIterable(Object[] array, String aMessage) {
        if (array == null || array.length == 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Collection}, if it is not empty. {@code null} is considered to be empty.
     *
     * @param collection checked
     * @param aMessage   message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNotEmptyIterable(Collection collection, String aMessage) {
        if (collection == null || collection.isEmpty()) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Map}, if it is not empty. {@code null} is considered to be empty.
     *
     * @param map      checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNotEmptyIterable(Map map, String aMessage) {
        if (map == null || map.isEmpty()) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an object reference, if is null
     *
     * @param anObject checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNull(Object anObject, String aMessage) {
        if (anObject != null) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an object reference, if is not null
     *
     * @param anObject checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNotNull(Object anObject, String aMessage) {
        if (anObject == null) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link double}, if the value is in the specified range (inclusive marginal values).
     *
     * @param aValue   checked
     * @param aMinimum lower bound
     * @param aMaximum upper bound
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isInRange(double aValue, double aMinimum, double aMaximum, String aMessage) {
        if (aValue < aMinimum || aValue > aMaximum) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link float}, if the value is in the specified range (inclusive marginal values).
     *
     * @param aValue   checked
     * @param aMinimum lower bound
     * @param aMaximum upper bound
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isInRange(float aValue, float aMinimum, float aMaximum, String aMessage) {
        if (aValue < aMinimum || aValue > aMaximum) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an {@link int}, if the value is in the specified range (inclusive marginal values).
     *
     * @param aValue   checked
     * @param aMinimum lower bound
     * @param aMaximum upper bound
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isInRange(int aValue, int aMinimum, int aMaximum, String aMessage) {
        if (aValue < aMinimum || aValue > aMaximum) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link byte}, if the value is in the specified range (inclusive marginal values).
     *
     * @param aValue   checked
     * @param aMinimum lower bound
     * @param aMaximum upper bound
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isInRange(byte aValue, byte aMinimum, byte aMaximum, String aMessage) {
        if (aValue < aMinimum || aValue > aMaximum) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link short}, if the value is in the specified range (inclusive marginal values).
     *
     * @param aValue   checked
     * @param aMinimum lower bound
     * @param aMaximum upper bound
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isInRange(short aValue, short aMinimum, short aMaximum, String aMessage) {
        if (aValue < aMinimum || aValue > aMaximum) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link long}, if the value is in the specified range (inclusive marginal values).
     *
     * @param aValue   checked
     * @param aMinimum lower bound
     * @param aMaximum upper bound
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isInRange(long aValue, long aMinimum, long aMaximum, String aMessage) {
        if (aValue < aMinimum || aValue > aMaximum) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Long}, if the value is in the specified range (inclusive marginal values).
     *
     * @param aValue   checked
     * @param aMinimum lower bound
     * @param aMaximum upper bound
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isInRange(Long aValue, long aMinimum, long aMaximum, String aMessage) {
        if (aValue != null && (aValue < aMinimum || aValue > aMaximum)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Long}, if the contained value is in the specified range (inclusive
     * marginal values).
     *
     * @param aValueOptional checked
     * @param aMinimum       lower bound
     * @param aMaximum       upper bound
     * @param aMessage       message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsInRange(Optional<Long> aValueOptional, long aMinimum, long aMaximum, String aMessage) {
        if (aValueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aValueOptional.isPresent()) {
            isInRange(aValueOptional.get(), aMinimum, aMaximum, aMessage);
        }
    }

    /**
     * Checks a {@link BigDecimal}, if the value is in the specified range (inclusive marginal values).
     *
     * @param aValue   checked
     * @param aMinimum lower bound
     * @param aMaximum upper bound
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isInRange(BigDecimal aValue, BigDecimal aMinimum, BigDecimal aMaximum, String aMessage) {
        if (aValue != null && (aValue.compareTo(aMinimum) < 0 || aValue.compareTo(aMaximum) > 0)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link BigDecimal}, if the contained value is in the specified range
     * (inclusive marginal values).
     *
     * @param aValueOptional checked
     * @param aMinimum       lower bound
     * @param aMaximum       upper bound
     * @param aMessage       message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsInRange(Optional<BigDecimal> aValueOptional, BigDecimal aMinimum,
 BigDecimal aMaximum, String aMessage) {
        if (aValueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aValueOptional.isPresent()) {
            isInRange(aValueOptional.get(), aMinimum, aMaximum, aMessage);
        }
    }

    /**
     * Checks a {@link BigInteger}, if the value is in the specified range (inclusive marginal values).
     *
     * @param aValue   checked
     * @param aMinimum lower bound
     * @param aMaximum upper bound
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isInRange(BigInteger aValue, BigInteger aMinimum, BigInteger aMaximum, String aMessage) {
        if (aValue != null && (aValue.compareTo(aMinimum) < 0 || aValue.compareTo(aMaximum) > 0)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link BigInteger}, if the contained value is in the specified range
     * (inclusive marginal values).
     *
     * @param aValueOptional checked
     * @param aMinimum       lower bound
     * @param aMaximum       upper bound
     * @param aMessage       message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsInRange(Optional<BigInteger> aValueOptional, BigInteger aMinimum,
     BigInteger aMaximum, String aMessage) {
        if (aValueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aValueOptional.isPresent()) {
            isInRange(aValueOptional.get(), aMinimum, aMaximum, aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Double}, if the contained value is in the specified range (inclusive
     * marginal values).
     *
     * @param aValueOptional checked
     * @param aMinimum       lower bound
     * @param aMaximum       upper bound
     * @param aMessage       message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsInRange(Optional<Double> aValueOptional, double aMinimum, double aMaximum,
     String aMessage) {
        if (aValueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aValueOptional.isPresent()) {
            isInRange(aValueOptional.get(), aMinimum, aMaximum, aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Float}, if the contained value is in the specified range (inclusive
     * marginal values).
     *
     * @param aValueOptional checked
     * @param aMinimum       lower bound
     * @param aMaximum       upper bound
     * @param aMessage       message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsInRange(Optional<Float> aValueOptional, float aMinimum, float aMaximum,
     String aMessage) {
        if (aValueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aValueOptional.isPresent()) {
            isInRange(aValueOptional.get(), aMinimum, aMaximum, aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Integer}, if the contained value is in the specified range (inclusive
     * marginal values).
     *
     * @param aValueOptional checked
     * @param aMinimum       lower bound
     * @param aMaximum       upper bound
     * @param aMessage       message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsInRange(Optional<Integer> aValueOptional, int aMinimum, int aMaximum,
     String aMessage) {
        if (aValueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aValueOptional.isPresent()) {
            isInRange(aValueOptional.get(), aMinimum, aMaximum, aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Short}, if the contained value is in the specified range (inclusive
     * marginal values).
     *
     * @param aValueOptional checked
     * @param aMinimum       lower bound
     * @param aMaximum       upper bound
     * @param aMessage       message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsInRange(Optional<Short> aValueOptional, short aMinimum, short aMaximum,
     String aMessage) {
        if (aValueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aValueOptional.isPresent()) {
            isInRange(aValueOptional.get(), aMinimum, aMaximum, aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Byte}, if the contained value is in the specified range (inclusive
     * marginal values).
     *
     * @param aValueOptional checked
     * @param aMinimum       lower bound
     * @param aMaximum       upper bound
     * @param aMessage       message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsInRange(Optional<Byte> aValueOptional, byte aMinimum, byte aMaximum, String aMessage) {
        if (aValueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (aValueOptional.isPresent()) {
            isInRange(aValueOptional.get(), aMinimum, aMaximum, aMessage);
        }
    }

    /**
     * Checks a {@link boolean}, if it is {@code true}.
     *
     * @param aBoolean checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isTrue(boolean aBoolean, String aMessage) {
        if (!aBoolean) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link String}, if it matches a given regular expression.
     *
     * @param value    checked
     * @param regEx    regular expression to be matched
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void regEx(String value, String regEx, String aMessage) {
        if (!value.matches(regEx)) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link String}, if the contained String  it matches a given regular expression.
     *
     * @param valueOptional checked
     * @param regEx         regular expression to be matched
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalRegEx(Optional<String> valueOptional, String regEx, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            regEx(valueOptional.get(), regEx, aMessage);
        }
    }

    /**
     * Checks a {@link byte}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(byte value, byte compareValue, String aMessage) {
        if (value <= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link byte}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(byte value, byte compareValue, String aMessage) {
        if (value >= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link byte}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(byte value, byte compareValue, String aMessage) {
        if (value < compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link byte}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(byte value, byte compareValue, String aMessage) {
        if (value > compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link byte}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(byte value, String aMessage) {
        if (value < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link byte}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(byte value, String aMessage) {
        if (value <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link byte}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(byte value, String aMessage) {
        if (value > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link byte}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(byte value, String aMessage) {
        if (value >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link short}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(short value, short compareValue, String aMessage) {
        if (value <= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link short}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(short value, short compareValue, String aMessage) {
        if (value >= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link short}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(short value, short compareValue, String aMessage) {
        if (value < compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link short}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(short value, short compareValue, String aMessage) {
        if (value > compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link short}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(short value, String aMessage) {
        if (value < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link short}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(short value, String aMessage) {
        if (value <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link short}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(short value, String aMessage) {
        if (value > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link short}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(short value, String aMessage) {
        if (value >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an {@link int}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(int value, int compareValue, String aMessage) {
        if (value <= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an {@link int}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(int value, int compareValue, String aMessage) {
        if (value >= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an {@link int}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(int value, int compareValue, String aMessage) {
        if (value < compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link int}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(int value, int compareValue, String aMessage) {
        if (value > compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an {@link int}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(int value, String aMessage) {
        if (value < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an {@link int}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(int value, String aMessage) {
        if (value <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an {@link int}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(int value, String aMessage) {
        if (value > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an {@link int}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(int value, String aMessage) {
        if (value >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link long}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(long value, long compareValue, String aMessage) {
        if (value <= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link long}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(long value, long compareValue, String aMessage) {
        if (value >= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link long}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(long value, long compareValue, String aMessage) {
        if (value < compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link long}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(long value, long compareValue, String aMessage) {
        if (value > compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link long}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(long value, String aMessage) {
        if (value < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link long}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(long value, String aMessage) {
        if (value <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link long}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(long value, String aMessage) {
        if (value > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link long}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(long value, String aMessage) {
        if (value >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link double}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(double value, double compareValue, String aMessage) {
        if (value <= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link double}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(double value, double compareValue, String aMessage) {
        if (value >= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link double}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(double value, double compareValue, String aMessage) {
        if (value < compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link double}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(double value, double compareValue, String aMessage) {
        if (value > compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link double}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(double value, String aMessage) {
        if (value < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link double}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(double value, String aMessage) {
        if (value <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link double}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(double value, String aMessage) {
        if (value > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link double}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(double value, String aMessage) {
        if (value >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link float}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(float value, float compareValue, String aMessage) {
        if (value <= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link float}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(float value, float compareValue, String aMessage) {
        if (value >= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link float}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(float value, float compareValue, String aMessage) {
        if (value < compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link float}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(float value, float compareValue, String aMessage) {
        if (value > compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link float}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(float value, String aMessage) {
        if (value < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link float}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(float value, String aMessage) {
        if (value <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link float}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(float value, String aMessage) {
        if (value > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link float}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(float value, String aMessage) {
        if (value >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Byte}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(Byte value, Byte compareValue, String aMessage) {
        if (value != null && compareValue != null && value <= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Byte}, if the contained value is greater than a given comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterThan(Optional<Byte> valueOptional, Byte compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Byte}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(Byte value, Byte compareValue, String aMessage) {
        if (value != null && compareValue != null && value >= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Byte}, if the contained value is less than a given comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessThan(Optional<Byte> valueOptional, Byte compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Byte}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(Byte value, Byte compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Byte}, if the contained value is greater than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterOrEqual(Optional<Byte> valueOptional, Byte compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Byte}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(Byte value, Byte compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Byte}, if the contained value is less than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessOrEqual(Optional<Byte> valueOptional, Byte compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Byte}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(Byte value, String aMessage) {
        if (value != null && value < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a numeric value
     * ({@link Byte}, {@link Short}, {@link Integer}, {@link Long},
     * {@link BigDecimal}, {@link BigInteger}, {@link Double}, {@link Float}),
     * if the contained value is positive or is zero.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsPositiveOrZero(Optional valueOptional, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            Object value = valueOptional.get();
            if (value instanceof Byte) {
                isPositiveOrZero((Byte) value, aMessage);
            } else if (value instanceof Short) {
                isPositiveOrZero((Short) value, aMessage);
            } else if (value instanceof Integer) {
                isPositiveOrZero((Integer) value, aMessage);
            } else if (value instanceof Long) {
                isPositiveOrZero((Long) value, aMessage);
            } else if (value instanceof BigDecimal) {
                isPositiveOrZero((BigDecimal) value, aMessage);
            } else if (value instanceof BigInteger) {
                isPositiveOrZero((BigInteger) value, aMessage);
            } else if (value instanceof Double) {
                isPositiveOrZero((Double) value, aMessage);
            } else if (value instanceof Float) {
                isPositiveOrZero((Float) value, aMessage);
            } else {
                throw new IllegalArgumentException("valueOptional parameter must contain " +
                    "Byte, Short, Integer, Long, BigDecimal, BigInteger, Double or Float. Other types are not " +
                     "supported!");
            }
        }
    }

    /**
     * Checks an Optional containing a numeric value
     * ({@link Byte}, {@link Short}, {@link Integer}, {@link Long},
     * {@link BigDecimal}, {@link BigInteger}, {@link Double}, {@link Float}),
     * if the contained value is positive.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsPositive(Optional valueOptional, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            Object value = valueOptional.get();
            if (value instanceof Byte) {
                isPositive((Byte) value, aMessage);
            } else if (value instanceof Short) {
                isPositive((Short) value, aMessage);
            } else if (value instanceof Integer) {
                isPositive((Integer) value, aMessage);
            } else if (value instanceof Long) {
                isPositive((Long) value, aMessage);
            } else if (value instanceof BigDecimal) {
                isPositive((BigDecimal) value, aMessage);
            } else if (value instanceof BigInteger) {
                isPositive((BigInteger) value, aMessage);
            } else if (value instanceof Double) {
                isPositive((Double) value, aMessage);
            } else if (value instanceof Float) {
                isPositive((Float) value, aMessage);
            } else {
                throw new IllegalArgumentException("valueOptional parameter must contain " +
                    "Byte, Short, Integer, Long, BigDecimal, BigInteger, Double or Float. Other types are not " +
                    "supported!");
            }
        }
    }

    /**
     * Checks a {@link Byte}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(Byte value, String aMessage) {
        if (value != null && value <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Byte}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(Byte value, String aMessage) {
        if (value != null && value > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a numeric value
     * ({@link Byte}, {@link Short}, {@link Integer}, {@link Long},
     * {@link BigDecimal}, {@link BigInteger}, {@link Double}, {@link Float}),
     * if the contained value is negative or zero.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsNegativeOrZero(Optional valueOptional, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            Object value = valueOptional.get();
            if (value instanceof Byte) {
                isNegativeOrZero((Byte) value, aMessage);
            } else if (value instanceof Short) {
                isNegativeOrZero((Short) value, aMessage);
            } else if (value instanceof Integer) {
                isNegativeOrZero((Integer) value, aMessage);
            } else if (value instanceof Long) {
                isNegativeOrZero((Long) value, aMessage);
            } else if (value instanceof BigDecimal) {
                isNegativeOrZero((BigDecimal) value, aMessage);
            } else if (value instanceof BigInteger) {
                isNegativeOrZero((BigInteger) value, aMessage);
            } else if (value instanceof Double) {
                isNegativeOrZero((Double) value, aMessage);
            } else if (value instanceof Float) {
                isNegativeOrZero((Float) value, aMessage);
            } else {
                throw new IllegalArgumentException("valueOptional parameter must contain " +
                    "Byte, Short, Integer, Long, BigDecimal, BigInteger, Double or Float. Other types are not " +
                     "supported!");
            }
        }
    }

    /**
     * Checks a {@link Byte}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(Byte value, String aMessage) {
        if (value != null && value >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a numeric value
     * ({@link Byte}, {@link Short}, {@link Integer}, {@link Long},
     * {@link BigDecimal}, {@link BigInteger}, {@link Double}, {@link Float}),
     * if the contained value is negative.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsNegative(Optional valueOptional, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            Object value = valueOptional.get();
            if (value instanceof Byte) {
                isNegative((Byte) value, aMessage);
            } else if (value instanceof Short) {
                isNegative((Short) value, aMessage);
            } else if (value instanceof Integer) {
                isNegative((Integer) value, aMessage);
            } else if (value instanceof Long) {
                isNegative((Long) value, aMessage);
            } else if (value instanceof BigDecimal) {
                isNegative((BigDecimal) value, aMessage);
            } else if (value instanceof BigInteger) {
                isNegative((BigInteger) value, aMessage);
            } else if (value instanceof Double) {
                isNegative((Double) value, aMessage);
            } else if (value instanceof Float) {
                isNegative((Float) value, aMessage);
            } else {
                throw new IllegalArgumentException("valueOptional parameter must contain " +
                    "Byte, Short, Integer, Long, BigDecimal, BigInteger, Double or Float. Other types are not " +
                     "supported!");
            }
        }
    }

    /**
     * Checks a {@link Short}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(Short value, Short compareValue, String aMessage) {
        if (value != null && compareValue != null && value <= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Short}, if the contained value is greater than a given comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterThan(Optional<Short> valueOptional, Short compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Short}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(Short value, Short compareValue, String aMessage) {
        if (value != null && compareValue != null && value >= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Short}, if the contained value is less than a given comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessThan(Optional<Short> valueOptional, Short compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Short}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(Short value, Short compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Short}, if the contained value is greater than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterOrEqual(Optional<Short> valueOptional, Short compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Short}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(Short value, Short compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Short}, if the contained value is less than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessOrEqual(Optional<Short> valueOptional, Short compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Short}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(Short value, String aMessage) {
        if (value != null && value < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Short}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(Short value, String aMessage) {
        if (value != null && value <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Short}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(Short value, String aMessage) {
        if (value != null && value > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Short}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(Short value, String aMessage) {
        if (value != null && value >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Integer}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(Integer value, Integer compareValue, String aMessage) {
        if (value != null && compareValue != null && value <= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Integer}, if the contained value is greater than a given comparative
     * value.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterThan(Optional<Integer> valueOptional, Integer compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks an {@link Integer}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(Integer value, Integer compareValue, String aMessage) {
        if (value != null && compareValue != null && value >= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Integer}, if the contained value is less than a given comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessThan(Optional<Integer> valueOptional, Integer compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks an {@link Integer}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(Integer value, Integer compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Integer}, if the contained value is greater than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterOrEqual(Optional<Integer> valueOptional, Integer compareValue,
 String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Integer}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(Integer value, Integer compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing an {@link Integer}, if the contained value is less than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessOrEqual(Optional<Integer> valueOptional, Integer compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks an {@link Integer}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(Integer value, String aMessage) {
        if (value != null && value < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an {@link Integer}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(Integer value, String aMessage) {
        if (value != null && value <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an {@link Integer}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(Integer value, String aMessage) {
        if (value != null && value > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an {@link Integer}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(Integer value, String aMessage) {
        if (value != null && value >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Long}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(Long value, Long compareValue, String aMessage) {
        if (value != null && compareValue != null && value <= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Long}, if the contained value is greater than a given comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterThan(Optional<Long> valueOptional, Long compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Long}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(Long value, Long compareValue, String aMessage) {
        if (value != null && compareValue != null && value >= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Long}, if the contained value is less than a given comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessThan(Optional<Long> valueOptional, Long compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Long}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(Long value, Long compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Long}, if the contained value is greater than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterOrEqual(Optional<Long> valueOptional, Long compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Long}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(Long value, Long compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Long}, if the contained value is less than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessOrEqual(Optional<Long> valueOptional, Long compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Long}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(Long value, String aMessage) {
        if (value != null && value < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Long}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(Long value, String aMessage) {
        if (value != null && value <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Long}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(Long value, String aMessage) {
        if (value != null && value > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Long}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(Long value, String aMessage) {
        if (value != null && value >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Double}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(Double value, Double compareValue, String aMessage) {
        if (value != null && compareValue != null && value <= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Double}, if the contained value is greater than a given comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterThan(Optional<Double> valueOptional, Double compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Double}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(Double value, Double compareValue, String aMessage) {
        if (value != null && compareValue != null && value >= compareValue) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Double}, if the contained value is less than a given comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessThan(Optional<Double> valueOptional, Double compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Double}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(Double value, Double compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Double}, if the contained value is greater than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterOrEqual(Optional<Double> valueOptional, Double compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Double}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(Double value, Double compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Double}, if the contained value is less than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessOrEqual(Optional<Double> valueOptional, Double compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Double}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(Double value, String aMessage) {
        if (value != null && value < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Double}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(Double value, String aMessage) {
        if (value != null && value <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Double}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(Double value, String aMessage) {
        if (value != null && value > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Double}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(Double value, String aMessage) {
        if (value != null && value >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Float}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(Float value, Float compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Float}, if the contained value is greater than a given comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterThan(Optional<Float> valueOptional, Float compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Float}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(Float value, Float compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Float}, if the contained value is less than a given comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessThan(Optional<Float> valueOptional, Float compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Float}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(Float value, Float compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Float}, if the contained value is greater than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterOrEqual(Optional<Float> valueOptional, Float compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Float}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(Float value, Float compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link Float}, if the contained value is less than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessOrEqual(Optional<Float> valueOptional, Float compareValue, String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link Float}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(Float value, String aMessage) {
        if (value != null && value < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Float}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(Float value, String aMessage) {
        if (value != null && value <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Float}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(Float value, String aMessage) {
        if (value != null && value > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link Float}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(Float value, String aMessage) {
        if (value != null && value >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link BigDecimal}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(BigDecimal value, BigDecimal compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link BigDecimal}, if the contained value is greater than a given comparative
     * value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterThan(Optional<BigDecimal> valueOptional, BigDecimal compareValue,
                                             String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link BigDecimal}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(BigDecimal value, BigDecimal compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link BigDecimal}, if the contained value is less than a given comparative
     * value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessThan(Optional<BigDecimal> valueOptional, BigDecimal compareValue,
     String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link BigDecimal}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(BigDecimal value, BigDecimal compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link BigDecimal}, if the contained value is greater than or equal to a given
      * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterOrEqual(Optional<BigDecimal> valueOptional, BigDecimal compareValue,
 String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link BigDecimal}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(BigDecimal value, BigDecimal compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link BigDecimal}, if the contained value is less than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessOrEqual(Optional<BigDecimal> valueOptional, BigDecimal compareValue,
                                             String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link BigDecimal}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(BigDecimal value, String aMessage) {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link BigDecimal}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(BigDecimal value, String aMessage) {
        if (value != null && value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link BigDecimal}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(BigDecimal value, String aMessage) {
        if (value != null && value.compareTo(BigDecimal.ZERO) > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link BigDecimal}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(BigDecimal value, String aMessage) {
        if (value != null && value.compareTo(BigDecimal.ZERO) >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link BigInteger}, if it is greater than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterThan(BigInteger value, BigInteger compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link BigInteger}, if the contained value is greater than a given comparative
      * value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterThan(Optional<BigInteger> valueOptional, BigInteger compareValue,
String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link BigInteger}, if it is less than a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessThan(BigInteger value, BigInteger compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link BigInteger}, if the contained value is less than a given comparative
     * value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessThan(Optional<BigInteger> valueOptional, BigInteger compareValue,
     String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessThan(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link BigInteger}, if it is greater than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isGreaterOrEqual(BigInteger value, BigInteger compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link BigInteger}, if the contained value is greater than or equal to a given
      * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsGreaterOrEqual(Optional<BigInteger> valueOptional, BigInteger compareValue,
String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isGreaterOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link BigInteger}, if it is less than or equal to a given comparative value.
     *
     * @param value        checked
     * @param compareValue for check
     * @param aMessage     message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isLessOrEqual(BigInteger value, BigInteger compareValue, String aMessage) {
        if (value != null && compareValue != null && value.compareTo(compareValue) > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks an Optional containing a {@link BigInteger}, if the contained value is less than or equal to a given
     * comparative value.
     * The Optional must not be {@code null}.
     *
     * @param valueOptional checked
     * @param compareValue  for check
     * @param aMessage      message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void optionalIsLessOrEqual(Optional<BigInteger> valueOptional, BigInteger compareValue,
     String aMessage) {
        if (valueOptional == null) {
            throw new IllegalArgumentException("The first parameter must not be null!");
        }
        if (valueOptional.isPresent()) {
            isLessOrEqual(valueOptional.get(), compareValue, aMessage);
        }
    }

    /**
     * Checks a {@link BigInteger}, if it is positive or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositiveOrZero(BigInteger value, String aMessage) {
        if (value != null && value.compareTo(BigInteger.ZERO) < 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link BigInteger}, if it is positive
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isPositive(BigInteger value, String aMessage) {
        if (value != null && value.compareTo(BigInteger.ZERO) <= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link BigInteger}, if it is negative or 0
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegativeOrZero(BigInteger value, String aMessage) {
        if (value != null && value.compareTo(BigInteger.ZERO) > 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

    /**
     * Checks a {@link BigInteger}, if it is negative
     *
     * @param value    checked
     * @param aMessage message for thrown exception
     * @throws DomainAssertionException if check failed
     */
    public static void isNegative(BigInteger value, String aMessage) {
        if (value != null && value.compareTo(BigInteger.ZERO) >= 0) {
            throw new DomainAssertionException(aMessage);
        }
    }

}
