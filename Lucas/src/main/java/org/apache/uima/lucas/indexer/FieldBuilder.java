/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.lucas.indexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.uima.cas.CASException;
import org.apache.uima.lucas.indexer.analysis.TokenStreamConcatenator;
import org.apache.uima.lucas.indexer.analysis.TokenStreamMerger;
import org.apache.uima.lucas.indexer.util.TokenStreamStringConcatenator;

public class FieldBuilder {

  public static final String FIELD_INDEX_NO = "no";

  public static final String FIELD_INDEX_YES = "yes";

  public static final String FIELD_INDEX_NO_NORMS = "no_norms";

  public static final String FIELD_INDEX_NO_TF = "no_tf";

  public static final String FIELD_INDEX_NO_NORMS_TF = "no_norms_tf";

  public static final String FIELD_TERM_VECTOR_NO = "no";

  public static final String FIELD_TERM_VECTOR_YES = "yes";

  public static final String FIELD_TERM_VECTOR_WITH_OFFSETS = "offsets";

  public static final String FIELD_TERM_VECTOR_WITH_POSITIONS = "positions";

  public static final String FIELD_TERM_VECTOR_WITH_POSITIONS_OFFSETS = "positions_offsets";

  public static final String FIELD_STORE_NO = "no";

  public static final String FIELD_STORE_YES = "yes";

  public static final String FIELD_STORE_COMPRESS = "compress";

  private TokenStreamStringConcatenator tokenStreamStringConcatenator;

  public FieldBuilder() {
    tokenStreamStringConcatenator = new TokenStreamStringConcatenator();
  }

  public Collection<Field> createFields(Collection<TokenStream> tokenStreams,
          FieldDescription fieldDescription) throws IOException, CASException {

    TokenStream tokenStream = null;
    if (fieldDescription.getMerge())
      tokenStream = new TokenStreamMerger(tokenStreams);
    else if (tokenStreams.size() > 1)
      tokenStream = new TokenStreamConcatenator(tokenStreams);
    else if (tokenStreams.size() == 1)
      tokenStream = tokenStreams.iterator().next();

    String fieldName = fieldDescription.getName();
    String delimiter = fieldDescription.getDelimiter();
    Collection<Field> fields = new ArrayList<Field>();

    Field.Store fieldStore = getFieldStore(fieldDescription.getStored());
    Field.Index fieldIndex = getFieldIndex(fieldDescription.getIndex());
    Field.TermVector fieldTermVector = getFieldTermVector(fieldDescription.getTermVector());

    if (fieldStore == Field.Store.YES || fieldStore == Field.Store.COMPRESS)
      fields.addAll(createStoredFields(fieldName, tokenStream, fieldStore, delimiter));

    boolean omitTF =
            fieldDescription.getIndex().equals(FIELD_INDEX_NO_TF)
                    || fieldDescription.getIndex().equals(FIELD_INDEX_NO_NORMS_TF);

    if (fieldIndex != Field.Index.NO)
      fields.add(createIndexedField(fieldName, tokenStream, fieldIndex, fieldTermVector, omitTF));

    return fields;
  }

  private Field createIndexedField(String fieldName, TokenStream tokenStream, Index fieldIndex,
          TermVector fieldTermVector, boolean omitTF) {

    Field field = new Field(fieldName, tokenStream, fieldTermVector);
    if (fieldIndex == Field.Index.NOT_ANALYZED_NO_NORMS)
      field.setOmitNorms(true);
    field.setOmitTf(omitTF);

    return field;
  }

  protected Collection<Field> createStoredFields(String fieldName, TokenStream tokenStream,
          Store fieldStore, String delimiter) throws IOException {
    Collection<Field> fields = new ArrayList<Field>();
    if (delimiter != null) {
      String value =
              tokenStreamStringConcatenator
                      .tokenStreamToStringWithDelimiter(tokenStream, delimiter);
      fields.add(new Field(fieldName, value, fieldStore, Field.Index.NO));
    } else {
      Token nextToken = tokenStream.next(new Token());
      while (nextToken != null) {
        String value = new String(nextToken.termBuffer(), 0, nextToken.termLength());
        fields.add(new Field(fieldName, value, fieldStore, Field.Index.NO));
        nextToken = tokenStream.next(nextToken);
      }
    }
    tokenStream.reset();

    return fields;
  }

  protected Field.Index getFieldIndex(String index) {
    if (index.equals(FIELD_INDEX_NO))
      return Field.Index.NO;
    else if (index.equals(FIELD_INDEX_YES))
      return Field.Index.NOT_ANALYZED;
    else if (index.equals(FIELD_INDEX_NO_NORMS))
      return Field.Index.NOT_ANALYZED_NO_NORMS;
    else if (index.equals(FIELD_INDEX_NO_NORMS_TF))
      return Field.Index.NOT_ANALYZED_NO_NORMS;
    else if (index.equals(FIELD_INDEX_NO_TF))
      return Field.Index.NOT_ANALYZED;

    throw new IllegalArgumentException("unknown index parameter: " + index);
  }

  protected Field.TermVector getFieldTermVector(String termVector) {
    if (termVector.equals(FIELD_TERM_VECTOR_NO))
      return Field.TermVector.NO;
    else if (termVector.equals(FIELD_TERM_VECTOR_YES))
      return Field.TermVector.YES;
    else if (termVector.equals(FIELD_TERM_VECTOR_WITH_OFFSETS))
      return Field.TermVector.WITH_OFFSETS;
    else if (termVector.equals(FIELD_TERM_VECTOR_WITH_POSITIONS))
      return Field.TermVector.WITH_POSITIONS;
    else if (termVector.equals(FIELD_TERM_VECTOR_WITH_POSITIONS_OFFSETS))
      return Field.TermVector.WITH_POSITIONS_OFFSETS;

    throw new IllegalArgumentException("unknown termVector parameter: " + termVector);
  }

  protected Field.Store getFieldStore(String store) {
    if (store.equals(FIELD_STORE_NO))
      return Field.Store.NO;
    else if (store.equals(FIELD_STORE_YES))
      return Field.Store.YES;
    else if (store.equals(FIELD_STORE_COMPRESS))
      return Field.Store.COMPRESS;

    throw new IllegalArgumentException("unknown stored parameter: " + store);
  }
}
