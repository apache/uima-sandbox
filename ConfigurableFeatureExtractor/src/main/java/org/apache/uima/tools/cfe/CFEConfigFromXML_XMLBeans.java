package org.apache.uima.tools.cfe;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.tools.cfe.config.xmlBeans.*;
import org.apache.xmlbeans.XmlException;

public class CFEConfigFromXML_XMLBeans
{
    private final CFEDescriptorXML m_CFEDescriptor;
    
    public CFEConfigFromXML_XMLBeans (String path) throws XmlException, IOException
    {
        m_CFEDescriptor = CFEDescriptorXML.Factory.parse(new File(path));
    }
    
    EnumFeatureValues getEnumFeatureValues(EnumFeatureValuesXML efvs_xml)
    throws IOException, URISyntaxException
    {
        String[] vals = efvs_xml.getValuesArray();

        if ((1 == vals.length) && (vals[0]).startsWith("file://")) {
            return new EnumFeatureValues(new URI((String)vals[0]).getPath(), efvs_xml.getCaseSensitive());
        }
        return new EnumFeatureValues(vals, efvs_xml.getCaseSensitive());
    }
    
    RangeFeatureValues getRangeFeatureValues(RangeFeatureValuesXML rfvs_xml)
    {
        String lb = rfvs_xml.getLowerBoundary().toString().trim();
        String ub = rfvs_xml.getUpperBoundary().toString().trim();
        
        return new RangeFeatureValues(Double.parseDouble(lb),
                                      rfvs_xml.getLowerBoundaryInclusive(),
                                      Double.parseDouble(ub),
                                      rfvs_xml.getUpperBoundaryInclusive());
    }

    BitsetFeatureValues getBitsetFeatureValues(BitsetFeatureValuesXML bfvs_xml)
    {
        return new BitsetFeatureValues(Integer.parseInt(bfvs_xml.getBitmask(), 16),
                                       bfvs_xml.getExactMatch());
    }
    
    PatternFeatureValues getPatternFeatureValues(PatternFeatureValuesXML pattern_xml)
    {
        return new PatternFeatureValues(pattern_xml.getPattern());
    }

    ObjectPathFeatureValues getObjectPathFeatureValues(ObjectPathFeatureValuesXML opfvs_xml,
                                                       String                     feature_class)
    {
        return new ObjectPathFeatureValues(feature_class, opfvs_xml.getObjectPath());
    }
    
    SingleFeatureMatcher getSingleFeatureMatcher(SingleFeatureMatcherXML sfm_xml, String obj_class)
    throws SecurityException, NoSuchMethodException, ClassNotFoundException, IOException, URISyntaxException
    {
        if (null != sfm_xml.getEnumFeatureValues()) {
            return new SingleFeatureMatcher(sfm_xml.getFeatureTypeName(),
                                            obj_class + ":" + sfm_xml.getFeaturePath(),
                                            sfm_xml.getExclude(),
                                            sfm_xml.getQuiet(),
                                            getEnumFeatureValues(sfm_xml.getEnumFeatureValues())); 
        }
        else if (null != sfm_xml.getRangeFeatureValues()) {
            return new SingleFeatureMatcher(sfm_xml.getFeatureTypeName(),
                                            obj_class + ":" + sfm_xml.getFeaturePath(),
                                            sfm_xml.getExclude(),
                                            sfm_xml.getQuiet(),
                                            getRangeFeatureValues(sfm_xml.getRangeFeatureValues())); 
        }
        else if (null != sfm_xml.getBitsetFeatureValues()) {
            return new SingleFeatureMatcher(sfm_xml.getFeatureTypeName(),
                                            obj_class + ":" + sfm_xml.getFeaturePath(),
                                            sfm_xml.getExclude(),
                                            sfm_xml.getQuiet(),
                                            getBitsetFeatureValues(sfm_xml.getBitsetFeatureValues())); 
        }
        else if (null != sfm_xml.getPatternFeatureValues()) {
            return new SingleFeatureMatcher(sfm_xml.getFeatureTypeName(),
                                            obj_class + ":" + sfm_xml.getFeaturePath(),
                                            sfm_xml.getExclude(),
                                            sfm_xml.getQuiet(),
                                            getPatternFeatureValues(sfm_xml.getPatternFeatureValues())); 
        }
        else if (null != sfm_xml.getObjectPathFeatureValues()) {
            return new SingleFeatureMatcher(sfm_xml.getFeatureTypeName(),
                                            obj_class + ":" + sfm_xml.getFeaturePath(),
                                            sfm_xml.getExclude(),
                                            sfm_xml.getQuiet(),
                                            getObjectPathFeatureValues(sfm_xml.getObjectPathFeatureValues(), sfm_xml.getFeatureTypeName())); 
        }
        else {
            return new SingleFeatureMatcher(sfm_xml.getFeatureTypeName(),
                                            obj_class + ":" + sfm_xml.getFeaturePath(),
                                            sfm_xml.getExclude(),
                                            sfm_xml.getQuiet(),
                                            new EnumFeatureValues()); // would match any value 
        }
    }
    
