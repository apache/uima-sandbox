@echo off
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

if not defined UIMA_HOME goto USAGE_UIMA
goto RUN

:USAGE_UIMA
echo UIMA_HOME environment variable is not set 
goto EXIT

:RUN
@call "%UIMA_HOME%\bin\setUimaClassPath"
@if "%JAVA_HOME%"=="" (set UIMA_JAVA_CALL=java) else (set UIMA_JAVA_CALL=%JAVA_HOME%\bin\java)

@if not exist expected (
  @echo need to run this from directory .../uima-as-distr/src/test/resources/deploy
  goto EXIT
)

@for %%f in (envVar\*) do (
  @echo ...processing %%f...
  @"%UIMA_JAVA_CALL%" -cp "%UIMA_CLASSPATH%;%cd%\..\..\..\main\saxon\saxon8.jar" -Xmx256M net.sf.saxon.Transform -l -s %%f -o expected\%%f "%cd%\..\..\..\main\scripts\dd2spring.xsl"
)

@for %%f in (*) do (
  @echo ...processing %%f...
  @"%UIMA_JAVA_CALL%" -cp "%UIMA_CLASSPATH%;%cd%\..\..\..\main\saxon\saxon8.jar" -Xmx256M net.sf.saxon.Transform -l -s %%f -o expected\%%f "%cd%\..\..\..\main\scripts\dd2spring.xsl"
)
:EXIT
