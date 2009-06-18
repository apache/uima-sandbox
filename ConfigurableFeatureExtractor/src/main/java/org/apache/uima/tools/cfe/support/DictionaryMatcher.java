package org.apache.uima.tools.cfe.support;

import java.lang.reflect.InvocationTargetException;

public interface DictionaryMatcher<T>
{
    abstract boolean matches(T key)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
    
    
}
