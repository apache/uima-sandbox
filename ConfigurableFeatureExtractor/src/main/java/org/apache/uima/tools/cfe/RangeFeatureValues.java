package org.apache.uima.tools.cfe;

public class RangeFeatureValues implements FeatureValues
{
    Double  m_lower_boundary;
    boolean m_lower_boundary_inclusive;

    Double  m_upper_boundary;
    boolean m_upper_boundary_inclusive;
    
    
    public RangeFeatureValues(double    lb,
                              boolean   lbi,
                              double    ub,
                              boolean   ubi)
    {
        m_lower_boundary = lb;
        m_lower_boundary_inclusive = lbi;
        m_upper_boundary = ub;
        m_upper_boundary_inclusive = ubi;
    }
    
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RangeFeatureValues)) {
            return false;
        }
        RangeFeatureValues other = (RangeFeatureValues)obj;
        if (!m_lower_boundary.equals(other.m_lower_boundary) ||
            (m_lower_boundary_inclusive != other.m_lower_boundary_inclusive) ||
            !m_upper_boundary.equals(other.m_upper_boundary) ||
            (m_upper_boundary_inclusive != other.m_upper_boundary_inclusive)) {
            return false;
        }
        return super.equals(obj);
    }
    
    
    
    public boolean matches (Object feature)
    {
        if (!(feature instanceof Number)) {
            return false;
        }
        Number nfeature = (Number)feature;
        
        int lb_res = m_lower_boundary.compareTo(nfeature.doubleValue());
        if (((0 == lb_res) && !m_lower_boundary_inclusive) || (lb_res < 0)) {
            return false;
        }

        int ub_res = m_upper_boundary.compareTo(nfeature.doubleValue());
        if (((0 == ub_res) && !m_upper_boundary_inclusive) || (ub_res > 0)) {
            return false;
        }
        return true;
    }
    
    public String getFeatureImage (Object feature)
    {
        return feature.toString();
    }
}
