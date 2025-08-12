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

package io.domainlifecycles.diagram.domain.notes;

import java.util.Objects;

/**
 * This record is used to pass notes to the diagram generator, that should be rendered
 * in the diagram.
 *
 * @param className full qualified name of the class the note is attached to
 * @param text the note text
 *
 * @author Mario Herb
 */
public record DomainClassNote(String className, String text) {

    /**
     * Constructor for a note.
     *
     * @param className the full qualified class name of the class the note is assigned to
     * @param text the not text to be rendered
     */
    public DomainClassNote(String className, String text) {
        this.className = Objects.requireNonNull(className, "A full qualified class name ist required to assign the note");
        this.text = Objects.requireNonNull(text, "A note text is required");
    }
}
