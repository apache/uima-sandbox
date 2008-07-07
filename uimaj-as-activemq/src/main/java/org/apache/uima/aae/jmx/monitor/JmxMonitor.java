/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.uima.aae.jmx.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.uima.aae.jmx.ServiceInfoMBean;
import org.apache.uima.aae.jmx.ServicePerformanceMBean;

public class JmxMonitor implements Runnable {
	private static final Class CLASS_NAME = JmxMonitor.class;

	private boolean running = false;
	private MBeanServerConnection mbsc;
	private ObjectName uimaServicePattern;
	private ObjectName uimaServiceQueuePattern;
	private Set<ObjectName> servicePerformanceNames;
	private long interval;
	private ConcurrentHashMap< ObjectName, StatEntry> stats = new ConcurrentHashMap<ObjectName, StatEntry>();
	private long startTime = System.nanoTime();
	private List<JmxMonitorListener> listeners = new ArrayList<JmxMonitorListener>();
	private int maxNameLength=0;
	private boolean verbose = false;
	
	private MBeanServerConnection getServerConnection( String remoteServerURI) throws Exception
	{
		JMXServiceURL url = new JMXServiceURL(remoteServerURI);
		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		MBeanServerConnection server = jmxc.getMBeanServerConnection();
		return server;
	}
	
	public void addJmxMonitorListener( JmxMonitorListener listener)
	{
		listeners.add( listener );
	}
	private void showServerEnvironment(RuntimeMXBean runtime)
	{
		echo("\nRemote JVM Info: \n\tJVM::"+runtime.getVmName()+"\n\tJVM Vendor::"+runtime.getVmVendor()+"\n\tJVM Version::"+runtime.getVmVersion()+"\n\n");
	}
	
	public void notifyListeners( long uptime, ServiceMetrics[] metrics )
	{
		for( JmxMonitorListener listener: listeners)
		{
			listener.onNewMetrics(uptime, metrics);
		}
	}
	
	public boolean isVerbose()
	{
		return verbose;
	}
	
