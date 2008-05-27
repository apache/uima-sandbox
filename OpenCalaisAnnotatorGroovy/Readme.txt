Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

This is a demo and test of processing the output from 
OpenCalais web service.  

It is written mostly in the Groovy language - an extension
of Java (google groovy for more information).

  - To run you'll need the Groovy runtime.  Easiest way
    to set this up is to install the Eclipse groovy
    plugin from 
    http://dist.codehaus.org/groovy/distributions/updateDev/

It doesn't actually use the service (yet); instead, please
go to http://opencalais.com/calaisAPI and scroll to the 
bottom, and click the link to download & unzip the 
"Preprocessed Content".  The first time the  
CalaisTestCollectionReader runs, it will generate a 
subdirectory "text" which contains the source text 
that go with these.

To run this, use the CPM runner, and specify the 
CalaisTestCollectionReader and the CalaisRdfProcessor.
The src/main/run-configuration has a version of the
runner with the right classpath (assuming you installed
the Eclipse groovy plugin).

To see the results, use the UIMA Annotation Viewer, 
specifying for the type system src/main/descriptors/CalaisTypes.xml

Groovy has a maven plugin to allow building this with 
maven, but it's not yet "ready" - it hits bugs reported
"fixed" with SNAPSHOT versions.  So, I'll wait a while
on that part.




** Or ** just ask the CPM runner to load the saved
Cpe Descriptor called CalaisCpeTester.  These are all
located in src/main/descriptors.

Set the class path for running to include  
 
