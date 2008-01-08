package org.apache.uima.aae.jmx;

public class ServiceInfo implements ServiceInfoMBean
{
	/**
	 * 
	 */
//	private static final long serialVersionUID = -4662094240276750977L;
	private static final String label="Service Info";
	private String brokerURL="";
	private String inputQueueName="";
	private String state="";
	private String[] deploymentDescriptor= new String[] {""};
	
	public String getLabel()
	{
		return label;
	}
	public String getBrokerURL()
	{
		return brokerURL;
	}

	public String[] getDeploymentDescriptor()
	{
		return deploymentDescriptor;
	}
	public void setDeploymentDescriptor(String deploymentDescriptor)
	{
		this.deploymentDescriptor[0] = deploymentDescriptor;
	}
	public void setBrokerURL(String aBrokerURL)
	{
		brokerURL = aBrokerURL;
	}
	public String getInputQueueName()
	{
		return inputQueueName;
	}
	public void setInputQueueName( String anInputQueueName)
	{
		inputQueueName = anInputQueueName;
	}
	public String getState()
	{
		return state;
	}
	public void setState( String aState )
	{
		state = aState;
	}


}
