package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.model.RepositoryModel;
import io.domainlifecycles.mirror.model.ServiceKindModel;

/**
 * Builder to create {@link ServiceKindMirrorBuilder}
 *
 * @author leonvoellinger
 */
public class ServiceKindMirrorBuilder extends DomainTypeMirrorBuilder {

    private final Class<? extends ServiceKind> serviceKindClass;

    public ServiceKindMirrorBuilder(Class<? extends ServiceKind> serviceKindClass) {
        super(serviceKindClass);
        this.serviceKindClass = serviceKindClass;
    }

    /**
     * Creates a new {@link ServiceKindMirror}.
     *
     * @return new instance of ServiceKindMirror
     */
    public ServiceKindMirror build() {
        return new ServiceKindModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            buildMethods(),
            buildInheritanceHierarchy(),
            buildInterfaceTypes()
        );
    }
}
