package tests.mirror;

public class SubEntityHiding extends BaseEntityWithHidden<String> {
    private String hiddenField = "hiding";

    private int[] intArray;

    @Override
    public String showTestOverridden(String s) {
        showTestNotOverridden(s);
        return super.showTestOverridden(s);
    }
}
