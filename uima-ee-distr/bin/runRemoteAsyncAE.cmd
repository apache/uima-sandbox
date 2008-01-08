setlocal
call "%UIMA_HOME%\bin\setUimaClassPath"
if "%JAVA_HOME%"=="" (set UIMA_JAVA_CALL=java) else (set UIMA_JAVA_CALL=%JAVA_HOME%\bin\java)
"%UIMA_JAVA_CALL%" -cp "%UIMA_CLASSPATH%" "-Djava.util.logging.config.file=%UIMA_LOGGER_CONFIG_FILE%" %UIMA_JVM_OPTS% org.apache.uima.examples.ee.RunRemoteAsyncAE %*
