package fixtures.topcommand;

import io.domainlifecycles.domain.types.DomainCommand;

public record TopCommand(Long value) implements DomainCommand {
}
