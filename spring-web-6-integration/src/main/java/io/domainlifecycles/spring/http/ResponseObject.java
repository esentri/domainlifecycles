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

package io.domainlifecycles.spring.http;

import java.util.List;
import java.util.Objects;

/**
 * General http response structure
 *
 * @param <T> type of returned object (mostly entity)
 * @author Mario Herb
 * @author Tobias Herb
 * @author Dominik Galler
 */
public class ResponseObject<T> {

    private T data;
    private List<Error> errors;

    ResponseObject(T data, List<Error> errors) {
        this.data = data;
        this.errors = errors;
    }

    /**
     * Factory method for ResponseObjectBuilder
     *
     * @param <T> type of returned object (mostly entity)
     * @return the builder instance of {@link ResponseObject}
     */
    public static <T> ResponseObjectBuilder<T> builder() {
        return new ResponseObjectBuilder<>();
    }

    /**
     * @return the data
     */
    public T getData() {
        return this.data;
    }

    /**
     * Set data
     *
     * @param data the data to be set in the ResponseObject
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return the errors
     */
    public List<Error> getErrors() {
        return this.errors;
    }

    /**
     * Set errors.
     *
     * @param errors the errors to be set in the ResponseObject
     */
    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final ResponseObject<?> other))
            return false;
        if (!other.canEqual(this)) return false;
        final Object this$data = this.getData();
        final Object other$data = other.getData();
        if (!Objects.equals(this$data, other$data))
            return false;
        return Objects.equals(this.getErrors(), other.getErrors());
    }

    /**
     * Check if the specified object can be equaled with this ResponseObject instance.
     *
     * @param other The object to compare with this ResponseObject instance.
     * @return true if the specified object is an instance of ResponseObject and can be equaled with this instance, false otherwise.
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ResponseObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = 1;
        final Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        final Object $errors = this.getErrors();
        result = result * 59 + ($errors == null ? 43 : $errors.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ResponseObject(data=" + this.getData() + ", errors=" + this.getErrors() + ")";
    }

    /**
     * {@link ResponseObject} builder static inner class.
     *
     * @param <T> type of returned object (mostly entity)
     */
    public static class ResponseObjectBuilder<T> {
        private T data;
        private List<Error> errors;

        ResponseObjectBuilder() {
        }

        /**
         * Sets the data for the {@link ResponseObjectBuilder}.
         *
         * @param data the data object to be set
         * @return the current instance of {@link ResponseObjectBuilder} for method chaining
         */
        public ResponseObjectBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        /**
         * Sets the list of errors for the {@link ResponseObjectBuilder}.
         *
         * @param errors the list of {@link java.lang.Error} objects to be set
         * @return the current instance of {@link ResponseObjectBuilder} for method chaining
         */
        public ResponseObjectBuilder<T> errors(List<Error> errors) {
            this.errors = errors;
            return this;
        }

        /**
         * Builder method for {@link ResponseObject}.
         *
         * @return the built {@link ResponseObject}
         */
        public ResponseObject<T> build() {
            return new ResponseObject<>(data, errors);
        }

        /**
         * Generates a string representation of the {@code ResponseObjectBuilder} instance,
         * including the {@code data} and {@code errors} properties.
         *
         * @return a string representation of the builder instance
         */
        public String toString() {
            return "ResponseObject.ResponseObjectBuilder(data=" + this.data + ", errors=" + this.errors + ")";
        }
    }
}
