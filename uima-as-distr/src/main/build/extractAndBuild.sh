#!/bin/sh

#   Licensed to the Apache Software Foundation (ASF) under one
#   or more contributor license agreements.  See the NOTICE file
#   distributed with this work for additional information
#   regarding copyright ownership.  The ASF licenses this file
#   to you under the Apache License, Version 2.0 (the
#   "License"); you may not use this file except in compliance
#   with the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing,
#   software distributed under the License is distributed on an
#   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#   KIND, either express or implied.  See the License for the
#   specific language governing permissions and limitations
#   under the License.

# Bourne shell syntax, this should hopefully run on pretty much anything.

usage() {
  echo "Usage: extractAndBuild.sh <level> <release candidate> [-notest] [-deploy]"
  echo "           (-notest and -deploy cannot be used together)"
  echo " examples of <level> <release candidate> :  uima-as-2.2.2 01"
  echo " for trunk, use trunk trunk  (yes, repeat trunk twice)"
}

vmargs=""
mvnCommand=install

# Check arguments
if [ $# = 0 ]
then
  usage
  exit 1
fi

if [ "$1" = "trunk" ]
then
  leveldir=uima-as
  svnloc=trunk/uima-as
else
  leveldir=$1-$2
  svnloc=tags/$1/$1-$2
fi

if [ $# -gt "3" ]
then
  usage
  exit 1
fi

if [ -n "$3" ]
then
# Check for -notest switch.  If present, add the no-test define to the mvn command line.
  if [ "$3" = "-notest" ]
  then
    vmargs="-Dmaven.test.skip=true"
# Check for -deploy switch.  If present, change maven command to deploy artifacts to remote Maven repo
  elif [ "$3" = "-deploy" ]
  then
    vmargs="-DsignArtifacts=true"
    mvnCommand="source:jar deploy"
  else
    usage
    exit 1
  fi
fi

svn checkout http://svn.apache.org/repos/asf/incubator/uima/sandbox/$svnloc
cd $leveldir/uimaj-as
mvn ${vmargs} $mvnCommand
cd ../uima-as-distr
mvn assembly:assembly

