if [ "$JAVA_HOME" = "" ]
then
  UIMA_JAVA_CALL=java
else
  UIMA_JAVA_CALL="$JAVA_HOME/bin/java"
fi
. "$UIMA_HOME/bin/setUimaClassPath.sh"
"$UIMA_JAVA_CALL" -cp "$UIMA_CLASSPATH:$UIMA_HOME/lib/saxon8.jar" "-Duima.datapath=$UIMA_DATAPATH" -Xmx256M net.sf.saxon.Transform -l -s $1 -o $2 $UIMA_HOME/bin/dd2spring.xsl $3
