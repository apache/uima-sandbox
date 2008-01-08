/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Apr 4, 2007, 8:52:46 PM
 * source:  AEDeployment_Impl.java
 */
package org.apache.uima.aae.deployment.impl;

import java.util.List;
import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.deployment.AEDeploymentConstants;
import org.apache.uima.aae.deployment.AEDeploymentMetaData;
import org.apache.uima.aae.deployment.AsyncAEErrorConfiguration;
import org.apache.uima.aae.deployment.AsyncAggregateErrorConfiguration;
import org.apache.uima.aae.deployment.AsyncPrimitiveErrorConfiguration;
import org.apache.uima.aae.deployment.RemoteAEDeploymentMetaData;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.resource.ResourceCreationSpecifier;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.resource.metadata.OperationalProperties;
import org.apache.uima.resource.metadata.impl.MetaDataObject_impl;
import org.apache.uima.resource.metadata.impl.PropertyXmlInfo;
import org.apache.uima.resource.metadata.impl.XmlizationInfo;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLParser;
import org.apache.uima.util.XMLizable;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


/**
 * 
 * 
 */
public class AEDeploymentMetaData_Impl extends DeploymentMetaData_Impl implements
AEDeploymentConstants, AEDeploymentMetaData {
  protected boolean topAnalysisEngine = false;

  protected boolean async = DEFAULT_ASYNC;

  protected boolean issetAsync = false;

  protected int numberOfInstances = DEFAULT_SCALEOUT_INSTANCES;

  protected boolean casMultiplier = false;

  protected int casMultiplierPoolSize = 0; // required - DEFAULT_CAS_MULTIPLIER_POOL_SIZE;

  protected AEDelegates_Impl delegates;

  protected AsyncAEErrorConfiguration asyncAEErrorConfiguration;

  protected ResourceSpecifier resourceSpecifier;

  protected Import importedAE;    // import by location|name of AE descriptor

  /** ********************************************************************** */

  /**
   * 
   */
  public AEDeploymentMetaData_Impl() {
  }

  public boolean isSet(int i) {
    return i != UNDEFINED_INT;
  }

  public ResourceSpecifier getResourceSpecifier() {
    return resourceSpecifier;
  }

  public void setResourceSpecifier(ResourceSpecifier rs, ResourceManager aResourceManager,
          boolean recursive) throws InvalidXMLException {
    resourceSpecifier = rs;

    setCasMultiplier(AEDeploymentDescription_Impl.isCASMultiplier(rs));
//  if (AEDeploymentDescription_Impl.isCASMultiplier(rs)) {
//  setCasMultiplierPoolSize(DEFAULT_CAS_MULTIPLIER_POOL_SIZE);
//  }

    if ( (rs instanceof AnalysisEngineDescription) && recursive ) {
      resolveDelegatesInternal((AnalysisEngineDescription) rs, aResourceManager, recursive);
    }
  }

  public void resolveDelegates(ResourceManager aResourceManager, boolean recursive)
  throws InvalidXMLException {
    if (getResourceSpecifier() == null) {
      Trace.err(getKey() + ": NO AnalysisEngineDescription");
      return;
    }
    ResourceSpecifier rs1 = getResourceSpecifier();
    if (!(rs1 instanceof AnalysisEngineDescription)) {
      // Need to be Aggregate AE
      return;
    }

    // Resolve Imports
    AnalysisEngineDescription aed = (AnalysisEngineDescription) rs1;
    aed.resolveImports(aResourceManager);
    Map mapDelegateAEs = aed.getDelegateAnalysisEngineSpecifiers(aResourceManager);

    // Verify that delegates exist in AnalysisEngineDescription aed
    if (delegates != null) {
      for (String key: delegates.delegateKeys) {
        if ( ! mapDelegateAEs.containsKey(key) ) {
          Trace.err(" >>> " + key + " is not in" + aed.getAnalysisEngineMetaData().getName());
          throw new DDEInvalidXMLException(DDEInvalidXMLException.DELEGATE_KEY_NOT_FOUND, new
                  Object[] {
                  key, aed.getSourceUrlString() });
        }
      }
    } else {
      delegates = new AEDelegates_Impl(this);
    }

    // Set the ResourceSpecifier of each AEDeploymentMetaData|RemoteAEDeploymentMetaData delegate
    for (Object m : mapDelegateAEs.entrySet()) {
      Map.Entry entry = (Map.Entry) m;
      // Trace.err("key: " + entry.getKey() + " ; " + entry.getValue().getClass().getName());

      XMLizable xmlizable = delegates.contains((String) entry.getKey());
      if (xmlizable != null) {
        // Trace.err("--- Delegate ALREADY defined for: " + (String) entry.getKey()
        //        + " (" + entry.getValue().getClass().getName() + ")");
        if (xmlizable instanceof AEDeploymentMetaData) {
          if (entry.getValue() instanceof Import) {
            ((AEDeploymentMetaData) xmlizable).setImportedAE((Import) entry.getValue());
            Trace.bug("Import should be resolved for: " + ((Import) entry.getValue()).getName());
          } else {
            ((AEDeploymentMetaData) xmlizable).setResourceSpecifier((ResourceSpecifier) entry.getValue(), aResourceManager, recursive);
          }
        } else if (xmlizable instanceof RemoteAEDeploymentMetaData) {
          if (entry.getValue() instanceof Import) {
            ((RemoteAEDeploymentMetaData) xmlizable).setImportedAE((Import) entry.getValue());
            Trace.bug("Import should be resolved for: " + ((Import) entry.getValue()).getName());
          } else {
            ((RemoteAEDeploymentMetaData) xmlizable).setResourceSpecifier((ResourceSpecifier) entry.getValue(), aResourceManager, recursive);
          }
        }
        continue;
      }

      // New delegate
      AEDeploymentMetaData_Impl meta = new AEDeploymentMetaData_Impl();
      meta.setKey((String) entry.getKey());
      if (entry.getValue() instanceof ResourceSpecifier) {
        meta.setResourceSpecifier((ResourceSpecifier) entry.getValue(), aResourceManager, recursive);
      }

      // Create a new Error Config
      Object obj = UIMAFramework.getResourceSpecifierFactory().createObject(AsyncAggregateErrorConfiguration.class);
      if (obj != null) {
        // Trace.err("OK to create " + cls.getName() + " for " + meta.getKey());
        ((AsyncAEErrorConfiguration) obj).getGetMetadataErrors().setTimeout(0);
        meta.setAsyncAEErrorConfiguration((AsyncAEErrorConfiguration) obj);
      } else {
        Trace.err("CANNOT create " + AsyncAggregateErrorConfiguration.class.getName() + " for " + meta.getKey());
      }

      delegates.addDelegate(meta);
    } // for    
  }

  protected void resolveDelegatesInternal(AnalysisEngineDescription aeDescription,
          ResourceManager aResourceManager, boolean recursive) throws InvalidXMLException {
    if (aeDescription.isPrimitive()) {
      return;
    }
    if (getDelegates() == null) {
      // Trace.err(getKey() + ": NO AEDeploymentMetaData delegates");
      return;
    }

    Map mapDelegateAEs = aeDescription.getDelegateAnalysisEngineSpecifiers(aResourceManager);
    List<XMLizable> list = getDelegates().getDelegates();
    for (XMLizable xmlizable : list) {
      if (xmlizable instanceof AEDeploymentMetaData) {
        AEDeploymentMetaData meta = (AEDeploymentMetaData) xmlizable;
        if (mapDelegateAEs.containsKey(meta.getKey())) {
          // Recursive
          // Trace.err(4, "Match2: " + meta.getKey());
          ResourceSpecifier rs = (ResourceSpecifier) mapDelegateAEs.get(meta.getKey());
          meta.setResourceSpecifier(rs, aResourceManager, recursive);
        } else {
          // Trace.err("NOT match: " + meta.getKey());
          throw new DDEInvalidXMLException(DDEInvalidXMLException.DELEGATE_KEY_NOT_FOUND, new
                  Object[] {meta.getKey(), aeDescription.getSourceUrlString() });
        }

      } else if (xmlizable instanceof RemoteAEDeploymentMetaData) {
        RemoteAEDeploymentMetaData meta = (RemoteAEDeploymentMetaData) xmlizable;
        if (mapDelegateAEs.containsKey(meta.getKey())) {
          // Trace.err(4, "match: " + meta.getKey());
          ResourceSpecifier rs = (ResourceSpecifier) mapDelegateAEs.get(meta.getKey());
          meta.setResourceSpecifier(rs, aResourceManager, recursive);

        } else {
          Trace.err("NOT Match: " + meta.getKey());
          throw new DDEInvalidXMLException(DDEInvalidXMLException.DELEGATE_KEY_NOT_FOUND, new
                  Object[] {meta.getKey(), aeDescription.getSourceUrlString() });
        }
      }

    } // for
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.uima.aae.deployment.impl.AEDeploymentMetaData#buildFromXMLElement(org.w3c.dom.Element,
   *      org.apache.uima.util.XMLParser, org.apache.uima.util.XMLParser.ParsingOptions)
   */

  public void buildFromXMLElement(Element aElement, XMLParser aParser,
          XMLParser.ParsingOptions aOptions) throws InvalidXMLException {
    // Find out about the parent node (used to decide the type of error config
    boolean isTopAnalysisEngine = false;
    if (aElement.getParentNode() instanceof Element) {
      if ( ((Element) aElement.getParentNode()).getTagName().equalsIgnoreCase(TAG_SERVICE) ) {
        isTopAnalysisEngine = true;
        // Trace.err(" >>> Top AE ");
      } else {
        // Trace.err(" >>> Delegate AE ");
      }
    } else {
      Trace.bug("Not an Element class: " + aElement.getParentNode().getClass().getName());
    }

    // Check for "key" and "async" attributes
    NamedNodeMap map = aElement.getAttributes();
    if (map != null) {
      for (int i=0; i<map.getLength(); ++i) {
        Node item = map.item(i);
        String name = item.getNodeName();
        String val = item.getNodeValue();
        if (val == null) {
          val = "";
        } else {
          val = val.trim();
        }
        if (AEDeploymentConstants.TAG_ATTR_KEY.equalsIgnoreCase(name)) {
          // Set "key = ..." attribute
          setKey(val);
        } else if (AEDeploymentConstants.TAG_ATTR_ASYNC.equalsIgnoreCase(name)) {
          // Set "async =[yes|no]" attribute
          setAsync(Boolean.parseBoolean(val));
        } else {
          throw new InvalidXMLException(InvalidXMLException.UNKNOWN_ELEMENT,
                  new Object[]{name});
        }
      }
    }

    // Has "key = ..." attribute ?
    if (!isTopAnalysisEngine && getKey() == null) {
      throw new InvalidXMLException(InvalidXMLException.ELEMENT_NOT_FOUND,
              new Object[]{AEDeploymentConstants.TAG_ATTR_KEY, AEDeploymentConstants.TAG_ANALYSIS_ENGINE});
    }

    NodeList childNodes = aElement.getChildNodes();
    String val;
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node curNode = childNodes.item(i);
      if (!(curNode instanceof Element)) {
        continue;
      }
      Element elem = (Element) curNode;

      if (AEDeploymentConstants.TAG_SCALE_OUT.equalsIgnoreCase(elem.getTagName())) {
        int n;
        val = DDParserUtil.checkAndGetAttributeValue(TAG_SCALE_OUT, TAG_ATTR_NUMBER_OF_INSTANCES, elem);
        if (val == null || val.trim().length() == 0) {
          n = 0;
        } else {
          try {
            n = Integer.parseInt(val);
          } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new InvalidXMLException(InvalidXMLException.UNKNOWN_ELEMENT,
                    new Object[] { TAG_ATTR_NUMBER_OF_INSTANCES }, e);
          }
        }
        setNumberOfInstances(n);

      } else if (TAG_CAS_MULTIPLIER.equalsIgnoreCase(elem.getTagName())) {
        // Get "poolSize" attribute
        int n;
        val = DDParserUtil.checkAndGetAttributeValue(TAG_CAS_MULTIPLIER, TAG_ATTR_POOL_SIZE, elem);
        if (val == null || val.trim().length() == 0) {
          n = 0;
        } else {
          try {
            n = Integer.parseInt(val);
          } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new InvalidXMLException(InvalidXMLException.UNKNOWN_ELEMENT,
                    new Object[] { TAG_ATTR_POOL_SIZE }, e);
          }
        }
        setCasMultiplierPoolSize(n);

      } else if (AEDeploymentConstants.TAG_DELEGATES.equalsIgnoreCase(elem.getTagName())) {
        delegates = new AEDelegates_Impl(this);
        NodeList nodes = elem.getChildNodes();
        if (nodes.getLength() > 0) {
          // Look for "analysisEngine" or "remoteDelegate"
          for (int k = 0; k < nodes.getLength(); ++k) {
            Node n = nodes.item(k);
            if (!(n instanceof Element)) {
              continue;
            }

            Element e = (Element) n;
            if (AEDeploymentConstants.TAG_ANALYSIS_ENGINE.equalsIgnoreCase(e.getTagName())) {
              // Add "Analysis Engine" to the list
              delegates.addDelegate((DeploymentMetaData_Impl) aParser.buildObject(e, aOptions));

            } else if (AEDeploymentConstants.TAG_REMOTE_DELEGATE.equalsIgnoreCase(e.getTagName())) {
              delegates.addDelegate((DeploymentMetaData_Impl) aParser.buildObject(e, aOptions));

            }
          }
        }
      } else if (AEDeploymentConstants.TAG_ASYNC_PRIMITIVE_ERROR_CONFIGURATION.equalsIgnoreCase(elem
              .getTagName())) {
        asyncAEErrorConfiguration = (AsyncPrimitiveErrorConfiguration) aParser.buildObject(elem,
                aOptions);
        asyncAEErrorConfiguration.sParentObject(this);

      } else if (AEDeploymentConstants.TAG_ASYNC_AGGREGATE_ERROR_CONFIGURATION.equalsIgnoreCase(elem
              .getTagName())) {
        asyncAEErrorConfiguration = (AsyncAggregateErrorConfiguration) aParser.buildObject(elem,
                aOptions);
        asyncAEErrorConfiguration.sParentObject(this);

      } else {
        throw new InvalidXMLException(InvalidXMLException.UNKNOWN_ELEMENT,
                new Object[]{elem.getTagName()});
      }

    } // for

    // Check if async=false and "delegates" are defined
    if ( (!isAsync()) && delegates != null) {
      setAsync(true);
    }

    // Create Error Config if not specified
    if (asyncAEErrorConfiguration == null) {
      Class cls;
      if (isTopAnalysisEngine) {
        cls = AsyncPrimitiveErrorConfiguration.class;          
      } else {
        cls = AsyncAggregateErrorConfiguration.class;          
      }
      asyncAEErrorConfiguration = (AsyncAEErrorConfiguration) UIMAFramework
                        .getResourceSpecifierFactory().createObject(cls);
      setAsyncAEErrorConfiguration(asyncAEErrorConfiguration);
      asyncAEErrorConfiguration.sParentObject(this);
      if (cls == AsyncAggregateErrorConfiguration.class) {
        asyncAEErrorConfiguration.getGetMetadataErrors().setTimeout(0);
      }
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.uima.aae.deployment.impl.AEDeploymentMetaData#toXML(org.xml.sax.ContentHandler,
   *      boolean)
   */
  public void toXML(ContentHandler aContentHandler, boolean aWriteDefaultNamespaceAttribute)
  throws SAXException {
    // <TAG_ANALYSIS_ENGINE key= async= >
    AttributesImpl attrs = new AttributesImpl();
    if (!isTopAnalysisEngine() && getKey() != null && getKey().trim().length() > 0) {
      attrs.addAttribute("", AEDeploymentConstants.TAG_ATTR_KEY,
              AEDeploymentConstants.TAG_ATTR_KEY, null, getKey());
    }
    // if (issetAsync) {
    attrs.addAttribute("", AEDeploymentConstants.TAG_ATTR_ASYNC,
            AEDeploymentConstants.TAG_ATTR_ASYNC, null, Boolean.toString(isAsync()));
    // }
    aContentHandler.startElement("", AEDeploymentConstants.TAG_ANALYSIS_ENGINE,
            AEDeploymentConstants.TAG_ANALYSIS_ENGINE, attrs);
    attrs.clear();

    // <scaleout numberOfInstances="1"/> <!-- optional -->
    if (getNumberOfInstances() != UNDEFINED_INT) {
      attrs
      .addAttribute("", AEDeploymentConstants.TAG_ATTR_NUMBER_OF_INSTANCES,
              AEDeploymentConstants.TAG_ATTR_NUMBER_OF_INSTANCES, null, ""
              + getNumberOfInstances());
      aContentHandler.startElement("", AEDeploymentConstants.TAG_SCALE_OUT,
              AEDeploymentConstants.TAG_SCALE_OUT, attrs);
      aContentHandler.endElement("", "", AEDeploymentConstants.TAG_SCALE_OUT);
      attrs.clear();
    }

    // <casMultiplier poolSize="5"/> <!-- req | omit-->
    if (AEDeploymentDescription_Impl.isCASMultiplier(getResourceSpecifier())) {
      if (getCasMultiplierPoolSize() != UNDEFINED_INT) {
        attrs.addAttribute("", AEDeploymentConstants.TAG_ATTR_POOL_SIZE,
                AEDeploymentConstants.TAG_ATTR_POOL_SIZE, null, "" + getCasMultiplierPoolSize());
        aContentHandler.startElement("", AEDeploymentConstants.TAG_CAS_MULTIPLIER,
                AEDeploymentConstants.TAG_CAS_MULTIPLIER, attrs);
        aContentHandler.endElement("", "", AEDeploymentConstants.TAG_CAS_MULTIPLIER);
        attrs.clear();
      }
    }
    // -------------- <delegates> ---------------
    if (isAsync() && delegates != null) {
      aContentHandler.startElement("", AEDeploymentConstants.TAG_DELEGATES,
              AEDeploymentConstants.TAG_DELEGATES, attrs);
      for (XMLizable x : delegates.getDelegates()) {
        if (x instanceof AEDeploymentMetaData_Impl) {
          org.apache.uima.aae.deployment.AEDeploymentMetaData aeDepl = (org.apache.uima.aae.deployment.AEDeploymentMetaData) x;
          aeDepl.toXML(aContentHandler, aWriteDefaultNamespaceAttribute);

        } else if (x instanceof RemoteAEDeploymentMetaData_Impl) {
          RemoteAEDeploymentMetaData_Impl aeDepl = (RemoteAEDeploymentMetaData_Impl) x;
          aeDepl.toXML(aContentHandler, aWriteDefaultNamespaceAttribute);
        } else {
        }
      }
      aContentHandler.endElement("", "", AEDeploymentConstants.TAG_DELEGATES);
    }
    // -------------- </delegates> ---------------

    // <asyncAggregateErrorConfiguration .../> <!-- optional -->
    if (asyncAEErrorConfiguration != null) {
      asyncAEErrorConfiguration.toXML(aContentHandler, aWriteDefaultNamespaceAttribute);
    } else {
      Trace.err(getKey() + ": asyncAEErrorConfiguration == null");
    }

    // </TAG_ANALYSIS_ENGINE>
    aContentHandler.endElement("", "", AEDeploymentConstants.TAG_ANALYSIS_ENGINE);
  }

  protected XmlizationInfo getXmlizationInfo() {
    // return XMLIZATION_INFO;
    return new XmlizationInfo(null, null);
    // this object has custom XMLization routines
  }

  static final private XmlizationInfo XMLIZATION_INFO = new XmlizationInfo(
          AEDeploymentConstants.TAG_ANALYSIS_ENGINE, new PropertyXmlInfo[] {
                  new PropertyXmlInfo("cpeDescriptor", false),
                  new PropertyXmlInfo("defaultCasProcessorSettings", false) });

  /** ********************************************************************** */

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.uima.aae.deployment.impl.AEDeploymentMetaData#getNumberOfInstances()
   */
  public int getNumberOfInstances() {
    return numberOfInstances;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.uima.aae.deployment.impl.AEDeploymentMetaData#setNumberOfInstances(int)
   */
  public void setNumberOfInstances(int numberOfInstances) {
    this.numberOfInstances = numberOfInstances;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.uima.aae.deployment.impl.AEDeploymentMetaData#getCasMultiplierPoolSize()
   */
  public int getCasMultiplierPoolSize() {
    return casMultiplierPoolSize;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.uima.aae.deployment.impl.AEDeploymentMetaData#setCasMultiplierPoolSize(int)
   */
  public void setCasMultiplierPoolSize(int poolSize) {
    this.casMultiplierPoolSize = poolSize;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.uima.aae.deployment.impl.AEDeploymentMetaData#getDelegates()
   */
  public AEDelegates_Impl getDelegates() {
    return delegates;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.uima.aae.deployment.impl.AEDeploymentMetaData#setDelegates(java.util.List)
   */
  public void setDelegates(AEDelegates_Impl delegates) {
    this.delegates = delegates;
  }

  /**
   * @param async
   *          the async to set
   */
  public void setAsync(boolean async) {
    issetAsync = true;
    this.async = async;
  }

  /**
   * @return the async
   */
  public boolean isAsync() {
    return async;
  }

  /**
   * @return the asyncPrimitiveErrorConfiguration
   */
  public AsyncAEErrorConfiguration getAsyncAEErrorConfiguration() {
    return asyncAEErrorConfiguration;
  }

  /**
   * @param asyncAEErrorConfiguration
   *          the asyncPrimitiveErrorConfiguration to set
   */
  public void setAsyncAEErrorConfiguration(AsyncAEErrorConfiguration asyncAEErrorConfiguration) {
    this.asyncAEErrorConfiguration = asyncAEErrorConfiguration;
    asyncAEErrorConfiguration.sParentObject(this);
  }

  /**
   * @return the topAnalysisEngine
   */
  public boolean isTopAnalysisEngine() {
    return topAnalysisEngine;
  }

  /**
   * @param topAnalysisEngine
   *          the topAnalysisEngine to set
   */
  public void setTopAnalysisEngine(boolean topAnalysisEngine) {
    if (topAnalysisEngine) {
      setKey("Top Analysis Engine");
    }
    this.topAnalysisEngine = topAnalysisEngine;
  }

  public Map getDelegateDeployments() {
    // Resolve delegates

    return null;
  }

  /**
   * @return the importedAE
   */
  public Import getImportedAE() {
    return importedAE;
  }

  /**
   * @param importedAE the importedAE to set
   */
  public void setImportedAE(Import importedAE) {
    this.importedAE = importedAE;
  }

  public boolean isCasMultiplier() {
    return casMultiplier;
  }

  public void setCasMultiplier(boolean casMultiplier) {
    this.casMultiplier = casMultiplier;
  }

  /** ********************************************************************** */

}
