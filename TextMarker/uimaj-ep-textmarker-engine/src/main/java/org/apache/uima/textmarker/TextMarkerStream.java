/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.textmarker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.ConstraintFactory;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.impl.FSIteratorImplBase;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.jcas.tcas.DocumentAnnotation;
import org.apache.uima.textmarker.rule.RuleElementMatch;
import org.apache.uima.textmarker.type.TextMarkerAnnotation;
import org.apache.uima.textmarker.type.TextMarkerBasic;
import org.apache.uima.textmarker.type.TextMarkerFrame;

public class TextMarkerStream extends FSIteratorImplBase<AnnotationFS> {

  private final CAS cas;

  private final FSIterator<AnnotationFS> basicIt;

  private FSIterator<AnnotationFS> currentIt;

  private AnnotationFS documentAnnotation;

  private TextMarkerBasic firstBasic;

  private Type documentAnnotationType;

  private Type basicType;

  private final List<TextMarkerBasic> basics;

  private FilterManager filter;

  private Map<Integer, TextMarkerBasic> pointerMap = new HashMap<Integer, TextMarkerBasic>();

  protected TextMarkerStream(CAS cas, FSIterator<AnnotationFS> basic,
          FSIterator<AnnotationFS> current, Type basicType, FilterManager filter) {
    super();
    this.cas = cas;
    this.filter = filter;
    this.basicType = basicType;
    AnnotationFS additionalWindow = filter.getWindowAnnotation();
    if (additionalWindow != null) {
      this.basicIt = cas.getAnnotationIndex(basicType).subiterator(additionalWindow);
    } else {
      this.basicIt = basic;
    }
    if (current == null) {
      // TODO use here a subiterator??
      currentIt = filter.createFilteredIterator(cas, basicIt, basicType);
      // currentIt = cas.createFilteredIterator(basic, filter.getDefaultConstraint());
    } else {
      currentIt = current;
    }
    // really an if? sub it of basic should fix this
    if (additionalWindow == null) {
      documentAnnotation = (DocumentAnnotation) getJCas().getDocumentAnnotationFs();
      documentAnnotationType = getCas().getDocumentAnnotation().getType();
      basicIt.moveToFirst();
      if (basicIt.isValid()) {
        firstBasic = (TextMarkerBasic) basicIt.get();
      }
    } else {
      documentAnnotation = additionalWindow;
      documentAnnotationType = filter.getWindowType();
      firstBasic = getFirstBasicInWindow(additionalWindow, basic);
    }
    // really faster???
    basics = new ArrayList<TextMarkerBasic>();
    FSIterator<AnnotationFS> iterator = cas.getAnnotationIndex(basicType).subiterator(
            documentAnnotation);
    while (iterator.isValid()) {
      basics.add((TextMarkerBasic) iterator.get());
      iterator.moveToNext();
    }
  }

  public TextMarkerStream(CAS cas, FSIterator<AnnotationFS> basic, Type basicType,
          FilterManager filter) {
    this(cas, basic, null, basicType, filter);
  }

  public void addAnnotation(TextMarkerBasic anchor, AnnotationFS annotation) {
    if (anchor == null)
      return;
    Type type = annotation.getType();
    String name = type.getName();
    TypeSystem typeSystem = cas.getTypeSystem();
    Type parent = type;
    while (parent != null) {
      anchor.setAnnotation(parent.getName(), annotation, parent == type);
      parent = typeSystem.getParent(parent);
    }
    List<TextMarkerBasic> basicAnnotationsInWindow = getAllBasicsInWindow(annotation);
    for (TextMarkerBasic basic : basicAnnotationsInWindow) {
      basic.addPartOf(name);
    }
  }

