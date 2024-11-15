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

package io.domainlifecycles.reflect;

/**
 * The arity determines the number of formal arguments or operands taken by a function or operation.
 * <p>
 * Be careful not to override fixed arity mixins defined from {@link Arity0} to {@link Arity8}.
 * <p>
 * For more information, read about
 * <a href="https://en.wikipedia.org/wiki/Arity">Arity</a>.
 *
 * @author Tobias Herb
 */
public enum Arities {
    ;
    // ----------------------------------------------------------
    //  ARITY.
    // ----------------------------------------------------------

    /**
     * Mixin to declare a rank.
     */
    public interface Arity {

        /**
         * Returns the rank.
         *
         * @return the rank
         */
        int arity();
    }

    // ----------------------------------------------------------
    //  ARITY 0.
    // ----------------------------------------------------------

    /**
     * Mixin interface to declare a rank of 0.
     */
    public interface Arity0 extends Arity {

        /**
         * Returns the fixed arity of 0.
         */
        @Override
        default int arity() {
            return 0;
        }
    }

    // ----------------------------------------------------------
    //  ARITY 1.
    // ----------------------------------------------------------

    /**
     * Mixin interface to declare a rank of 1.
     */
    public interface Arity1 extends Arity {

        /**
         * Returns the fixed arity of 1.
         */
        @Override
        default int arity() {
            return 1;
        }
    }

    // ----------------------------------------------------------
    //  ARITY 2.
    // ----------------------------------------------------------

    /**
     * Mixin interface to declare a rank of 2.
     */
    public interface Arity2 extends Arity {

        /**
         * Returns the fixed arity of 2;
         */
        @Override
        default int arity() {
            return 2;
        }
    }

    // ----------------------------------------------------------
    //  ARITY 3.
    // ----------------------------------------------------------

    /**
     * Mixin interface to declare a rank of 3.
     */
    public interface Arity3 extends Arity {

        /**
         * Returns the fixed arity of 3.
         */
        @Override
        default int arity() {
            return 3;
        }
    }

    // ----------------------------------------------------------
    //  ARITY 4.
    // ----------------------------------------------------------

    /**
     * Mixin interface to declare a rank of 4.
     */
    public interface Arity4 extends Arity {

        /**
         * Returns the fixed arity of 4.
         */
        @Override
        default int arity() {
            return 4;
        }
    }

    // ----------------------------------------------------------
    //  ARITY 5.
    // ----------------------------------------------------------

    /**
     * Mixin interface to declare a rank of 5.
     */
    public interface Arity5 extends Arity {

        /**
         * Returns the fixed arity of 5.
         */
        @Override
        default int arity() {
            return 5;
        }
    }

    // ----------------------------------------------------------
    //  ARITY 6.
    // ----------------------------------------------------------

    /**
     * Mixin interface to declare a rank of 6.
     */
    public interface Arity6 extends Arity {

        /**
         * Returns the fixed arity of 6.
         */
        @Override
        default int arity() {
            return 6;
        }
    }

    // ----------------------------------------------------------
    //  ARITY 7.
    // ----------------------------------------------------------

    /**
     * Mixin interface to declare a rank of 7.
     */
    public interface Arity7 extends Arity {

        /**
         * Returns the fixed arity of 7.
         */
        @Override
        default int arity() {
            return 7;
        }
    }

    // ----------------------------------------------------------
    //  ARITY 8.
    // ----------------------------------------------------------

    /**
     * Mixin interface to declare a rank of 8.
     */
    public interface Arity8 extends Arity {

        /**
         * Returns the fixed arity of 8.
         */
        @Override
        default int arity() {
            return 8;
        }
    }
}

