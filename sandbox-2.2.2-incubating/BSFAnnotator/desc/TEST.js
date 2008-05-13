// Example of javascript script to be used as input for the BSFAnnotator
importPackage(Packages.org.apache.uima.analysis_engine);
importPackage(Packages.org.apache.uima.analysis_engine.annotator);
importPackage(Packages.org.apache.uima.jcas);
importPackage(Packages.org.apache.uima.jcas.tcas);
importPackage(Packages.org.apache.uima.annotator.bsf.types);
importPackage(java.util.regex);

var matchPattern;

// Annotator initialize method
// see org.apache.uima.analysis_engine.annotator.Annotator_ImplBase#initialize(org.apache.uima.analysis_engine.annotator.AnnotatorContext)
function initialize(context) {
  var pattern = context.getConfigParameterValue("Regexp");
  matchPattern = Pattern.compile(pattern);
}

// Annotator process method
// see org.apache.uima.analysis_engine.annotator.JTextAnnotator#process(org.apache.uima.jcas.JCas, org.apache.uima.analysis_engine.ResultSpecification)
function process(jcas, rs) {
  var text = jcas.getDocumentText();
  var matcher = matchPattern.matcher(text);
  while (matcher.find()) {
    var token = new Token(jcas, matcher.start(), matcher.end());
    token.addToIndexes();
  }
}
