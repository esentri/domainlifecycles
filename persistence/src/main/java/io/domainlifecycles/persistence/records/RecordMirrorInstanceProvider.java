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

package io.domainlifecycles.persistence.records;

import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.mirror.api.EntityRecordMirror;
import io.domainlifecycles.persistence.mirror.api.ValueObjectRecordMirror;

import java.util.List;
import java.util.Map;

/**
 * Provides record mirrors.
 *
 * @author Mario Herb
 */
public interface RecordMirrorInstanceProvider<BASE_RECORD_TYPE> {


    EntityRecordMirror<BASE_RECORD_TYPE> provideEntityRecordMirror(String recordTypeName,
                                                 String entityTypeName,
                                                 RecordMapper<BASE_RECORD_TYPE, ?, ?> mapper,
                                                 List<ValueObjectRecordMirror<BASE_RECORD_TYPE>> valueObjectRecordMirrors,
                                                 Map<String, List<String>> recordCanonicalNameToDomainObjectTypeMap);

    /**
     * Provides the value object record mirror for a record type
     */
    ValueObjectRecordMirror<BASE_RECORD_TYPE> provideValueObjectRecordMirror(String containingEntityTypeName,
                                                                    String containedValueObjectTypeName,
                                                                    Class<? extends BASE_RECORD_TYPE> valueObjectRecordType,
                                                                    List<String> pathFromEntityToValueObject,
                                                                    RecordMapper<BASE_RECORD_TYPE, ?, ?> mapper,
                                                                    Map<String, List<String>> recordCanonicalNameToDomainObjectTypeMap);

}
