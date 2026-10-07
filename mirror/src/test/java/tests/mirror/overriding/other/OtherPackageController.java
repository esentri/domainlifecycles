package tests.mirror.overriding.other;

import tests.mirror.overriding.OverridingBase;

public class OtherPackageController extends OverridingBase {

    @Override
    public String create(String input) {
        return input;
    }

    @Override
    public CharSequence covariant() {
        return "other";
    }

    @Override
    protected void hook() {
    }

    // does not override the package-private method of the base class, which lives in another package
    public void packageLocal() {
    }
}
