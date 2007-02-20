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

package org.apache.uima.caseditor.core.model.dotcorpus;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.uima.caseditor.CasEditorPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

/**
 * This class is responsible to read and write {@link DotCorpus} objects from or to a byte stream.
 */
public class DotCorpusSerializer {
  private static final String CHARSET = "UTF-8";

  private static final String COPORA_ELEMENT = "copora";

  private static final String CORPUS_ELEMENT = "corpus";

  private static final String CORPUS_FOLDER_ATTRIBUTE = "folder";

  private static final String STYLE_ELEMENT = "style";

  private static final String STYLE_TYPE_ATTRIBUTE = "type";

  private static final String STYLE_STYLE_ATTRIBUTE = "style";

  private static final String STYLE_COLOR_ATTRIBUTE = "color";

  private static final String TYPESYSTEM_ELEMENT = "typesystem";

  private static final String TYPESYTEM_FILE_ATTRIBUTE = "file";

  private static final String TAGGER_ELEMENT = "config";

  private static final String TAGGER_FOLDER_ATTRIBUTE = "folder";

  private static final String EDITOR_ELEMENT = "editor";

  private static final String EDITOR_LINE_LENGTH_ATTRIBUTE = "line-length-hint";

  /**
   * Creats a {@link DotCorpus} object from a given {@link InputStream}.
   * 
   * @param dotCorpusStream
   * @return the {@link DotCorpus} instance.
   * @throws CoreException
   */
  public static DotCorpus parseDotCorpus(InputStream dotCorpusStream) throws CoreException {
    DocumentBuilderFactory documentBuilderFacoty = DocumentBuilderFactory.newInstance();

    DocumentBuilder documentBuilder;

    try {
      documentBuilder = documentBuilderFacoty.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      String message = ("This should never happen:" + e.getMessage() != null ? e.getMessage() : "");

      IStatus s = new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK, message, e);

      throw new CoreException(s);
    }

    org.w3c.dom.Document dotCorpusDOM;

    try {
      dotCorpusDOM = documentBuilder.parse(dotCorpusStream);
    } catch (SAXException e) {
      String message = (e.getMessage() != null ? e.getMessage() : "");

      IStatus s = new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK, message, e);

