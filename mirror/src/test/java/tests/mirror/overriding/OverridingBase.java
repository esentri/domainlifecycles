package tests.mirror.overriding;

public abstract class OverridingBase implements OverridingApi {

    protected abstract void hook();

    public String describe() {
        return "base";
    }

    void packageLocal() {
    }

    private void secret() {
    }

    public static void utility() {
    }
}
