@setlocal

@REM Remove quotes from UIMA_HOME
@rem All this nonsense is necessary to remove quotes from the variable and also handle the case where it is not set
@set _NOQUOTES=%UIMA_HOME:"=%
@set UIMA_HOME=%_NOQUOTES:"=%
@if "%UIMA_HOME%"=="=" set UIMA_HOME=

@call "%UIMA_HOME%\bin\setUimaClassPath"


@if "%JAVA_HOME%"=="" (set UIMA_JAVA_CALL=java) else (set UIMA_JAVA_CALL=%JAVA_HOME%\bin\java)
@"%UIMA_JAVA_CALL%" -cp "%UIMA_CLASSPATH%;%UIMA_HOME%\lib\saxon8.jar" "-Duima.datapath=%UIMA_DATAPATH%" -Xmx256M net.sf.saxon.Transform -l -s %1 -o %2 "%UIMA_HOME%\bin\dd2spring.xsl" %3 %4 %5 %6 %7 %8 %9
