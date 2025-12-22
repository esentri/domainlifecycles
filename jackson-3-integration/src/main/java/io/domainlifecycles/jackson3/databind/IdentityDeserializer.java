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

package io.domainlifecycles.jackson3.databind;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.deser.std.StdDeserializer;
import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.jackson3.exception.DLCJacksonException;
import io.domainlifecycles.mirror.api.Domain;

import java.io.IOException;

/**
 * {@link Domain} based deserialization of {@link Identity} instances.
 *
 * @author Mario Herb
 * @see StdDeserializer
 */
public class IdentityDeserializer extends StdDeserializer<Identity<?>> {

    /**
     * Constructs an IdentityDeserializer instance with the specified JavaType.
     *
     * @param valueType the JavaType to be used for deserialization
     */
    public IdentityDeserializer(
        JavaType valueType
    ) {
        super(valueType);
    }

    /**
     * Deserialize Identities.
     *
     * @param jsonParser             Parsed used for reading JSON content
     * @param deserializationContext Context that can be used to access information about
     *                               this deserialization activity.
     * @throws JacksonException if deserialization fails
     */
    @Override
    public Identity<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JacksonException {

        var idTypeName = this._valueType.getRawClass().getName();
        while (jsonParser.hasCurrentToken()) {
            if (jsonParser.currentToken().isScalarValue()) {
                var idMirror = Domain.identityMirrorFor(idTypeName);

                Class<?> idValueType = DlcAccess.getClassForName(
                    idMirror.getValueTypeName()
                        .orElseThrow(() -> DLCJacksonException.fail(
                            "Identity Deserialization failed for '%s'. ValueType not defined",
                            this._valueType.getTypeName()))
                );
                Object val = jsonParser.readValueAs(idValueType);
                if (val != null) {
                    return DlcAccess.newIdentityInstance(val, idTypeName);
                } else {
                    return null;
                }
            }
            jsonParser.nextToken();
        }
        throw DLCJacksonException.fail("Identity Deserialization failed for '%s'.", this._valueType.getTypeName());
    }

}
