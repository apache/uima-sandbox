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
package org.apache.uima.annotator.calais_groovy

import org.apache.uima.jcas.cas.FSArray
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase
import org.apache.uima.jcas.JCas


public class RdfProcessor extends JCasAnnotator_ImplBase{
  
  static debugPrint = 0

  def descriptionMap // map, key = url-like ids, value = description node
  def entityMap
  def relationMap
  def instances
  static final multiCompanyRelations = ['Alliance', 'BusinessRelation', 'JointVenture', 'Merger']

  
  /* (non-Javadoc)
   * @see org.apache.uima.analysis_component.JCasAnnotator_ImplBase#process(org.apache.uima.jcas.JCas)
   */
  public void process(JCas jcas){
    def rdfindex = jcas.getIndexRepository().getIndex("org.apache.uima.annotator.calais.Rdf")
    rdfindex.each { processRdf(it, jcas) } // should only be one
  }
  
  def processRdf(rdfTextInstance, jcas) {
    // 4 passes
    // pass 1 - create url to node map
    // pass 2 - create entities
    // pass 3 - create relations (point to entities)
    // pass 4 - create instances of entities and relations - 
    //          ref entities and relations, and have begin/end refs to text
 
    // pass 2, 3, and 4 blended and done in 1 iteration.  
    def rdf = new XmlSlurper().parseText(rdfTextInstance.rdfText)
    // pass 1
    
    descriptionMap = [ : ]   
    rdf.Description.each {descriptionMap.put(it.@about.toString(), it)}

    if (debugPrint >= 3) { 
      descriptionMap.each {k, v -> println "key = $k,  value=$v"}
    }
    // pass 2, 3 and 4 blended
    entityMap = [ : ]  // key = url for entity, value = featureStructure for it
    instances = []
    
    relationMap = [ : ] 
    
    rdf.Description.each {
      def typeurl = it.type[0].@resource
      if (isEntity(typeurl)) {
        getOrMakeEntity(it.@about.toString(), jcas)
      } else if (isRelation(typeurl)) {
        getOrMakeRelation(it.@about.toString(), jcas)
      } else if (isInstance(typeurl)) {
        def entityOrRelation = descriptionMap.get(it.subject[0].@resource.toString())
        def is_entity = isEntity(entityOrRelation.type[0].@resource)
        def kind = is_entity ? 'Entity' : 'Relation'
        def instance = newJCasInstance(jcas, "${kind}Instance")
        instances.add(instance)
        if (is_entity) {
          instance.entity = getOrMakeEntity(entityOrRelation.@about.toString(), jcas)
        } else {
          instance.relation = getOrMakeRelation(entityOrRelation.@about.toString(), jcas)
        }
        instance.begin = Integer.valueOf(it.offset[0].text())
        instance.end =   Integer.valueOf(it.offset[0].text()) + Integer.valueOf(it.length[0].text())    
        instance.addToIndexes()
      }
    }


    if (debugPrint >= 2) {
      entityMap.each{k, v -> println "entity key = $k,  value=$v"} // test-debug
    }
   
    if (debugPrint >= 2) {
      relationMap.each {k, v -> println "relation: k: $k, v: $v"}  // debug test
    }
           
    if (debugPrint >= 1) {
      instances.each {println "instance $it"}
    }
    
    descriptionMap = entityMap = relationMap = instances = null

  }
  
  def lastPart(thing) {
    def s = thing.toString()
    s.substring(s.lastIndexOf('/') + 1)
  }

  def isRelation(url) {
//      println " is relation: ${url.toString()}"
      return url?.toString()?.startsWith('http://s.opencalais.com/1/type/em/r/')
  }

  def isEntity(url) {
//      println " is relation: ${url.toString()}"
      return url?.toString()?.startsWith('http://s.opencalais.com/1/type/em/e/')
  }

  def isInstance(url) {
//  println " is relation: ${url.toString()}"
  return (url?.toString() == 'http://s.opencalais.com/1/type/sys/InstanceInfo')
}

  def newJCasInstance(jcas, type) {
    def clasz = Class.forName("org.apache.uima.calaisType.$type".toString(), true, jcas.getCas().JCasClassLoader)
    def constructor = clasz.getDeclaredConstructor(JCas.class);
    return constructor.newInstance(jcas)
    // mock as a map
//    return args.clone()
  }

  def getOrMakeEntity(key, jcas) {
    def instance = entityMap.get(key)
    if (instance) {
      return instance
    }
    def description = descriptionMap[key]
    def typeurl = description.type[0].@resource
    instance = newJCasInstance(jcas, "entity.${lastPart(typeurl)}")
    entityMap.put(key, instance)    
    instance.canonicalForm = description.name[0].text()                                    
    instance.addToIndexes()
    return instance
  }
  
  def getOrMakeRelation(key, jcas) {
    def instance = relationMap.get(key)
    if (instance) {
      return instance
    }
    def description = descriptionMap[key]
    def typeurl = description.type[0].@resource
    def relationName = lastPart(typeurl)
    instance = newJCasInstance(jcas, "relation.$relationName")
    relationMap.put(description.@about.toString(), instance)

    def multiCompany = multiCompanyRelations.contains(relationName)
    if (multiCompany) {
      // special handling - make an fs array of all companies
      def numberOfCompanies = description.company.size()
      instance.company = new FSArray(jcas, numberOfCompanies)
      description.company.eachWithIndex {obj, i ->
        instance.setCompany(i, entityMap.get(obj.@resource.toString()))
      }
    }
    description.children().each {
      def featureName = it.name()
      if (featureName == 'type' || 
          (multiCompany && featureName == 'company')) {
        return
      }
      def resourceurl = it.@resource.toString()
      if (resourceurl) {
        def value = getOrMakeEntity(resourceurl, jcas)
        instance[featureName] = value
      } else {
        instance[featureName] = it.text()
      }
    }
    instance.addToIndexes()
  }
}