	public void setVerbose()
	{
		verbose = true;
	}
	//	Should be called after initialize()
	public int getMaxServiceNameLength()
	{
		return maxNameLength;
	}
	/**
	 * Connects to a remote JMX server identified by given <code>remoteServerURI</code>.
	 * Creates proxies for all UIMA AS ServicePerformance MBeans found in the JMX server registry.
	 * If UIMA AS service MBean indicates a remote service, this method connects to a remote 
	 * Broker and creates a proxy to an input queue for that service.
	 *  
	 * 
	 * @param remoteServerURI
	 * @param samplingInterval
	 * @throws Exception
	 */
	public void initialize(String remoteServerURI, long samplingInterval) throws Exception
	{
		interval = samplingInterval;
		mbsc = getServerConnection(remoteServerURI);
		
		ObjectName objName = new  ObjectName(ManagementFactory.RUNTIME_MXBEAN_NAME);
		uimaServicePattern = new ObjectName("org.apache.uima:type=ee.jms.services,*");
		uimaServiceQueuePattern = new ObjectName("org.apache.activemq:BrokerName=localhost,Type=Queue,*");
		//	Fetch UIMA AS MBeans from the JMX Server
		Set<ObjectName> names = new HashSet<ObjectName>(mbsc.queryNames(uimaServicePattern, null));
		//	Fetch All queue MBeans from the JMX Server
		//		names = new TreeSet<ObjectName>(mbsc.queryNames(uimaServicePattern, null));
		String key = "";
		if ( verbose )
			System.out.println("\nFound UIMA AS Services Managed by JMX Server:"+remoteServerURI);
		//	Find all Service Performance MBeans
		for (ObjectName name : names) {
			
			//	Set up a key for matching Service Performance MBean names
			String perfKey = "_Service Performance";
			//	Check if the current name is for the Service Performance MBean
			if ( name.toString().endsWith(perfKey) )
			{
				if ( servicePerformanceNames == null )
				{
					servicePerformanceNames = new HashSet<ObjectName>();
				}
				//	Reduce the Set to Service Performance MBeans only
				servicePerformanceNames.add(name);
				//	Create a proxy object for the Service Performance MBean
				ServicePerformanceMBean perfMBeanProxy = MBeanServerInvocationHandler.newProxyInstance(mbsc, name,ServicePerformanceMBean.class, true);
				//	Extract the service name from the MBean name
				int beginIndx = name.toString().indexOf(perfKey);
				key = name.toString().substring(0, beginIndx);

				int nameLen = name.getKeyProperty("name").length();
				if ( nameLen > maxNameLength )
				{
					maxNameLength = nameLen;
				}
				
				//	Create Service Info proxy
				ServiceInfoMBean infoMBeanProxy = getServiceInfoMBean(names, key);
				key = key.substring(key.indexOf(",name=")+",name=".length());
				
				
				long depth = 0; 
				//	Create a Map entry containing MBeans
				StatEntry entry = new StatEntry(perfMBeanProxy, infoMBeanProxy);
				String location = "Collocated";
				//	If a service is colocated in the same JVM fetch the service queue proxy 
				if ( infoMBeanProxy.getBrokerURL().startsWith("Embedded Broker"))
				{
					QueueViewMBean queueProxy = getQueueMBean(mbsc, infoMBeanProxy.getInputQueueName());
					if (queueProxy != null )
					{
						depth = queueProxy.getQueueSize();
						entry.setQueueInfo(queueProxy);
					}
				}
				else
				{
					//	The MBean is for a remote service. Connect to this service Broker and create a proxy
					//	to an input queue for the service.
					location = "Remote";	
					int spos = infoMBeanProxy.getBrokerURL().indexOf("//");
					int endpos = infoMBeanProxy.getBrokerURL().lastIndexOf(":");
					
					String remoteHostname = infoMBeanProxy.getBrokerURL().substring(spos+2,endpos); 
					if ( verbose )
						System.out.println("Connecting to a remote JMX Server: "+remoteHostname+" key:"+key);
					String remoteJMX = "service:jmx:rmi:///jndi/rmi://"+remoteHostname+":1099/jmxrmi"; 
					MBeanServerConnection remoteServer = getServerConnection(remoteJMX);
					QueueViewMBean queueProxy = getQueueMBean(remoteServer, infoMBeanProxy.getInputQueueName());
					if (queueProxy != null )
					{
						depth = queueProxy.getQueueSize();
						entry.setQueueInfo(queueProxy);
					}
					else
					{
						System.out.println("Unable to find Queue:"+infoMBeanProxy.getInputQueueName()+" In JMX Registry:"+remoteJMX);
					}
				}
				if ( verbose )
					System.out.println("\nUIMA AS Service:"+key+"[>>> "+location+" <<<]\n\tService Broker:"+infoMBeanProxy.getBrokerURL()+"\n\tQueue Name:"+infoMBeanProxy.getInputQueueName());

				//System.out.println("Info Bean:"+key+" Broker URL:"+infoMBeanProxy.getBrokerURL()+" Service Queue Name:"+infoMBeanProxy.getInputQueueName()+" Input Queue Depth:"+depth);
				stats.put(name, entry);

				/** jdk1.6 code
				ServicePerformanceMBean mbeanProxy =
					 JMX.newMBeanProxy(mbsc, name, ServicePerformanceMBean.class, true);
					 **/
			}
		}
	}
	
	private ServiceInfoMBean getServiceInfoMBean(Set<ObjectName> names, String key)
	{
		String target = key+"_Service Info";
		for (ObjectName name : names) {
			ServiceInfoMBean infoMBeanProxy = null;
			if ( name.toString().equals(target) )
			{
				return MBeanServerInvocationHandler.newProxyInstance(mbsc, name,ServiceInfoMBean.class, true);
			}
		}		
		return null;
	}

