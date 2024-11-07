package tests.mirror;

import io.domainlifecycles.domain.types.QueryClient;

public interface QueryClientInterfaceBounded<VM extends MyReadModel> extends QueryClient<VM> {
}
