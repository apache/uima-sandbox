package org.apache.uima.casviewer.core.internal;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.SofaFS;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.casviewer.core.internal.style.TypesystemBaseStyle;
import org.apache.uima.resource.metadata.TypePriorities;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.TypeSystemUtil;
 
/**
 *  This class wraps UIMA BaseCAS and other objects related to CAS 
 *  
 *  Each BaseCASObject has multiple ICASObjectView which are accessed by Ids.
 *  Each ICASObjectView is associated with a sofa name by registration.
 *  
 *  
 *  Base CAS has:                       <---> BaseCASObject
 *   - Multiple CAS Views              
 *   
 *  CAS View has:                        <---> CASObjectView (ICASObjectView)
 *   - associated Sofa identified 
 *   by Sofa name (or Sofa Id)
 *   - index repository
 *  
 *
 */
public class BaseCASObject {
    
    protected CAS                   aBaseCAS;
    protected TypeSystem            typeSystem;
    protected TypeSystemDescription typeSystemDescription;
    protected TypePriorities        typePriorities;
    protected Map<String,ICASObjectView>  mapSofIdToCASView = new TreeMap<String,ICASObjectView>();
    protected TypeTree              typeTree;
    
    protected String                xmiFileLocation = null; // Full path to xmi file
    /*************************************************************************/

    public BaseCASObject(CAS aCAS) 
    {
        super();
        this.aBaseCAS = aCAS;
        this.typeSystemDescription =
            TypeSystemUtil.typeSystem2TypeSystemDescription (aCAS.getTypeSystem());
        this.typePriorities = null;
    }
    
    public BaseCASObject(CAS aCAS, TypeSystemDescription typeSystemDescription,
                               TypePriorities typePriorities) {
        super();
        this.aBaseCAS = aCAS;
        this.typeSystemDescription = typeSystemDescription;
        this.typePriorities = typePriorities;
        // CAS defaultView = aCAS.getView(CAS.NAME_DEFAULT_SOFA);
    }
    
    public BaseCASObject(CAS aCAS, TypePriorities typePriorities) 
    {
        super();
        this.aBaseCAS = aCAS;
        this.typeSystemDescription =
            TypeSystemUtil.typeSystem2TypeSystemDescription (aCAS.getTypeSystem());
        this.typePriorities = typePriorities;
    }
        
    public ICASObjectView getCASView (String sofaId)
    {
        return (ICASObjectView) mapSofIdToCASView.get(sofaId);        
    }
 
    public ICASObjectView getDefaultCASView ()
    {
        return (ICASObjectView) mapSofIdToCASView.get("Default Sofa");        
    }
    
    public Collection<ICASObjectView> getCASViews () {
        return mapSofIdToCASView.values();
    }

    public Object registerCASView (String sofaId, ICASObjectView aICASView) {
        return mapSofIdToCASView.put(sofaId, aICASView);
    }
    
    public Object unregisterCASView (String sofaId) {
        return mapSofIdToCASView.remove(sofaId);
    }    
    
    /**
     * Register ALL ICASObjectView(s) of the specified BaseCASObject
     * 
     * @param aCASObject
     * @return void
     */
    static public void registerCASViews (BaseCASObject aCASObject)
    {
        FSIterator it = aCASObject.getCAS().getSofaIterator();
        while (it.hasNext()) {
            SofaFS sofa = (SofaFS) it.next();
            aCASObject.mapSofIdToCASView.put(sofa.getStringValue(sofa.getType().getFeatureByBaseName(CAS.FEATURE_BASE_NAME_SOFAID)),  
                    new CASObjectView(aCASObject, sofa));
        }             
    }
    
    /*************************************************************************/

    public TypeTree getTypeTree ()
    {
        if (typeTree == null) {
            typeTree = TypeTree.createTypeTreeFromTypeSystemDescription(typeSystemDescription);
        }
        return typeTree;
    }

    public TypeTree newTypeTree ()
    {
        return TypeTree.createTypeTreeFromTypeSystemDescription(typeSystemDescription);
    }
    
    /*************************************************************************/
    // May create a subclass and support getTypeSystemStyle(sofId) per Sofa
    
    protected TypesystemBaseStyle       typeSystemStyle;
    
    /**
     * @return the typeSystemStyle
     */
    public TypesystemBaseStyle getTypeSystemStyle() {
        return typeSystemStyle;
    }

    /**
     * @param typeSystemStyle the typeSystemStyle to set
     */
    public void setTypeSystemStyle(TypesystemBaseStyle typeSystemStyle) {
        this.typeSystemStyle = typeSystemStyle;
    }
    
    /*************************************************************************/


    /**
     * @return Returns the typePriorities.
     */
    public TypePriorities getTypePriorities() {
        return typePriorities;
    }

    /**
     * @param typePriorities The typePriorities to set.
     */
    public void setTypePriorities(TypePriorities typePriorities) {
        this.typePriorities = typePriorities;
    }

    /**
     * @return Returns the typeSystemDescription.
     */
    public TypeSystemDescription getTypeSystemDescription() {
        return typeSystemDescription;
    }

    /**
     * @param typeSystemDescription The typeSystemDescription to set.
     */
    public void setTypeSystemDescription(TypeSystemDescription typeSystemDescription) {
        this.typeSystemDescription = typeSystemDescription;
    }

    /**
     * @return Returns the  BaseCAS.
     */
    public CAS getCAS() {
        return aBaseCAS;
    }

    public TypeSystem getTypeSystem () {
        if (typeSystem == null) {
            typeSystem = aBaseCAS.getTypeSystem();
        }
        return typeSystem;
    }

    /**
     * @return the xmiFileLocation
     */
    public String getXmiFileLocation() {
        return xmiFileLocation;
    }

    /**
     * @param xmiFileLocation the xmiFileLocation to set
     */
    public void setXmiFileLocation(String xmiFileLocation) {
        this.xmiFileLocation = xmiFileLocation;
    }

}
