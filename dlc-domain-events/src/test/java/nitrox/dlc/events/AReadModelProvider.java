package nitrox.dlc.events;

import nitrox.dlc.domain.types.DomainEvent;
import nitrox.dlc.domain.types.ListensTo;
import nitrox.dlc.domain.types.ReadModelProvider;

import java.util.ArrayList;
import java.util.List;

public class AReadModelProvider implements ReadModelProvider {

    public List<DomainEvent> received = new ArrayList<>();
    @ListensTo(domainEventType = ADomainEvent.class)
    public void onADomainEvent(ADomainEvent domainEvent){
        System.out.println("ADomainEvent received in AReadModelProvider! Message = " + domainEvent.message());
        received.add(domainEvent);
    }

    @ListensTo(domainEventType = PassThroughDomainEvent.class)
    public void onDomainEvent(PassThroughDomainEvent domainEvent){
        System.out.println("PassThroughDomainEvent received in AReadModelProvider! Message = " + domainEvent.message());
        received.add(domainEvent);
    }
}
