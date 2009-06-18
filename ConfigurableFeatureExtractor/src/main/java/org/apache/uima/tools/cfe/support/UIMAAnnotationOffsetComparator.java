package org.apache.uima.tools.cfe.support;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.uima.jcas.tcas.Annotation;


public class UIMAAnnotationOffsetComparator implements Comparator<Annotation>
{
    final private List<Class<?>> m_typePriorities; 
    
    public UIMAAnnotationOffsetComparator (Class<?>[] typePriorities)
    {
        if (null == typePriorities) {
            m_typePriorities = null;
        }
        else {
            m_typePriorities = Arrays.asList(typePriorities);
        }
    }
    
    public UIMAAnnotationOffsetComparator ()
    {
        this(null);
    }
    
    public int compare(Annotation a1, Annotation a2)
    {
        if (a1.getBegin() < a2.getBegin()) {
            return -1;
        }
        else if (a1.getBegin() > a2.getBegin()) {
            return 1;
        }
        else if (a1.getEnd() < a2.getEnd()) {
            return -1;
        }
        else if (a1.getEnd() > a2.getEnd()) {
            return 1;
        }

        int p1 = 0; 
        int p2 = 0;
        if (null != m_typePriorities) {
            p1 = m_typePriorities.indexOf(a1.getClass()); 
            p2 = m_typePriorities.indexOf(a2.getClass());
        }

        return (p1 == p2) ?
                   (a1.getAddress() == a2.getAddress() ? 0 : (a1.getAddress() > a2.getAddress()) ? 1 : -1) :
                   (p1 <  p2 ? -1 : 1);  
    }
    
    public boolean equal(Annotation a1, Annotation a2)
    {
        return 0 == compare(a1, a2);
    }
}