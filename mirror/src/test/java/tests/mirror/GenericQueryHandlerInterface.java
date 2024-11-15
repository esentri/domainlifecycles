package tests.mirror;

import io.domainlifecycles.domain.types.QueryHandler;
import io.domainlifecycles.domain.types.ReadModel;

public interface GenericQueryHandlerInterface<T extends ReadModel> extends QueryHandler<T> {
}