  public void removeAnnotation(TextMarkerBasic anchor, Type type) {
    TypeSystem typeSystem = cas.getTypeSystem();
    String name = type.getName();
    AnnotationFS expandAnchor = expandAnchor(anchor, type, true);
    if (expandAnchor == null) {
      // there is no annotation to remove
      return;
    }
    List<TextMarkerBasic> basicAnnotationsInWindow = getAllBasicsInWindow(expandAnchor);
    for (TextMarkerBasic basic : basicAnnotationsInWindow) {
      basic.removePartOf(name);
    }
    Type parent = type;
    while (parent != null) {
      anchor.removeAnnotation(parent.getName(), parent == type);
      parent = typeSystem.getParent(parent);
    }
    if (!(expandAnchor instanceof TextMarkerBasic)) {
      cas.removeFsFromIndexes(expandAnchor);
    }
  }

  public FSIterator<AnnotationFS> getFilteredBasicIterator(FSMatchConstraint constraint) {
    ConstraintFactory cf = cas.getConstraintFactory();
    FSMatchConstraint matchConstraint = cf.and(constraint, filter.getDefaultConstraint());
    return cas.createFilteredIterator(basicIt, matchConstraint);
  }

  public TextMarkerStream getWindowStream(AnnotationFS windowAnnotation, Type windowType) {
    if (windowAnnotation.getBegin() == documentAnnotation.getBegin()
            && windowAnnotation.getEnd() == documentAnnotation.getEnd()) {
      return this;
    }

    FilterManager filterManager = new FilterManager(filter.getDefaultFilterTypes(),
            filter.getDefaultRetainTags(), filter.getCurrentFilterTypes(),
            filter.getCurrentRetainTypes(), filter.getCurrentFilterTags(),
            filter.getCurrentRetainTags(), windowAnnotation, windowType, cas);
    // TODO can I use the old currentIT here? check this please!

    TextMarkerStream stream = new TextMarkerStream(cas, basicIt, basicType, filterManager);
    return stream;
  }

  public FSIterator<AnnotationFS> copy() {
    return new TextMarkerStream(cas, basicIt.copy(), currentIt.copy(), basicType, filter);
  }

  public AnnotationFS get() throws NoSuchElementException {
    return currentIt.get();
  }

  public boolean isValid() {
    return currentIt.isValid();
  }

