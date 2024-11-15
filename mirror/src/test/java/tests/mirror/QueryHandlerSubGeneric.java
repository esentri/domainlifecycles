package tests.mirror;

import io.domainlifecycles.domain.types.ReadModel;

public class QueryHandlerSubGeneric<T1, T2 extends ReadModel> implements GenericQueryHandlerInterface<T2> {
}
