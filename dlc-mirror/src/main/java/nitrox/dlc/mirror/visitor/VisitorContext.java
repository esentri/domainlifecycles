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

package nitrox.dlc.mirror.visitor;

import nitrox.dlc.mirror.api.DomainTypeMirror;
import nitrox.dlc.mirror.api.FieldMirror;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Holds the context information of a {@link ContextDomainObjectVisitor}.
 * Enables to query the current path of a field being visited, or to query the currently visited domain type.
 *
 * @author Mario Herb
 */
public class VisitorContext {

    private final List<FieldMirror> currentPath = new ArrayList<>();
    private DomainTypeMirror currentDomainTypeMirror;

    /**
     * starting type name (full qualified class name)
     */
    public final String startingTypeName;
    private final Set<DomainTypeMirror> alreadyVisitedTypeMirrors = new HashSet<>();
    private Optional<FieldMirror> currentFieldMirror;

    protected VisitorContext(String startingTypeName) {
        this.startingTypeName = startingTypeName;
    }

    protected void enterType(DomainTypeMirror type){
        currentDomainTypeMirror = type;
        alreadyVisitedTypeMirrors.add(type);
    }

    protected void leaveType(DomainTypeMirror type){
        currentDomainTypeMirror = type;
    }

    protected void visitFieldStart(FieldMirror fieldMirror){
        currentFieldMirror = Optional.of(fieldMirror);
        currentPath.add(fieldMirror);
    }

    protected void visitFieldEnd(FieldMirror fieldMirror){
        currentFieldMirror = Optional.empty();
        removeLastPathEntry();
    }

    private void removeLastPathEntry() {
        if (!currentPath.isEmpty()) {
            currentPath.remove(currentPath.size() - 1);
        }
    }

    /**
     * Return the current field mirror being visited
     */
    public Optional<FieldMirror> getCurrentFieldMirror() {
        return currentFieldMirror;
    }

    /**
     * Return the current path to the currently visited structure
     * @return current path
     */
    public List<FieldMirror> getCurrentPath() {
        return currentPath;
    }


    /**
     * The current type being visited.
     *
     * @return currently visited type
     */
    public DomainTypeMirror getCurrentType() {
        return currentDomainTypeMirror;
    }

    /**
     * Was the type already visited. Also used to break cycles.
     *
     * @param typeName to be checked
     * @return true if type was already visited, else false
     */
    public boolean isAlreadyVisited(String typeName){
        return alreadyVisitedTypeMirrors.stream().map(DomainTypeMirror::getTypeName)
            .anyMatch(t -> t.equals(typeName));
    }
}
