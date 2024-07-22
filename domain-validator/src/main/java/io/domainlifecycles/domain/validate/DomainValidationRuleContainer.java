package io.domainlifecycles.domain.validate;

import io.domainlifecycles.mirror.api.DomainType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainValidationRuleContainer {

    private List<String> disabledRuleIds = new ArrayList<>();
    private Map<DomainType, List<DomainValidationRule>> rules = new HashMap<>();

    private void load(){

    }


    public void disableRules(String... ruleIds){
        disabledRuleIds.addAll(Arrays.stream(ruleIds).toList());
    }

    public void addRules(DomainValidationRule... addedRules){
        Arrays.stream(addedRules).forEach(rule -> rules.computeIfAbsent(rule.applicableOn(), s -> new ArrayList<>()).add(rule));
    }
}
