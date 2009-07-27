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

package org.apache.uima.aae;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Marker;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.impl.AllowPreexistingFS;
import org.apache.uima.cas.impl.OutOfTypeSystemData;
import org.apache.uima.cas.impl.Serialization;
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
  
  private ConcurrentHashMap xmlReaderMap = new ConcurrentHashMap();
  private Object mux = new Object();
	/**
	 * Serializes XCas into CAS
	 * 
	 * @param anXcas
	 * @param aCas
	 * @throws Exception
	 */
	public OutOfTypeSystemData deSerialiazeFromXCAS(String anXcas, CAS aCas) throws Exception
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
	public void serializeToXCAS(OutputStream stream, CAS aCAS, String encoding, TypeSystem typeSystem, OutOfTypeSystemData otsd) throws IOException, SAXException
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
	public void serializeToXMI(OutputStream stream, CAS aCAS, String encoding, TypeSystem typeSystem, OutOfTypeSystemData otsd) throws IOException, SAXException
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
	public String serializeCasToXmi(CAS aCAS, XmiSerializationSharedData serSharedData) throws Exception
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
	
	public String serializeCasToXmi(CAS aCAS, XmiSerializationSharedData serSharedData, Marker aMarker) throws Exception
	{
		Writer writer = new StringWriter();
		try
		{
			XMLSerializer xmlSer = new XMLSerializer(writer, false);
			XmiCasSerializer ser = new XmiCasSerializer(aCAS.getTypeSystem()); 
			ser.serialize(aCAS, xmlSer.getContentHandler(), null, serSharedData, aMarker);
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
	
	/** Utility method for deserializing a CAS from an XMI String */
	public void deserializeCasFromXmi(String anXmlStr, CAS aCAS, XmiSerializationSharedData aSharedData, 
			boolean aLenient, int aMergePoint) 
	throws FactoryConfigurationError, ParserConfigurationException, SAXException, IOException
	{
		
    XMLReader xmlReader = null;
    //  Create a new instance of a XMLReader if one doesnt exist in the
    //  global map. Each thread reuses its own instance.
    synchronized( mux ) {
      if ( !xmlReaderMap.containsKey(Thread.currentThread().getId())) {
        xmlReader = XMLReaderFactory.createXMLReader();
        xmlReaderMap.put(Thread.currentThread().getId(), xmlReader);
      } else {
        xmlReader = (XMLReader) xmlReaderMap.get(Thread.currentThread().getId());
      }
    }
    if ( xmlReader == null ) {
      throw new ParserConfigurationException("XMLReaderMap Doesnt Contain a Reader Object for Key:"+Thread.currentThread().getId());
    }

    Reader reader = new StringReader(anXmlStr);
    XmiCasDeserializer deser = new XmiCasDeserializer(aCAS.getTypeSystem());
    ContentHandler handler = deser.getXmiCasHandler(aCAS, aLenient, aSharedData, aMergePoint);
    xmlReader.setContentHandler(handler);
    xmlReader.parse(new InputSource(reader));
	}

	public void deserializeCasFromXmi(String anXmlStr, CAS aCAS, XmiSerializationSharedData aSharedData, 
			boolean aLenient, int aMergePoint, AllowPreexistingFS allow) 
	throws FactoryConfigurationError, ParserConfigurationException, SAXException, IOException
	{
    XMLReader xmlReader = null;
    synchronized( mux ) {
      if ( !xmlReaderMap.containsKey(Thread.currentThread().getId())) {
        xmlReader = XMLReaderFactory.createXMLReader();
        xmlReaderMap.put(Thread.currentThread().getId(), xmlReader);
      } else {
        xmlReader = (XMLReader) xmlReaderMap.get(Thread.currentThread().getId());
      }
    }
    if ( xmlReader == null ) {
      throw new ParserConfigurationException("XMLReaderMap Doesnt Contain a Reader Object for Key:"+Thread.currentThread().getId());
    }
    Reader reader = new StringReader(anXmlStr);
    XmiCasDeserializer deser = new XmiCasDeserializer(aCAS.getTypeSystem());
    ContentHandler handler = deser.getXmiCasHandler(aCAS, aLenient, aSharedData, aMergePoint, allow);
    xmlReader.setContentHandler(handler); 
    xmlReader.parse(new InputSource(reader));
	}
  /** Utility method for deserializing a CAS from a binary */
  public void deserializeCasFromBinary(byte[] binarySource, CAS aCAS) throws Exception
  {
    ByteArrayInputStream fis = null;
    try
    {
      fis =  new ByteArrayInputStream(binarySource);
      Serialization.deserializeCAS(aCAS, fis);
    }
    catch( Exception e)
    {
      throw e;
    }
    finally
    {
      if ( fis != null )
      {
        fis.close();
      }
    }
  }

  public byte[] serializeCasToBinary(CAS aCAS) throws Exception
  {
    ByteArrayOutputStream fos = null;
    try
    {
      fos = new ByteArrayOutputStream();
      Serialization.serializeCAS(aCAS, fos);
      return fos.toByteArray();
    }
    catch( Exception e)
    {
      throw e;
    }
    finally
    {
      if ( fos != null)
      {
        fos.close();
      }
    }
  }

  public byte[] serializeCasToBinary(CAS aCAS, Marker aMark) throws Exception
  {
    ByteArrayOutputStream fos = null;
    try
    {
      fos = new ByteArrayOutputStream();
      Serialization.serializeCAS(aCAS, fos, aMark);
      return fos.toByteArray();
    }
    catch( Exception e)
    {
      throw e;
    }
    finally
    {
      if ( fos != null)
      {
        fos.close();
      }
    }
  }
  
  public void reset() {
    xmlReaderMap.clear();
  }
}	

