package tests.shared.inheritance;

import lombok.Getter;

@Getter
public abstract class AbstractDomainCommand implements BasicDomainCommandType {
    protected final String msg;

    public AbstractDomainCommand(String msg) {
        this.msg = msg;
    }

}
