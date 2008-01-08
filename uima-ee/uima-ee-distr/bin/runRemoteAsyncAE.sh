. "$UIMA_HOME/bin/setUimaClassPath.sh"
if [ "$JAVA_HOME" = "" ]
then
  UIMA_JAVA_CALL=java
else
  UIMA_JAVA_CALL="$JAVA_HOME/bin/java"
fi
"$UIMA_JAVA_CALL" -cp "$UIMA_CLASSPATH" -Djava.util.logging.config.file=$UIMA_LOGGER_CONFIG_FILE $UIMA_JVM_OPTS org.apache.uima.examples.ee.RunRemoteAsyncAE $*
