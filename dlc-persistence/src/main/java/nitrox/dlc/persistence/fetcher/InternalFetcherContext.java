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

package nitrox.dlc.persistence.fetcher;

import nitrox.dlc.mirror.api.FieldMirror;
import nitrox.dlc.persistence.fetcher.simple.SimpleFetcherContext;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * InternalFetcherContext is the base class for all internal fetcher contexts.
 *
 * @param <RECORD> the record type
 *
 * @author Mario Herb
 */
public class InternalFetcherContext<RECORD> extends SimpleFetcherContext<RECORD> implements FetcherContext<RECORD> {

    private final Set<BackRef<RECORD>> backReferences = new HashSet<>();

    /**
     * Returns the back references.
     *
     * @return the back references
     */
    public Set<BackRef<RECORD>> getBackReferences() {
        return this.backReferences;
    }

    protected void addBackRef(FieldMirror entityReferenceMirror, RECORD ownerRecord, RECORD referencedRecord) {
        backReferences.add(new BackRef<>(entityReferenceMirror, ownerRecord, referencedRecord));
    }

    /**
     * BackRef represents a back reference.
     *
     * @param <RECORD> the record type
     */
    public static class BackRef<RECORD> {

        private final FieldMirror fieldMirror;
        private final RECORD ownerRecord;
        private final RECORD referencedRecord;

        /**
         * Creates a new BackRef.
         *
         * @param fieldMirror that field, that constitutes the back reference
         * @param ownerRecord the owner record
         * @param referencedRecord the referenced record
         */
        public BackRef(FieldMirror fieldMirror, RECORD ownerRecord, RECORD referencedRecord) {
            Objects.requireNonNull(fieldMirror);
            Objects.requireNonNull(ownerRecord);
            Objects.requireNonNull(referencedRecord);
            this.fieldMirror = fieldMirror;
            this.ownerRecord = ownerRecord;
            this.referencedRecord = referencedRecord;
        }

        /**
         * Returns the FieldMirror that represents the back reference.
         */
        public FieldMirror getBackReferenceMirror() {
            return fieldMirror;
        }

        /**
         * Returns the owner record.
         *
         * @return the owner record
         */
        public RECORD getOwnerRecord() {
            return ownerRecord;
        }

        /**
         * Returns the referenced record.
         *
         * @return the referenced record
         */
        public RECORD getReferencedRecord() {
            return referencedRecord;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BackRef<?> backRef = (BackRef<?>) o;
            return fieldMirror.equals(backRef.fieldMirror) &&
                ownerRecord.equals(backRef.ownerRecord) &&
                ownerRecord.getClass().equals(backRef.ownerRecord.getClass()) &&
                referencedRecord.equals(backRef.referencedRecord) &&
                referencedRecord.getClass().equals(backRef.referencedRecord.getClass());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(fieldMirror, ownerRecord, referencedRecord);
        }
    }
}
