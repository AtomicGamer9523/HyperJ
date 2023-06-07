package net.hyperj.jhell.priority;

import net.hyperj.jhell.Injection;
import net.hyperj.result.Result;

public interface PriorityInjection extends Injection {
    /**
     * Reads a variable from the class.
     * Only static variables can be accessed if you injected into a {@link Class class}.
     * Otherwise, you can read both static and instance variables.
     *
     * @param variableName the name of the variable you would like to read.
     * @return a {@link Result result} of the read.
     */
    Result<Object> read$(String variableName);

    /**
     * Writes a value to a variable from the class, returns the previous value (before write).
     * Only static variables can be written if you injected into a {@link Class class}.
     * Otherwise, you can write to both static and instance variables.
     *
     * @param variableName the name of the variable you would like to write to.
     * @param value        the new value you would like to write
     * @return a {@link Result result} of the variable before write.
     */
    Result<Object> write$(String variableName, Object value);

    /**
     * Calls the function and returns it's output.<br>
     * <b>CAN BE {@link net.hyperj.misc.JVoid VOID}! DON'T FORGET TO {@link Result#isVoid() CHECK}</b><br>
     * Only static methods can be called if you injected into a {@link Class class}.
     * Otherwise, you can call to both static and instance variables.
     *
     * @param methodName the name of the method (
     * @param params     the parameters the function accepts
     * @return the function output (can be void, don't forget to check if it {@link Result#isVoid() is void}
     */
    Result<Object> call$(String methodName, Object... params);
}
