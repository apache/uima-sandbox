package org.apache.uima.casviewer.core.internal;

public interface IItemTypeConstants {
    // Item Type (from com.ibm.uima.repository.model.typesystem
    // in "com.ibm.uima.repository.core.jar"
    public final static int ITEM_TYPE_UNKNOW                    = 0;
    public final static int ITEM_TYPE_TYPE                      = 1;
    public final static int ITEM_TYPE_FEATURE                   = 2;
    public final static int ITEM_TYPE_TYPE_SYSTEM               = 3;
//    public final static int ITEM_TYPE_COMPONENT                 = 4;
//    public final static int ITEM_TYPE_CATEGORY                  = 5;
//    public final static int ITEM_TYPE_CONCEPT                   = 6;
//    public final static int ITEM_TYPE_INPUT_TYPE                = 7;
//    public final static int ITEM_TYPE_OUTPUT_TYPE               = 8;
//    public final static int ITEM_TYPE_INPUT_CONCEPT             = 9;
//    public final static int ITEM_TYPE_OUTPUT_CONCEPT            = 10;
//
//    public final static int ITEM_TYPE_CONCEPT_ASSOCIATED_TYPE       = 11;
//    public final static int ITEM_TYPE_CONCEPT_ASSOCIATED_COMPONENT  = 12;
//    
//    public final static int ITEM_TYPE_REPOSITORYVIEW            = 14;
//    public final static int ITEM_TYPE_VIEWITEM                  = 15;
//    public final static int ITEM_TYPE_VIEWMAPPING               = 16;
    
    public final static int ITEM_TYPE_LABEL_FEATURES            = 20;
//    public final static int ITEM_TYPE_LABEL_IMPLEMENTED_BY      = 21;
//    public final static int ITEM_TYPE_LABEL_INPUT_TYPES         = 22;
//    public final static int ITEM_TYPE_LABEL_OUTPUT_TYPES        = 23;
//    public final static int ITEM_TYPE_LABEL_INPUT_CONCEPTS      = 24;
//    public final static int ITEM_TYPE_LABEL_OUTPUT_CONCEPTS     = 25;
//    
//    public final static int ITEM_TYPE_LABEL_CONCEPT_ASSOCIATED_TYPES    = 31;
//    public final static int ITEM_TYPE_LABEL_CONCEPT_ASSOCIATED_COMP_LIST= 32;
    public final static int ITEM_TYPE_LABEL_COLLECTION_READERS  = 33;
    public final static int ITEM_TYPE_LABEL_CAS_PROCESSORS      = 34;
    public final static int ITEM_TYPE_LABEL_CAS_CONSUMERS       = 35;
    public final static int ITEM_TYPE_LABEL_CAS_INITIALIZERS    = 36;
    
    public final static int ITEM_TYPE_FS_INDEX                  = 50;
    public final static int ITEM_TYPE_LABEL_FS_INDEX            = 55;
    
    public final static int ITEM_TYPE_FS                        = 200; // FeatureStructure
    public final static int ITEM_TYPE_LABEL_FSs                 = 201; // Label of FeatureStructure(s)
    public final static int ITEM_TYPE_ANNOTATION_OBJECTS        = 202; // List of AnnotationObject Wrapper of FeatureStructure
    
//    public final static int ITEM_TYPE_ENTITY_MAP                = 81;
//    public final static int ITEM_TYPE_ENTITY_OBJECT             = 82;
//    public final static int ITEM_TYPE_ENTITY_ANNOTATION         = 83;
//    public final static int ITEM_TYPE_LABEL_ENTITY_MAP          = 84;
//    public final static int ITEM_TYPE_LABEL_ENTITY_OBJECT       = 85;
//    public final static int ITEM_TYPE_LABEL_ENTITY_ANNOTATION   = 86;
    
    // Item Type
    public final static int ITEM_TYPE_U_BASE                   = 1000;
    public final static int ITEM_TYPE_U_FS                      = 1 + ITEM_TYPE_U_BASE;
    public final static int ITEM_TYPE_U_FS_INDEX                = 2 + ITEM_TYPE_U_BASE;
    
    // Labels
    public final static int ITEM_TYPE_LABEL_U_FS                = 10 + ITEM_TYPE_U_BASE;
    public final static int ITEM_TYPE_LABEL_U_FS_INDEX          = 11 + ITEM_TYPE_U_BASE;
    
}
