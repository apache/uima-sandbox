package org.apache.uima.tm.dltk.core.extensions;

import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.apache.uima.tm.dltk.parser.ast.TextMarkerExpression;
import org.eclipse.dltk.ast.expressions.Expression;


public interface IIDETypeFunctionExtension extends ITextMarkerExtension {

  TextMarkerExpression createTypeFunction(Token type, List<Expression> args)
          throws RecognitionException;
}
