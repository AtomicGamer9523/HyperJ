package net.hyperj.jhell;

import net.hyperj.result.*;

public interface Injection {
    /**
     * Reads a variable from the class.
     * <b>Only static variables can be accessed</b> if you injected into a {@link Class class}.
     * Otherwise, you can read both static and instance variables.
     *
     * @param variableName the name of the variable you would like to read.
     * @return a {@link Result result} of the read.
     */
    Result<Object> read(String variableName);

    /**
     * Writes a value to a variable from the class, returns the previous value (before write).
     * <b>Only static variables can be written</b> if you injected into a {@link Class class}.
     * Otherwise, you can write to both static and instance variables.
     *
     * @param variableName the name of the variable you would like to write to.
     * @param value the new value you would like to write
     * @return a {@link Result result} of the variable before write.
     */

    Result<Object> write(String variableName, Object value);

    /**
     *
     * @param methodName
     * @param params
     * @return
     */
    Result<Object> call(String methodName, Object... params);
}
