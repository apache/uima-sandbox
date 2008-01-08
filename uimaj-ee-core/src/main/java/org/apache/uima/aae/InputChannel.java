package org.apache.uima.aae;

import org.apache.uima.aae.jmx.ServiceInfo;
import org.apache.uima.aae.message.MessageContext;

public interface InputChannel extends Channel
{
	public int getSessionAckMode();
	public void ackMessage( MessageContext aMessageContext );
	public String getServerUri();
	public void setServerUri(String aServerUri);
	public String getInputQueueName();
	public ServiceInfo getServiceInfo();
//	public void stop() throws Exception;
	public boolean isStopped();
    static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
