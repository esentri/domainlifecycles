package test.domain;

import io.domainlifecycles.domain.types.ApplicationService;

import java.util.Optional;

public class MyApplicationService implements ApplicationService {

    private final MyRepository repository;

    private final MyConcreteBaseService baseService;

    public MyApplicationService(MyRepository repository, MyConcreteBaseService baseService) {
        this.repository = repository;
        this.baseService = baseService;
    }

    // Case Q: the static type at the call site is a CONCRETE class that is nevertheless
    // overridden. Both MyConcreteBaseService.execute and MyExtendingService.execute can run.
    public void doCallOnConcreteBase(MyDomainCommand command) {
        baseService.execute(command);
    }

    public void doSomething(MyDomainCommand command) {
        var agg = repository.findById(command.id());
        agg.ifPresent(a -> {
           a.doSomething(command);
           repository.update(a);
        });
    }

    // In MyApplicationService

    // Case A: nested lambdas (lambda in lambda in lambda),
// domain call only at the innermost level
    public void doNestedLambdas(MyDomainCommand command) {
        var agg = repository.findById(command.id());
        agg.ifPresent(a -> {
            Runnable outer = () -> {
                Runnable inner = () -> {
                    a.doSomething(command);   // innermost lambda body
                };
                inner.run();
            };
            outer.run();
        });
    }

    // Case B: method reference instead of a lambda expression
    public void doMethodReference(MyDomainCommand command) {
        var agg = repository.findById(command.id());
        // method reference to a domain method of the aggregate
        agg.ifPresent(this::handle);   // calls handle(MyAggregateRoot) -> a.doSomething(...)
        // direct method reference to a repository method
        agg.ifPresent(repository::update);
    }

    private void handle(MyAggregateRoot a) {
        a.doSomething(null);   // domain call so the method-reference path carries a target
    }

    // Case C: lambda built here, executed elsewhere
    private java.util.function.Consumer<MyAggregateRoot> updater;

    public void buildDeferredLambda(MyDomainCommand command) {
        // the lambda is DEFINED here; the domain call sits in the lambda body
        this.updater = a -> repository.update(a);
    }

    public void runDeferredLambda(MyDomainCommand command) {
        // the lambda is EXECUTED here, but not defined here
        var agg = repository.findById(command.id());
        agg.ifPresent(updater);
    }

    // Case D: call to a private helper method (which is itself in the mirror).
    // doViaHelper calls helper(...), and helper(...) itself calls a domain method.
    public void doViaHelper(MyDomainCommand command) {
        var agg = repository.findById(command.id());
        agg.ifPresent(a -> helper(a, command));
    }

    // private helper - present in the mirror as a domain method of MyApplicationService
    private void helper(MyAggregateRoot a, MyDomainCommand command) {
        a.doSomething(command);   // domain call made from inside the helper
    }

    // Case E: unbound instance method reference (MyAggregateRoot::doSomethingNoArg).
    // The receiver is NOT bound here; it becomes the argument of the functional
    // interface. forEach calls aggregateRoot.doSomethingNoArg() for each element.
    public void doUnboundMethodReference(MyDomainCommand command) {
        var agg = repository.findById(command.id());
        // Stream of aggregates, unbound reference: each element is the receiver
        agg.stream().forEach(MyAggregateRoot::doSomethingNoArg);
    }

    // Case G: overloaded methods - same name, different parameter types.
    // Both overloads are called; each call must resolve to the matching overload,
    // not collapse onto the name.
    public void doOverloaded(MyDomainCommand command) {
        var agg = repository.findById(command.id());
        agg.ifPresent(a -> {
            a.handle(command);              // calls handle(MyDomainCommand)
            a.handle(command.id());         // calls handle(MyAggregateRoot.Id)
        });
    }

    // Case H: generic method with a compiler-generated bridge method in the impl.
    // findById is declared on the generic MyRepository<A> as Optional<A> findById(Identity).
    // The call here goes through the MyRepository interface type, so the call site
    // carries the erased signature (which matches the bridge method in the impl).
    public void doGenericCall(MyDomainCommand command) {
        Optional<MyAggregateRoot> agg = repository.findById(command.id());
        agg.ifPresent(a -> a.doSomething(command));
    }

    // Case I: static method reference (MyAggregateRoot::staticValidate).
    // Nothing is bound; the method handle points directly at the static method.
    // boundReceiverType should yield no usable domain receiver, so the fallback
    // (report against the handle type) must apply.
    public void doStaticMethodReference(MyDomainCommand command) {
        var agg = repository.findById(command.id());
        // static reference used as a Predicate<MyAggregateRoot>
        agg.filter(MyAggregateRoot::staticValidate)
            .ifPresent(a -> a.doSomething(command));
    }

    public void doStaticMethodReferenceCrossType(MyDomainCommand command) {
        var agg = repository.findById(command.id());
        // static reference on MyDomainService, first param is MyAggregateRoot
        agg.filter(MyDomainService::validate)
            .ifPresent(a -> a.doSomething(command));
    }

    // Case K: unbound instance method reference (MyAggregateRoot::compareTo style).
    // The receiver is NOT bound; it becomes the first parameter of the functional
    // interface. Used here as a BiFunction where the first arg is the receiver type.
    public void doUnboundMethodReference2(MyDomainCommand command) {
        var agg = repository.findById(command.id());
        // unbound reference: MyAggregateRoot::describe, used as Function<MyAggregateRoot, String>
        agg.map(MyAggregateRoot::describe)
            .ifPresent(s -> {});
        agg.ifPresent(a -> a.doSomething(command));
    }

    public void doMethodRefOnInherited(MyDomainCommand command) {
        Runnable r = repository::someInheritedOperation;
        r.run();
    }

    // Case L1: a lambda that calls its own enclosing method (lambda -> enclosing).
    public void recursiveViaLambda(MyDomainCommand command) {
        var agg = repository.findById(command.id());
        agg.ifPresent(a -> {
            a.doSomething(command);
            recursiveViaLambda(command);   // lambda calls the enclosing method again
        });
    }

    // Case L2: direct self-recursion plus a domain call.
    public void directRecursion(MyDomainCommand command) {
        repository.findById(command.id());
        directRecursion(command);          // direct self-call
    }

    // Case L3: mutual recursion across two methods, each via a lambda.
    public void mutualA(MyDomainCommand command) {
        var agg = repository.findById(command.id());
        agg.ifPresent(a -> mutualB(command));   // A's lambda calls B
    }

    public void mutualB(MyDomainCommand command) {
        var agg = repository.findById(command.id());
        agg.ifPresent(a -> {
            a.doSomething(command);
            mutualA(command);                    // B's lambda calls A
        });
    }

}
