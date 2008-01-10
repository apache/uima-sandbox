package org.apache.uima.aae.handler;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.PrimitiveAnalysisEngineController;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.MessageContext;
import org.apache.uima.aae.message.UIMAMessage;
import org.apache.uima.aae.monitor.Monitor;
import org.apache.uima.cas.CAS;
import org.apache.uima.util.Level;

public abstract class HandlerBase implements Handler
{
	private static final Class CLASS_NAME = HandlerBase.class;

	private Handler delegateHandler;
	private Handler parentHandler;
    private String handlerName;
    private AnalysisEngineController controller;
    
	public AnalysisEngineController getController()
	{
		return controller;
	}

	public void setController(AnalysisEngineController controller)
	{
		this.controller = controller;
	}
	public static ErrorContext populateErrorContext( MessageContext aMessageCtx )
	{
		ErrorContext errorContext = new ErrorContext();
		if ( aMessageCtx != null )
		{
			try
			{
				if ( aMessageCtx.propertyExists(AsynchAEMessage.Command))
				{
					errorContext.add( AsynchAEMessage.Command, aMessageCtx.getMessageIntProperty(AsynchAEMessage.Command));
				}
			
				if ( aMessageCtx.propertyExists(AsynchAEMessage.MessageType))
				{
					errorContext.add( AsynchAEMessage.MessageType, aMessageCtx.getMessageIntProperty(AsynchAEMessage.MessageType));
				}
				
				if ( aMessageCtx.propertyExists(AsynchAEMessage.CasReference))
				{
					errorContext.add( AsynchAEMessage.CasReference, aMessageCtx.getMessageIntProperty(AsynchAEMessage.CasReference));
				}
				errorContext.add(UIMAMessage.RawMsg, aMessageCtx.getRawMessage());
			}
			catch( Exception e) { /*ignore */ }
		}
		return errorContext;
	}

	public void validate(Object anObjectToHandle) throws AsynchAEException
	{
		if (anObjectToHandle == null)
		{
			throw new AsynchAEException("Nothing to Handle - Input Object is Null");
		}
		if (  !(anObjectToHandle instanceof MessageContext) )
		{
			throw new AsynchAEException("Invalid Object in Handler. Expected MessageContext Instead Got::"+anObjectToHandle.getClass().getName());
		}
	}

	public boolean isHandlerForMessage( MessageContext aMessageContext, int anExpectedMessageType, int anExpectedCommand ) throws AsynchAEException
	{
		int messageType = aMessageContext.getMessageIntProperty(AsynchAEMessage.MessageType);
		int command = aMessageContext.getMessageIntProperty(AsynchAEMessage.Command);
		if (anExpectedMessageType == messageType && anExpectedCommand == command)
		{
			return true;
		}
		return false;
	}
	public void invokeProcess(CAS aCAS, String anInputCasReferenceId, String aNewCasReferenceId, MessageContext aMessageContext, String aNewCasProducedBy) throws AsynchAEException
	{
		try
		{
			//	Use empty string as key. Top level component stats are stored under this key.
			controller.getMonitor().incrementCount("", Monitor.ProcessCount);

			if ( controller instanceof AggregateAnalysisEngineController )
			{
				boolean isNewCAS = aMessageContext.propertyExists(AsynchAEMessage.CasSequence);
				if ( isNewCAS )
				{
					((AggregateAnalysisEngineController)controller).process(aCAS, anInputCasReferenceId, aNewCasReferenceId, aNewCasProducedBy);
				}
				else
				{
					((AggregateAnalysisEngineController)controller).process(aCAS, anInputCasReferenceId);
				}
			}
			else if ( controller instanceof PrimitiveAnalysisEngineController )
			{
				((PrimitiveAnalysisEngineController)controller).process(aCAS, anInputCasReferenceId, aMessageContext.getEndpoint());
			}
			else
			{
				throw new AsynchAEException("Invalid Controller. Expected AggregateController or PrimitiveController. Got:"+controller.getClass().getName());
			}
		}
		catch ( AsynchAEException e)
		{
			throw e;
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
		
		
	}
	public void delegate( MessageContext aMessageContext) throws AsynchAEException
	{
		int messageType = aMessageContext.getMessageIntProperty(AsynchAEMessage.MessageType);
		if ( hasDelegateHandler() )
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "delegate", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_dispatch_msg_to_handler__FINE",
	                new Object[] { messageType, aMessageContext.getEndpoint().getEndpoint(), getDelegate().getName()  });
			
			getDelegate().handle(aMessageContext);
		}
		else
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "delegate", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_handler_for_message__FINE",
	                new Object[] { messageType, aMessageContext.getEndpoint().getEndpoint()  });
		}
	}
	public HandlerBase(String aName )
    {
    	handlerName = aName;
    }

	public Handler getDelegate()
    {
    	return delegateHandler;
    }
    
	public void setParent(Handler aHandler)
	{
		parentHandler = aHandler;
	}
	public String getName()
	{
		return handlerName;
	}
	public void setDelegate(Handler aHandler)
	{
		delegateHandler = aHandler;
	}
	public boolean hasDelegateHandler()
	{
		return ( delegateHandler != null );
	}
	
	public void removeDelegate(String aHandlerName)
	{
		if ( handlerName.equals( aHandlerName))
		{
			if ( hasDelegateHandler())
			{
				//	connect the delegate with the parent of this handler
				((HandlerBase)delegateHandler).setParent(parentHandler);
				parentHandler.setDelegate(delegateHandler);
			}
		}
		else
		{
			delegateHandler.removeDelegate(aHandlerName);
		}
		
	}

	public void resequenceDelegateHandler(String aHandlerName, int aNewPositionInHandlerChain)
	{
		// TODO Auto-generated method stub

	}
	public void handle(Object anObjectToHandle) throws AsynchAEException
	{
		getController().getControllerLatch().waitUntilInitialized();
	}
	
	public void handle(Object anObjectToHandle, String expectedOutputType) throws AsynchAEException
	{
	}
	

}
