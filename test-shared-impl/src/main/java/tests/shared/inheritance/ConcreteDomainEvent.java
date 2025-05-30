package tests.shared.inheritance;


import lombok.Builder;
import lombok.Getter;

@Getter
public class ConcreteDomainEvent implements BasicDomainEventType{
    final String msg;

    @Builder
    public ConcreteDomainEvent(String msg) {
        this.msg = msg;
    }
}
