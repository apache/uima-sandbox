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

package org.apache.uima.simpleserver.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.uima.simpleserver.Service;
import org.apache.uima.simpleserver.config.Output;
import org.apache.uima.simpleserver.config.ServerSpec;
import org.apache.uima.simpleserver.config.TypeMap;
import org.apache.uima.simpleserver.output.Result;
import org.apache.uima.simpleserver.output.ResultConverter;

/*
 * a base class that implements the specification of a UIMA Servlet
 */
public abstract class UimaServletBase extends HttpServlet {

  public File baseWebappDirectory = null;

  public Service server = null;
  
  private boolean initializationSuccessful = false;
  
  private Logger logger = Logger.getAnonymousLogger();

  // define possible parameter names
  // map parameter name --> parameter description
  protected Map<String, String> servletGETParameters = new HashMap<String, String>();

  protected Map<String, String> servletPOSTParameters = new HashMap<String, String>();

  // for some parameters, we can define supported values
  // map parameter name --> map ( value --> description )
  protected Map<String, Map<String, String>> servletGETParamOptions = new HashMap<String, Map<String, String>>();

  protected Map<String, Map<String, String>> servletPOSTParamOptions = new HashMap<String, Map<String, String>>();

  protected Logger getLogger() {
    return this.logger;
  }
  
  // creates the mappings for standard parameter description
  // this method can be overridden for non-standard parameter sets
  protected void declareServletParameters() {
    this.servletGETParameters.put("mode", "This parameter should define, what"
        + " the servlet should return. Some options are available.");
    Map<String, String> options = new HashMap<String, String>();
    this.servletGETParamOptions.put("mode", options);
    options.put("description", "will return a description of a service "
        + "in  HTML (human-readable) format. This description is"
        + " partially automatically generated, and partially created "
        + "by the author of this service.");
    options.put("xsd", "will return a XSD schema definition of the text " + "analysis results");
    options.put("form", "will show a form with input fields, which will "
        + "allow you to try out this service");
    options.put("xmldesc", "will show a specification of this service in XML " + "format");

    this.servletPOSTParameters.put("text", "the value of this parameter is the "
        + "text to analyze. Expected encoding is UTF-8. This " + "parameter must always be set.");

    this.servletPOSTParameters.put("mode", "This parameter should define, what"
        + " view of the analyss result the servlet should return. "
        + "If this parameter is not set, XML output will be produced.");
    options = new HashMap<String, String>();
    options.put("xml", "means to output the result as a XML-document "
        + "containing a list of found entities");
    options.put("inline", "returns inline-xml containing the analyzed "
        + "text in which all found entities are represented by tags");
    options.put("csv", "returns the found entities in a comma-separated list");
    this.servletPOSTParamOptions.put("mode", options);

    this.servletPOSTParameters.put("lang", "This parameter sets the language "
        + "of the text. If this parameter is not set, the value" + "&quot;en&quot; will be used");

  }

  /*
   * implements the GET behavior described in the documentation
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (this.server == null) {
      System.out.println("Server object is null, but a GET request is received");
    }
    response.setCharacterEncoding("UTF-8");
    request.setCharacterEncoding("UTF-8");
    PrintWriter writer = response.getWriter();
    String mode = request.getParameter("mode");
    try {
      if ("xsd".equals(mode)) {
        // just give out the XSD definition provided by the server
        writer.println(this.server.getXMLResultXSD());
        writer.close();
      } else if ("description".equals(mode)) {
        // output a service description in HTML format
        writer.print(this.constructHtmlDescription(request.getRequestURL().toString()));
        writer.close();
      } else if ("form".equals(mode)) {
        // create a tryout HTML form and give it out
        writer.print(this.getHtmlForm(request.getRequestURL().toString()));
        writer.close();
      } else if ("xmldesc".equals(mode)) {
        if (this.server == null) {
          throw new RuntimeException("Server object is null");
        }

        writer.print(this.server.getServiceDescription());
        writer.close();
      } else {
        // interpret this as a request for actual analysis
        analyze(request, response);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
   * implements the default POST behavior described in the documentation
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    analyze(request, response);
  }

  /*
   * handles requests that send some text to be analyzed
   */
  protected void analyze(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    System.out.println(this.getClass().getName() + ": POST request received: " + new Date());
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    String text = request.getParameter("text");
    String lang = request.getParameter("lang");
    System.out.println("Given text: " + text.substring(0, Math.min(50, text.length())));
    String mode = request.getParameter("mode");
    if ((lang == null) || lang.equals("")) {
      lang = "en";
    }
    System.out.println("mode: " + mode);
    System.out.println("lang: " + lang);
    // process the text
    Result result = this.server.process(text, lang);
    PrintWriter writer = response.getWriter();
    writer.write(transformResult(result, mode));
    writer.close();
  }

  // just create a Server instance and check where the project directory is
  // (baseWebappDirectory)
  @Override
  public void init() {
    System.out.println("Servlet " + this.getClass().getCanonicalName()
        + " -- initialisation begins");
    this.baseWebappDirectory = new File(getServletContext().getRealPath(""));
    // File xsdFile = new File(baseWebappDirectory.getAbsoluteFile()
    // + "/schema/ResultSpecification.xsd");
    // server = new Server(xsdFile);
    this.server = new Service();
    if (this.server == null) {
      System.out.println("server object is null!");
    }
    this.initializationSuccessful = initServer();
    declareServletParameters();
  }

  /*
   * this method must be overridden in the subclasses
   * 
   * it should provide a valid ResultSpecification and a valid AE ot PEAR file
   * for the server.
   */
  protected abstract boolean initServer();

