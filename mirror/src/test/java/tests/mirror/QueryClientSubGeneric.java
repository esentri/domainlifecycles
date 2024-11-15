package tests.mirror;

import io.domainlifecycles.domain.types.ReadModel;

public class QueryClientSubGeneric<T1, T2 extends ReadModel> implements GenericQueryClientInterface<T2> {
}
