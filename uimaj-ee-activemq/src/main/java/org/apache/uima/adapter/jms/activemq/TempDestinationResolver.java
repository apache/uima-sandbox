package org.apache.uima.adapter.jms.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.support.destination.DestinationResolver;


public class TempDestinationResolver implements DestinationResolver
{
	private ActiveMQConnectionFactory factory = null;
	private UimaDefaultMessageListenerContainer listener;
	
	public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException
	{
		Destination destination = session.createTemporaryQueue();

		if ( listener != null )
		{
			listener.setDestination(destination);
		}
		return destination;
	}

	public void setListener( UimaDefaultMessageListenerContainer aListener )
	{
		listener = aListener;
	}
	public void setConnectionFactory( ActiveMQConnectionFactory aFactory )
	{
		factory = aFactory;
	}
}