  public void moveTo(FeatureStructure fs) {
    try {
      currentIt.moveTo(fs);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void moveToFirst() {
    currentIt.moveToFirst();
  }

  public void moveToLast() {
    currentIt.moveToLast();
  }

  public void moveToNext() {
    currentIt.moveToNext();
  }

  public void moveToPrevious() {
    currentIt.moveToPrevious();
  }

  public List<AnnotationFS> getOverappingAnnotations(AnnotationFS window, Type type) {
    List<AnnotationFS> result = new ArrayList<AnnotationFS>();
    AnnotationFS newWindow = cas.createAnnotation(type, window.getBegin(), window.getEnd() - 1);
    FSIterator<AnnotationFS> iterator = cas.getAnnotationIndex(type).iterator(newWindow);
    if (!iterator.isValid()) {
      iterator.moveToLast();
    }
    while (iterator.isValid()) {
      FeatureStructure fs = iterator.get();
      if (fs instanceof AnnotationFS) {
        AnnotationFS a = (AnnotationFS) fs;
        if (a.getEnd() >= window.getEnd() && a.getBegin() <= window.getBegin()) {
          result.add(a);
        }
      }
      iterator.moveToPrevious();
    }
    return result;
  }

  public List<Annotation> getAnnotationsFollowing(Annotation annotation) {
    List<Annotation> result = new ArrayList<Annotation>();
    moveTo(annotation);
    while (currentIt.isValid()) {
      currentIt.moveToNext();
      if (currentIt.isValid()) {
        Annotation nextAnnotation = (Annotation) currentIt.get();
        if (nextAnnotation.getBegin() == annotation.getEnd()) {
          result.add(nextAnnotation);
        } else if (nextAnnotation.getBegin() >= annotation.getEnd()) {
          break;
        }
      }
    }
    return result;
  }

  public CAS getCas() {
    return cas;
  }

  public JCas getJCas() {
    try {
      return cas.getJCas();
    } catch (CASException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<AnnotationFS> getAllofType(Type type) {
    List<AnnotationFS> result = new ArrayList<AnnotationFS>();
    FSIterator<AnnotationFS> iterator = cas.getAnnotationIndex(type).iterator();
    while (iterator.isValid()) {
      FeatureStructure featureStructure = iterator.get();
      result.add((AnnotationFS) featureStructure);
      iterator.moveToNext();
    }
    return result;
  }

  public List<AnnotationFS> getAnnotationsInWindow2(AnnotationFS windowAnnotation, Type type) {
    List<AnnotationFS> result = new ArrayList<AnnotationFS>();
    if (windowAnnotation instanceof TextMarkerBasic) {
      TextMarkerBasic basic = (TextMarkerBasic) windowAnnotation;
      AnnotationFS a = basic.getType(type.getName());
      if (a != null) {
        result.add(a);
      }
      return result;
    }
    windowAnnotation = cas.createAnnotation(type, windowAnnotation.getBegin(),
            windowAnnotation.getEnd() + 1);
    FSIterator<AnnotationFS> completeIt = getCas().getAnnotationIndex(type).iterator();
    if (getDocumentAnnotation().getEnd() < windowAnnotation.getEnd()) {
      completeIt.moveToLast();
    } else {
      completeIt.moveTo(windowAnnotation);
    }
    while (completeIt.isValid()
            && ((Annotation) completeIt.get()).getBegin() >= windowAnnotation.getBegin()) {
      completeIt.moveToPrevious();
    }

    if (completeIt.isValid()) {
      completeIt.moveToNext();
    } else {
      completeIt.moveToFirst();
    }

    while (completeIt.isValid()
            && ((Annotation) completeIt.get()).getBegin() < windowAnnotation.getBegin()) {
      completeIt.moveToNext();
    }

    while (completeIt.isValid()
            && ((Annotation) completeIt.get()).getBegin() >= windowAnnotation.getBegin()) {
      Annotation annotation = (Annotation) completeIt.get();
      if (getCas().getTypeSystem().subsumes(type, annotation.getType())
              && annotation.getEnd() <= windowAnnotation.getEnd()) {
        result.add(annotation);
      }
      completeIt.moveToNext();
    }
    return result;
  }

  public List<AnnotationFS> getAnnotationsInWindow(AnnotationFS windowAnnotation, Type type) {
    List<AnnotationFS> result = new ArrayList<AnnotationFS>();
    List<AnnotationFS> inWindow = getAnnotationsInWindow2(windowAnnotation, type);
    result = inWindow;
    return result;
  }

  public List<TextMarkerBasic> getAllBasicsInWindow(AnnotationFS windowAnnotation) {
    List<TextMarkerBasic> result = new ArrayList<TextMarkerBasic>();
    if (windowAnnotation instanceof TextMarkerBasic) {
      result.add((TextMarkerBasic) windowAnnotation);
      return result;
    } else if (windowAnnotation.getBegin() <= documentAnnotation.getBegin()
            && windowAnnotation.getEnd() >= documentAnnotation.getEnd()) {
      return basics;
    }
    TextMarkerFrame frame = new TextMarkerFrame(getJCas(), windowAnnotation.getBegin(),
            windowAnnotation.getEnd());
    FSIterator<AnnotationFS> iterator = cas.getAnnotationIndex(basicType).subiterator(frame);
    while (iterator.isValid()) {
      result.add((TextMarkerBasic) iterator.get());
      iterator.moveToNext();
    }
    return result;
  }

  public List<TextMarkerBasic> getBasicsInWindow(AnnotationFS windowAnnotation) {
    List<TextMarkerBasic> result = new ArrayList<TextMarkerBasic>();
    if (windowAnnotation instanceof TextMarkerBasic) {
      result.add((TextMarkerBasic) windowAnnotation);
      return result;
    }
    FSMatchConstraint defaultConstraint = filter.getDefaultConstraint();
    FSIterator<AnnotationFS> iterator = cas.createFilteredIterator(cas
            .getAnnotationIndex(basicType).subiterator(windowAnnotation), defaultConstraint);

    while (iterator.isValid()) {
      result.add((TextMarkerBasic) iterator.get());
      iterator.moveToNext();
    }
    return result;
  }

  public TextMarkerBasic getFirstBasicInWindow(AnnotationFS windowAnnotation) {
    return getFirstBasicInWindow(windowAnnotation, currentIt);
  }

  public TextMarkerBasic getFirstBasicInWindow(AnnotationFS windowAnnotation,
          FSIterator<AnnotationFS> it) {
    if (windowAnnotation instanceof TextMarkerBasic) {
      return (TextMarkerBasic) windowAnnotation;
    }
    it.moveTo(windowAnnotation);
    if (it.isValid()) {
      return (TextMarkerBasic) it.get();
    }
    return null;
  }

  public List<TextMarkerBasic> getAnnotationsOverlappingWindow(TextMarkerBasic basic, Type type) {
    AnnotationFS expand = expandPartOf(basic, type);
    if (expand != null) {
      return getBasicsInWindow(expand);
    } else {
      return new ArrayList<TextMarkerBasic>();
    }
  }

  private AnnotationFS expandPartOf(TextMarkerBasic currentBasic, Type type) {
    if (type == null) {
      return currentBasic;
    }
    AnnotationFS result = currentBasic.getType(type.getName());
    if (result != null) {
      return result;
    }
    if (currentBasic.isPartOf(type.getName())) {
      moveTo(currentBasic);
      while (isValid()) {
        TextMarkerBasic each = (TextMarkerBasic) get();
        if (each.isAnchorOf(type.getName())) {
          AnnotationFS annotation = each.getType(type.getName());
          if (annotation.getBegin() <= currentBasic.getBegin()
                  && annotation.getEnd() >= currentBasic.getEnd()) {
            return annotation;
          }
        }
        moveToPrevious();
      }
    }
    return null;
  }

  public AnnotationFS expandAnchor(TextMarkerBasic currentBasic, Type type) {
    return expandAnchor(currentBasic, type, false);
  }

  public AnnotationFS expandAnchor(TextMarkerBasic currentBasic, Type type, boolean ret) {
    if (type == null || currentBasic == null) {
      return currentBasic;
    }
    AnnotationFS result = currentBasic.getType(type.getName());
    if (result != null) {
      return result;
    }
    if (type.getName().equals(getDocumentAnnotationType().getName())) {
      return documentAnnotation;
    }
    if (!ret) {
      return currentBasic;
    } else {
      return null;
    }
  }

  public FSIterator<AnnotationFS> getUnfilteredBasicIterator() {
    return basicIt;
  }

  public AnnotationFS getDocumentAnnotation() {
    return documentAnnotation;
  }

  public TextMarkerAnnotation getCorrectTMA(List<AnnotationFS> annotationsInWindow,
          TextMarkerAnnotation heuristicAnnotation) {
    for (AnnotationFS annotation : annotationsInWindow) {
      if (annotation instanceof TextMarkerAnnotation) {
        TextMarkerAnnotation tma = (TextMarkerAnnotation) annotation;
        if (tma.getBegin() == heuristicAnnotation.getBegin()
                && tma.getEnd() == heuristicAnnotation.getEnd()
                && tma.getAnnotation().getType()
                        .equals(heuristicAnnotation.getAnnotation().getType())) {
          return tma;
        }
      }
    }
    return null;
  }

  public void retainTypes(List<Type> list) {
    filter.retainTypes(list);
    FSMatchConstraint defaultConstraint = filter.getDefaultConstraint();
    currentIt = cas.createFilteredIterator(basicIt, defaultConstraint);
  }

  public void filterTypes(List<Type> list) {
    filter.filterTypes(list);
    FSMatchConstraint defaultConstraint = filter.getDefaultConstraint();
    currentIt = cas.createFilteredIterator(basicIt, defaultConstraint);
  }

  public void retainTags(List<String> list) {
    filter.retainTags(list);
    FSMatchConstraint defaultConstraint = filter.getDefaultConstraint();
    currentIt = cas.createFilteredIterator(basicIt, defaultConstraint);
  }

  public void filterTags(List<String> list) {
    filter.filterTags(list);
    FSMatchConstraint defaultConstraint = filter.getDefaultConstraint();
    currentIt = cas.createFilteredIterator(basicIt, defaultConstraint);
  }

  public FilterManager getFilter() {
    return filter;
  }

  public TextMarkerBasic getFirstBasicOfAll() {
    return firstBasic;
  }

  public Type getDocumentAnnotationType() {
    return documentAnnotationType;
  }

  public TextMarkerBasic nextAnnotation(RuleElementMatch match) {
    if (!match.matched()) {
      return null;
    }
    List<AnnotationFS> textsMatched = match.getTextsMatched();
    AnnotationFS lastAnnotation = null;
    if (textsMatched != null && !textsMatched.isEmpty()) {
      lastAnnotation = textsMatched.get(textsMatched.size() - 1);
    }
    if (lastAnnotation == null || lastAnnotation.getEnd() >= getDocumentAnnotation().getEnd()) {
      return null;
    }
    // if (getNextBasic(lastAnnotation) == null) {
    // System.out.println();
    // }
    // if (getNextBasic(lastAnnotation) != null
    // && getNextBasic(lastAnnotation).getBegin() == lastAnnotation.getBegin()) {
    // System.out.println();
    // }
    TextMarkerBasic nextBasic = getNextBasic(lastAnnotation);
    // TextMarkerBasic nextBasic2 = getNextBasic2(lastAnnotation);
    // if (nextBasic != nextBasic2) {
    // String string = nextBasic == null ? "null" : nextBasic.getCoveredText();
    // String string2 = nextBasic == null ? "null" : (nextBasic.getBegin() + "");
    // System.out.println("nextBasic.getBegin() != nextBasic2.getBegin() " + string + " "
    // + nextBasic2.getCoveredText());
    // System.out.println(lastAnnotation.getBegin() + "=" + string2 + "=" + nextBasic2.getBegin());
    // }
    return nextBasic;
  }

  public TextMarkerBasic getNextBasic(AnnotationFS previous) {
    TextMarkerBasic pointer = pointerMap.get(previous.getEnd());
    if (pointer == null) {
      // FIXME: hotfix for ML stuff
      // pointer = new TextMarkerFrame(getJCas(), previous.getEnd()-1, previous.getEnd());
      pointer = (TextMarkerBasic) cas.createAnnotation(basicType, previous.getEnd() - 1,
              previous.getEnd());
      pointerMap.put(previous.getEnd(), pointer);
    }
    currentIt.moveTo(pointer);
    if (currentIt.isValid()) {
      TextMarkerBasic basic = (TextMarkerBasic) currentIt.get();
      if (basic.getBegin() == previous.getBegin()) {
        // if (basic.getBegin() >= previous.getBegin() || basic.getEnd() <= previous.getEnd()) {
        currentIt.moveToNext();
        if (currentIt.isValid()) {
          return (TextMarkerBasic) currentIt.get();
        } else {
          return null;
        }
      } else {
        return basic;
      }
    }
    return null;
  }

  public TextMarkerBasic getNextBasic2(AnnotationFS previous) {
    AnnotationFS pointer = cas
            .createAnnotation(basicType, previous.getEnd() - 1, previous.getEnd());
    currentIt.moveTo(pointer);
    if (currentIt.isValid()) {
      TextMarkerBasic basic = (TextMarkerBasic) currentIt.get();
      return basic;
    }
    return null;
  }

  public TextMarkerStream getCompleteStream() {
    FilterManager defaultFilter = new FilterManager(filter.getDefaultFilterTypes(),
            filter.getDefaultRetainTags(), getCas());
    return new TextMarkerStream(getCas(), basicIt, basicType, defaultFilter);
  }

}
