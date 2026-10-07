package fixtures.flowcalls;

// an exception, never shown
public class BillingException extends RuntimeException {

    public BillingException(String message) {
        super(message);
    }

    public String reason() {
        return getMessage();
    }
}
