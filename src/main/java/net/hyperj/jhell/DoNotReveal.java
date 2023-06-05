package net.hyperj.jhell;

import java.lang.annotation.*;

/**
 * A simple annotation I made to keep <b>VERY</b> private class data private.
 * <br>
 * <b style="color:red">WARNING: CAN BE BYPASSED</b>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface DoNotReveal {
    int priority() default 10;
}
