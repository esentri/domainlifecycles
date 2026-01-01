package io.dommainlifecycles.events.gruelbox;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SerializationStressTestInput {
    private boolean enabled = false;
    private MonetaryAmount amount = MonetaryAmount.ONE_HUNDRED_GBP;
    private Set<String> investments = Set.of("investment1", "investment2", "investment3");
    private Map<String, MonetaryAmount> investmentAmounts =
        Map.of(
            "investment1",
            MonetaryAmount.ofGbp("33"),
            "investment2",
            MonetaryAmount.ofGbp("34"),
            "investment3",
            MonetaryAmount.ofGbp("33"));

    public SerializationStressTestInput() {
    }

    @Override
    public String toString() {
        return "SerializationStressTestInput{" +
            "enabled=" + enabled +
            ", amount=" + amount +
            ", investments=" + investments +
            ", investmentAmounts=" + investmentAmounts +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SerializationStressTestInput that = (SerializationStressTestInput) o;
        return enabled == that.enabled && Objects.equals(amount, that.amount) && Objects.equals(investments, that.investments) && Objects.equals(investmentAmounts, that.investmentAmounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, amount, investments, investmentAmounts);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }

    public Set<String> getInvestments() {
        return investments;
    }

    public Map<String, MonetaryAmount> getInvestmentAmounts() {
        return investmentAmounts;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAmount(MonetaryAmount amount) {
        this.amount = amount;
    }

    public void setInvestments(Set<String> investments) {
        this.investments = investments;
    }

    public void setInvestmentAmounts(Map<String, MonetaryAmount> investmentAmounts) {
        this.investmentAmounts = investmentAmounts;
    }
}
