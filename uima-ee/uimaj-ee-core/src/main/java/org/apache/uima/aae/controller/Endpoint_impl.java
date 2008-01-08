package org.apache.uima.aae.controller;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.MessageTimeoutException;
import org.apache.uima.aae.jmx.ServiceInfo;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.util.Level;

public class Endpoint_impl implements Endpoint, Cloneable
{
	private static final Class CLASS_NAME = Endpoint_impl.class;

	private Object destination = null;
	
	private String endpoint;

	private String serverURI;

	private boolean initialized;

	private Timer timer;

	private String replyTo;
	
	private boolean waitingForResponse;

	private int metadataRequestTimeout;

	private int processRequestTimeout;

	private int collectionProcessCompleteTimeout;
	
	private boolean isRemote;

	private String descriptor;

	private String serializer;

	private boolean finalEndpoint;
	
	private final long timeIn = System.nanoTime(); 
	
	private long checkpointTimer;
	
	private AnalysisEngineController controller;
	
	private Endpoint selfRef = this;
	
	private boolean retryEnabled;
	
	private Object monitor = new Object();
	
	private String highWaterMark = null;
	
	private boolean completedProcessingCollection;
	
	private boolean noConsumers =false;
	
	private boolean remove = false;
	
	private boolean isCasMultiplier = false;
	
	private int shadowCasPoolSize = 0;
	
	private boolean isReplyEndpointFlag;
	
	private ServiceInfo serviceInfo = null;

	private int command; 
	
	private boolean registeredWithParent;
	
		
	private int initialHeapSize;
	
	public int getCommand()
	{
		return command;
	}

	public void setCommand(int command)
	{
		this.command = command;
	}

	public void setNoConsumers(boolean trueOrFalse)
	{
		noConsumers = trueOrFalse;
	}
	
	public void setReplyEndpoint(boolean tORf )
	{
		isReplyEndpointFlag = tORf;
	}
	public boolean isReplyEndpoint()
	{
		return isReplyEndpointFlag;
	}
	public boolean remove()
	{
		return remove;
		
	}
	
	public void setRemove( boolean rm)
	{
		remove = rm;
	}
	public boolean hasNoConsumers()
	{
		return noConsumers;
	}
	public String getReplyToEndpoint()
	{
		return replyTo;
	}
	public void setReplyToEndpoint( String anEndpointName )
	{
		replyTo = anEndpointName;
	}
	public boolean completedProcessingCollection()
	{
		return completedProcessingCollection;
	}
	
	public void setCompletedProcessingCollection(  boolean completed )
	{
		completedProcessingCollection = completed;
	}

	
	public void setHighWaterMark( String aHighWaterMark )
	{
		highWaterMark = aHighWaterMark;
	}
	
	public String getHighWaterMark()
	{
		return highWaterMark;
	}
	
	public boolean isRetryEnabled()
	{
		return retryEnabled;
	}
	public void setRetryEnabled(boolean retryEnabled)
	{
		this.retryEnabled = retryEnabled;
	}
	public void setController( AnalysisEngineController aController )
	{
		controller = aController;
	}
	public void startCheckpointTimer()
	{
		checkpointTimer = System.nanoTime();
	}
	public long getCheckpointTimer()
	{
		return checkpointTimer;
	}


