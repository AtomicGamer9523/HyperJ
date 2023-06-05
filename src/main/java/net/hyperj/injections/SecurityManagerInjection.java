package net.hyperj.injections;

import net.hyperj.result.*;

import java.util.*;

@SuppressWarnings("all")// To many warnings ;)
public class SecurityManagerInjection {
    private static SecurityManagerInjection INSTANCE;

    public static SecurityManagerInjection getInstance() {
        if (INSTANCE == null) INSTANCE = new SecurityManagerInjection();
        return INSTANCE;
    }

    private boolean securityManagerCurrentlyEnabled;
    private final Optional<SecurityManager> originalSecurityManagerState;

    private SecurityManagerInjection() {
        SecurityManager securityManager = System.getSecurityManager();
        originalSecurityManagerState = Optional.ofNullable(securityManager);
        updateState();
    }

    private void updateState() {
        securityManagerCurrentlyEnabled = System.getSecurityManager() != null;
    }

    public <R> Result<R> runBypass(BypassableFunction<R> f) {
        updateState();
        try {
            R res = f.apply();
            return JResult.success(res);
        } catch (Exception e) {
            if (!(e instanceof SecurityException)) return JResult.fail(e);
            if (securityManagerCurrentlyEnabled) {
                System.setSecurityManager(null);
                Result<R> res = runBypass(f);
                System.setSecurityManager(originalSecurityManagerState.orElse(null));
                return res;
            }
            return JResult.fail(new IllegalStateException(
                "Can't get 'SecurityException' if the current security manager is null..."
            ));
        }
    }

    @FunctionalInterface
    public interface BypassableFunction<R> {
        R apply() throws Exception;
    }
}
