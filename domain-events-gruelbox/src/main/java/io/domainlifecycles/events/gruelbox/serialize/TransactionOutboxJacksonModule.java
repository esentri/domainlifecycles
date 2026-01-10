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

package io.domainlifecycles.events.gruelbox.serialize;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gruelbox.transactionoutbox.Invocation;
import com.gruelbox.transactionoutbox.TransactionOutboxEntry;
import tools.jackson.core.Version;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.TypeResolverBuilder;
import tools.jackson.databind.jsontype.impl.DefaultTypeResolverBuilder;
import tools.jackson.databind.module.SimpleDeserializers;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.module.SimpleSerializers;

import static tools.jackson.databind.DefaultTyping.NON_FINAL;

/**
 * Jackson 3 module for serializing and deserializing TransactionOutboxEntry and Invocation objects.
 *
 * @author Mario Herb
 */
public class TransactionOutboxJacksonModule extends SimpleModule {

    /**
     * Returns the name of this module.
     *
     * @return module name
     */
    @Override
    public String getModuleName() {
        return "TransactionOutboxJackson3Module";
    }

    /**
     * Returns the version information for this module.
     *
     * @return module version
     */
    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    /**
     * Initializes the module with custom serializers and deserializers.
     *
     * @param setupContext
     */
    @Override
    public void setupModule(JacksonModule.SetupContext setupContext) {
        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(Invocation.class, new CustomInvocationSerializer());
        setupContext.addSerializers(serializers);

        SimpleDeserializers deserializers = new SimpleDeserializers();
        deserializers.addDeserializer(Invocation.class, new CustomInvocationDeserializer());
        deserializers.addDeserializer(
            TransactionOutboxEntry.class, new TransactionOutboxEntryDeserializer());
        setupContext.addDeserializers(deserializers);
    }

    /**
     * Returns a type resolver builder for handling polymorphic types.
     *
     * @return type resolver builder
     */
    public static TypeResolverBuilder<?> typeResolver() {
        return new DefaultTypeResolverBuilder(
            BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class).build(),
            NON_FINAL,
            JsonTypeInfo.As.WRAPPER_OBJECT,
            JsonTypeInfo.Id.CLASS,
            null
        );
    }
}
