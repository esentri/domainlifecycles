package fixtures.nondomain;

/**
 * A plain class implementing no domain marker interface, injected into
 * {@link SomeOutboundService}.
 */
public class NonDomainHelperClient {

    public String doSomething() {
        return "done";
    }
}
