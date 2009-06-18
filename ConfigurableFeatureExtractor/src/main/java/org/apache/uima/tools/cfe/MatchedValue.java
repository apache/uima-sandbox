/**
 * 
 */
package org.apache.uima.tools.cfe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MatchedValue
{
    protected Object    m_matchedObject;
    public List<Object> m_orderedPath;
    
    public MatchedValue (Object matchedObject, List<Object> orderedPath)
    {
        m_matchedObject = matchedObject;
        m_orderedPath = orderedPath;
    }
    
    public MatchedValue (Object matchedObject)
    {
        m_matchedObject = matchedObject;
        m_orderedPath = new ArrayList<Object>();
    }
    
    
    public static <T extends MatchedValue> boolean contains (Collection<T> values, Object obj)
    {
        for (T mv : values) {
            if (obj == mv.m_matchedObject) {
                return true;
            }
        }
        return false;
    }
    
    public static <T extends MatchedValue> T get (Collection<T> values, Object obj)
    {
        for (T mv : values) {
            if (obj == mv.m_matchedObject) {
                return mv;
            }
        }
        return null;
    }
    
    
    public static <T extends MatchedValue> void removeAll (Collection<T>  removeFrom,
                                                           Collection<T>  contained)
    {
        Collection<T> matched = new ArrayList<T>(); 
        for (T mv : removeFrom) {
            if (contains(contained, mv.m_matchedObject)) {
                matched.add(mv);
            }
        }
        removeFrom.removeAll(matched);
    }
}