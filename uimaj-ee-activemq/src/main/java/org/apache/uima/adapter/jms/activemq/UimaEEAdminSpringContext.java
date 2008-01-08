package org.apache.uima.adapter.jms.activemq;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.activemq.broker.BrokerService;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UimaEEAdminContext;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.util.Level;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class UimaEEAdminSpringContext 
implements UimaEEAdminContext, ApplicationListener
{
	private static final Class CLASS_NAME = UimaEEAdminSpringContext.class;

	private FileSystemXmlApplicationContext springContainer = null;
	private BrokerService service;
	private boolean isShutdown;
	private ConcurrentHashMap listenerMap = new ConcurrentHashMap();
	
	
	public UimaEEAdminSpringContext( FileSystemXmlApplicationContext aSpringContainer )
	{
		springContainer = aSpringContainer;
		String beanNames[] = springContainer.getBeanNamesForType(org.apache.uima.adapter.jms.activemq.UimaDefaultMessageListenerContainer.class);
		for( int i=0; beanNames != null && i < beanNames.length; i++)
		{
			try
			{
				UimaDefaultMessageListenerContainer listenerContainer = 
					((UimaDefaultMessageListenerContainer)springContainer.getBean(beanNames[i]));
				ListenerEntry listenerEntry = new ListenerEntry();
				listenerEntry.setListenerContainer(listenerContainer);
				listenerMap.put(listenerContainer.getDestinationName(), listenerEntry);
			}
			catch( Exception e) {}
		}
	}
	
	public void setBroker( BrokerService aBrokerService )
	{
		service = aBrokerService;
	}
	
	public ApplicationContext getSpringContainer()
	{
		return springContainer;
	}
	/**
	 * Stops a listener thread on a given endpoint
	 */
	public synchronized void stopListener(String anEndpointName )
	{
		try
		{
			if ( anEndpointName != null && anEndpointName.trim().length() > 0 && springContainer.isActive() && listenerMap.containsKey(anEndpointName))
			{
				ListenerEntry listenerEntry = null;
				
				listenerEntry =((ListenerEntry)listenerMap.get(anEndpointName));
				if ( listenerEntry != null && listenerEntry.isStopped() == false )
				{
					listenerEntry.setStopped(true);
					UimaDefaultMessageListenerContainer listenerContainer = 
						((ListenerEntry)listenerMap.get(anEndpointName)).getListenerContainer();
					spinThreadForListenerShutdown(listenerContainer);
				}
			}				
		}
		catch( Exception e) {}
	}
	
	private void spinThreadForListenerShutdown(final UimaDefaultMessageListenerContainer listenerContainer)
	{
		//	Spin a shutdown thread to terminate listener. The thread is needed due
		//	to Spring. 
		new Thread() {
			public void run()
			{
				try
				{
					listenerContainer.setAutoStartup(false);
					listenerContainer.setRecoveryInterval(0);
					listenerContainer.shutdown();
					listenerContainer.destroy();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, this.getClass().getName(),
			                "spinThreadForListenerShutdown.run()", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stop_listener__INFO",
			                new Object[] {  listenerContainer.getEndpointName() });
				}
				catch( Exception e) { e.printStackTrace();}
			}
		}.start();
	}
	public void onApplicationEvent(ApplicationEvent anEvent)
	{
		shutdown();
	}
	/**
	 *	Stops the Spring Container 
	 */
	public void shutdown()
	{
		if ( springContainer.isActive() )
		{
			isShutdown = true;
			//	Spin a thread so that the Spring container can shut itself down
			new Thread("Spring Container Shutdown Thread") {
				public void run()
				{
					try
					{
						System.out.println("Destroying Spring Container");
						springContainer.destroy();
						springContainer = null;
					}
					catch( Exception e) { e.printStackTrace();}
				}
			}.start();
		}
		listenerMap.clear();
	}
	public boolean isShutdown()
	{
		return isShutdown;
	}

	protected class ListenerEntry
	{
		private boolean stopped = false;
		private UimaDefaultMessageListenerContainer listenerContainer = null;
		
		protected UimaDefaultMessageListenerContainer getListenerContainer()
		{
			return listenerContainer;
		}
		protected void setListenerContainer(UimaDefaultMessageListenerContainer listenerContainer)
		{
			this.listenerContainer = listenerContainer;
		}
		protected boolean isStopped()
		{
			return stopped;
		}
		protected void setStopped(boolean stopped)
		{
			this.stopped = stopped;
		}
		
	}
	
	private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;

}
