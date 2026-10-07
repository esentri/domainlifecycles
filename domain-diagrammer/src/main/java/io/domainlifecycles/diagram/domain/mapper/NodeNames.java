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

package io.domainlifecycles.diagram.domain.mapper;

import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * The names of the nodes of a diagram. Nomnoml identifies a node by its name, so two classes sharing a simple name
 * would be drawn as one node - e.g. two services of the same name in different Bounded Contexts. Such classes get a
 * hint to their package appended: the part of the package in which they differ, e.g.
 * {@code OrderService (billing.domain)} and {@code OrderService (shipping.domain)} for
 * {@code com.example.billing.domain.OrderService} and {@code com.example.shipping.domain.OrderService}.
 *
 * @author Mario Herb
 */
public final class NodeNames {

    private final DomainDiagramConfig domainDiagramConfig;
    private final DomainMirror domainMirror;
    /** the package hint by the full qualified name a node is shown with, only for names shared by several classes */
    private final Map<String, String> packageHints;

    private NodeNames(DomainDiagramConfig domainDiagramConfig, DomainMirror domainMirror,
                      Map<String, String> packageHints) {
        this.domainDiagramConfig = domainDiagramConfig;
        this.domainMirror = domainMirror;
        this.packageHints = packageHints;
    }

    /**
     * @param domainDiagramConfig diagram configuration
     * @return node names as derived from the class names alone, without any package hint
     */
    public static NodeNames withoutPackageHints(DomainDiagramConfig domainDiagramConfig) {
        return new NodeNames(domainDiagramConfig, null, Map.of());
    }

    /**
     * @param nodes               the classes drawn as nodes of the diagram
     * @param domainMirror        the domain the classes belong to
     * @param domainDiagramConfig diagram configuration
     * @return the node names of the diagram, with a package hint for the classes sharing a name
     */
    public static NodeNames of(Collection<? extends DomainTypeMirror> nodes,
                               DomainMirror domainMirror,
                               DomainDiagramConfig domainDiagramConfig) {
        // several classes may be drawn as one node on purpose, e.g. the implementations of a service interface
        Map<String, Set<String>> shownTypeNamesByName = nodes.stream()
            .map(node -> DomainMapperUtils.shownTypeName(node, domainDiagramConfig))
            .distinct()
            .collect(Collectors.groupingBy(
                typeName -> DomainMapperUtils.mapTypeName(typeName, domainDiagramConfig),
                Collectors.toCollection(TreeSet::new)));
        Map<String, String> packageHints = new HashMap<>();
        shownTypeNamesByName.values().stream()
            .filter(typeNames -> typeNames.size() > 1)
            .forEach(typeNames -> packageHints.putAll(packageHints(typeNames)));
        return new NodeNames(domainDiagramConfig, domainMirror, packageHints);
    }

    /**
     * @param domainTypeMirror a class drawn as node
     * @return the name of its node
     */
    public String name(DomainTypeMirror domainTypeMirror) {
        return withPackageHint(DomainMapperUtils.shownTypeName(domainTypeMirror, domainDiagramConfig));
    }

    /**
     * @param fullQualifiedTypeName the full qualified name of a class drawn as node
     * @return the name of its node
     */
    public String name(String fullQualifiedTypeName) {
        if (domainMirror != null) {
            var domainTypeMirror = domainMirror.getDomainTypeMirror(fullQualifiedTypeName);
            if (domainTypeMirror.isPresent()) {
                return name(domainTypeMirror.get());
            }
        }
        return withPackageHint(fullQualifiedTypeName);
    }

    private String withPackageHint(String shownTypeName) {
        var name = DomainMapperUtils.mapTypeName(shownTypeName, domainDiagramConfig);
        var packageHint = packageHints.get(shownTypeName);
        return packageHint == null ? name : name + " (" + packageHint + ")";
    }

    /**
     * The packages of classes sharing a name, below the package they have in common. A class right in that common
     * package is named by its full package.
     */
    private static Map<String, String> packageHints(Set<String> typeNames) {
        List<List<String>> packages = typeNames.stream()
            .map(NodeNames::packageSegments)
            .toList();
        int common = commonPrefixLength(packages);
        Map<String, String> packageHints = new HashMap<>();
        for (String typeName : typeNames) {
            List<String> segments = packageSegments(typeName);
            List<String> distinct = segments.subList(common, segments.size());
            packageHints.put(typeName, String.join(".", distinct.isEmpty() ? segments : distinct));
        }
        return packageHints;
    }

    private static List<String> packageSegments(String typeName) {
        int lastDot = typeName.lastIndexOf('.');
        return lastDot < 0 ? List.of() : Arrays.asList(typeName.substring(0, lastDot).split("\\."));
    }

    private static int commonPrefixLength(List<List<String>> packages) {
        int length = 0;
        while (true) {
            final int index = length;
            if (packages.stream().anyMatch(segments -> segments.size() <= index)) {
                return length;
            }
            String segment = packages.get(0).get(index);
            if (packages.stream().anyMatch(segments -> !segments.get(index).equals(segment))) {
                return length;
            }
            length++;
        }
    }
}
