package fixtures.flowcalls;

// a non-domain class holding another one
public class ReportController {

    private ReportHelper reportHelper;

    private SummaryDriver summaryDriver;

    // calls ReportHelper.render
    public String show() {
        return "";
    }
}
