package fixtures.flowcalls;

// a non-domain class handing out services, e.g. in place of an injection
public class ServiceLocator {

    public static SummaryDriver driver() {
        return null;
    }

    public static EscalationService escalation() {
        return null;
    }
}
