package org.apache.uima.adapter.jms.activemq;
import org.apache.uima.adapter.jms.activemq.UimaDefaultMessageListenerContainer;


public interface ModifiableListener
{
	public void setListener( UimaDefaultMessageListenerContainer aContainer);
}
