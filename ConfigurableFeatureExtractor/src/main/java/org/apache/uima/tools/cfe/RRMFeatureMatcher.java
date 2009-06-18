package org.apache.uima.tools.cfe;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;



public class RRMFeatureMatcher extends CommonFeatureMatcher 
{
    public static final String PARAM_INCLUDEFEATUREWINDOWIMAGE  = "IncludeFeatureWindowImage";
    final boolean m_isFeatureWindowImage;
    
    public RRMFeatureMatcher(boolean includeAnnotationName, boolean includeFeatureName, boolean isFeatureWindowImage)
    {
        super(false, includeAnnotationName, includeFeatureName, "_", new String[]{"_"});
        m_isFeatureWindowImage = isFeatureWindowImage;
    }

    protected String featureWindowImage (MatchedAnnotationDescriptor mad)
    {
        StringBuffer buf = new StringBuffer("W");
        if (mad.m_feature_matcher.m_windowsizeLeft > 0) {
            buf.append("L" + mad.m_feature_matcher.m_windowsizeLeft); 
        }
        if (mad.m_feature_matcher.m_windowsizeInside > 0) {
            buf.append("I" + mad.m_feature_matcher.m_windowsizeInside); 
        }
        if (mad.m_feature_matcher.m_windowsizeRight > 0) {
            buf.append("R" + mad.m_feature_matcher.m_windowsizeRight); 
        }
        if (mad.m_feature_matcher.m_windowsizeEnclosed > 0) {
            String[] name_toks = mad.m_enclosing.getClass().getName().split("\\.");
            buf.append(name_toks[name_toks.length - 1]); 
        }
        if (mad.m_feature_matcher.m_windowFlags > 0) {
            buf.append("F" + mad.m_feature_matcher.m_windowFlags); 
        }
        return buf.toString(); 
    }

    protected void processFeatureGroup(JCas                                     jcas,
                                       Annotation                               trg,
                                       String                                   class_label, 
                                       Collection<MatchedAnnotationDescriptor>  features)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        
        // to avoid duplicates
        Set<String> featureImages = new TreeSet<String>();

        for (Iterator<MatchedAnnotationDescriptor> feat_it = features.iterator(); feat_it.hasNext();) {
            MatchedAnnotationDescriptor mad = feat_it.next();
            
            String feature = make_RRM_feature(mad, make_image(mad, trg));
            if (m_isFeatureWindowImage) {
                feature = featureWindowImage(mad) + m_fieldSeparator + feature;
            }

            featureImages.add(feature);
        }

        StringArray uima_feats = new StringArray(jcas, featureImages.size());
        int ind = 0;
            
        for (Iterator<String> img_it = featureImages.iterator(); img_it.hasNext();) {
            uima_feats.set(ind++, img_it.next());
        }

        SenseAnnotation sa = new SenseAnnotation(jcas, trg.getBegin(), trg.getEnd());
        sa.setSENSE(class_label);
        AppliedAnnotation aa = new AppliedAnnotation(jcas, trg.getBegin(), trg.getEnd());
        aa.setTruth(sa);
        ApplierInfoAnnotation aia = new ApplierInfoAnnotation(jcas, trg.getBegin(), trg.getEnd());
        aia.setDocumentId(getDocumentId(jcas, aia));
        aia.setAppliedAnnotation(aa);
        aia.setFeatures(uima_feats);
        aia.addToIndexes();
    }

    private String make_RRM_feature (MatchedAnnotationDescriptor mad, String suffix)
    {
        if (suffix.indexOf(" ") >= 0) {
            System.err.println("ERROR: \"" + suffix + "\"" + "has spaces"); 
        }
        StringBuffer feature = new StringBuffer();
        
        Pattern pat = Pattern.compile("\\W");
        Matcher mat = pat.matcher(suffix);
        
        int start = 0;
        while (mat.find()) {
            int gstart = mat.start();
            feature.append(suffix.substring(start, gstart));
            start = mat.end();  
            StringBuffer replaced = new StringBuffer(); 
            String group = mat.group();
            for (int i = 0; i < group.length(); ++i) {
                replaced.append("__CHAR" + (int)group.charAt(i) + "__");
            }
            feature.append(replaced);
        }
        feature.append(suffix.substring(start));
        
        StringBuffer strBuf = new StringBuffer();
        if (mad.m_feature_matcher.m_orientation || mad.m_feature_matcher.m_distance) {
            strBuf.append(make_image_position(mad));
            strBuf.append(m_fieldSeparator);
        }
        strBuf.append(feature);

        return strBuf.toString();
    }
}
