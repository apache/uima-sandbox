package org.apache.uima.aae;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.impl.OutOfTypeSystemData;
import org.apache.uima.cas.impl.XCASDeserializer;
import org.apache.uima.cas.impl.XCASSerializer;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.cas.impl.XmiSerializationSharedData;
import org.apache.uima.util.XMLSerializer;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class UimaSerializer
{
	/**
	 * Serializes XCas into CAS
	 * 
	 * @param anXcas
	 * @param aCas
	 * @throws Exception
	 */
	public static OutOfTypeSystemData deSerialiazeFromXCAS(String anXcas, CAS aCas) throws Exception
	{
		OutOfTypeSystemData otsd = new OutOfTypeSystemData();
		TypeSystem typesToLoad2 = aCas.getTypeSystem();
		ByteArrayInputStream bis = new ByteArrayInputStream(anXcas.getBytes());
		XCASDeserializer deser2 = new XCASDeserializer(typesToLoad2);
		ContentHandler deserHandler2 = deser2.getXCASHandler(aCas, otsd);

		SAXParserFactory fact2 = SAXParserFactory.newInstance();
		SAXParser parser2;
		parser2 = fact2.newSAXParser();
		XMLReader xmlReader2 = parser2.getXMLReader();
		xmlReader2.setContentHandler(deserHandler2);
		xmlReader2.parse(new InputSource(bis));
		return otsd;
	}

	/**
	 * Serializes CAS into a given OutputStream
	 * 
	 * @param stream
	 * @param aCAS
	 * @param encoding
	 * @param typeSystem
	 * @throws IOException
	 * @throws SAXException
	 */
	public static void serializeToXCAS(OutputStream stream, CAS aCAS, String encoding, TypeSystem typeSystem, OutOfTypeSystemData otsd) throws IOException, SAXException
	{

		if (typeSystem == null)
			typeSystem = aCAS.getTypeSystem();
		XMLSerializer xmlSer = new XMLSerializer(stream, false);
		if (encoding != null)
			xmlSer.setOutputProperty(OutputKeys.ENCODING, encoding);
		XCASSerializer ser = new XCASSerializer(typeSystem);
		ser.serialize(aCAS, xmlSer.getContentHandler(), false, otsd);
	}

	/**
	 * Serializes CAS into a given OutputStream
	 * 
	 * @param stream
	 * @param aCAS
	 * @param encoding
	 * @param typeSystem
	 * @throws IOException
	 * @throws SAXException
	 */
	public static void serializeToXMI(OutputStream stream, CAS aCAS, String encoding, TypeSystem typeSystem, OutOfTypeSystemData otsd) throws IOException, SAXException
	{

		if (typeSystem == null)
			typeSystem = aCAS.getTypeSystem();
//		XMLSerializer xmlSer = new XMLSerializer(writer, false);
		XMLSerializer xmlSer = new XMLSerializer(stream, false);
		if (encoding != null)
			xmlSer.setOutputProperty(OutputKeys.ENCODING, encoding);

		XmiCasSerializer ser = new XmiCasSerializer(typeSystem);
		
		ser.serialize(aCAS, xmlSer.getContentHandler());
		// ser.serialize(aCAS, xmlSer.getContentHandler(),false, otsd);
	}

	/**
	 * Utility method for serializing a CAS to an XMI String
	 */
	public static String serializeCasToXmi(CAS aCAS, XmiSerializationSharedData serSharedData) throws Exception
	{
		Writer writer = new StringWriter();
		try
		{
			XMLSerializer xmlSer = new XMLSerializer(writer, false);
			XmiCasSerializer ser = new XmiCasSerializer(aCAS.getTypeSystem()); 
			ser.serialize(aCAS, xmlSer.getContentHandler(), null, serSharedData);
			return writer.toString();
		}
		catch( Exception e)
		{
			throw e;
		}
		finally
		{
			writer.close();
		}
	}
/*
	public static  String serializeCasToXmi(CAS cas, XmiSerializationSharedData serSharedData) throws IOException, SAXException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XmiCasSerializer.serialize(cas, null, baos, false, serSharedData);
		
		baos.close();
		String xmiStr = new String(baos.toByteArray(), "UTF-8"); 
		return xmiStr;
	}
*/
	
	/** Utility method for deserializing a CAS from an XMI String */
	public static void deserializeCasFromXmi(String anXmlStr, CAS aCAS, XmiSerializationSharedData aSharedData, boolean aLenient, int aMergePoint) 
	throws FactoryConfigurationError, ParserConfigurationException, SAXException, IOException
	{
		
		Reader reader = new StringReader(anXmlStr);
		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
	    XmiCasDeserializer deser = new XmiCasDeserializer(aCAS.getTypeSystem());
	    ContentHandler handler = deser.getXmiCasHandler(aCAS, aLenient, aSharedData, aMergePoint);
	    xmlReader.setContentHandler(handler);
	    xmlReader.parse(new InputSource(reader));

	    
	    
	    
	    
	    /*		
		byte[] bytes = xmlStr.getBytes("UTF-8"); // this assumes the encoding
													// is UTF-8, which is the
													// default output encoding
													// of the XmiCasSerializer
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		try
		{
			XmiCasDeserializer.deserialize(bais, cas, lenient, sharedData, mergePoint);
		}
		finally
		{
			bais.close();
		}
*/
		
	}


private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
