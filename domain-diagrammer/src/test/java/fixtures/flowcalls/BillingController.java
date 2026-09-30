package fixtures.flowcalls;

// reaches the Invoice aggregate over BillingService and its repository
public class BillingController {

    private BillingService billingService;

    // calls BillingService.bill and Invoice.amount
    public void bill() {
    }
}
