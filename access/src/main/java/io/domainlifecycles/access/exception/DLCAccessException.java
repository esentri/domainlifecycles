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

package io.domainlifecycles.access.exception;

import static java.util.Objects.requireNonNull;

/**
 * RuntimeException that causes DLC to shut down. Should should only be used for
 * cases where the code is bad, e.g. because a case is not covered that is
 * necessary for the framework to work correctly.
 *
 * @author Tobias Herb
 * @author Mario Herb
 */
public class DLCAccessException extends RuntimeException {


    /**
     * @param detail detailed exception message
     * @return a new DLCAccessException with a detail message.
     */
    public static DLCAccessException fail(final String detail) {
        return new DLCAccessException(requireNonNull(detail));
    }

    /**
     * @param detail detailed exception message
     * @param cause  Throwable
     * @return a new DLCAccessException with a detail message and a cause.
     */
    public static DLCAccessException fail(final String detail, final Throwable cause) {
        return new DLCAccessException(requireNonNull(detail), requireNonNull(cause));
    }

    /**
     * @param detail detailed exception message
     * @param args   message parameters
     * @return a new DLCAccessException with a detail message formatted to include with given message parameters.
     */
    public static DLCAccessException fail(final String detail, final Object... args) {
        return new DLCAccessException(requireNonNull(detail), requireNonNull(args));
    }

    /**
     * @param detail detailed exception message
     * @param args   message parameters
     * @param cause  Throwable
     * @return a new DLCAccessException with a detail message formatted to include with given message parameters and
     * containing a cause.
     */
    public static DLCAccessException fail(final String detail, final Throwable cause, final Object... args) {
        return new DLCAccessException(requireNonNull(detail), requireNonNull(cause), requireNonNull(args));
    }

    private static String format(final String detail, final Object[] args) {
        return args.length > 0 ? String.format(detail, args) : detail;
    }

    private DLCAccessException(String detail, final Object... args) {
        super(format(detail, args));
    }

    private DLCAccessException(String detail, Throwable cause, Object... args) {
        super(format(detail, args), cause);
    }
}
