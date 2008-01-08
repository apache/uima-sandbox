package org.apache.uima.aae.deployment;

import java.util.List;
import java.util.Map;

import org.apache.uima.aae.deployment.impl.AEDelegates_Impl;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLParser;
import org.w3c.dom.Element;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


public interface AEDeploymentMetaData 
{
    
    /*************************************************************************/

    public boolean isSet (int i);
    
    public void buildFromXMLElement(Element aElement, XMLParser aParser,
            XMLParser.ParsingOptions aOptions) throws InvalidXMLException;

    public void toXML(ContentHandler aContentHandler,
            boolean aWriteDefaultNamespaceAttribute) throws SAXException;

    public AsyncAEErrorConfiguration getAsyncAEErrorConfiguration();
    public void setAsyncAEErrorConfiguration(AsyncAEErrorConfiguration asyncAEErrorConfiguration);

    /**
     * @return the key
     */
    public String getKey();

    /**
     * @param key the key to set
     */
    public void setKey(String key);
    
    public ResourceSpecifier getResourceSpecifier ();
    public void setResourceSpecifier (ResourceSpecifier rs, 
                        ResourceManager aResourceManager, boolean recursive) throws InvalidXMLException;

    public Import getImportedAE();
    public void setImportedAE(Import importedAE);

    public void resolveDelegates (ResourceManager aResourceManager, boolean recursive) throws InvalidXMLException;

    /**
     * @return the numberOfInstances
     */
    public int getNumberOfInstances();

    /**
     * @param numberOfInstances the numberOfInstances to set
     */
    public void setNumberOfInstances(int numberOfInstances);
    
    public boolean isCasMultiplier();

    public void setCasMultiplier(boolean casMultiplier);

    /**
     * @return the poolSize
     */
    public int getCasMultiplierPoolSize();

    /**
     * @param poolSize the poolSize to set
     */
    public void setCasMultiplierPoolSize(int poolSize);

    /**
     * @return the parent of this AEDeploymentMetaData
     */
    public AEDeploymentMetaData getParent ();
    /**
     * @param parent the parent to set
     */
    public void setParent(AEDeploymentMetaData parent);
    
    /**
     * This method returns an unmodifiable Map whose keys are string identifiers 
     * and whose values are the DeploymentMetaData objects.
     * 
     * @return
     * @return Map
     */
    public Map getDelegateDeployments ();
    
    
    /**
     * @return the delegates
     */
    public AEDelegates_Impl getDelegates();

    /**
     * @param delegates the delegates to set
     */
    public void setDelegates(AEDelegates_Impl delegates);

    public boolean isAsync();
    public void setAsync(boolean async);
    
    /**
     * @return the topAnalysisEngine
     */
    public boolean isTopAnalysisEngine();

    /**
     * @param topAnalysisEngine the topAnalysisEngine to set
     */
    public void setTopAnalysisEngine(boolean topAnalysisEngine);
}