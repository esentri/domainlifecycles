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

package io.domainlifecycles.plugins.viewer.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.domainlifecycles.plugins.viewer.model.jackson.DomainMirrorJsonSerializer;
import java.util.List;

/**
 * A Data Transfer Object (DTO) that encapsulates the required information
 * for uploading a domain mirror to an external service or system.
 *
 * The class includes two primary attributes:
 * - `domainMirror`: A serialized JSON string representing the domain mirror,
 *   which is expected to be in its raw JSON form. This field is serialized using
 *   a custom serializer, {@code DomainMirrorJsonSerializer}, to ensure the raw
 *   JSON format is preserved during serialization.
 * - `domainModelPackages`: A list of package names associated with the domain mirror.
 *
 * This DTO is useful for constructing JSON payloads required for interaction
 * with external APIs or services, often in conjunction with tools that handle
 * domain models or diagram visualization.
 *
 * Constructor and accessors are provided for initializing and retrieving the
 * field values in an immutable instance of this class.
 *
 * @author Leon Völlinger
 */
public class DomainMirrorUploadDto {

    @JsonSerialize(using = DomainMirrorJsonSerializer.class)
    private final String domainMirror;
    private final List<String> domainModelPackages;

    /**
     * Constructs a {@code DomainMirrorUploadDto} with the specified domain mirror
     * and associated domain model packages.
     *
     * @param domainMirror a serialized JSON string representing the domain mirror,
     *                     which is preserved in its raw JSON format
     * @param domainModelPackages a list of package names associated with the domain mirror
     */
    public DomainMirrorUploadDto(String domainMirror, List<String> domainModelPackages) {
        this.domainMirror = domainMirror;
        this.domainModelPackages = domainModelPackages;
    }

    /**
     * Retrieves the domain mirror in its raw JSON string format.
     *
     * The domain mirror is serialized using a custom serializer to ensure
     * it remains in its original raw JSON form without any modifications
     * or transformations.
     *
     * @return a serialized JSON string representing the domain mirror
     */
    public String getDomainMirror() {
        return domainMirror;
    }

    /**
     * Retrieves the list of package names associated with the domain mirror.
     *
     * This list represents the domain model packages linked to the domain mirror,
     * which can be used for further processing or integration with external systems or tools.
     *
     * @return a list of package names associated with the domain mirror
     */
    public List<String> getDomainModelPackages() {
        return domainModelPackages;
    }
}
