@if .%1 == . goto usage
@if NOT exist %1 goto usage
@setlocal
@call "%UIMA_HOME%\bin\setUimaClassPath"

@rem set spring_file=%~n1_spring.xml
@rem ddmake does not work because it will not rebuild if the component descriptor changed
@rem but the deployment descriptor did not.  For now we always build the Spring XML.
@rem call ddmake %1
@rem call dd2spring %1 %spring_file%

@if "%JAVA_HOME%"=="" (set UIMA_JAVA_CALL=java) else (set UIMA_JAVA_CALL=%JAVA_HOME%\bin\java)
@"%UIMA_JAVA_CALL%" -cp "%UIMA_CLASSPATH%" "-Duima.datapath=%UIMA_DATAPATH%" "-Djava.util.logging.config.file=%UIMA_LOGGER_CONFIG_FILE%" %UIMA_JVM_OPTS%  org.apache.uima.adapter.jms.service.UIMA_Service -saxonURL "file:%UIMA_HOME%\lib\saxon8.jar" -xslt "%UIMA_HOME%\bin\dd2spring.xsl" -dd %*
@goto end
:usage
 @echo Deployment descriptor %1 not found
 @echo Usage: deployAsyncService file-path-of-deployment-descriptor [another-dd ...]
:end