    GroupFeatureMatcher getGroupFeatureMatcher(GroupFeatureMatcherXML gfm_xml, String obj_class_name)
    throws SecurityException, NoSuchMethodException, ClassNotFoundException, IOException, URISyntaxException
    {
        SingleFeatureMatcherXML[] fms_xml = gfm_xml.getFeatureMatchersArray();
        List<SingleFeatureMatcher> sfms = new ArrayList<SingleFeatureMatcher>();

        for (int i = 0; i < fms_xml.length; ++i) {
            sfms.add(getSingleFeatureMatcher(fms_xml[i], obj_class_name));
        }
        return new GroupFeatureMatcher(sfms, gfm_xml.getExclude()); 
    }
    
    PartialObjectMatcher getPartialObjectMatcher(PartialObjectMatcherXML pom_xml)
    throws SecurityException, NoSuchMethodException, IOException, URISyntaxException, ClassNotFoundException
    {
        GroupFeatureMatcherXML[] gfms_xml = pom_xml.getGroupFeatureMatchersArray();
        List<GroupFeatureMatcher> gfms = new ArrayList<GroupFeatureMatcher>();
        
        for (int i = 0; i < gfms_xml.length; ++i) {
            gfms.add(getGroupFeatureMatcher(gfms_xml[i], pom_xml.getAnnotationTypeName()));
        }
        return new PartialObjectMatcher(pom_xml.getAnnotationTypeName(), pom_xml.getFullPath(), gfms); 
    }
    
    FeatureObjectMatcher getFeatureObjectMatcher(FeatureObjectMatcherXML fom_xml)
    throws SecurityException, NoSuchMethodException, IOException, ClassNotFoundException, URISyntaxException
    {
        GroupFeatureMatcherXML[] gfms_xml = fom_xml.getGroupFeatureMatchersArray();
        List<GroupFeatureMatcher> gfms = new ArrayList<GroupFeatureMatcher>();

        for (int i = 0; i < gfms_xml.length; ++i) {
            gfms.add(getGroupFeatureMatcher(gfms_xml[i], fom_xml.getAnnotationTypeName()));
        }
        return new FeatureObjectMatcher(fom_xml.getAnnotationTypeName(), fom_xml.getFullPath(), gfms, 
                                        fom_xml.getWindowsizeLeft(),
                                        fom_xml.getWindowsizeInside(),
                                        fom_xml.getWindowsizeRight(),
                                        fom_xml.getWindowsizeEnclosed(),
                                        fom_xml.getWindowFlags(),
                                        fom_xml.getOrientation(), 
                                        fom_xml.getDistance()); 
    }
    
    TargetAnnotationDescriptor getTargetAnnotationDescriptor(TargetAnnotationXML ta_xml, int priorityOrder)
    throws SecurityException, NoSuchMethodException, IOException, ClassNotFoundException, URISyntaxException
    {
        PartialObjectMatcher ta_matcher = getPartialObjectMatcher(ta_xml.getTargetAnnotationMatcher());
        FeatureObjectMatcherXML[] fams_xml = ta_xml.getFeatureAnnotationMatchersArray();
        
        List<FeatureObjectMatcher> fams = new ArrayList<FeatureObjectMatcher>();
        for (int i = 0; i < fams_xml.length; ++i) {
            fams.add(getFeatureObjectMatcher(fams_xml[i]));
        }
        return new TargetAnnotationDescriptor(ta_xml.getClassName(),
                                              ta_xml.getEnclosingAnnotation(),
                                              ta_matcher,
                                              fams,
                                              priorityOrder);
    } 
    
    public List<TargetAnnotationDescriptor> getTargetAnnotationDescriptors()
    throws SecurityException, NoSuchMethodException, IOException, ClassNotFoundException, URISyntaxException
    {
        List<TargetAnnotationDescriptor> result = new ArrayList<TargetAnnotationDescriptor>();
        TargetAnnotationXML[] tans = m_CFEDescriptor.getTargetAnnotationsArray();
        for (int i = 0; i < tans.length; ++i) {
            result.add(getTargetAnnotationDescriptor(tans[i], result.size() + 1));
        }
        return result;
    }
    
    public String getNullValueImage()
    {
        return m_CFEDescriptor.getNullValueImage();  
    }
}
