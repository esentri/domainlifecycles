package tests.mirror.overriding;

public class OverridingController extends OverridingBase {

    // implements an abstract interface method, additionally final
    @Override
    public final String create(String input) {
        return input;
    }

    // covariant return type, the compiler generates a bridge method
    @Override
    public String covariant() {
        return "covariant";
    }

    // widens the visibility from protected to public
    @Override
    public void hook() {
    }

    @Override
    public final String describe() {
        return "controller";
    }

    @Override
    void packageLocal() {
    }

    // does not override the private method of the base class
    public void secret() {
    }

    // hides, but does not override the static method of the base class
    public static void utility() {
    }
}
