package net.hyperj.injections;

import net.hyperj.util.*;
import net.hyperj.*;

public class SecurityManagerInjection {
    private static SecurityManagerInjection INSTANCE;

    public static SecurityManagerInjection getInstance() {
        if (INSTANCE == null) INSTANCE = new SecurityManagerInjection();
        return INSTANCE;
    }

    private boolean securityManagerCurrentlyEnabled;
    @SuppressWarnings("removal")
    private final SecurityManager originalSecurityManagerState;

    @SuppressWarnings("removal")
    private SecurityManagerInjection() {
        originalSecurityManagerState = System.getSecurityManager();
        updateState();
    }

    @SuppressWarnings("removal")
    private void updateState() {
        securityManagerCurrentlyEnabled = System.getSecurityManager() != null;
    }

    @SuppressWarnings("removal")
    public <R> Result<R> runBypass(Func<R> f) {
        updateState();
        try {
            R res = f.apply();
            return JResult.some(res);
        } catch (Exception e) {
            if (!(e instanceof SecurityException)) return JResult.fail(e);
            if (securityManagerCurrentlyEnabled) {
                System.setSecurityManager(null);
                Result<R> res = runBypass(f);
                System.setSecurityManager(originalSecurityManagerState);
                return res;
            }
            return JResult.fail(new IllegalStateException(
                "Can't get 'SecurityException' if the current security manager is null..."
            ));
        }
    }

    @FunctionalInterface
    public interface Func<R> {
        R apply() throws Exception;
    }
}
