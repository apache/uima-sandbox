package org.apache.uima.simpleserver.output;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/*
 * This class contains static methods responsible for the creation
 * of particular view of the analysis result 
 */
public class ResultConverter {

  /*
   * Returns a string containing an XML document with analysis results, with tag
   * names and attribute names as specified by the current Result Specification
   * 
   * 
   * Straight-forward method, no other methods are used.
   */
  public static String getXMLString(Result result) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      DOMImplementation impl = builder.getDOMImplementation();

      Document document = impl.createDocument(null, null, null);
      Element root = document.createElement("result");
      document.appendChild(root);

      for (ResultEntry resultEntry : result.getResultEntries()) {
        Element element = document.createElement(resultEntry.getEntryName());
        root.appendChild(element);
        for (String attributeName : resultEntry.getAttributeNames()) {
          String attributeValue = resultEntry.getAttriuteValue(attributeName);
          element.setAttribute(attributeName, attributeValue);
        }
        // TODO covered text - DONE
        if (resultEntry.getCoveredText() != null) {
          Node textNode = document.createTextNode(resultEntry.getCoveredText());
          element.appendChild(textNode);
        }
      }

      DOMSource source = new DOMSource(document);
      TransformerFactory tFactory = TransformerFactory.newInstance();
      Transformer transformer = tFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      StringWriter stringWriter = new StringWriter();
      StreamResult streamResult = new StreamResult(stringWriter);
      transformer.transform(source, streamResult);

      return stringWriter.toString();
    } catch (Throwable t) {
      throw new RuntimeException("XML output failed", t);
    }
  }

  /*
   * Delegates the call to the InlineXMLGenerator class, where the inline XML is
   * constructed from the given Result object
   */
  public static String getInlineXML(Result result) {
    try {
      return InlineXMLGenerator.getInlineXML(result);
    } catch (Throwable t) {
      throw new RuntimeException("Tagged text output failed", t);
    }
  }

}
