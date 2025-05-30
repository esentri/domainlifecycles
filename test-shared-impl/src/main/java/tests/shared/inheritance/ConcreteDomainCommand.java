package tests.shared.inheritance;

import lombok.Builder;

public class ConcreteDomainCommand extends AbstractDomainCommand{

    @Builder
    public ConcreteDomainCommand(String msg) {
        super(msg);
    }
}
