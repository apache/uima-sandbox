Apache UIMA BSFAnnotator README file

INTRODUCTION

The BSFAnnotator component provides an UIMA annotator implementation that allow the use of a 
BSF-supported scripting language to implement the initialize and process methods.  The annotator 
has one mandatory parameter the 'SourceFile' that contains the script.  Have a look at the 
BeanshellTestAnnotator.xml, JRubyTestAnnotator.xml or at the more complex BSFAggregatedAE.xml and 
their associated scripts.



