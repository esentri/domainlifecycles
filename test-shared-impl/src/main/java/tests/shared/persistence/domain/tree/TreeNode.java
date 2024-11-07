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

package tests.shared.persistence.domain.tree;

import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.EntityBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Getter
public class TreeNode extends EntityBase<TreeNodeId> {

    @Id
    private final TreeNodeId id;

    private Optional<TreeNodeId> parentNodeId;

    private String nodeName;

    private Optional<TreeRootId> rootId;

    private List<TreeNode> childNodes;

    @Builder(setterPrefix = "set")
    public TreeNode(TreeNodeId id,
                    TreeRootId rootId,
                    TreeNodeId parentNodeId,
                    long concurrencyVersion,
                    String nodeName,
                    List<TreeNode> childNodes
    ) {
        super(concurrencyVersion);
        this.id = id;
        DomainAssertions.isNotNull(id, "Eine ID muss angegeben sein!");
        this.rootId = Optional.ofNullable(rootId);
        this.parentNodeId = Optional.ofNullable(parentNodeId);
        setNodeName(nodeName);
        this.childNodes = (childNodes == null ? new ArrayList<>() : childNodes);
    }

    public void setNodeName(String nodeName) {
        DomainAssertions.isNotEmpty(nodeName, "Der Name darf nicht leer sein!");
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    @Override
    public void validate() {
        DomainAssertions.isTrue(
            parentNodeId.isPresent() ^ rootId.isPresent(),
            "Ein Knoten muss entweder dem TreeRoot oder einem anderen TreeNode untergeordnet sein!"
        );
    }
}