	public long getEntryTime()
	{
		return timeIn;
	}
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch ( CloneNotSupportedException e)
		{
			throw new InternalError(e.toString());
		}
	}

	public String getSerializer()
	{
		return serializer;
	}
	public boolean isFinal()
	{
		return finalEndpoint;
	}
	
	public void setFinal( boolean isFinal )
	{
		finalEndpoint = isFinal;
	}
	public void setSerializer(String serializer)
	{
		this.serializer = serializer;
	}

	public int getMetadataRequestTimeout()
	{
		return metadataRequestTimeout;
	}

	public void setMetadataRequestTimeout(int metadataRequestTimeout)
	{
		this.metadataRequestTimeout = metadataRequestTimeout;
	}

	public int getProcessRequestTimeout()
	{
		return processRequestTimeout;
	}

	public void setProcessRequestTimeout(int processRequestTimeout)
	{
		this.processRequestTimeout = processRequestTimeout;
	}

	public void setCollectionProcessCompleteTimeout(int cpcTimeout)
	{
		this.collectionProcessCompleteTimeout = cpcTimeout;
	}

	public int getCollectionProcessCompleteTimeout()
	{
		return collectionProcessCompleteTimeout;
	}
	public boolean isInitialized()
	{
		return initialized;
	}

	public void setInitialized(boolean initialized)
	{
		this.initialized = initialized;
	}

	public String getEndpoint()
	{
		return endpoint;
	}

	public void setEndpoint(String endpoint)
	{
		this.endpoint = endpoint;
	}

	public String getServerURI()
	{
		return serverURI;
	}

	public void setServerURI(String aServerURI)
	{
		this.serverURI = aServerURI;
		if ( aServerURI != null && aServerURI.startsWith("vm:") == true )
		{
			setRemote(false);
		}
		else
		{
			setRemote(true);
		}
	}
	public void setWaitingForResponse(boolean isWaiting)
	{
		waitingForResponse = isWaiting;
		
	}

	private void startTimer(final int aTimeToWait, String aCasReferenceId, int command)
	{
		synchronized( monitor )
		{
			final String casReferenceId = aCasReferenceId;
			final int cmd = command;
			Date timeToRun = new Date(System.currentTimeMillis() + aTimeToWait);
			
			
			setWaitingForResponse(true);
//			timer = new Timer();
			
			
			if ( controller != null )
			{
				timer = new Timer("Controller:"+controller.getComponentName()+":TimerThread-Endpoint_impl:"+endpoint+":"+System.nanoTime()+":Cmd:"+cmd);
			}
			else
			{
				timer = new Timer("TimerThread-Endpoint_impl:"+endpoint+":"+System.nanoTime()+":Cmd:"+cmd);
			}

			
			timer.schedule(new TimerTask() {
				public void run()
				{
					if ( AsynchAEMessage.Process == cmd  )
					{
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, this.getClass().getName(),
				                "TimerTask.run", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cas_timeout_no_reply__INFO",
				                new Object[] {  endpoint, aTimeToWait, casReferenceId });
					}
					else if ( AsynchAEMessage.GetMeta == cmd )
					{
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, this.getClass().getName(),
				                "TimerTask.run", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_meta_timeout_no_reply__INFO",
				                new Object[] {  endpoint, aTimeToWait });
					}
					else
					{
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, this.getClass().getName(),
				                "TimerTask.run", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cpc_timeout_no_reply__INFO",
				                new Object[] {  endpoint, aTimeToWait });

					}
					
					waitingForResponse = false;
					if ( timer != null )
					{
						timer.cancel();
						timer.purge();
					}

					if ( controller != null )
					{

							ErrorContext errorContext = new ErrorContext();
							if ( casReferenceId != null )
							{
								errorContext.add(AsynchAEMessage.CasReference, String.valueOf(casReferenceId));
							}
							errorContext.add(AsynchAEMessage.Command, cmd);
							errorContext.add(AsynchAEMessage.Endpoint, selfRef);
							if ( controller != null && controller.getErrorHandlerChain() != null )
							{
								//	Handle Timeout
								controller.getErrorHandlerChain().handle(new MessageTimeoutException(), errorContext, controller);
							}
					}
				}
			}, timeToRun);
		}

	}
	public ServiceInfo getServiceInfo()
	{
		if ( serviceInfo == null )
		{
			serviceInfo = new ServiceInfo();
			serviceInfo.setBrokerURL(serverURI);
			serviceInfo.setInputQueueName(endpoint);
			serviceInfo.setState("Active");
		}
		return serviceInfo;
	}
	public void startProcessRequestTimer(String aCasReferenceId)
	{
		if ( getProcessRequestTimeout() > 0 )
		{
			startTimer(processRequestTimeout, aCasReferenceId, AsynchAEMessage.Process);
		}
		else
		{
			setWaitingForResponse(true);
		}

	}

	public void startMetadataRequestTimer()
	{
		if ( getMetadataRequestTimeout() > 0 )
		{
			startTimer(metadataRequestTimeout, null, AsynchAEMessage.GetMeta);
		}
		else
		{
			setWaitingForResponse(true);
		}
	}

	public void startCollectionProcessCompleteTimer()
	{
		if ( getCollectionProcessCompleteTimeout() > 0 )
		{
			startTimer(collectionProcessCompleteTimeout, null, AsynchAEMessage.CollectionProcessComplete);
		}
		else
		{
			setWaitingForResponse(true);
		}

	}

	public void cancelTimer()
	{
		synchronized( monitor )
		{
			if (timer != null)
			{
				waitingForResponse = false;
				timer.cancel();
				timer = null;
			}
		}
	}

	public boolean isWaitingForResponse()
	{

		return waitingForResponse;
	}

	public boolean isRemote()
	{
		return isRemote;
	}

	public void setRemote(boolean aRemote)
	{
		isRemote = aRemote;

	}

	public String getDescriptor()
	{
		return descriptor;
	}

	public void setDescriptor(String aDescriptor)
	{
		descriptor = aDescriptor;
	}

	public void initialize() throws AsynchAEException
	{
		// TODO Auto-generated method stub

	}
	
	public boolean isOpen()
	{
		return true;
	}
	public void close()
	{
		
	}
	public boolean isCasMultiplier()
	{
		return isCasMultiplier;
	}
	
	public void setIsCasMultiplier(boolean trueORfalse)
	{
		isCasMultiplier = trueORfalse;
	}
	public void setShadowCasPoolSize( int aPoolSize )
	{
		shadowCasPoolSize = aPoolSize;
	}
	
	public int getShadowPoolSize()
	{
		return shadowCasPoolSize;
	}
	
	public Object getDestination()
	{
		return destination;
	}
	public void setDestination( Object aDestination)
	{
		destination = aDestination;
	}
	public void setRegisteredWithParent()
	{
		registeredWithParent = true;
	}
	
	public boolean isRegisteredWithParent()
	{
		return registeredWithParent;
	}
	public void setInitialFsHeapSize(int aHeapSize)
	{
		initialHeapSize = aHeapSize;
	}


}
