package net.hyperj.jhell.priority;

import net.hyperj.result.*;
import net.hyperj.jhell.*;

public interface PriorityInjection extends Injection {

    Result<Object> read$(String fieldName);

    Result<Object> write$(String fieldName, Object value);

    Result<Object> call$(String methodName, Object... params);
}
