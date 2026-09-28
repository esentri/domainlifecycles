package fixtures.flowcalls;

// reads the summary it gets from the driver, holding no field of it
public class SummaryRule {

    private SummaryDriver summaryDriver;

    // calls SummaryDriver.compute and Summary.total, count, average and max
    public boolean check() {
        return true;
    }
}
