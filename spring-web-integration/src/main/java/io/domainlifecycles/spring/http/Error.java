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

package io.domainlifecycles.spring.http;

import java.util.Objects;

/**
 * General http response structure for errors.
 *
 * @author Mario Herb
 * @author Tobias Herb
 */
public class Error {

    private String code;
    private String message;

    public Error(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public Error() {
    }

    /**
     * Returns the error code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Sets the error code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Returns the error message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets the error message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Error other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$code = this.getCode();
        final Object other$code = other.getCode();
        if (!Objects.equals(this$code, other$code))
            return false;
        final Object this$message = this.getMessage();
        final Object other$message = other.getMessage();
        return Objects.equals(this$message, other$message);
    }

    /**
     * Returns true, if other is an Error.
     */
    protected boolean canEqual(final Object other) {
        return other instanceof Error;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $code = this.getCode();
        result = result * PRIME + ($code == null ? 43 : $code.hashCode());
        final Object $message = this.getMessage();
        result = result * PRIME + ($message == null ? 43 : $message.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "Error(code=" + this.getCode() + ", message=" + this.getMessage() + ")";
    }
}
