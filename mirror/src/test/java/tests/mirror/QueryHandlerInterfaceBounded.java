package tests.mirror;

import io.domainlifecycles.domain.types.QueryHandler;

public interface QueryHandlerInterfaceBounded<VM extends MyReadModel> extends QueryHandler<VM> {
}
