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
 *  Copyright 2019-2023 the original author or authors.
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

package nitrox.dlc.spring.http;

import java.util.List;
import java.util.Objects;

/**
 * General http response structure
 *
 * @param <T> type of returned object (mostly entity)
 *
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
     * Get data
     */
    public T getData() {
        return this.data;
    }

    /**
     * Set data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Get errors.
     */
    public List<Error> getErrors() {
        return this.errors;
    }

    /**
     * Set errors.
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
     * Returns true, if other is a ResponseObject
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
     * Inner Builder for ResponseObject instances.
     */
    public static class ResponseObjectBuilder<T> {
        private T data;
        private List<Error> errors;

        ResponseObjectBuilder() {
        }

        public ResponseObjectBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseObjectBuilder<T> errors(List<Error> errors) {
            this.errors = errors;
            return this;
        }

        public ResponseObject<T> build() {
            return new ResponseObject<>(data, errors);
        }

        public String toString() {
            return "ResponseObject.ResponseObjectBuilder(data=" + this.data + ", errors=" + this.errors + ")";
        }
    }
}
