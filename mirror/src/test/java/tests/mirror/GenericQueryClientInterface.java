package tests.mirror;

import io.domainlifecycles.domain.types.ReadModel;
import io.domainlifecycles.domain.types.QueryClient;

public interface GenericQueryClientInterface<T extends ReadModel> extends QueryClient<T> {
}
