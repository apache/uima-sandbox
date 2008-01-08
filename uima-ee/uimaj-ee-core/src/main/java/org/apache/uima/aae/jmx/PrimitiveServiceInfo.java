package org.apache.uima.aae.jmx;

public class PrimitiveServiceInfo 
extends ServiceInfo implements PrimitiveServiceInfoMBean//, ServiceInfoMBean
//extends ServiceInfo implements PrimitiveServiceInfoMBean//, ServiceInfoMBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6416623322329673083L;
	/**
	 * 
	 */
	
	private int instanceCount = 0;
	
	public int getAnalysisEngineInstanceCount()
	{
		return instanceCount;
	}
	public void setAnalysisEngineInstanceCount(int anAEInstanceCount)
	{
		instanceCount = anAEInstanceCount;
	}
}