  /*
   * choose the output format, depending on the value of the given "mode"
   * parameter
   */
  public String transformResult(Result result, String mode) {
    if ("xml".equals(mode)) {
      return ResultConverter.getXMLString(result);
    }
    if ("inline".equals(mode)) {
      return ResultConverter.getInlineXML(result);
    }
    return ResultConverter.getXMLString(result);
  }

  // can be overridden by the creator of the subclasses.
  // just some HTML that delivers additional info about the service
  public String getCustomDescription() {
    return "";
  }

  // creates a service description as a HTML page, using the provided
  // maps woth parameters, values and their descriptions,
  // as well as the current URL,
  // and the additional descritpion specified in the previous method.

  public String constructHtmlDescription(String servletURL) {
    ServerSpec rspec = this.server.getServiceSpec();
    String html = "<html>" + "<head>" + "<title>"
        + rspec.getShortDescription()
        + "</title>"
        + "</head>"
        + "<body>"
        + "<h2>"
        + rspec.getShortDescription()
        + "</h2>"
        + rspec.getLongDescription()
        + "<h3>Usage</h3>"
        + "In odrer to use this service, a POST- or GET-request should be sent to the server with the following URL:"
        + "<pre>"
        + servletURL
        + "</pre>"
        + "<br/>"
        + "The following request parameters are expected:"
        + constructParameterDescription()
        + "<h3>Result</h3>"
        + "If XML or inline-XML output is requested, it will contain the tags listed below. "
        + "The XSD-definition of the output in XML-format can be downloaded "
        + "<a href=\""
        + servletURL
        + "?mode=xsd\">here</a>."
        + constructResultDescription()
        + ""
        + (getCustomDescription().equals("") ? "" : "<h3>Additional description </h3> "
            + getCustomDescription())
        + ""
        + "<h3>Example of usage</h3>"
        + "<pre>"
        + "String text = \"Hello Mr. John Smith !\"; \n"
        + "String parameters = \"text=\" + URLEncoder.encode(text, \"UTF-8\") + \"&mode=inline\";\n"
        + "URL url = new URL(\""
        + servletURL
        + "\"); \n"
        + "URLConnection connection = url.openConnection(); \n"
        + "connection.setDoOutput(true); \n"
        + "OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream()); \n"
        + "writer.write(parameters);\n"
        + "writer.flush(); \n\n"
        + "BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), \"UTF-8\")); \n"
        + "String line; \n" + "while ((line = reader.readLine()) != null) { \n"
        + "    System.out.println(line);\n" + "} \n" + "</pre>" + "<body>" + "</html>";

    return html;
  }

  // routine used in the previous method
  public String constructParameterDescription() {
    String s = "";
    s += "<h3>POST-parameters</h3> POST request should be sent to use " + "the service";
    s += parameterDescription(this.servletPOSTParameters, this.servletPOSTParamOptions);
    s += "<h3>GET-parameters</h3> GET request should be sent to obtain "
        + "information about the service";
    s += parameterDescription(this.servletGETParameters, this.servletGETParamOptions);
    return s;
  }

  // routine used in the previous method
  public String parameterDescription(Map<String, String> params,
      Map<String, Map<String, String>> options) {
    String s = "";
    s += "<ul>";
    for (String param : params.keySet()) {
      String desc = params.get(param);
      s += "<li/> ";
      s += "<b>" + param + "</b> " + " -- " + desc;
      Map<String, String> opts = options.get(param);
      if (opts != null) {
        s += "<ul>";
        s += "Possible values: <br/>";
        for (String opt : opts.keySet()) {
          s += "<li/> ";

          s += "<b>" + opt + "</b> " + " -- " + opts.get(opt);
        }
        s += "</ul>";
      }
    }
    s += "</ul>";
    return s;
  }

  public String constructResultDescription() {
    String s = "";
    try {
      s += "<h4>XML elemets of result</h4>";
      s += "<ul>";
      for (TypeMap t : this.server.getServiceSpec().getTypeSpecs()) {
        s += "<li/>";
        s += "Element <b>" + t.getOutputTag() + "</b> -- " + t.getShortDescription();
        s += "<br/> " + t.getLongDescription();
        s += "<ul>";
        s += "Attributes: <br/>";

        for (Output o : t.getOutputs()) {
          s += "<li/>";
          s += "Attribute <b>" + o.getAttribute() + "</b> -- " + o.getShortDescription();
          s += "<br/> " + o.getLongDescription();
        }

        s += "</ul>";
        s += "<br/>";
      }

      s += "</ul>";

    } catch (Exception e) {
      e.printStackTrace();
    }
    return s;
  }

  // creates a HTML page with a form which allows to try out the service
  public String getHtmlForm(String serverUrl) {
    String str = "<html>" + "<head>" + "<title>" + serverUrl + " tryout form " + "</title>"
        + "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" >" + "</head>"
        + "<body>" + "<h2>Tryout form of " + serverUrl + "</h2>"
        + "<form method=\"post\"  action=\"" + serverUrl + "\">" + "Enter text: <br/>"
        + "<textarea name=\"text\" rows=\"25\" cols=\"80\"></textarea><br/>"
        + "Enter language(optional):<br/>" + "<input type=\"text\" name=\"lang\" /><br/>"
        + "Display result as<br/>"
        + "<input type=\"radio\" name=\"mode\" value=\"xml\"/> XML document <br/>"
        + "<input type=\"radio\" name=\"mode\" value=\"inline\"/> inline XML <br/>"
        + "<input type=\"radio\" name=\"mode\" value=\"csv\"/> CSV <br/>" + "" + "" + "" + "" + ""
        + "" + "" + "" + "<input type=\"submit\">" + "</form>" + "</body>" + "</html>";
    return str;
  }

}
