@REM   Licensed to the Apache Software Foundation (ASF) under one
@REM   or more contributor license agreements.  See the NOTICE file
@REM   distributed with this work for additional information
@REM   regarding copyright ownership.  The ASF licenses this file
@REM   to you under the Apache License, Version 2.0 (the
@REM   "License"); you may not use this file except in compliance
@REM   with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM   Unless required by applicable law or agreed to in writing,
@REM   software distributed under the License is distributed on an
@REM   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM   KIND, either express or implied.  See the License for the
@REM   specific language governing permissions and limitations
@REM   under the License.

@setlocal

@REM Remove quotes from UIMA_HOME
@rem All this nonsense is necessary to remove quotes from the variable and also handle the case where it is not set
@set _NOQUOTES=%UIMA_HOME:"=%
@set UIMA_HOME=%_NOQUOTES:"=%
@if "%UIMA_HOME%"=="=" set UIMA_HOME=

@call "%UIMA_HOME%\bin\setUimaClassPath"


@if "%JAVA_HOME%"=="" (set UIMA_JAVA_CALL=java) else (set UIMA_JAVA_CALL=%JAVA_HOME%\bin\java)
@"%UIMA_JAVA_CALL%" -cp "%UIMA_CLASSPATH%;%UIMA_HOME%\lib\saxon8.jar" "-Duima.datapath=%UIMA_DATAPATH%" -Xmx256M net.sf.saxon.Transform -l -s %1 -o %2 "%UIMA_HOME%\bin\dd2spring.xsl" %3 %4 %5 %6 %7 %8 %9
