package tests.mirror.completeness.nondomain;

import tests.mirror.completeness.outside.OutsideValue;

public class NonDomainMapper {

    private OutsideValue last;

    public OutsideValue map(String value) {
        last = new OutsideValue(value);
        return last;
    }
}
