package org.core.common.reflection;

import java.io.Serializable;
import java.util.function.Function;

public interface SFunction<T, R> extends Function<T, R>, Serializable {
 
}