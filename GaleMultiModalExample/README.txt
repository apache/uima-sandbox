     Apache UIMA GaleMultiModalSample README file

   GALE Multi-Modal NLP Sample Application

INTRODUCTION
------------

When complete this sample application will demonstrate how to implement a multi-modal UIMA pipeline
of NLP services capable of chaining together independently developed NLP engines to produce, for
example, a speech-to-speech application that transforms audio from one language to another, or one
that generates English captions for a foreign language news video.  
It will include data-transformation routines that convert the output of one NLP engine into a format
suitable for input to another, while maintaining cross-reference links that allow, for example,
words in a translation to be aligned with the audio in the source language that generated them.  
The sample NLP engines included will only simulate the real NLP work (e.g. speech recognition,
machine translation, entity detection, etc.) but examples of the usage of some open-source engines
may ne provided. 

This sample multi-modal application is based on the one used to develop the GALE Interoperabilty
Demonstration system (IOD) that uses UIMA-AS to interconnect 11 different types of NLP analytics
distributed over 7 research facilities in 3 countries to transcribe, translate, and extract
information from foreign language news broadcasts [1]. 
The GALE TypeSystem (GTS) is spread over a number of files so that each NLP engine's types are
concentrated on one or two files [2]. 

CONTENTS
--------

Just the typesystem so far.


REFERENCES
----------

[1] J. F. Pitrelli, B. L. Lewis, E. A. Epstein, M. Franz, D. Kiecza, J. L. Quinn, G. Ramaswamy,
A. Srivastava, and P. Virga,  
"Aggregating Distributed STT, MT, and Information Extraction Engines:
 The GALE Interoperability-Demo System", 
Proc. Interspeech 2008. 
http://www.isca-speech.org/archive/interspeech_2008/i08_2743.html

[2] J. F. Pitrelli, B. L. Lewis, E. A. Epstein, J. L. Quinn, and G. Ramaswamy, 
"A Data Format Enabling Interoperation of Speech Recognition, Translation and Information Extraction Engines:
 The GALE Type System", 
Proc. Interspeech 2008.  
http://www.isca-speech.org/archive/interspeech_2008/i08_1654.html
