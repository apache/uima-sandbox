package org.apache.uima.tools.cfe.support;

import java.util.Comparator;

import org.apache.uima.jcas.tcas.Annotation;


public class ArrayComparatorFinder
{
    public static Comparator<Annotation> find (Object[] objs)
    {
        for (int i = 0; i < objs.length; ++i) {
            if (null == objs[i]) {
                continue;
            }
            if (!(objs[i] instanceof Annotation)) {
                return null;
            }
        }
        return new UIMAAnnotationOffsetComparator();
    }
}