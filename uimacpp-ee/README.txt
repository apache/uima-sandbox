Instructions for building and testing the UIMA C++ service wrapper, deployCppService

"deployCppService" is a C++ application used to instantiate UIMA C++ based analytics, 
and run them as UIMA services using the ActiveMQ implementation of JMS middleware 
(http://en.wikipedia.org/wiki/Java_Message_Service). UIMA Perl, Python and Tcl analytics 
can also be run as services with this wrapper.

There are three prerequisites: the UIMA-EE Java SDK, the UIMA-EE C++ SDK and the 
ActiveMQ-CPP library. Please download and unpack the UIMA-EE Java SDK, and then the 
UIMA-EE C++ SDK appropriate to your platform. Note that the UIMA C++ SDK should be 
installed directly under the Java SDK, in UIMA_HOME.

Install the UIMA-EE Java SDK as per the README_FIRST instructions in that package.


Building on Linux
-----------------

1) Download http://www.apache.org/dyn/closer.cgi/activemq/activemq-cpp/source/activemq-cpp-2.1-src.tar.gz
   and unpack it. Export ACTIVEMQ_HOME = yourinstallpath/activemq-cpp-2.1
2) Follow the directions in ACTIVEMQ_HOME/README.txt for "Building on Unix/Linux/OS X/Cygwin"
3) Setup the UIMACPP environment:
      export UIMACPP_HOME=$UIMA_HOME/uimacpp
      PATH=$PATH:$UIMACPP_HOME/bin
      export LD_LIBRARY_PATH=$UIMACPP_HOME/lib
4) cd to $UIMACPP_HOME/examples/service and build the service:
      make -f deployCppService.mak
5) Copy the service binary and strip it:
      cp deployCppService $UIMACPP_HOME/bin
      strip $UIMACPP_HOME/deployCppService


Building on Windows
-------------------

1) Download http://www.apache.org/dyn/closer.cgi/activemq/activemq-cpp/source/activemq-cpp-2.1-src.zip
   and unpack it. Set ACTIVEMQ_HOME=yourinstallpath\activemq-cpp-2.1
2) Build ActiveMQ using MS VC++ 2005 project:
      cd %ACTIVEMQ_HOME%\vs2005-build
      devenv vs2005-activemq-cpp.sln /build Release (or Debug)
3) Setup the UIMACPP environment:
      set UIMACPP_HOME=%UIMA_HOME%\uimacpp
      set PATH=%PATH%;%UIMACPP_HOME%\bin
4) cd to %UIMACPP_HOME%\examples\service and build the service:
      devenv deployCppService.vcproj /build Release (or Debug)
5) Copy the ActiveMQ binaries:
      copy %ACTIVEMQ_HOME%\vs2005-build\ReleaseDLL\activemq-cpp.dll %UIMACPP_HOME%\bin
6) Copy the service binary:
      copy deployCppService.exe %UIMACPP_HOME%\bin


Testing the service wrapper
---------------------------

1) Open a terminal and run an ActiveMQ broker:
      setup the environment for the UIMA-EE Java SDK (UIMA_HOME, PATH)
      start the ActiveMQ broker with "startBroker.sh" [Linux] or "startBroker" [Windows]
2) Open a 2nd terminal build and test a C++ annotator, DaveDetector:
      Build all the C++ samples using the directions in UIMACPP_HOME/examples/readme.html
      Test DaveDetector following directions in the same readme
3) In the 2nd terminal, start DaveDetector as a UIMA-EE service:
      cd UIMACPP_HOME/examples/descriptors
      deployCppService DaveDetector.xml DaveDetectorQ
4) Open a 3rd terminal and use the UIMA-EE sample client to test the service:
      setup the environment for the UIMA-EE Java SDK, and run the client
      On Linux (all one line):
        runRemoteAsyncAE.sh tcp://localhost:61616 DaveDetectorQ
           -c $UIMA_HOME/examples/descriptors/collection_reader/FileSystemCollectionReader.xml
      On Windows (all one line):
        runRemoteAsyncAE tcp://localhost:61616 DaveDetectorQ
           -c %UIMA_HOME%\examples\descriptors\collection_reader\FileSystemCollectionReader.xml
   The console should indicate successful processing of 8 documents.