	private QueueViewMBean getQueueMBean(MBeanServerConnection server, String key) throws Exception
	{
		Set<ObjectName> queues= new HashSet<ObjectName>(server.queryNames(uimaServiceQueuePattern, null));
		String target = "Destination="+key;
		for (ObjectName name : queues) {
			
			if ( name.toString().endsWith(target) )
			{
				return MBeanServerInvocationHandler.newProxyInstance(server, name,QueueViewMBean.class, true);
			}
		}		
		return null;
	}
	
	
	public void run()
	{
		running = true;
		boolean initial = true;
		while( running )
		{
			
			long uptime = System.nanoTime() - startTime;
			
			ServiceMetrics[] metrics = new ServiceMetrics[servicePerformanceNames.size()];
			int index = 0;
			for (ObjectName name : servicePerformanceNames) {
				try
				{
					//	Fetch previous metrics for service identified by 'name'
					StatEntry entry = stats.get(name);
					//	Get the current reading from MBeans 
					double idleTime = entry.getServicePerformanceMBeanProxy().getIdleTime();
					double casPoolWaitTime = entry.getServicePerformanceMBeanProxy().getCasPoolWaitTime();
					double shadowCasPoolWaitTime = entry.getServicePerformanceMBeanProxy().getShadowCasPoolWaitTime();
					double timeInCMGetNext = entry.getServicePerformanceMBeanProxy().getTimeSpentInCMGetNext();
					long processCount = entry.getServicePerformanceMBeanProxy().getNumberOfCASesProcessed();
					QueueViewMBean queueInfo = entry.getQueueInfo();

					long queueDepth = -1;
					if ( queueInfo != null )
					{
						queueDepth = queueInfo.getQueueSize();
					}
					double casPoolWait = 0.0;
					//	compute the delta idle time by subtracting previously reported idle time from the
					//	current idle time
					double deltaIdleTime = 0;
					if ( initial == false )
					{
						deltaIdleTime = idleTime - entry.getIdleTime();
					}
					
					double deltaTimeInCMGetNext = 0;
					if ( timeInCMGetNext > 0 )
					{
						deltaTimeInCMGetNext = timeInCMGetNext - entry.getLastWaitTimeOnCMGetNext();
					}
					
					boolean isRemote = entry.getServiceInfoMBeanProxy().getBrokerURL().startsWith("tcp:");
					boolean topLevel = entry.getServiceInfoMBeanProxy().isTopLevel();
					
					ServiceMetrics serviceMetrics = new ServiceMetrics();
					serviceMetrics.setCasMultiplier(entry.getServiceInfoMBeanProxy().isCASMultiplier());
					serviceMetrics.setServiceRemote(isRemote);
					serviceMetrics.setTopLevelService(topLevel);
					serviceMetrics.setTimestamp(uptime/1000000);
					serviceMetrics.setIdleTime(deltaIdleTime);
					serviceMetrics.setServiceName(name.getKeyProperty("name"));
					serviceMetrics.setProcessCount(processCount-entry.getLastCASCount());
					serviceMetrics.setTimeInCMGetNext(deltaTimeInCMGetNext);
					serviceMetrics.setQueueDepth(queueDepth);

					//	populate shadow CAS pool metric for remote CAS multiplier. Filter out the top level service
					if ( entry.getServiceInfoMBeanProxy().isCASMultiplier() && isRemote && !topLevel )
					{
						serviceMetrics.setShadowCasPoolWaitTime(shadowCasPoolWaitTime-entry.getTotalSCPWaitTime());
						entry.setSCPWaitTime(shadowCasPoolWaitTime);
					}
					else
					{
						//	populate CAS pool metric 
						serviceMetrics.setCasPoolWaitTime(casPoolWaitTime-entry.getTotalCPWaitTime());
						entry.setCPWaitTime(casPoolWaitTime);
					}
					//	Add metrics collected from the service to the array of metrics
					//	in the current sampling (interval). The metrics array will 
					//	be provided to all listeners plugged into this monitor.
					metrics[index++] = serviceMetrics;
					//	Save current metrics for the next delta
					entry.setIdleTime(idleTime);
					entry.incrementCASCount(processCount);
					entry.setLastWaitTimeOnCMGetNext(timeInCMGetNext);
				}
				catch( Exception e){e.printStackTrace();}
			} // for
			synchronized( this )
			{
				try
				{
					wait(interval);
				}
				catch( InterruptedException e)
				{
					running = false;
				}
			}
			initial = false;
			
			//	Notify listeners with current metrics collected from MBeans
			notifyListeners(uptime, metrics);
		}
	}
	/*
	 * For simplicity, we declare "throws Exception". Real programs will usually
	 * want finer-grained exception handling.
	 */
	public static void main(String[] args) throws Exception {

		JmxMonitor monitor = new JmxMonitor();
		if ( System.getProperty("verbose") != null )
		{
			//	Shows a list of UIMA-AS services found in a given JMX server
			monitor.setVerbose();
		}
		long samplingFrequency = 1000;  // default sampling every 1000 ms
		
		if ( args.length == 0 || args.length > 2)
		{
			printUsage();
			System.exit(-1);
		}

		if ( args.length == 3 )
		{
			//	The second parameter is the sampling frequency
			samplingFrequency = Long.parseLong(args[2]);
		}
		
		echo("\nJMX Sampling Frequency:"+samplingFrequency);
		//	Use the URI provided in the first arg to connect to the JMX server.
		//	Also define sampling frequency. The monitor will collect the metrics
		//	at this interval.
		monitor.initialize(args[0], samplingFrequency);
		// Create listener
		BasicUimaJmxMonitorListener listener = new BasicUimaJmxMonitorListener(monitor.getMaxServiceNameLength());
		//	Plug in the monitor listener
		monitor.addJmxMonitorListener(listener);
		//	Create and start the monitor thread
		Thread t = new Thread(monitor);
		t.start();
		
	}

