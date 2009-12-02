package org.apache.uima.casviewer.core.internal;

import java.util.LinkedList;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.SofaFS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.resource.metadata.TypePriorities;
import org.apache.uima.resource.metadata.TypeSystemDescription;

public interface ICASObjectView {

    /*************************************************************************/


    public void setTypeSelectionByName (List types, boolean selection);
    
    public TypeTree getTypeTree ();
    public TypeTree newTypeTree ();

    public List<Type>   getTypesHavingAnnotations();
    public LinkedList   getIndexedAnnotations ();

    public String       getDocumentText();

//    public Type getTypeByName(String typeName);
    public Type getTypeFromTypeSystem (String typeName);
    
    public List getAnnotationsForTypeName(String typeName);

    /**
     * @return The TypeSystem associated with this CAS.
     */
    public TypeSystem getTypeSystem ();
    
    public Type getTypeByName (String typeName);
    
    /**
     * @return Returns the typeSystemDescription.
     */
    public TypeSystemDescription getTypeSystemDescription();

    public CasIndexRepository getCasIndexRepository ();
    
    /**
     * @return Returns the typePriorities.
     */
    public TypePriorities getTypePriorities();

    public CAS getCurrentView();

    /**
     * @return the sofa
     */
    public SofaFS getSofa();

    /**
     * @return the sofaId
     */
    public String getSofaId();

    /**
     * @return the baseCASObject
     */
    public BaseCASObject getBaseCASObject();

}