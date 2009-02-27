#!/bin/sh

#   Licensed to the Apache Software Foundation (ASF) under one
#   or more contributor license agreements.  See the NOTICE file
#   distributed with this work for additional information
#   regarding copyright ownership.  The ASF licenses this file
#   to you under the Apache License, Version 2.0 (the
#   "License"); you may not use this file except in compliance
#   with the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing,
#   software distributed under the License is distributed on an
#   #  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#   KIND, either express or implied.  See the License for the
#   specific language governing permissions and limitations
#   under the License.

UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/examples/resources:$UIMA_HOME/lib/uima-core.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/lib/uima-document-annotation.jar:$UIMA_HOME/lib/uima-cpe.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/lib/uima-tools.jar:$UIMA_HOME/lib/uima-examples.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/lib/uima-adapter-soap.jar:$UIMA_HOME/lib/uima-adapter-vinci.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$CATALINA_HOME/webapps/axis/WEB-INF/lib/axis.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$CATALINA_HOME/webapps/axis/WEB-INF/lib/commons-discovery.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$CATALINA_HOME/webapps/axis/WEB-INF/lib/commons-discovery-0.2.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$CATALINA_HOME/webapps/axis/WEB-INF/lib/commons-logging.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$CATALINA_HOME/webapps/axis/WEB-INF/lib/commons-logging-1.0.4.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$CATALINA_HOME/webapps/axis/WEB-INF/lib/jaxrpc.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$CATALINA_HOME/webapps/axis/WEB-INF/lib/saaj.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$CATALINA_HOME/webapps/axis/WEB-INF/lib/activation.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/lib/jVinci.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/apache-activemq-4.1.1.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/lib/optional/activemq-optional-4.1.1.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/lib/optional/commons-httpclient-2.0.1.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/lib/optional/xstream-1.2.2.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/lib/optional/xmlpull-1.1.3.4d_b4_min.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/lib/optional/jetty-6.0.1.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/lib/optional/jetty-util-6.0.1.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/lib/optional/servlet-api-2.5-6.0.1.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/lib/commons-logging-1.1.1.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/lib/optional/commons-pool-1.2.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/lib/optional/spring-2.0.6.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/lib/optional/commons-collections-3.1.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/lib/geronimo-jms_1.1_spec-1.0.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/apache-activemq-4.1.1/lib/geronimo-j2ee-management_1.0_spec-1.0.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/lib/uimaj-as-core.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/lib/uimaj-as-activemq.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/lib/uimaj-as-jms.jar
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/config:$CLASSPATH

if [ "$UIMACPP_HOME" = "" ]
then
  UIMACPP_HOME=$UIMA_HOME/uimacpp
fi
#set LD_LIBRARY_PATH to support running C++ annotators
export LD_LIBRARY_PATH=$UIMACPP_HOME/lib:$UIMACPP_HOME/examples/tutorial/src:$LD_LIBRARY_PATH
#also set DYLD_LIBRARY_PATH, used by Mac OSX
export DYLD_LIBRARY_PATH=$UIMACPP_HOME/lib:$UIMACPP_HOME/examples/tutorial/src:$DYLD_LIBRARY_PATH

#also set default values for VNS_HOST and VNS_PORT
if [ "$VNS_HOST" = "" ];
then
  VNS_HOST=localhost
fi
if [ "$VNS_PORT" = "" ];
then
  VNS_PORT=9000
fi
#also set default vlaue for UIMA_LOGGER_CONFIG_FILE
if [ "$UIMA_LOGGER_CONFIG_FILE" = "" ]
then
  UIMA_LOGGER_CONFIG_FILE=$UIMA_HOME/config/Logger.properties
fi
#set default JVM opts
if [ "$UIMA_JVM_OPTS" = "" ]
then
  UIMA_JVM_OPTS="-Xms128M -Xmx800M"
fi

