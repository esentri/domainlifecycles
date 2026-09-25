package fixtures.backwardflow;

import io.domainlifecycles.domain.types.DomainCommand;

/**
 * Only used to verify that a command is rejected as a backward flow target.
 */
public class TargetCommand implements DomainCommand {
}