	private static void printUsage()
	{
		System.out.println("Usage: \njava -cp %UIMA_HOME%/lib/uimaj-as-activemq.jar;%UIMA_HOME%/lib/uimaj-as-core.jar;%UIMA_HOME%/lib/uima-core.jar;%UIMA_HOME%/apache-activemq-4.1.1/apache-activemq-4.1.1.jar -Djava.util.logging.config.file=%UIMA_HOME%/config/MonitorLogger.properties    org.apache.uima.aae.jmx.monitor.JmxMonitor uri <frequency>\nuri - jmx server URI- required\nfrequency - how often the checkpoint is done. Default: 1000ms - optional");
	}
	private static void echo(String msg) {
		System.out.println(msg);
	}

	private class StatEntry
	{
		ServicePerformanceMBean servicePerformanceMBeanProxy;
		ServiceInfoMBean serviceInfoMBeanProxy;
		QueueViewMBean queueInfo;
		String name="";
		
		double lastIdleTime = 0;
		double lastWaitTimeOnCMGetNext = 0;
		long casProcessedCount = 0;
		double cpWaitTime;
		double scpWaitTime;
		
		public StatEntry( ServicePerformanceMBean perfBean, ServiceInfoMBean infoBean)
		{
			servicePerformanceMBeanProxy = perfBean;
			serviceInfoMBeanProxy = infoBean;
		}
		public void setQueueInfo( QueueViewMBean queueView)
		{
			queueInfo = queueView;
		}
		
		public QueueViewMBean getQueueInfo()
		{
			return queueInfo;
		}
		public double getIdleTime() {
			return lastIdleTime;
		}
		public void incrementCASCount(long aCASCount )
		{
			casProcessedCount =+ aCASCount;
		}
		
		public long getLastCASCount()
		{
			return casProcessedCount;
		}
		public void setIdleTime(double lastIdleTime) {
			this.lastIdleTime = lastIdleTime;
		}
		public ServicePerformanceMBean getServicePerformanceMBeanProxy() {
			return servicePerformanceMBeanProxy;
		}

		public ServiceInfoMBean getServiceInfoMBeanProxy() {
			return serviceInfoMBeanProxy;
		}
		public void setName( String aName )
		{
			name = aName;
		}
		
		public String getName()
		{
			return name;
		}

		public void setLastWaitTimeOnCMGetNext( double aLastWaitTimeOnCMGetNext)
		{
			lastWaitTimeOnCMGetNext = aLastWaitTimeOnCMGetNext;
		}
		
		public double getLastWaitTimeOnCMGetNext()
		{
			return lastWaitTimeOnCMGetNext;
		}
		public double getTotalCPWaitTime()
		{
			return cpWaitTime;
		}
		
		public void setCPWaitTime(double aWaitTime)
		{
			cpWaitTime = aWaitTime;
		}
		public double getTotalSCPWaitTime()
		{
			return scpWaitTime;
		}
		
		public void setSCPWaitTime(double aWaitTime)
		{
			scpWaitTime = aWaitTime;
		}
	}
}
