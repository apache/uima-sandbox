package org.apache.uima.aae.deployment.impl;

import org.apache.uima.util.InvalidXMLException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DDParserUtil {


  /**
   * Check and get the attribute value 
   * 
   * @param parentElementName
   * @param attributeName
   * @param aElement
   * @throws InvalidXMLException
   * @return String
   */
  static public String checkAndGetAttributeValue (String parentElementName, String attributeName, 
          Element aElement) throws InvalidXMLException {
    // Check for attributeName attribute
    boolean found = false;
    String val = null;
    NamedNodeMap map = aElement.getAttributes();
    if (map != null) {
      for (int i=0; i<map.getLength(); ++i) {
        Node item = map.item(i);
        String name = item.getNodeName();
        val = item.getNodeValue();
        if (val == null) {
          val = "";
        } else {
          val = val.trim();
        }
        if (attributeName.equalsIgnoreCase(name)) {
          // Found
          found = true;
          break;

        } else {
          throw new InvalidXMLException(InvalidXMLException.UNKNOWN_ELEMENT,
                  new Object[]{name, parentElementName});
        }
      }
    }

    // Check for missing attributes
    if (!found) {
      throw new InvalidXMLException(InvalidXMLException.ELEMENT_NOT_FOUND,
              new Object[]{attributeName, parentElementName});
    }
    return val;
  }

}
