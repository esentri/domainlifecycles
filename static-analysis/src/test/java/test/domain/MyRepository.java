package test.domain;

import io.domainlifecycles.domain.types.Repository;

import java.util.List;

public interface MyRepository extends Repository<MyAggregateRoot.Id, MyAggregateRoot> {

    List<MyAggregateRoot> findSome(String searchName);

    void someInheritedOperation();

}
