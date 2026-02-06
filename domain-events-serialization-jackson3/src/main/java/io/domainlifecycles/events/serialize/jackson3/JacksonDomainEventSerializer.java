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

package io.domainlifecycles.events.serialize.jackson3;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.serialize.DomainEventSerializer;
import io.domainlifecycles.jackson3.module.DlcJacksonModule;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * A serializer implementation that uses Jackson 3 for serializing and deserializing domain events.
 * The {@code Jackson3DomainEventSerializer} class provides functionality to convert {@link DomainEvent}
 * objects into their JSON string representation and deserialize JSON strings back into domain event objects.
 * <p>
 * This implementation leverages the Jackson library, utilizing a custom {@link DlcJacksonModule}
 * for enhanced serialization and deserialization capabilities tailored to domain-specific requirements.
 * The module includes customizations and mappings for domain objects using a {@link DomainObjectBuilderProvider}.
 * <p>
 * The class supports two constructors: one default constructor that initializes the serializer with a
 * default {@link InnerClassDomainObjectBuilderProvider}, and another constructor that allows specifying
 * a custom {@code DomainObjectBuilderProvider}.
 *
 * @author Mario Herb
 */
public class JacksonDomainEventSerializer implements DomainEventSerializer {

    private final ObjectMapper objectMapper;

    /**
     * Constructs an instance of {@code Jackson3DomainEventSerializer} with a provided {@link ObjectMapper}.
     * This constructor allows customization of the serializer by injecting a pre-configured instance
     * of {@link ObjectMapper}, which is used for serializing and deserializing domain events.
     *
     * @param objectMapper the {@link ObjectMapper} instance used for JSON processing;
     *                     must not be null
     */
    public JacksonDomainEventSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Default constructor for the {@code Jackson3DomainEventSerializer} class.
     * This constructor initializes the serializer with a predefined configuration that includes
     * an instance of {@link DlcJacksonModule}, using an {@link InnerClassDomainObjectBuilderProvider}.
     * <p>
     * The {@link DlcJacksonModule} enhances the Jackson serialization process with domain-specific
     * customizations, enabling proper handling of domain events and their attributes. The
     * {@link InnerClassDomainObjectBuilderProvider} ensures support for constructing domain objects
     * during deserialization.
     * <p>
     * This configuration allows the serializer to process domain events with custom mappings and
     * ensure proper transformation between JSON strings and domain event objects.
     */
    public JacksonDomainEventSerializer() {
        objectMapper = JsonMapper.builder()
            .addModule(new DlcJacksonModule(new InnerClassDomainObjectBuilderProvider()))
            .build();
    }

    /**
     * Constructor for the {@code Jackson3DomainEventSerializer} class that allows
     * specifying a custom {@link DomainObjectBuilderProvider}.
     * This provider is utilized to configure the {@link DlcJacksonModule}, enabling
     * advanced domain-specific customizations for serialization and deserialization
     * of domain events.
     *
     * @param domainObjectBuilderProvider the provider responsible for customizing
     *                                     domain object construction during deserialization
     */
    public JacksonDomainEventSerializer(DomainObjectBuilderProvider domainObjectBuilderProvider) {
        objectMapper = JsonMapper.builder()
            .addModule(new DlcJacksonModule(domainObjectBuilderProvider))
            .build();
    }

    /**
     * Serializes a {@link DomainEvent} object into its JSON string representation.
     * Utilizes an underlying {@code ObjectMapper} instance with domain-specific configurations
     * to convert the provided event object into a JSON format.
     *
     * @param event the {@link DomainEvent} instance to be serialized; must not be null
     * @return a JSON string representation of the provided {@link DomainEvent} object
     */
    @Override
    public String serialize(DomainEvent event) {
        return objectMapper.writeValueAsString(event);
    }

    /**
     * Deserializes a JSON string representation of a {@link DomainEvent} into its corresponding domain event object.
     * This method uses the configured {@code ObjectMapper} to convert the given JSON string into an instance of the
     * specified {@code DomainEvent} subclass.
     *
     * @param serializedEvent the JSON string representation of the domain event to be deserialized; must not be null
     * @param cls the {@link Class} object representing the specific {@code DomainEvent} subclass to be deserialized into; must not be null
     * @return an instance of the specified {@code DomainEvent} subclass, deserialized from the provided JSON string
     */
    @Override
    public DomainEvent deserialize(String serializedEvent, Class<?extends DomainEvent> cls) {
        return objectMapper.readValue(serializedEvent, cls);
    }
}
