@echo off

REM   Licensed to the Apache Software Foundation (ASF) under one
REM   or more contributor license agreements.  See the NOTICE file
REM   distributed with this work for additional information
REM   regarding copyright ownership.  The ASF licenses this file
REM   to you under the Apache License, Version 2.0 (the
REM   "License"); you may not use this file except in compliance
REM   with the License.  You may obtain a copy of the License at
REM
REM    http://www.apache.org/licenses/LICENSE-2.0
REM
REM   Unless required by applicable law or agreed to in writing,
REM   software distributed under the License is distributed on an
REM   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
REM   KIND, either express or implied.  See the License for the
REM   specific language governing permissions and limitations
REM   under the License.

REM Run with -notest to skip the unit tests

@echo on

@if "%~1"=="" goto usage
@if not "%4"=="" goto usage
@if "%~1"=="trunk" goto trunk
@set leveldir=uima-as-%~1-%~2
@set svnloc=tags/uima-as-%~1/%leveldir%
@goto checkargs

@:trunk
@set leveldir=uima-as
@set svnloc=trunk/uima-as
@goto checkargs

@:checkargs
@set jvmarg=
@set mvnCommand=clean install

@if "%~3"=="" goto execute
@if "%~3"=="-notest" goto notest
@if "%~3"=="-deploy" goto deploy
@goto usage

@:usage
@echo off
echo running this command in a directory produces an extract as a subdirectory of that directory.
echo Usage: extractAndBuild.bat level release-candidate [-notest] [-deploy]
echo            (-notest and -deploy cannot be used together)
echo  examples of the 1st 2 arguments, level release-candidate, are  trunk trunk   or  2.2.2  01
echo  If trunk, repeat trunk, eg. extractAndBuild.bat trunk trunk 
@echo on
@goto exit

@:notest
@set jvmarg="-Dmaven.test.skip=true"
@goto execute

@:deploy
@set jvmarg="-DsignArtifacts=true"
@set mvnCommand=source:jar deploy
@goto execute

@:execute
svn checkout -r HEAD http://svn.apache.org/repos/asf/incubator/uima/sandbox/%svnloc%
cd %leveldir%
cd uimaj-as
call mvn %jvmarg%  %mvnCommand%
cd ..
cd uimaj-distr
call mvn assembly:assembly

@:exit
