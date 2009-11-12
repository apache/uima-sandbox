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




public class CalaisTypes {
  
  static String INSTANCE_INFO = 'http://s.opencalais.com/1/type/sys/InstanceInfo'
  
  
  static getCalaisTypeNameEntity (typeName) {
    return "http://s.opencalais.com/1/type/em/e/$typeName"
  }
  
  static getCalaisTypeNameRelation (typeName) {
    return "http://s.opencalais.com/1/type/em/r/$typeName"
  }
  
  // calais entities become annotations of a same name
  // calais relations become annotations of same name, with refs to other entity annotations
  
  static calaisTypes = new NodeBuilder().types {
    entity {
      Anniversary {attrs { name() } }
      City        {attrs { name() } }    
      Company     {attrs { name() } }  
      Continent   {attrs { name() } }
      Country     {attrs { name() } }
      Currency    {attrs { name() } }  // is currency denomination, normalized, eg. USD (for $)
      EmailAddress {attrs { name() } } 
      EntertainmentAwardEvent {attrs { name() } }
      Facility    {attrs { name() } }
      FaxNumber   {attrs { name() } } 
      Holiday     {attrs { name() } }
      IndustryTerm{attrs { name() } }
      MedicalCondition {attrs { name() } }
      Movie       {attrs { name() } }
      MusicAlbum  {attrs { name() } }
      MusicGroup  {attrs { name() } }
      NaturalDisaster {attrs { name() } } // some normalization
      NaturalFeature  {attrs { name() } }
      Organization    {attrs { name() } }
      Person      {attrs { name()
                           persontype() } } // values N/A sports entertainment political etc. 
      PhoneNumber {attrs { name() } }
      Product     {attrs { name() } }  // drug products
      ProvinceOrState {attrs { name() } }
      PublishedMedium {attrs { name() } }
      Region      {attrs { name() } } // eg Far East
      SportsEvent {attrs { name() } }
      SportsGame  {attrs { name() } }
      Technology  {attrs { name() } }
      TVShow      {attrs { name() } }
      URL         {attrs { name() } }
    }
    
    factOrEvent {
      Acquisition {
        attrs {company_acquirer {range 'Company'}       // values are refs to company 
               company_beingacquired {range 'Company'}  // values are refs to company
               status {
                 allowedValues([
                   'announced',
                   'planned',
                   'cancelled',
                   'postponed',
                   'rumored',
                   'known']) } } }
      Alliance { 
        attrs {company {range 'Company'; multivalued()}
               status {
                 allowedValues([
                   'announced',
                   'planned',
                   'cancelled',
                   'postponed',
                   'rumored',
                   'known']) } } }

      AnalystEarningsEstimate {
        attrs {company_source {range 'Company'}
               person_source {range 'Person'}
               company_rated {range 'Company'}
               quarter {allowedValues([
                'Q1', 'Q2', 'Q3', 'Q4', 
                'H1', 'NINE_MONTHS', 'FY']) }
               year()
        }
      }
      
      AnalystRecommendation {
        attrs {company_source {range 'Company'}
               person_source {range 'Person'}
               company_rated {range 'Company'}
               trend {allowedValues([
                 'upgraded', 'downgraded', 'reiterated', 
                 'initiated'])}
               rank_new()  // Strong Buy, Hold, etc.
               rank_old()
        }
      }
      Bankruptcy {
        attrs {
          company {range 'Company'}
          bankruptcystatus()  // considered, expected to emerge
          date()
        }
      }
      
      BusinessRelation {
        attrs {
          company {range 'Company'; multivalued()}
          status {
            allowedValues([
              'announced',
              'planned',
              'cancelled',
              'postponed',
              'rumored',
              'known' ])
          }
        }
      }
      Buybacks {
        attrs {
          company {range 'Company'}
          date()
        }
      }
      CompanyAffiliates {
        attrs {
          company_affiliate {range 'Company'}
          company_parent {range 'Company'}
          relation() // e.g. subsidiary, division, child, parent
        }
      }
      CompanyCustomer {
        attrs { // only one of Company_customer/Organization_Customer is required
          company_provider {range 'Company'}
          company_customer {range 'Company'}
          organization_customer()
        }
      }
      CompanyEarningsAnnouncement {
        attrs {
          company {range 'Company'}
          quarter()
          year()
        }
      }
      CompanyEarningsGuidance {
        attrs {
          company {range 'Company'}
          quarter()
          year()
          trend()
        }
      }
      CompanyInvestment {
        attrs {
          company {range 'Company'}
          company_investor {range 'Company'}
          status()
        }
      }
      CompanyLegalIssues {
        attrs {
          company_sued {range 'Company'}
          sueddescription()
          company_plaintiff {range 'Company'}
          person_plaintiff {range 'Person'}
          lawsuitclass()
          date()
        }
      }
      CompanyLocation {
        attrs {
          company {range 'Company'}
          city {range 'City'}
          provinceorstate {range 'ProvinceOrState'}
          country {range 'Country'}
        }
      }
      CompanyMeeting {
        attrs {
          company {range 'Company'}
          companymeetingtype {
            allowedValues([ 
              'AGM', 'EGM', "Shareholders' Meeting"])
          }
          country {range 'Country'}
          city {range 'City'}
          provinceorstate {range 'ProvinceOrState'}
          status()
          date()
          meetingsite()
        }
      }
      CompanyReorganization {
        attrs {
          company {range 'Company'}
          status()
        }
      }
      CompanyTechnology {
        attrs {
          company {range 'Company'}
          technology()
        }
      }
      ConferenceCall {
        attrs {
          company {range 'Company'}
          ccalltype()
          quarter()
          status {allowedValues([
            'announced', 'rumored', 'planned', 
            'cancelled', 'postponed', 'known']) }
          date()
        }
      }
      CreditRating {
        attrs {
          company_source {range 'Company'}
          company_rated {range 'Company'}
          organization_rated {range 'Organization'}
          trend {allowedValues([
            'affirms', 'assigns', 'changes', 'cuts', 'expects to change', 
            'puts', 'raises', 'rates', 'removes', 'says', 'withdraws']) }
          rank_new()
          rank_old()
        }
      }
      FamilyRelation {
        attrs {
          person {range 'Person'}
          person_relative ()    // not a range of person, just a string
          familyrelationtype()
        }
      }
      IPO {
        attrs {
          company {range 'Company'}
          status {allowedValues([
            'planned', 'announced', 'delayed', 'known']) }
          date()
        }
      }
      JointVenture {
        attrs {
          company {range 'Company'; multivalued()}
          company_newname()
          status {allowedValues([ 
            'announced', 'planned', 'cancelled', 'postponed', 
            'known'])
          }
        }
      }
      ManagementChange {
        attrs {
          company {range 'Company'}
          organization {range 'Organization'}
          person {range 'Person'}
          position()
          action {allowedValues([
            'enters', 'leaves', 'retired'])
          }
        }
      }
      Merger {
        attrs {
          company {range 'Company'; multivalued()}
          status {allowedValues([
            'announced', 'planned', 'cancelled', 
            'postponed', 'rumored', 'known']) }
        }
      }
      PersonEducation {
        attrs {
          person {range 'Person'}
          certification()
          degree()
          schoolororganization()
        }
      }
      PersonPolitical {
        attrs {
          person {range 'Person'}
          position()
          country {range 'Country'}
          provinceorstate {range 'ProvinceOrState'}
          city {range 'City'}
        }
      }
      PersonPoliticalPast {
        attrs {
          person {range 'Person'}
          position()
          country {range 'Country'}
          provinceorstate {range 'ProvinceOrState'}
          city {range 'City'}
        }
      }
      PersonProfessional {
        attrs {
          person {range 'Person'}
          position()
          company {range 'Company'}
          organization {range 'Organization'}
        }
      }
      PersonProfessionalPast {
        attrs {
          person {range 'Person'}
          position()
          company {range 'Company'}
          organization {range 'Organization'}
        }
      }
      Quotation {
        attrs {
          person {range 'Person'}
          quote()
        }
      }
      StockSplit {
        attrs {
          company {range 'Company'}
        }
      }
    }    
  }
  
