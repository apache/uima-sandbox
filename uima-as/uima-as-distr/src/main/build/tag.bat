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

@echo on

REM  The form %~1 (as opposed to %1) removes surrounding quotes

@if "%~1"=="" goto usage
@if not "%3"=="" goto usage
@set leveldir=%~1-%~2
@set svnloc=tags/%~1/%leveldir%
@goto execute

@:usage
@echo off
echo Run this command in its directory to tag a uima-as release
echo This will copy the HEAD in the repository for several projects
echo Usage: tag level release-candidate
echo  example tag uima-as-2.2.2 01
@echo on
@goto exit


@:execute
@set baseURL=https://svn.apache.org/repos/asf/incubator/uima/uimaj/trunk
@set asURL=https://svn.apache.org/repos/asf/incubator/uima/sandbox/trunk
@set tagURL=https://svn.apache.org/repos/asf/incubator/uima/sandbox/%svnloc%
@set commitMsg=Create Tag for UIMA-AS release candidate %2

svn copy  %asURL%/uima-as             %tagURL%                    -m "%commitMsg%"
svn copy  %baseURL%/uimaj             %tagURL%/uimaj              -m "%commitMsg%"
svn copy  %baseURL%/uimaj-distr       %tagURL%/uimaj-distr        -m "%commitMsg%"
svn copy  %baseURL%/uimaj-examples    %tagURL%/uimaj-examples     -m "%commitMsg%"
svn copy  %baseURL%/uima-docbooks     %tagURL%/uima-docbooks      -m "%commitMsg%"
svn copy  %baseURL%/uima-docbook-tool %tagURL%/uima-docbook-tool  -m "%commitMsg%"

@:exit
