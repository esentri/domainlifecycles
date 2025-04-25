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

package io.domainlifecycles.plugins.exception;

import static java.util.Objects.requireNonNull;

/**
 * RuntimeException that causes the DLC-Plugin to shut down. Should only be used for
 * cases where the code is bad, e.g. because a case is not covered that is
 * necessary for the framework to work correctly.
 *
 * @author Mario Herb
 */
public class DLCPluginsException extends RuntimeException {

    /**
     * @param detail detailed exception message
     * @return a new DLCPluginException with a detail message.
     */
    public static DLCPluginsException fail(final String detail) {
        return new DLCPluginsException(requireNonNull(detail));
    }

    /**
     * @param detail detailed exception message
     * @param cause  Throwable
     * @return a new DLCPluginException with a detail message and a cause.
     */
    public static DLCPluginsException fail(final String detail, final Throwable cause) {
        return new DLCPluginsException(requireNonNull(detail), requireNonNull(cause));
    }

    /**
     * @param detail detailed exception message
     * @param args   message parameters
     * @return a new DLCPluginException with a detail message formatted to include with given message parameters.
     */
    public static DLCPluginsException fail(final String detail, final Object... args) {
        return new DLCPluginsException(requireNonNull(detail), requireNonNull(args));
    }

    /**
     * @param detail detailed exception message
     * @param args   message parameters
     * @param cause  Throwable
     * @return a new DLCPluginException with a detail message formatted to include with given message parameters and
     * containing a cause.
     */
    public static DLCPluginsException fail(final String detail, final Throwable cause, final Object... args) {
        return new DLCPluginsException(requireNonNull(detail), requireNonNull(cause), requireNonNull(args));
    }

    private static String format(final String detail, final Object[] args) {
        return args.length > 0 ? String.format(detail, args) : detail;
    }

    private DLCPluginsException(String detail, final Object... args) {
        super(format(detail, args));
    }

    private DLCPluginsException(String detail, Throwable cause, Object... args) {
        super(format(detail, args), cause);
    }
}