      throw new CoreException(s);
    } catch (IOException e) {
      String message = (e.getMessage() != null ? e.getMessage() : "");

      IStatus s = new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK, message, e);

      throw new CoreException(s);
    }

    DotCorpus dotCorpus = new DotCorpus();

    // get corpora root element
    Element corporaElement = dotCorpusDOM.getDocumentElement();

    if (COPORA_ELEMENT.equals(corporaElement.getNodeName())) {
      // TODO:
      // throw exception
    }

    NodeList corporaChildNodes = corporaElement.getChildNodes();

    for (int i = 0; i < corporaChildNodes.getLength(); i++) {
      Node corporaChildNode = corporaChildNodes.item(i);

      if (!(corporaChildNode instanceof Element)) {
        continue;
      }

      Element corporaChildElement = (Element) corporaChildNode;

      if (TYPESYSTEM_ELEMENT.equals(corporaChildElement.getNodeName())) {
        dotCorpus.setTypeSystemFilename(corporaChildElement.getAttribute(TYPESYTEM_FILE_ATTRIBUTE));
      } else if (CORPUS_ELEMENT.equals(corporaChildElement.getNodeName())) {
        String corpusFolderName = corporaChildElement.getAttribute(CORPUS_FOLDER_ATTRIBUTE);

        dotCorpus.addCorpusFolder(corpusFolderName);
      } else if (STYLE_ELEMENT.equals(corporaChildElement.getNodeName())) {
        String type = corporaChildElement.getAttribute(STYLE_TYPE_ATTRIBUTE);

        String styleString = corporaChildElement.getAttribute(STYLE_STYLE_ATTRIBUTE);

        String colorString = corporaChildElement.getAttribute(STYLE_COLOR_ATTRIBUTE);

        int colorInteger = Integer.parseInt(colorString);

        AnnotationStyle style = new AnnotationStyle(type, AnnotationStyle.Style
                .valueOf(styleString), new Color(colorInteger));

        dotCorpus.setStyle(style);
      } else if (TAGGER_ELEMENT.equals(corporaChildElement.getNodeName())) {
        dotCorpus
                .setUimaConfigFolderName(corporaChildElement.getAttribute(TAGGER_FOLDER_ATTRIBUTE));
      } else if (EDITOR_ELEMENT.equals(corporaChildElement.getNodeName())) {
        String lineLengthHintString = corporaChildElement
                .getAttribute(EDITOR_LINE_LENGTH_ATTRIBUTE);

        int lineLengthHint = Integer.parseInt(lineLengthHintString);

        dotCorpus.setEditorLineLength(lineLengthHint);
      } else {
        String message = ("Unexpected element: " + corporaChildElement.getNodeName());

        IStatus s = new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK, message, null);

        throw new CoreException(s);
      }
    }

    return dotCorpus;
  }

  /**
   * Writes the <code>DotCorpus</code> instance to the given <code>OutputStream</code>.
   * 
   * @param dotCorpus
   *          the {@link DotCorpus} object to serialize.
   * @param out -
   *          the stream to write the current <code>DotCorpus</code> instance.
   * @throws CoreException
   */
  public static void serialize(DotCorpus dotCorpus, OutputStream out) throws CoreException {
    OutputStreamWriter writer;

    try {
      writer = new OutputStreamWriter(out, CHARSET);
    } catch (UnsupportedEncodingException e) {
      String message = (e.getMessage() != null ? e.getMessage() : "");

      IStatus s = new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK, message, e);

      throw new CoreException(s);
    }

    XMLSerializer xmlSerialzer = new XMLSerializer(writer, new OutputFormat("XML", CHARSET, true));
    try {
      xmlSerialzer.startDocument();
      xmlSerialzer.startElement(COPORA_ELEMENT, null);

      for (String corpusFolder : dotCorpus.getCorpusFolderNameList()) {
        AttributesImpl corpusFolderAttributes = new AttributesImpl();
        corpusFolderAttributes.addAttribute("", CORPUS_FOLDER_ATTRIBUTE, "", "", corpusFolder);

        xmlSerialzer.startElement("", CORPUS_ELEMENT, "", corpusFolderAttributes);
        xmlSerialzer.endElement(CORPUS_ELEMENT);
      }

      for (AnnotationStyle style : dotCorpus.getAnnotationStyles()) {
        AttributesImpl corpusFolderAttributes = new AttributesImpl();
        corpusFolderAttributes
                .addAttribute("", STYLE_TYPE_ATTRIBUTE, "", "", style.getAnnotation());
        corpusFolderAttributes.addAttribute("", STYLE_STYLE_ATTRIBUTE, "", "", style.getStyle()
                .name());

        Integer color = style.getColor().getRGB();
        corpusFolderAttributes.addAttribute("", STYLE_COLOR_ATTRIBUTE, "", "", color.toString());

        xmlSerialzer.startElement("", STYLE_ELEMENT, "", corpusFolderAttributes);
        xmlSerialzer.endElement(STYLE_ELEMENT);

      }

      if (dotCorpus.getTypeSystemFileName() != null) {
        AttributesImpl typeSystemFileAttributes = new AttributesImpl();
        typeSystemFileAttributes.addAttribute("", TYPESYTEM_FILE_ATTRIBUTE, "", "", dotCorpus
                .getTypeSystemFileName());

        xmlSerialzer.startElement("", TYPESYSTEM_ELEMENT, "", typeSystemFileAttributes);
        xmlSerialzer.endElement(TYPESYSTEM_ELEMENT);
      }

      if (dotCorpus.getUimaConfigFolder() != null) {
        AttributesImpl taggerConfigAttributes = new AttributesImpl();
        taggerConfigAttributes.addAttribute("", TAGGER_FOLDER_ATTRIBUTE, "", "", dotCorpus
                .getUimaConfigFolder());

        xmlSerialzer.startElement("", TAGGER_ELEMENT, "", taggerConfigAttributes);
        xmlSerialzer.endElement(TAGGER_ELEMENT);
      }

      if (dotCorpus.getEditorLineLengthHint() != DotCorpus.EDITOR_LINE_LENGTH_HINT_DEFAULT) {
        AttributesImpl editorLineLengthHintAttributes = new AttributesImpl();
        editorLineLengthHintAttributes.addAttribute("", EDITOR_LINE_LENGTH_ATTRIBUTE, "", "",
                Integer.toString(dotCorpus.getEditorLineLengthHint()));

        xmlSerialzer.startElement("", EDITOR_ELEMENT, "", editorLineLengthHintAttributes);
        xmlSerialzer.endElement(EDITOR_ELEMENT);
      }

      xmlSerialzer.endElement(COPORA_ELEMENT);
      xmlSerialzer.endDocument();
    } catch (SAXException e) {
      String message = (e.getMessage() != null ? e.getMessage() : "");

      IStatus s = new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK, message, e);

      throw new CoreException(s);
    }
  }
}