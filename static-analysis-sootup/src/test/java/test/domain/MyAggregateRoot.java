package test.domain;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MyAggregateRoot extends AggregateRootBase<MyAggregateRoot.Id> {

    private Id id;

    private String name;

    private long concurrencyVersion;

    @Builder
    public MyAggregateRoot(long concurrencyVersion, Id id, String name, long concurrencyVersion1) {
        super(concurrencyVersion);
        this.id = id;
        this.name = name;
        this.concurrencyVersion = concurrencyVersion1;
    }

    public record Id(Long value) implements Identity<Long> {}

    @Publishes(domainEventTypes = MyDomainEvent.class)
    public void doSomething(MyDomainCommand command) {

    }

    public void doSomethingNoArg() {
        // some domain logic
    }

    public void handle(MyDomainCommand command) {
        // domain logic for the command overload
    }

    public void handle(MyAggregateRoot.Id id) {
        // domain logic for the id overload
    }

    // In MyAggregateRoot
    public static boolean staticValidate(MyAggregateRoot a) {
        // some domain-level static check. A static body has no `this`, so nothing here can be
        // redispatched - the call goes to the parameter's static type.
        a.doSomethingNoArg();
        return true;
    }

    public String describe() {
        return "...";   // domain-level instance method
    }

}
