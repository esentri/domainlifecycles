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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.staticanalysis;

import java.util.Objects;

/**
 * Something the analysis could not do, reported alongside the result.
 * <p>
 * A static analysis fails quietly: an incomplete classpath or a method the mirror does not hold
 * does not raise an error, it just produces fewer calls. Without diagnostics a consumer cannot
 * tell "this method calls nothing" from "this method could not be analyzed", which is the
 * difference between a trustworthy and a misleading result.
 * <p>
 * {@link Severity#WARNING} means the result is likely incomplete and the cause should be fixed -
 * typically a missing classpath entry, see {@link DomainClasspath}. {@link Severity#INFO} means
 * something was deliberately skipped, e.g. a compiler generated bridge method that has no
 * counterpart in the mirror.
 *
 * @param kind    what could not be done
 * @param subject the type or method signature it happened on
 * @param detail  additional context, may be empty
 * @author Mario Herb
 */
public record Diagnostic(Kind kind, String subject, String detail) {

    public Diagnostic {
        Objects.requireNonNull(kind, "A Kind must be given!");
        Objects.requireNonNull(subject, "A subject must be given!");
        Objects.requireNonNull(detail, "A detail must be given, possibly empty!");
    }

    /**
     * How much a diagnostic affects the trustworthiness of the result.
     */
    public enum Severity {

        /**
         * The result is likely incomplete.
         */
        WARNING,

        /**
         * Something was skipped, the result is expected to be complete nevertheless.
         */
        INFO
    }

    /**
     * The kinds of gap the analysis can run into.
     */
    public enum Kind {

        /**
         * A mirrored type was not found on the analyzed classpath, so none of its methods and none
         * of the calls they make could be analyzed.
         */
        TYPE_NOT_ON_CLASSPATH(Severity.WARNING),

        /**
         * A method body exists but could not be loaded or translated. Its calls are missing from
         * the result.
         */
        BODY_NOT_LOADABLE(Severity.WARNING),

        /**
         * A method the analysis wanted to descend into was not found on the classpath. Expected
         * for references into code outside the analyzed classpath, e.g. the JDK.
         */
        METHOD_NOT_ON_CLASSPATH(Severity.INFO),

        /**
         * A resolved call target has no counterpart in the mirror and was dropped. Expected for
         * compiler generated bridge and synthetic methods.
         */
        TARGET_NOT_IN_MIRROR(Severity.INFO),

        /**
         * A method whose calls were resolved has no counterpart in the mirror, so its calls could
         * not be attributed to a caller. Expected for compiler generated bridge and synthetic
         * methods.
         */
        CALLER_NOT_IN_MIRROR(Severity.INFO);

        private final Severity severity;

        Kind(Severity severity) {
            this.severity = severity;
        }

        /**
         * @return how much this kind of gap affects the trustworthiness of the result
         */
        public Severity severity() {
            return severity;
        }
    }

    /**
     * @return the severity of this diagnostic's {@link #kind()}
     */
    public Severity severity() {
        return kind.severity();
    }

    /**
     * @param typeName the full qualified name of the mirrored type
     * @return a {@link Kind#TYPE_NOT_ON_CLASSPATH} diagnostic
     */
    public static Diagnostic typeNotOnClasspath(String typeName) {
        return new Diagnostic(Kind.TYPE_NOT_ON_CLASSPATH, typeName,
            "The type is mirrored but not present on the analyzed classpath.");
    }

    /**
     * @param methodSignature the signature of the method whose body failed to load
     * @param reason          the reason the body could not be loaded
     * @return a {@link Kind#BODY_NOT_LOADABLE} diagnostic
     */
    public static Diagnostic bodyNotLoadable(String methodSignature, String reason) {
        return new Diagnostic(Kind.BODY_NOT_LOADABLE, methodSignature, reason);
    }

    /**
     * @param methodSignature the signature of the method that was not found
     * @return a {@link Kind#METHOD_NOT_ON_CLASSPATH} diagnostic
     */
    public static Diagnostic methodNotOnClasspath(String methodSignature) {
        return new Diagnostic(Kind.METHOD_NOT_ON_CLASSPATH, methodSignature,
            "The method was not found on the analyzed classpath, it was not descended into.");
    }

    /**
     * @param methodSignature the signature of the dropped call target
     * @param ownerTypeName   the type the target was attributed to
     * @return a {@link Kind#TARGET_NOT_IN_MIRROR} diagnostic
     */
    public static Diagnostic targetNotInMirror(String methodSignature, String ownerTypeName) {
        return new Diagnostic(Kind.TARGET_NOT_IN_MIRROR, methodSignature,
            "No matching method on the mirror of " + ownerTypeName + ", the call was dropped.");
    }

    /**
     * @param methodSignature the signature of the method whose calls could not be attributed
     * @param ownerTypeName   the type the method was attributed to
     * @return a {@link Kind#CALLER_NOT_IN_MIRROR} diagnostic
     */
    public static Diagnostic callerNotInMirror(String methodSignature, String ownerTypeName) {
        return new Diagnostic(Kind.CALLER_NOT_IN_MIRROR, methodSignature,
            "No matching method on the mirror of " + ownerTypeName
                + ", its calls were dropped.");
    }

    @Override
    public String toString() {
        return severity() + " " + kind + " " + subject
            + (detail.isEmpty() ? "" : " - " + detail);
    }
}
