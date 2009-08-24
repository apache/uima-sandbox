// Example of javascript script to be used as input for the BSFAnnotator
importPackage(Packages.org.apache.uima);
importPackage(Packages.org.apache.uima.analysis_component);
importPackage(Packages.org.apache.uima.jcas);
importPackage(Packages.org.apache.uima.jcas.tcas);
importPackage(Packages.org.apache.uima.annotator.bsf.types);
importPackage(java.util.regex);

var matchPattern;

// Annotator initialize method
function initialize(context) {
  var pattern = context.getConfigParameterValue("Regexp");
  matchPattern = Pattern.compile(pattern);
}

// Annotator process method
function process(jcas) {
  var text = jcas.getDocumentText();
  var matcher = matchPattern.matcher(text);
  while (matcher.find()) {
    var token = new Token(jcas, matcher.start(), matcher.end());
    token.addToIndexes();
  }
}
