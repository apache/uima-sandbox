package org.apache.uima.adapter.jms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.adapter.jms.activemq.BrokerDeployer;
import org.apache.uima.adapter.jms.activemq.UimaEEAdminSpringContext;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class UIMA_Service
{
	private static final Class CLASS_NAME = UIMA_Service.class;

	private BrokerService service;
	private TransportConnector connector;
	private TransportConnector stompConnector;
	private TransportConnector httpConnector;
	private Object semaphore = new Object();

	public void startInternalBroker() throws Exception
	{

		service = new BrokerService();
		String[] connectors = service.getNetworkConnectorURIs();
		String brokerURI;
		if (connectors != null)
		{
			for (int i = 0; i < connectors.length; i++)
			{
				System.out.println("ActiveMQ Broker Started With Connector:" + connectors[i]);
			}
			brokerURI = service.getMasterConnectorURI();
		}
		else
		{
			String connectorList = "";
			service.setPersistent(false);
			service.setUseJmx(true);
			brokerURI = generateInternalURI("tcp", 18810, true, false);
			connector = service.addConnector(brokerURI);
			System.out.println("Adding TCP Connector:" + connector.getConnectUri());
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "JmsMessageContext", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_adding_connector__CONFIG", new Object[]
			{ "Adding TCP Connector", connector.getConnectUri() });

			connectorList = connector.getName();
			if (System.getProperty("StompSupport") != null)
			{
				String stompURI = generateInternalURI("stomp", 61613, false, false);
				stompConnector = service.addConnector(stompURI);
				System.out.println("Adding STOMP Connector:" + stompConnector.getConnectUri());
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "JmsMessageContext", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_adding_connector__CONFIG", new Object[]
				{ "Adding STOMP Connector", stompConnector.getConnectUri() });
				connectorList += "," + stompConnector.getName();
			}
			if (System.getProperty("HTTP") != null)
			{
				String stringPort = System.getProperty("HTTP");
				int p = Integer.parseInt(stringPort);
				String httpURI = generateInternalURI("http", p, false, true);
				httpConnector = service.addConnector(httpURI);
				System.out.println("Adding HTTP Connector:" + httpConnector.getConnectUri());
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "JmsMessageContext", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_adding_connector__CONFIG", new Object[]
				{ "Adding HTTP Connector", httpConnector.getConnectUri() });

				connectorList += "," + httpConnector.getName();
			}
			service.start();
			System.setProperty("ActiveMQConnectors", connectorList);
			System.out.println("Broker Service Started - URL:" + service.getVmConnectorURI());
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "JmsMessageContext", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_broker_started__CONFIG", new Object[]
			{ service.getVmConnectorURI() });
		}

		// Allow the connector time to start
		synchronized (semaphore)
		{
			semaphore.wait(5000);
		}
		System.setProperty("jms.brokerUrl", brokerURI);
	}

	/**
	 * Generates a unique port for the Network Connector that will be plugged
	 * into the internal Broker. This connector externalizes the internal broker
	 * so that remote delegates can reply back to the Aggregate. This method
	 * tests port 18810 for availability and it fails increments the port by one
	 * until a port is valid.
	 * 
	 * @return - Broker URI with a unique port
	 */

	private String generateInternalURI(String aProtocol, int aDefaultPort, boolean cacheURL, boolean oneTry) throws Exception
	{
		boolean success = false;
		int openPort = aDefaultPort;
		ServerSocket ssocket = null;

		while (!success)
		{
			try
			{
				ssocket = new ServerSocket(openPort);
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "generateInternalURI", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_port_available__CONFIG", new Object[]
				{ openPort });

				String uri = aProtocol + "://" + ssocket.getInetAddress().getLocalHost().getCanonicalHostName() + ":" + openPort;
				success = true;
				if (cacheURL)
				{
					System.setProperty("BrokerURI", uri);
				}
				return uri;
			}
			catch (BindException e)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "generateInternalURI", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_port_not_available__CONFIG", new Object[]
				{ openPort });
				if (oneTry)
				{
					System.out.println("Given port:" + openPort + " is not available for " + aProtocol);
					throw e;
				}
				openPort++;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "generateInternalURI", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING", new Object[]
				{ JmsConstants.threadName(), e });
				if (oneTry)
				{
					throw e;
				}
			}
			finally
			{
				try
				{
					if (ssocket != null)
					{
						ssocket.close();
					}
				}
				catch (IOException ioe)
				{
				}
			}
		}
		return null;

	}

	public ApplicationContext initialize(String[] springXmlConfigFiles) throws ResourceInitializationException
	{
		try
		{
			ApplicationContext ctx = new FileSystemXmlApplicationContext(springXmlConfigFiles);
			((AbstractApplicationContext) ctx).registerShutdownHook();
			return ctx;
		}
		catch (Exception e)
		{
			throw new ResourceInitializationException(e);
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
				+ "path to Spring XML Configuration File which is the output of running dd2spring\n");
	}

	/**
	 * The main routine for starting the deployment of a UIMA-EE instance. The
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
			return;
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
				springConfigFileArray[dd] = "file:" + springConfigFile.getAbsolutePath();

				// if any are bad, fail
				if (null == springConfigFile)
				{
					return;
				}

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

		// now try to deploy the array of spring context files
		for (int cf = 0; cf < springConfigFileArray.length; cf++)
		{
			try
			{
				ApplicationContext ctx = new FileSystemXmlApplicationContext(springConfigFileArray[cf]);

				UimaEEAdminSpringContext springAdminContext = new UimaEEAdminSpringContext((FileSystemXmlApplicationContext) ctx);
				String topLevelBroker = null;

				String[] factoryBeans = ctx.getBeanNamesForType(org.apache.activemq.ActiveMQConnectionFactory.class);
				for (int i = 0; factoryBeans != null && i < factoryBeans.length; i++)
				{
					if (factoryBeans[i].startsWith("qBroker_tcp_c"))
					{
						ActiveMQConnectionFactory factory = (ActiveMQConnectionFactory) ctx.getBean(factoryBeans[i]);
						topLevelBroker = factory.getBrokerURL();
						break;
					}
				}

				String[] controllers = ctx.getBeanNamesForType(org.apache.uima.aae.controller.AnalysisEngineController.class);
				for (int i = 0; controllers != null && i < controllers.length; i++)
				{
					AnalysisEngineController controller = (AnalysisEngineController) ctx.getBean(controllers[i]);
					controller.setUimaEEAdminContext(springAdminContext);
					if (controller.isTopLevelComponent())
					{
						controller.getInputChannel().setServerUri(topLevelBroker);
						controller.setDeployDescriptor(deployedDescriptors[cf]);
						if (controller instanceof AggregateAnalysisEngineController)
						{
							String[] brokerDeployerBeanName = ctx.getBeanNamesForType(org.apache.uima.adapter.jms.activemq.BrokerDeployer.class);
							if (brokerDeployerBeanName != null && brokerDeployerBeanName.length > 0)
							{
								springAdminContext.setBroker(((BrokerDeployer) ctx.getBean(brokerDeployerBeanName[0])).getBroker());
							}
						}
					}
				}
				((AbstractApplicationContext) ctx).registerShutdownHook();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
