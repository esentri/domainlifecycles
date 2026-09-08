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

package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.Objects;

/**
 * A callable node of the domain call graph: a mirrored method together with the concrete type that
 * owns it.
 * <p>
 * The owning type is deliberately not the declaring type of the method. An inherited method is
 * attributed to the concrete class that owns it, so {@code MyOverridingService.process} and
 * {@code MyBaseService.process} are two distinct nodes even though the mirror reports the same
 * declaring type for both. Losing that distinction would report a base class where the analysis
 * determined the overriding subclass.
 * <p>
 * This is the node type of both {@link DomainCalls} and {@link Flow}. An edge between two nodes is
 * a {@link DomainCalls.CallSite} in the call graph and a {@link Step} in a flow.
 *
 * @param typeName the full qualified name of the concrete owner type
 * @param mirror   the mirrored method
 * @author Mario Herb
 */
public record DomainMethod(String typeName, MethodMirror mirror) {

    public DomainMethod {
        Objects.requireNonNull(typeName, "An owning type name must be given!");
        Objects.requireNonNull(mirror, "A MethodMirror must be given!");
    }

    /**
     * @return the name of the mirrored method
     */
    public String name() {
        return mirror.getName();
    }

    /**
     * Returns the method signature without the owner type, e.g.
     * {@code handle(java.lang.String)}. Identifies the method within its owner type, which makes
     * it usable as a key.
     *
     * @return the method name and its parameter types
     */
    public String signature() {
        var sb = new StringBuilder();
        sb.append(mirror.getName()).append("(");
        var start = true;
        for (var param : mirror.getParameters()) {
            if (!start) {
                sb.append(", ");
            } else {
                start = false;
            }
            sb.append(param.getType().getTypeName());
        }
        return sb.append(")").toString();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(typeName).append(".");
        appendType(sb, mirror.getReturnType().getContainerTypeName().orElse(null),
            mirror.getReturnType().getTypeName());
        sb.append(" ");
        sb.append(mirror.getName()).append("(");
        var start = true;
        for (var param : mirror.getParameters()) {
            if (!start) {
                sb.append(", ");
            } else {
                start = false;
            }
            appendType(sb, param.getType().getContainerTypeName().orElse(null),
                param.getType().getTypeName());
        }
        return sb.append(")").toString();
    }

    private static void appendType(StringBuilder sb, String containerTypeName, String typeName) {
        if (containerTypeName != null) {
            sb.append(containerTypeName).append("<").append(typeName).append(">");
        } else {
            sb.append(typeName);
        }
    }
}