  static final String TYPE_PREFIX = 'org.apache.uima.calaisType.'
  /**
   * Run this to print out a UIMA type system description for the above calais types
   * 
   * Types:
   *   each entity has a type
   *   each relation has a type
   *   an instance of an entity or relation has begin/end plus a ref to an entity or a type
   */
  static void main(args) {
    def writer = new StringWriter()
    def utypes = new groovy.xml.MarkupBuilder(writer)     
    utypes.types {
      typeDescription {
        name "${TYPE_PREFIX}Base"
        supertypeName 'uima.cas.TOP'
      }
      typeDescription {
        name "${TYPE_PREFIX}Entity"
        supertypeName "${TYPE_PREFIX}Base"
      }
      typeDescription {
        name "${TYPE_PREFIX}Relation"
        supertypeName "${TYPE_PREFIX}Base"
      }
      typeDescription {
        name "${TYPE_PREFIX}Instance"
        supertypeName "uima.tcas.Annotation"
      }
      
      typeDescription {
        name "${TYPE_PREFIX}EntityInstance"
        supertypeName "${TYPE_PREFIX}Instance"
        features {
          featureDescription {
            name 'entity'
            rangeTypeName "${TYPE_PREFIX}Entity"
          }
        }
      }
      
      typeDescription {
        name "${TYPE_PREFIX}RelationInstance"
        supertypeName "${TYPE_PREFIX}Instance"
        features {
          featureDescription {
            name 'relation'
            rangeTypeName "${TYPE_PREFIX}Relation"
          }
        }
      }
      
      calaisTypes.entity[0].each {
        def entityName = it.name()
        typeDescription {
          name "${TYPE_PREFIX}entity.${entityName}"
          supertypeName "${TYPE_PREFIX}Entity"
          features {
            featureDescription {
              name 'canonicalForm'
              rangeTypeName 'uima.cas.String'
            }
          }
        }
      }
      
      calaisTypes.factOrEvent[0].each {
        def relation = it
        typeDescription {
          name "${TYPE_PREFIX}relation.${relation.name()}"
          supertypeName "${TYPE_PREFIX}Relation"
          features {
            relation.attrs[0].each {
              def feat = it
              featureDescription {
               name feat.name()
                if (feat.allowedValues[0]) { 
                  rangeTypeName 'uima.cas.String'                
                } else if (feat.range[0]) {
                  if (feat.multivalued[0]) {
                    rangeTypeName 'uima.cas.FSArray'
                    elementType "${TYPE_PREFIX}entity.${feat.range[0].text()}"
                  } else {
                    rangeTypeName "${TYPE_PREFIX}entity.${feat.range[0].text()}"
                  }
                } else {
                  rangeTypeName 'uima.cas.String'
                }
              } 
            }
          }
        }
      }
    }
    println writer.toString()
  }
}
