
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

package org.apache.uima.adapter.jms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.jmx.monitor.BasicUimaJmxMonitorListener;
import org.apache.uima.aae.jmx.monitor.JmxMonitor;
import org.apache.uima.aae.jmx.monitor.JmxMonitorListener;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.adapter.jms.activemq.SpringContainerDeployer;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class UIMA_Service implements  ApplicationListener
{
	private static final Class CLASS_NAME = UIMA_Service.class;

	protected boolean serviceInitializationCompleted;
	protected boolean serviceInitializationException;
	protected Object serviceMonitor = new Object();
	private JmxMonitor monitor = null;
	private Thread monitorThread = null;
	
	/**
	 * Parse command args, run dd2spring on the deployment descriptors to generate Spring context files.
	 * 
	 * @param args - command line arguments
	 * @return - an array of Spring context files generated from provided deployment descriptors
	 * @throws Exception
	 */
	public String[] initialize(String[] args) throws Exception
	{
		UIMAFramework.getLogger(CLASS_NAME).log(Level.INFO, "UIMA-AS version " + UIMAFramework.getVersionString());

		String[] springConfigFileArray =
		{};
		String[] deployedDescriptors =
		{};
		String[] deploymentDescriptors =
		{};
		int nbrOfArgs = args.length;

		deploymentDescriptors = getMultipleArg("-d", args);
		if (deploymentDescriptors.length == 0)
		{
		  // allow multiple args for one key
			deploymentDescriptors = getMultipleArg2("-dd", args);
		}
		String saxonURL = getArg("-saxonURL", args);
		String xslTransform = getArg("-xslt", args);
		String uimaAsDebug = getArg("-uimaEeDebug", args);
				
		if (nbrOfArgs < 1 || (args[0].startsWith("-") && (deploymentDescriptors.length == 0 || saxonURL.equals("") || xslTransform.equals(""))))
		{
			printUsageMessage();
			return null;
		}
    String brokerURL = getArg("-brokerURL",args);
    if ( brokerURL != null ) {
      System.out.println(">>> Setting defaultBrokerURL to:"+brokerURL);
      System.setProperty("defaultBrokerURL", brokerURL);
    } else {
      System.setProperty("defaultBrokerURL", "tcp://localhost:61616");
    }
    
    if (deploymentDescriptors.length == 0)
		{
			// array of context files passed in
			springConfigFileArray = args;
			deployedDescriptors = args;
		}
		else
		{
			// create a String array of spring context files
			springConfigFileArray = new String[deploymentDescriptors.length];
			deployedDescriptors = new String[deploymentDescriptors.length];

			Dd2spring aDd2Spring = new Dd2spring();
			for (int dd = 0; dd < deploymentDescriptors.length; dd++)
			{
				String deploymentDescriptor = deploymentDescriptors[dd];

				File springConfigFile = aDd2Spring.convertDd2Spring(deploymentDescriptor, xslTransform, saxonURL, uimaAsDebug);

				// if any are bad, fail
				if (null == springConfigFile)
				{
					return null;
				}
				springConfigFileArray[dd] = springConfigFile.getAbsolutePath();

				// get the descriptor to register with the engine controller
				String deployDescriptor = "";
				File afile = null;
				FileInputStream fis = null;
				try
				{
					afile = new File(deploymentDescriptor);
					fis = new FileInputStream(afile);
					byte[] bytes = new byte[(int) afile.length()];
					fis.read(bytes);
					deployDescriptor = new String(bytes);
					deployedDescriptors[dd] = deployDescriptor;
					// Log Deployment Descriptor
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "main", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_deploy_desc__FINEST", new Object[]
					{ deployDescriptor });
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					if (fis != null)
					{
						try
						{
							fis.close();
						}
						catch (IOException e)
						{
						}
					}
				}
			}
		}
		return springConfigFileArray;
		

		
	}
	/**
	 * Deploy Spring context files in a Spring Container.
	 * 
	 * @param springContextFiles - array of Spring context files
	 * 
	 * @throws Exception
	 */
	public void deploy( String[] springContextFiles ) throws Exception
	{
		SpringContainerDeployer springDeployer =
			new SpringContainerDeployer();
		// now try to deploy the array of spring context files
		springDeployer.deploy(springContextFiles);
		//	Poll the deployer for the initialization status. Wait for either successful
		//	initialization or failure.
		while( !springDeployer.isInitialized() && !springDeployer.initializationFailed())
		{
			synchronized( springDeployer )
			{
				springDeployer.wait(100);
			}
		}
		//	Check if the deployer failed
		if ( springDeployer.initializationFailed() )
		{
			throw new ResourceInitializationException();
		}
		//	Register this class to receive Spring container notifications. Specifically, looking
		//	for an even signaling the container termination. This is done so that we can stop
		//	the monitor thread
		FileSystemXmlApplicationContext context = springDeployer.getSpringContext();
		context.addApplicationListener(this);
	}
	/**
	 * Creates an instance of a {@link JmxMonitor}, initializes it with the JMX Server URI and
	 * checkpoint frequency, and finally starts the monitor.
	 * 
	 * @param samplingFrequency - how often the JmxMonitor should checkpoint to fetch service metrics
	 * 
	 * @throws Exception - error on monitor initialization or startup
	 */
	public void startMonitor(long samplingFrequency) throws Exception
	{
		monitor = new JmxMonitor();

		//	Check if the monitor should run in the verbose mode. In this mode
		//	the monitor dumps JMX Server URI, and a list of UIMA-AS services
		//	found in that server. The default is to not show this info.
		if ( System.getProperty("verbose") != null )
		{
			monitor.setVerbose();
		}

		//	Use the URI provided in the first arg to connect to the JMX server.
		//	Also define sampling frequency. The monitor will collect the metrics
		//	at this interval.
		String jmxServerPort = null;
		//	get the port of the JMX Server. This property can be set on the command line via -d
		//	OR it is set automatically by the code that creates an internal JMX Server. The latter
		//	is created in the {@link BaseAnalysisEngineController} constructor.
		if ( ( jmxServerPort = System.getProperty("com.sun.management.jmxremote.port")) != null )
		{
			//	parameter is set, compose the URI
			String jmxServerURI = "service:jmx:rmi:///jndi/rmi://localhost:"+jmxServerPort+"/jmxrmi";
			//	Connect to the JMX Server, configure checkpoint frequency, create MBean proxies for 
			//	UIMA-AS MBeans and service input queues
			monitor.initialize(jmxServerURI, samplingFrequency);
			// Create formatter listener
			JmxMonitorListener listener = null;
			String formatterListenerClass = null;
			//	Check if a custom monitor formatter listener class is provided. The user provides this
			//	formatter by adding a -Djmx.monitor.formatter=<class> parameter which specifies a class
			//	that implements {@link JmxMonitorListener} interface
			if ( (formatterListenerClass = System.getProperty(JmxMonitor.FormatterListener)) != null )
			{
				Object object = null;
				try
				{
					//	Instantiate the formatter listener class
					Class formatterClass = Class.forName( formatterListenerClass );
					object = formatterClass.newInstance();
				}
				catch( ClassNotFoundException e)
				{
					System.out.println("Class Not Found:"+formatterListenerClass+". Provide a Formatter Class Which Implements:org.apache.uima.aae.jmx.monitor.JmxMonitorListener");
					throw e;
				}
				if ( object instanceof JmxMonitorListener )
				{
					listener = (JmxMonitorListener)object; 
				}
				else
				{
					throw new InvalidClassException("Invalid Monitor Formatter Class:"+formatterListenerClass+".The Monitor Requires a Formatter Which Implements:org.apache.uima.aae.jmx.monitor.JmxMonitorListener");
				}
			}
			else
			{
				//	The default formatter listener which logs to the UIMA log
				listener = new BasicUimaJmxMonitorListener(monitor.getMaxServiceNameLength());
			}
			//	Plug in the monitor listener
			monitor.addJmxMonitorListener(listener);
			//	Create and start the monitor thread
			monitorThread = new Thread(monitor);
			
			//	Start the monitor thread. It will run until the Spring container stops. When this happens
			//	the UIMA_Service receives notication via a {@code onApplicationEvent()} callback. There
			//	the monitor is stopped allowing the service to terminate.
			monitorThread.start();
			System.out.println(">>> Started JMX Monitor.\n\t>>> MBean Server Port:"+jmxServerPort+"\n\t>>> Monitor Checkpoint Frequency:"+samplingFrequency+"\n\t>>> Monitor Formatter Class:"+listener.getClass().getName());
		}
		
	}

	/**
	 * scan args for a particular arg, return the following token or the empty
	 * string if not found
	 * 
	 * @param id
	 *            the arg to search for
	 * @param args
	 *            the array of strings
	 * @return the following token, or a 0 length string if not found
	 */
	private static String getArg(String id, String[] args)
	{
		for (int i = 0; i < args.length; i++)
		{
			if (id.equals(args[i]))
				return (i + 1 < args.length) ? args[i + 1] : "";
		}
		return "";
	}

	/**
	 * scan args for a particular arg, return the following token(s) or the
	 * empty string if not found
	 * 
	 * @param id
	 *            the arg to search for
	 * @param args
	 *            the array of strings
	 * @return the following token, or a 0 length string array if not found
	 */
	private static String[] getMultipleArg(String id, String[] args)
	{
		String[] retr =
		{};
		for (int i = 0; i < args.length; i++)
		{
			if (id.equals(args[i]))
			{
				String[] temp = new String[retr.length + 1];
				for (int s = 0; s < retr.length; s++)
				{
					temp[s] = retr[s];
				}
				retr = temp;
				retr[retr.length - 1] = (i + 1 < args.length) ? args[i + 1] : null;
			}
		}
		return retr;
	}

  /**
   * scan args for a particular arg, return the following token(s) or the empty
   * string if not found
   * 
   * @param id
   *            the arg to search for
   * @param args
   *            the array of strings
   * @return the following token, or a 0 length string array if not found
   */
  private static String[] getMultipleArg2(String id, String[] args)
  {
    String[] retr = {};
    for (int i = 0; i < args.length; i++) {
      if (id.equals(args[i])) {
        int j=0;
        while ( (i+1+j < args.length) && !args[i+1+j].startsWith("-") ) {
          String[] temp = new String[retr.length + 1];
          for (int s=0; s<retr.length; s++) {
            temp[s] = retr[s];
            }
          retr = temp;
          retr[retr.length-1] = args[i + 1 + j++];
          }
        return retr;
        }
      }
    return retr;
    }
  
	protected void finalize()
	{
	     System.err.println(this + " finalized");
	}	

	private static void printUsageMessage()
	{
		System.out.println(" Arguments to the program are as follows : \n" + "-d path-to-UIMA-Deployment-Descriptor [-d path-to-UIMA-Deployment-Descriptor ...] \n" + "-saxon path-to-saxon.jar \n" + "-xslt path-to-dd2spring-xslt\n" + "   or\n"
				+ "path to Spring XML Configuration File which is the output of running dd2spring\n"+
				"-defaultBrokerURL the default broker URL to use for the service and all its delegates");
	}
	public void onApplicationEvent(ApplicationEvent event) {
		if ( event instanceof ContextClosedEvent && monitor != null && monitor.isRunning() )
		{
			System.out.println("Stopping Monitor");
			//	Stop the monitor. The service has stopped
			monitor.doStop();
		}
	}

	/**
	 * The main routine for starting the deployment of a UIMA-AS instance. The
	 * args are either: 1 or more "paths" to Spring XML descriptors representing
	 * the information needed or some number of parameters, preceeded by a "-"
	 * sign. If the first arg doesn't start with a "-" it is presumed to be the
	 * first format.
	 * 
	 * For the 2nd style, the arguments are: -saxonURL a-URL-to-the-saxon-jar
	 * usually starting with "file:", -xslt path-to-the-dd2spring.xsl file, -d
	 * path-to-UIMA-deployment-descriptor [-d path-to-another-dd ...] these
	 * arguments may be in any order)
   * 
   * For the 3rd style, like #2 but with multiple dd-files following a single -dd
   * Useful for calling from scripts.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			UIMA_Service service = new UIMA_Service();
			//	parse command args and run dd2spring to generate spring context
			//	files from deployment descriptors
			String contextFiles[] = service.initialize(args);
			//	If no context files generated there is nothing to do
			if ( contextFiles == null )
			{
				return;
			}
			//	Deploy components defined in Spring context files. This method blocks until
			//	the container is fully initialized and all UIMA-AS components are succefully
			//	deployed.
			service.deploy( contextFiles );
			//	Check if we should start an optional JMX-based monitor that will provide service metrics
			//	The monitor is enabled by existence of -Djmx.monitor.frequency=<number> parameter. By default
			//	the monitor is not enabled.
			String monitorCheckpointFrequency;
			if ( ( monitorCheckpointFrequency = System.getProperty(JmxMonitor.CheckpointFrequency)) != null)
			{
				//	Found monitor checkpoint frequency parameter, configure and start the monitor.
				//	If the monitor fails to initialize the service is not effected. 
				service.startMonitor(Long.parseLong(monitorCheckpointFrequency));
			}
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}

}
