#####################################
# UNIX Makefile for a UIMACPP application
#####################################

# name of the annotator to be created
TARGET_FILE=deployCppService

# list of user's object files to be linked when building the application
OBJS=ActiveMQAnalysisEngineService.o deployCppService.o

#Use this var to pass additional user-defined parameters to the compiler
USER_CFLAGS= -I$(ACTIVEMQ_HOME)/src/main

#Use this var to pass additional user-defined parameters to the linker
USER_LINKFLAGS= -L$(ACTIVEMQ_HOME)/src/main/ -lpthread -luuid -lactivemq-cpp

# Set DEBUG=1 for a debug build (if not 1 a ship build will result)
DEBUG=1

# Set DLL_BUILD=1 to build an annotator (shared library)
#    if not 1 an executable binary will be built
DLL_BUILD=0

# include file with generic compiler instructions
include $(UIMACPP_HOME)/lib/base.mak
