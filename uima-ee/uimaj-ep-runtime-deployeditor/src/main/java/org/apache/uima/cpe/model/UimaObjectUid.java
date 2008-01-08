/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Mar 8, 2006, 3:30:20 PM
 * source:  UimaObjectId.java
 */
package org.apache.uima.cpe.model;

/**
 * 
 *
 */
public class UimaObjectUid {

    // Top level objects
    static final public int UID_COLLECTION_READER_LIST    = 10;
    static final public int UID_COLLECTION_READER         = 11;
    static final public int UID_CAS_INITIALIZER           = 12;
    static final public int UID_AE_LIST                   = 13;
    static final public int UID_CAS_CONSUMER_LIST         = 14;
    static final public int UID_CAS_PROCESSOR_LIST        = 15;
    static final public int UID_AE                        = 16;
    static final public int UID_CAS_CONSUMER              = 17;
    static final public int UID_CAS_PROCESSOR             = 18;
    static final public int UID_CAS_INITIALIZER_LIST      = 19;
    
    // UIMA Xml Descriptors
    static final public int UID_XML_DESCRIPTOR              = 20;
    static final public int UID_XML_DESC_UIMA_APP           = 21;
    static final public int UID_XML_DESC_COLL_READER        = 22;
    static final public int UID_XML_DESC_CAS_INIT           = 23;
    static final public int UID_XML_DESC_AE                 = 24;
    static final public int UID_XML_DESC_CAS_CONSUMER       = 25;
    static final public int UID_XML_DESC_CAS_PROCESSOR      = 26;
    static final public int UID_XML_DESC_CPE                = 27;
    
    static final public int UID_CONFIG_PARAM_MODELS         = 111;
    static final public int UID_CONFIG_PARAM_MODEL          = 112;
    static final public int UID_CONFIG_PARAM_MULTIVALUE     = 113;
    
    
    
    // <cpeConfig>
    static final public int UID_CPE_CONFIG_SETTINGS       = 200;    // Category
    static final public int UID_CPE_CONFIG                = 210;
    static final public int UID_CPE_NUMTOPROCESS          = 211;
    static final public int UID_CPE_DEPLOY_AS             = 212;
    static final public int UID_CPE_CHECKPOINT            = 213;
    static final public int UID_CPE_CHECKPOINT_FILE       = 214;
    static final public int UID_CPE_CHECKPOINT_FREQUENCY  = 215;
    static final public int UID_CPE_TIMER                 = 216;
    
    // <casProcessors casPoolSize="3" processingUnitThreadCount="1">
    static final public int UID_CPE_CASPROCESSORS_SETTINGS          = 301;
    static final public int UID_CPE_POOL_SIZE                       = 302;
    static final public int UID_CPE_PROCESSING_UNIT_THREAD_COUNT    = 303;
    static final public int UID_CPE_DROP_CAS_ON_EXCEPTION           = 304;
    static final public int UID_CPE_NUMB_OF_PIPELINES               = 305;
    
    // per <casProcessor deployment="integrated" name="Meeting Detector TAE">
    static final public int UID_CAS_PROCESSOR_DEPLOYMENT_TYPE  = 30;
    static final public int UID_CAS_PROCESSOR_NAME             = 31;
    
    // <checkpoint batch="10000"/>
    static final public int UID_CPE_CAS_PROC_CHECKPOINT            = 35;
    static final public int UID_CPE_CHECKPOINT_BATCH               = 36;
    
    static final public int UID_CPE_CAS_PROC_BATCH_SIZE            = 37;
    
    //<errorHandling>
    static final public int UID_CPE_ERROR_SETTINGS                  = 600;  // Category
    static final public int UID_CPE_ERROR_HANDLING                  = 610;
    static final public int UID_CPE_ERROR_RATE_THRESHOLD            = 611;
    static final public int UID_CPE_ERROR_RATE_THRESHOLD_MAX_ERROR_COUNT     = 612;
    static final public int UID_CPE_ERROR_RATE_THRESHOLD_MAX_ERROR_SAMPLE_SIZE = 613;
    static final public int UID_CPE_ERROR_RATE_THRESHOLD_ACTION    = 614;
    static final public int UID_CPE_ERROR_TIMEOUT_MAX               = 615;
    static final public int UID_CPE_ERROR_MAX_CONSECUTIVE_RESTARTS = 616;
    static final public int UID_CPE_ERROR_MAX_CONSECUTIVE_RESTARTS_VALUE = 617;
    static final public int UID_CPE_ERROR_MAX_CONSECUTIVE_RESTARTS_ACTION = 618;
    
    /*************************************************************************/
    /*              Extended Settings in UIMA App Xml (NOT in CPE XML)       */
    /*************************************************************************/
    // Default settings in CPE Xml for Cas Processor
    static final public int UID_CPE_DEFAULT_SETTINGS                = 1010; // Category
    static final public int UID_CPE_DEFAULT_CAS_PROC_SETTINGS       = 1011;
    static final public int UID_CPE_DEFAULT_ERROR_HANDLING          = 1012;
    static final public int UID_CPE_DEFAULT_CHECKPOINT              = 1020;
    static final public int UID_CPE_DEFAULT_CAS_PROC_BATCH_SIZE     = 1030;
    static final public int UID_CPE_DEFAULT_CHECKPOINT_FREQUENCY    = 1040;
    

}
