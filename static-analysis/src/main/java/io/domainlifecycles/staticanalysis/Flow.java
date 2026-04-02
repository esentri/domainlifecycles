package io.domainlifecycles.staticanalysis;

import java.util.List;

public class Flow {

    private final List<Step> steps;

    Flow(List<Step> steps) {
        this.steps = steps;
    }

    public List<Step> getSteps() {
        return steps;
    }
}
