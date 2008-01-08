#!/bin/ksh
if [ $# -lt 1 ]
  then echo "You must specify one or more deployment descriptors.  Usage: deployAsyncService.sh file-path-of-deployment-descriptor [another-dd ...]"
       exit;
fi;
if [ ! -f $1 ]
  then echo "File '"$1"' does not exist";
       exit;
fi;

. "$UIMA_HOME/bin/setUimaClassPath.sh"

if [ "$JAVA_HOME" = "" ]
then
  UIMA_JAVA_CALL=java
else
  UIMA_JAVA_CALL="$JAVA_HOME/bin/java"
fi
"$UIMA_JAVA_CALL" -cp "$UIMA_CLASSPATH" "-Duima.datapath=$UIMA_DATAPATH" -Djava.util.logging.config.file=$UIMA_LOGGER_CONFIG_FILE $UIMA_JVM_OPTS org.apache.uima.adapter.jms.service.UIMA_Service -saxonURL file:$UIMA_HOME/lib/saxon8.jar -xslt $UIMA_HOME/bin/dd2spring.xsl -dd $*
