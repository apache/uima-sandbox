package org.apache.uima.aae;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContextAdmin;
import org.apache.uima.aae.jmx.JmxManagement;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.asb.impl.FlowControllerContainer;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.analysis_engine.metadata.FlowControllerDeclaration;
import org.apache.uima.analysis_engine.metadata.SofaMapping;
import org.apache.uima.cas.CAS;
import org.apache.uima.flow.FlowController;
import org.apache.uima.flow.FlowControllerContext;
import org.apache.uima.flow.FlowControllerDescription;
import org.apache.uima.flow.impl.FlowControllerContext_impl;
import org.apache.uima.resource.Resource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.resource.metadata.impl.Import_impl;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

public class UimaClassFactory
{
	public static final String IMPORT_BY_NAME_PREFIX = "*importByName:";
	
	public static AnalysisEngine produceAnalysisEngine()
	{
		return null;
	}

	
	public static ResourceSpecifier produceResourceSpecifier( String aFileResource )
	throws InvalidXMLException, ResourceInitializationException, IOException
	{
	    XMLInputSource input = resolveImportByName(aFileResource, UIMAFramework.newDefaultResourceManager());
		return 	UIMAFramework.getXMLParser().parseResourceSpecifier(input);

	}
	
	public static AnalysisEngine produceAnalysisEngine(String aDescriptor)
	throws InvalidXMLException, ResourceInitializationException, IOException
	{
		
		ResourceSpecifier specifier = 
			UIMAFramework.getXMLParser().
				parseAnalysisEngineDescription(new XMLInputSource(new File(aDescriptor)));		
		
		
		
		AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(specifier);
		return ae;
	}

	public static ResourceManager produceResourceManager()
	{
		return UIMAFramework.newDefaultResourceManager();
	}

	public static ProcessingResourceMetaData produceProcessingResourceMetaData(AnalysisEngine analysisEngine)
	{
		return analysisEngine.getProcessingResourceMetaData();
	}
	private static XMLInputSource resolveImportByName( String aFileResource, ResourceManager aResourceManager ) throws InvalidXMLException,
    ResourceInitializationException, IOException
	{
	    XMLInputSource input;
	    
	    if (aFileResource.startsWith(IMPORT_BY_NAME_PREFIX)) 
	    {
	      Import theImport = new Import_impl();
	      theImport.setName(aFileResource.substring(IMPORT_BY_NAME_PREFIX.length()));
	      input = new XMLInputSource(theImport.findAbsoluteUrl(aResourceManager));
	    } 
	    else 
	    {
	      input = new XMLInputSource(aFileResource);
	    }
	    return input;
	    
	}

	public static FlowControllerContainer produceAggregateFlowControllerContainer(
			AnalysisEngineDescription aeSpecifier, String aFlowControllerDescriptor,
	          Map anAggregateMergedTypeSystem, UimaContextAdmin aParentContext, SofaMapping[] aSofaMappings, JmxManagement aJmxManagementInterface ) throws InvalidXMLException,
	          ResourceInitializationException, IOException {
		{
			
		    FlowControllerDeclaration fcd = aeSpecifier.getFlowControllerDeclaration();
		    String key = "_FlowController";   // default
		    if ( fcd != null && fcd.getKey() != null && fcd.getKey().trim().length() > 0)
		    {
		    	key = fcd.getKey();
		    }

		    ResourceManager resourceManager = aParentContext.getRootContext().getResourceManager();   // NEW
			XMLInputSource input = resolveImportByName(aFlowControllerDescriptor, resourceManager);

		    FlowControllerDescription specifier = (FlowControllerDescription) UIMAFramework.getXMLParser()
		            .parseResourceSpecifier(input);
			AnalysisEngineMetaData anAggregateMetadata = aeSpecifier.getAnalysisEngineMetaData();
		    
		    Map sofamap = new TreeMap();
		    if (aSofaMappings != null && aSofaMappings.length > 0) {
		      for (int s = 0; s < aSofaMappings.length; s++) {
		        // the mapping is for this analysis engine
		        if (aSofaMappings[s].getComponentKey().equals(key)) {
		          // if component sofa name is null, replace it with the default for TCAS sofa name
		          // This is to support old style TCAS
		          if (aSofaMappings[s].getComponentSofaName() == null)
		            aSofaMappings[s].setComponentSofaName(CAS.NAME_DEFAULT_SOFA);
		          sofamap.put(aSofaMappings[s].getComponentSofaName(), aSofaMappings[s]
		                  .getAggregateSofaName());
		        }
		      }
		    }
		    Map flowControllerParams = new HashMap();
		    FlowControllerContext fctx = new FlowControllerContext_impl(aParentContext, key,
		    		sofamap, anAggregateMergedTypeSystem, anAggregateMetadata);
		    
		    flowControllerParams.put(Resource.PARAM_UIMA_CONTEXT, fctx);
		    flowControllerParams.put(Resource.PARAM_RESOURCE_MANAGER, resourceManager);
		    flowControllerParams.put(AnalysisEngine.PARAM_MBEAN_NAME_PREFIX, aJmxManagementInterface.getJmxDomain());
		    if( aJmxManagementInterface.getMBeanServer() != null )
		    {
			    flowControllerParams.put(AnalysisEngine.PARAM_MBEAN_SERVER, aJmxManagementInterface.getMBeanServer());
		    }
		    FlowControllerContainer flowControllerContainer = new FlowControllerContainer();
		    flowControllerContainer.initialize(specifier, flowControllerParams);
		    return flowControllerContainer;
		}
	}
	
	public static UimaContextAdmin produceUimaContext()
	{
		UimaContextAdmin uctxt = UIMAFramework.newUimaContext(UIMAFramework.getLogger(), 
				UIMAFramework.newDefaultResourceManager(), 
				UIMAFramework.newConfigurationManager());

		
		return uctxt;
	}
	
	
    public static FlowController produceFlowController( String aFlowControllerDescriptor ) throws Exception
    {
		FlowControllerDescription specifier = (FlowControllerDescription)
			UIMAFramework.getXMLParser().parseResourceSpecifier(new XMLInputSource(new File(aFlowControllerDescriptor)));

		String flowControllerClassName;
		flowControllerClassName = specifier.getImplementationName();

		if (flowControllerClassName == null || flowControllerClassName.length() == 0)
		{
			throw new ResourceInitializationException(ResourceInitializationException.MISSING_IMPLEMENTATION_CLASS_NAME, new Object[] { specifier.getSourceUrlString() });
		}
		// load FlowController class
		Class flowControllerClass = null;
		try
		{
			// use application ClassLoader to load the class
			flowControllerClass = Class.forName(flowControllerClassName);
		}
		catch ( ClassNotFoundException e)
		{
			throw new ResourceInitializationException(ResourceInitializationException.CLASS_NOT_FOUND, new Object[] { flowControllerClassName, specifier.getSourceUrlString() }, e);
		}

		Object userObject;
		try
		{
			userObject = flowControllerClass.newInstance();
		}
		catch ( Exception e)
		{
			throw new ResourceInitializationException(ResourceInitializationException.COULD_NOT_INSTANTIATE, new Object[] { flowControllerClassName, specifier.getSourceUrlString() }, e);
		}
		if (!(userObject instanceof FlowController))
		{
			throw new ResourceInitializationException(ResourceInitializationException.RESOURCE_DOES_NOT_IMPLEMENT_INTERFACE, new Object[] { flowControllerClassName, FlowController.class, specifier.getSourceUrlString() });
		}
		return (FlowController) userObject;
    }



}
