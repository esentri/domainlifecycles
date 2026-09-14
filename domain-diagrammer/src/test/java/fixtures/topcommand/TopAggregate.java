package fixtures.topcommand;

import io.domainlifecycles.domain.types.base.AggregateRootBase;

/**
 * Also processes {@link TopCommand} directly - a delegation chain where the Aggregate,
 * {@link InnerDomainService} and {@link OuterApplicationService} all technically process the same
 * command, the way it happens when a service forwards a command down into the Aggregate method that
 * ultimately does the work.
 */
public class TopAggregate extends AggregateRootBase<TopAggregateId> {

    private final TopAggregateId id;

    public TopAggregate(TopAggregateId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }

    public void handle(TopCommand command) {
        // processes TopCommand directly
    }
}
