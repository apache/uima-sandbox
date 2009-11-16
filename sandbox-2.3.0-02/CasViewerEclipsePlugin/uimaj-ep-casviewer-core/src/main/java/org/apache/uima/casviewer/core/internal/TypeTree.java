package org.apache.uima.casviewer.core.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.ResourceSpecifierFactory;
import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.metadata.AllowedValue;
import org.apache.uima.resource.metadata.FeatureDescription;
import org.apache.uima.resource.metadata.FsIndexDescription;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.tools.internal.util.UimaToolsUtil;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.InvalidXMLException;

/**
 * This type tree is based on TypeDescription (NOT TypeMetadata)
 * and TypeNode
 *
 */
public class TypeTree implements Serializable {

	private static final long serialVersionUID = 10L;

    public final static String TYPESYSTEM_ROOT_NAME			= "SUPER";
    public final static String TYPESYSTEM_ROOT_SHORT_NAME	= "SUPER";
    
    protected final static String UIMA_CAS_TOP = "uima.cas.TOP";
    
	/************************************************************************/
    
    // key: TypeDescription.name ; value:TypeDescription
    private Hashtable   _builtInTypesHashtable  = new Hashtable(); 
	
	// Contains key=TreeBaseNode.label and value=TreeBaseNode
	// Used to check duplicate nodes
    protected Hashtable<String,TypeNode> _nodesHashtable = new Hashtable<String,TypeNode>();
	
	// key: TypeDescription.name ; value:TypeNode
    protected Hashtable<String,TypeNode> _undefinedTypesHashtable = new Hashtable<String,TypeNode>(); 
    
    // SUPER Root of everything
    protected TypeNode   _rootSuper = null;

    protected String          _containmentKey = "supertypeName";
        
    // Array of ImportedTypeSystem
    // private List       _importedTypeSystems = new ArrayList();
    
    private int _totalOfBuiltInTypes = 0;
    
	/************************************************************************/
    
    protected TypeTree () {        
    }
    
	public TypeTree (List<TypeDescription> typeMetadataList) {
        this(null, typeMetadataList);
	}
	
	public TypeTree (TypeNode root, List<TypeDescription> typeMetadataList) {

		if (root == null) {
            // Create TypeMetadata for SUPER (root) (NO add to tree since parent==null)
//            TypeMetadata rootTypeMetadata = new TypeMetadata();
//            rootTypeMetadata.setName(TYPESYSTEM_ROOT_NAME);
//            rootTypeMetadata.setShortName(TypeMetadata.getMyShortName(TYPESYSTEM_ROOT_NAME));
//            rootTypeMetadata.setSuperTypeName(null);
//            rootTypeMetadata.setSuperTypeShortName(null);        
            TypeDescription rootTypeDescr = createTypeDescription (TYPESYSTEM_ROOT_NAME, null);
            
            // Create Root Node for "SUPER"
            _rootSuper = new TypeNode();
            _rootSuper.setLabel(rootTypeDescr.getName());
            _rootSuper.setObjectType(IItemTypeConstants.ITEM_TYPE_TYPE);
            _rootSuper.setObject(rootTypeDescr);         
        } else {
			_rootSuper = root;
		}
        _nodesHashtable.put (_rootSuper.getLabel(), _rootSuper);
        
        _totalOfBuiltInTypes = getBuiltInTypesFromUIMATypeSystem ();
        _containmentKey = "supertypeName";
		buildTypeSystemHierarchy (typeMetadataList);	   
        
	} // TypeTree
    
	public static TypeTree createTypeTreeFromXmlDescriptor ( String mainXmlDescriptionFileName, ResourceManager rm )
	{
	  // Create TypeSystemDescription from NEW Xml File
	  TypeSystemDescription typeSystemDesc = null;
    try {
        typeSystemDesc = TypeSystemUtils2.getTypeSystemDescription (mainXmlDescriptionFileName, rm);
    } catch (InvalidXMLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (ResourceInitializationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
      if (typeSystemDesc == null) {
        return null;
      }
      return createTypeTreeFromTypeSystemDescription(typeSystemDesc);
    }
      
	public static TypeTree createTypeTreeFromTypeSystemDescription (TypeSystemDescription typeSystemDesc)
	{
	  TypeDescription t[] = typeSystemDesc.getTypes();
	  List<TypeDescription> list = new ArrayList<TypeDescription>(t.length);
	  for (int i=0; i<t.length; ++i) {
	      if ( !select(t[i].getName()) ) {
	          continue;
	      }
	      list.add(t[i]);
	  }
	  return new TypeTree(list);
	} // createTypeTreeFromTypeSystemDescription

    /**
     * Filter unwanted type names
     * 
     * @param typeName
     * @return boolean True if selected (to be included in tree), otherwise false
     * 
     * TODO Create a static table for unwanted types
     */
	@Deprecated
    static private boolean select (String typeName) {
        if (typeName.equals("com.ibm.uima.examples.SourceDocumentInformation")
            || typeName.equals("uima.tcas.DocumentAnnotation") ) {
            return false;
        }
        return true;
    }
    
    /*************************************************************************/
	
    public static void main(String[] args) {
      
      TypeTree tree = createTypeTreeFromXmlDescriptor(args[0], null);
      TypeNode root = tree.getRoot();     
      tree.dumpTree(root);
      
      // System.out.println("====================");
      // UimaTypeTree uTree = UimaTypeUtil.createUimaTypeTreeFromXmlDescriptor(args[0], null);
      // uTree.dumpTree(uTree.getRoot());
    }

    static public TypeDescription createTypeDescription (String name, String superName) {
      ResourceSpecifierFactory rsFactory = UIMAFramework.getResourceSpecifierFactory();
      TypeDescription t = rsFactory.createTypeDescription();
      t.setName(name);
      t.setSupertypeName(superName);
      return t;
    }
    /************************************************************************/
    
    public static TypeSystem getUIMABuiltInTypeSystem ()
    {
        TypeSystemDescription       typeSystemDesc = null;
        CAS tcas = null;
        try {
            tcas = CasCreationUtils.createCas(typeSystemDesc, null, new FsIndexDescription[0]);
            TypeSystem typeSystem = tcas.getTypeSystem();
            return typeSystem;
        } catch (ResourceInitializationException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    
    public static List<TypeDescription> getBuiltInTypeDescriptions ()
    {
        TypeSystem typeSystem = getUIMABuiltInTypeSystem ();
        if (typeSystem == null) {
          return null;
        }
//        TypeSystemDescription ts = TypeSystemUtil.typeSystem2TypeSystemDescription(typeSystem);
//        return ts.getTypes();       

        List<TypeDescription> typeMetadataList = new ArrayList<TypeDescription>();
        TypeSystemUtils2.createTypeDescriptionListFromUIMATypeSystem (typeMetadataList, 
                typeSystem, typeSystem.getTopType());
        // return (TypeDescription[]) typeMetadataList.toArray(new TypeDescription[typeMetadataList.size()]);
        return typeMetadataList;
    } // getBuiltInTypeDescriptions   

    protected int getBuiltInTypesFromUIMATypeSystem ()
    {
      List<TypeDescription> ts = getBuiltInTypeDescriptions ();
      // Trace.err("BuiltInTypes: " + ts.length);
        for (TypeDescription t: ts) {
            _builtInTypesHashtable.put (t.getName(), t);
            // Trace.err(ts[j].getName());
       }
        
       return ts.size();
    }

    public TypeDescription getBuiltInType (String typeName)
    {       
        return (TypeDescription) _builtInTypesHashtable.get(typeName);
    }
    
    // Used by rcp
    public boolean isBuiltInType (String typeName)
    {       
        return (_builtInTypesHashtable.get(typeName)!=null ? true:false);
    }
    
	///////////////////////////////////////////////////////////////////////////
	
	private void buildTypeSystemHierarchy (List<TypeDescription> list)
	{
		if ( list == null ) {
            return;
        }
        TypeDescription typeObj = null;
			
			// Build Type Tree from "Parent-Children Relationship"
			// Create New Type System Tree
			for (int j = 0 ; j<list.size() ; ++j) {
				typeObj = list.get(j);
                // @TODO Need to optimize by using "addNode()" (no verification
				// addNode (typeObj.getSuperTypeName(), typeObj);
                addTypeWithValidation (typeObj, false, false);
				// System.out.println("[buildTypeSystemHierarchy] addNode # " + j + " - " + typeObj.getParentName() + " <- " + typeObj.getName());
			}
	} // buildTypeSystemHierarchy

    /*************************************************************************/
    /*                          Import Type Systems                          */
    /*************************************************************************/            
    
//    public List getImportedTypeSystems () {
//        return _importedTypeSystems;
//    }
        
    /*************************************************************************/
    /*                          Get/Query SUPER Root Node                    */
    /*************************************************************************/    
    
    public TypeNode getRoot () {
        return _rootSuper;
    }
    
    public static String getTypeSystemRootName () {
        return TYPESYSTEM_ROOT_NAME;
    }
    
    public static String getTypeSystemRootShortName () {
        return TYPESYSTEM_ROOT_SHORT_NAME;
    }

    public static boolean isTypeSystemRootName (String name) {
        return TYPESYSTEM_ROOT_NAME.equalsIgnoreCase(name);  
    }

    public static boolean isTypeSystemRootShortName (String name) {
        return TYPESYSTEM_ROOT_SHORT_NAME.equalsIgnoreCase(name);  
    }
    
    /*************************************************************************/
    /*                          Get Node/Type /Feature                       */
    /*************************************************************************/

    public TypeNode getTypeNode (String typeName) {
        return (TypeNode) _nodesHashtable.get(typeName);
    }
    
    /************************************************************************/
    
    /**
     *      Add type to type system.
     *      The "added" type is either a new type or the type of "forwarded"
     *      reference type. 
     * 
     * @return null if duplicated type or error. Otherwise, the node of the added type.
     *
     */
    private TypeNode addNode (String parentName, TypeDescription childTypeMetadata) 
    {	
        // Find "parent" node
        if (parentName != null && parentName.trim().length() == 0) {
            parentName = null;
        }
        TypeNode nodeParent = null;
        if (parentName != null) {
            nodeParent = (TypeNode) _nodesHashtable.get(parentName);
        }
        
        // Find "child" node
        String childName = childTypeMetadata.getName();
    	TypeNode node = (TypeNode) _nodesHashtable.get(childName);

    	// System.err.println(" parentName: " + parentName + " ; childName: " + childName);
    
        // NEW type definition ?
    	if ( node == null ) {		
    		// Not found "child". NEW type definition.
    		if ( nodeParent == null ) {
                // Parent is NEW
                // TOP has null parent
                if (parentName != null) {
                  TypeDescription typeParent = getBuiltInType(parentName);
                    if (typeParent != null) {
                        // Built-in Type as Parent
                        nodeParent = addNode (typeParent.getSupertypeName(), typeParent);
                        // Trace.trace(" -- addNode: " + childName + " has New Built-in Parent: " + parentName);
                    } else {                    
            			// NEW parent also.
                        // "parentName" is FORWARD Reference Node
            			nodeParent = insertForwardedReferenceNode(_rootSuper, parentName);  
                        // Trace.trace(" -- addNode: " + childName + " has New FORWARD Parent: " + parentName);
                    }
                }
    		}
    		// System.out.println(" -- addNode: New child");		
    		return insertNewNode (nodeParent, childTypeMetadata);
    	}
    	
        //
        // childTypeMetadata is ALREADY in Hierarchy
        //
        
    	// This node can be a Forwarded Reference type
    	if (node.getObject() == null) {
    		// Set Object for type definition
    		// Reset label.
    		// Trace.trace("Update and define previously forwarded reference type: "
    		//		+ node.getLabel() + " -> " + parentName);
            // Need to "remove" and "put" back for TreeMap (no modification is allowed ?)
            node.getParent().removeChild(node);
    		node.setObject(childTypeMetadata);
            node.setLabel(childTypeMetadata.getName());
            node.getParent().addChild(node);
            
            // Remove from undefined types
            // Trace.trace("Remove forward ref: " + childTypeMetadata.getName());
            _undefinedTypesHashtable.remove(childTypeMetadata.getName());
    	}
    	
    	if (parentName == null) {
    		// NO Parent
            if (childTypeMetadata.getName().compareTo(UIMA_CAS_TOP) != 0) {
                Trace.err("??? Possible BUG ? parentName==null for type: "
                        + childTypeMetadata.getName());
            }
    		return node;
    	}
    	
    	// Found "child".
    	// This node can be the "parent" of some nodes 
    	// and it own parent is "root" OR node of "parentName".
    	if ( node.getParent() == _rootSuper ) {
    		// Current parent is SUPER which may not be the right parent
    		// if "node" has a previously forward referenced parent of some type.
    		// Find the real "parent".
    		if ( nodeParent != null ) {
    		    // Parent node exists. 
    			if ( nodeParent != _rootSuper ) {
    			    // Move "node" from "root" and put it as child of "nodeParent"
    				// Also, remove "node" from "root"
    			    // _rootSuper.getChildren().remove(node.getLabel());
                    // System.out.println("B remove");
    			    if (_rootSuper.removeChild(node) == null) {
                        System.out.println("??? [.addNode] Possible BUG 1 ? cannot remove "
                                + node.getLabel() + " from SUPER");
                        System.out.println("  node: " + ((Object)node).toString());  
                        Object[] objects = _rootSuper.getChildrenArray();
                        for (int i=0; i<objects.length; ++i) {
                            System.out.println("    " + objects[i].toString());              
                        }                  
                    }
                    // System.out.println("E remove");
    			} else {
    				// "nodeParent" is "SUPER".
    				return node;
    			}
    		} else {
    			// NEW parent
				// Remove "node" from "SUPER" and insert it as child of "nodeParent"
			    // _rootSuper.getChildren().remove(node);
                if (_rootSuper.removeChild(node) == null) {
                    System.err.println("??? [.addNode] Possible BUG 2 ? cannot remove "
                            + node.getLabel() + " from SUPER");
                }			    
                TypeDescription typeParent = getBuiltInType(parentName);
                if (typeParent != null) {
                    // Built-in Type as Parent
                    nodeParent = addNode (typeParent.getSupertypeName(), typeParent);
                    // Trace.trace(" -- addNode 2: " + childName + " has New Built-in Parent: " + parentName);
                } else {                
    			    // It is a NEW parent and this parent is forwarded reference (not defined yet). 
    			    // Insert this parent as child of "SUPER"
    				nodeParent = insertForwardedReferenceNode(_rootSuper, parentName);
                    // Trace.trace(" -- addNode 2: " + childName + " has New FORWARD Parent: " + parentName);
                }
    		}				
    		TypeNode tempNode;
    		if ( (tempNode = nodeParent.insertChild(node)) != null && tempNode != node) {
    			// Duplicate Label
                Trace.err("Duplicate Label 1");
//    			node.setShowFullName(true);
    			if (node.getObject() != null) {
    				node.setLabel(((TypeDescription)node.getObject()).getName());
    			}				
    			nodeParent.insertChild(node);
    		}
    	} else if ( node.getParent() != null ) {
    	    //
    	    //	ERROR !!!
    		//  "duplicate definition" or "have different parents"
    	    //
    	    
    	    // "nodeParent" should be non-null and be the same as "node.getParent"
    	    // In this case, it is duplicate definition
    	    if ( nodeParent != null ) {
    	        if (nodeParent == node.getParent()) {
    				// Error in descriptor
    		        // Duplicate definition
    				// System.err.println("[TypeSystemHierarchy - addNode] Duplicate type: child=" + childName + " ; parent =" + parentName);
    	        } else {
    	            // Error: "node" has different parents
    	            // Both parents are registered
    				System.err.println("[TypeSystemHierarchy - addNode] Different registered parents: child=" + childName 
    				        + " ; old node.getParent() =" + node.getParent().getLabel()
    				        + " ; new parent =" + parentName);
    	        }
    	    } else {
    	        // Error 
                // Error: "node" has different parents
                // Old parent is registered
    	        // New parent is NOT registered
    			System.err.println("[TypeSystemHierarchy - addNode] Different parents: child=" + childName 
    			        + " ; old registered node.getParent() =" + node.getParent().getLabel()
    			        + " ; new NON-registered parent =" + parentName);
    	    }
    	    return null;   // ERROR
    
    	} else {
    		//
    	    // Program BUG !!!
    		// since Parent of "registered" node cannot be null.
            // if (childTypeMetadata.getName().compareTo(UIMA_CAS_TOP) != 0) {
                System.err.println("[TypeSystemHierarchy - addNode] Program BUG !!! (node.getParent() == null): child=" + childName + " ; parent =" + parentName);
                return null;
            // }
    	}
    		
    	return node;
    } // addNode
            		
    public int addTypesToHierarchy (ArrayList typeMetadataList)
    {
        buildTypeSystemHierarchy(typeMetadataList);
        // this.dumpTree (getRoot());
        return 0;
    } // addTypesToHierarchy
    
	
	/************************************************************************/
	
	//
    //  "typeName" is not yet defined and it is forward refernced by other types
    //
    // Note:    The caller of this method ALREADY checked that this forward ref 
    //          IS NOT in tree.
	// Note:	Node can be inserted without TypeDescription object.
	//			For example, some types may have super-type that is
	//			not defined yet.
    protected TypeNode insertForwardedReferenceNode (TypeNode parent, String typeName) 
    {
        TypeNode node = null;
        
		if ( typeName == null || typeName.trim().length() == 0 ) return null;

        // Same type may be forwarded ref MULTIPLE TIMES
        node = (TypeNode) _nodesHashtable.get(typeName);
        if (node != null) {
            // BUG !!! This FORWARD reference is already in tree since the caller
            // of this method ALREADY checked that this forward ref IS NOT in tree.
            
          TypeDescription meta = (TypeDescription) ((TypeNode)_nodesHashtable.get(typeName)).getObject();
            if ( meta != null ) {
                // Already defined
                node.setLabel(TypeSystemUtils2.getMyShortName(typeName)); // Use short name
                node.setObject(meta);
                _nodesHashtable.put (((TypeDescription)node.getObject()).getName(), node);
                Trace.err("??? BUG -- Already defined for " + typeName);
            }
        } else {
            // NEW forwarded ref
            node = new TypeNode(parent, UimaToolsUtil.getMyShortName(typeName));
            node.setObjectType(IItemTypeConstants.ITEM_TYPE_TYPE);            
            // Not yet defined (Forward reference)
            // Cannot use short name if no TypeDescription object (see TreeBaseNode.compare)
            // Trace.trace("Forward reference to " + typeName);
            // _nodesHashtable.put (node.getLabel(), node);
            _nodesHashtable.put (typeName, node);
            
            // Add to undefined type hashtable
            // _undefinedTypesHashtable.put (node.getLabel(), node);
            _undefinedTypesHashtable.put (typeName, node);
        }
        		
		// Insert "node" as child of "parent"
		TypeNode tempNode;
		if (parent == null) {
		    if (_rootSuper == null) {
		        _rootSuper = node;
		    } else {
				if ( (tempNode = _rootSuper.insertChild(node)) != null ) {
					if (tempNode != node) {				 
						// Duplicate Label
                        // Use full name as label
						Trace.trace(" 1 Duplicate (short name) Label:" + node.getLabel());
//						node.setShowFullName(true);
						if (node.getObject() != null) {
                            // Use full name as label
							node.setLabel(((TypeDescription)node.getObject()).getName());
						}				
						_rootSuper.insertChild(node);
					} else {
						
					}
				}
		    }
		} else {
			// parent.insertChild (node);
			if ( (tempNode = parent.insertChild(node)) != null && tempNode != node) {
				// Duplicate Label. Use full name as label.
                Trace.trace(" 2 Duplicate (short name) Label:" + node.getLabel());
//				node.setShowFullName(true);
				if (node.getObject() != null) {
                    // Use full name as label
					node.setLabel(((TypeDescription)node.getObject()).getName());
				}				
				parent.insertChild(node);
			}
		}
		
		return node;
	} // insertForwardedReferenceNode

    /**
     * 
     * 
     * @param parent
     * @param typeDescription
     * @return
     * @return TreeBaseNode
     * 
     * Note: Undefined range types will be add to undefined type hashtable
     */
	private TypeNode insertNewNode (TypeNode parent, TypeDescription typeDescription) 
    {
        if ( typeDescription.getName().trim().length() == 0 ) return null;
        
        TypeNode node = new TypeNode(parent, UimaToolsUtil.getMyShortName(typeDescription.getName()),
                IItemTypeConstants.ITEM_TYPE_TYPE, typeDescription);
        _nodesHashtable.put (typeDescription.getName(), node);
        
//        if ( _undefinedTypesHashtable.containsKey(typeMetadata.getName()) ) {
//            Trace.trace(" Remove forward ref:" + typeMetadata.getName());
//        }
        _undefinedTypesHashtable.remove (typeDescription.getName());
        
        // Collect undefined range types
        String rangeType = null;
//        FeatureMetadata feature;
//        for (int i=0; i<typeMetadata.getfeatureListSize(); ++i) {
//            feature = ((FeatureMetadata) typeMetadata.getFeatureList().get(i));
//            rangeType = feature.getRangeTypeName();
//            if (_nodesHashtable.get(rangeType) == null && getBuiltInType(rangeType) == null) {
//                // Have undefined range-type that is not a built-in type
//                // Trace.trace(" Forward range-type ref:" + rangeType);
//                _undefinedTypesHashtable.put(rangeType, node);
//                feature.setValidityAttribut(ItemAttributes.STATUS_INVALID_RANGETYPE);
//            } else {
//                // set status of feature to valid
//                feature.setValidityAttribut(ItemAttributes.STATUS_VALID);
//            }            
//        }
                
        // Insert "node" as child of "parent"
        TypeNode tempNode;
        if (parent == null) {
            if (_rootSuper == null) {
                _rootSuper = node;
            } else {
                // _rootSuper.insertChild (node);
                if ( (tempNode = _rootSuper.insertChild(node)) != null && tempNode != node) {
                    // Duplicate Label
                    System.out.println("[InsertNode] 1 Duplicate (short name) Label:" + node.getLabel());
//                    node.setShowFullName(true);
                    if (node.getObject() != null) {
                        node.setLabel(((TypeDescription)node.getObject()).getName());
                    }               
                    _rootSuper.insertChild(node);
                }
            }
        } else {
            if ( (tempNode = parent.insertChild(node)) != null && tempNode != node) {
                // Duplicate Label
                System.out.println("[InsertNode] 2 Duplicate (short name) Label:" + node.getLabel());
//                node.setShowFullName(true);
                if (node.getObject() != null) {
                    node.setLabel(((TypeDescription)node.getObject()).getName());
                }               
                parent.insertChild(node);
            }
        }
        
        return node;
    } // insertNewNode

    
    /*************************************************************************/
    
    private String getNameFromNode (TypeNode typeNode) 
    {
        if (typeNode.getObject() == null) {
            return typeNode.getLabel();
        } else {
            return ((TypeDescription) typeNode.getObject()).getName();
        }
    } // getNameFromNode
    
    private String getSupertypeNameFromNode (TypeNode typeNode) 
    {
        String supertypeName;
        
        if (typeNode.getObject() == null) {
            // Forwarded reference type
            TypeNode parentNode = (TypeNode) typeNode.getParent();
            if (typeNode.getObject() == null) {
                // Forwarded reference type
                supertypeName = parentNode.getLabel();
            } else {
                supertypeName = ((TypeDescription) parentNode.getObject()).getName();
            }
        } else {
            supertypeName = getNameFromNode((TypeNode)typeNode.getParent());
        }
        
        return supertypeName;
    } // getSupertypeNameFromNode
        
    // Used by rcp
    public void addTypeWithValidation (TypeDescription addedType, 
                                        boolean validate, boolean refresh)
    {
        // Added type to hierarchy
        // TreeBaseNode addedTypeNode = addNode (addedType.getSuperTypeName(), addedType);
        TypeNode addedTypeNode = addNode (addedType.getSupertypeName(), addedType);
        if (addedTypeNode == null) {
            return;
        }
         
    } // addTypeWithValidation
    
	/*************************************************************************/
    
	public void dumpNode (TypeNode fromNode, int level) {
		TypeNode    node;
		
		for (int i=0 ; i<level ; ++i) {
			System.out.print("---- ");
		}
		// System.out.println (nodeFrom.getLabel());
		
		if ( fromNode.getObject() instanceof TypeDescription ) {
            // Print # Features
          TypeDescription type = (TypeDescription) fromNode.getObject();
          AllowedValue[] av = type.getAllowedValues();
          if (av == null || av.length == 0) {
		    if ( type.getFeatures() != null ) {
                String info;
                info = " features)";
		        System.out.println (fromNode.getLabel() + " (" +
		        		+ type.getFeatures().length 
		        		+ info);
		    } else {
                System.out.println (fromNode.getLabel());
		    }
          }
		} else {
		    if ( fromNode.getObject() == null ) {
    	        String errorText = "??? [dumpNode] Node " + fromNode.getLabel() + " doesn't have associated object (fromNode.getObject() == null)";
			    if ( fromNode.getParent() != null ) {
			        errorText += " and Parent: " + fromNode.getParent().getLabel();
			    }
			    System.out.println (errorText);
		    } else {
		        System.out.println("??? Unknown Type: " + fromNode.getObject().getClass().getName());
		    }

		}
		if ( fromNode.getChildren() == null ) {
			return;
		}
		
		Iterator iter = fromNode.getChildrenIterator();
		while (iter.hasNext()) {
			node = (TypeNode) iter.next();
			dumpNode (node, level+1);
		}
	} // dumpNode
	
	public void dumpTree (TypeNode root) {
		
		dumpNode (root, 0);
		
	} // dumpTree
	
	private static int _count=0;
	private String getName () {
		_count++;
		return new String("a"+_count);
	}
	
    // Used by filter ()
	private String arrayNames[] = { "ArrayBase", "Float", "Integer", 
									"ListBase", // "String", 
			"uima.cas.ArrayBase", "uima.cas.Float", "uima.cas.Integer", 
			"uima.cas.ListBase", "uima.cas.String",
			"FSList", "FloatList", "IntegerList", "StringList",
            "SofA", "uima.cas.SofA"
	};
	/************************************************************************/
	int _countNode = 0;
	
	private String genXmlNodeName () {
		_countNode++;
		return new String("a"+_countNode);
	}
	
	String indent (int level) {
		String text = "";
		for (int i=0 ; i<level ; ++i) {
			text += "     ";
		}
		
		return text;
	}
	
	public boolean filter (String name) {
		boolean skip = false;

		for (int i=0 ; i<arrayNames.length ; ++i) {
			if ( name.compareToIgnoreCase(arrayNames[i]) == 0 ) {
				skip = true;
				// System.out.println("skip: " + name);
			}
		}
		
		return skip;
	} // filter
	
    /*************************************************************************/
    
    private void traverseTreeWithRemoveOption (ArrayList list, TypeNode fromNode, 
            						boolean doRemove, int level) 
    {
    	TypeNode    node;
    	
    	if ( fromNode.getObject() instanceof TypeDescription ) {
    	    // Add to list
    	    list.add(fromNode.getObject());    	    
    	} else {
    		// Error
    	    if ( fromNode.getObject() == null ) {
    	        String errorText = "??? [traverseTreeWithRemoveOption] Node " + fromNode.getLabel() + " WITHOUT associated object (fromNode.getObject() == null)";
    		    if ( fromNode.getParent() != null ) {
    		        errorText += " and Parent: " + fromNode.getParent().getLabel();
    		    }
    		    System.out.println (errorText);
    	    } else {
    	        System.out.println("??? [traverseTreeWithRemoveOption] Unknown node's Type: " + fromNode.getObject().getClass().getName());
    	    }   
    	}
    	
    	if (doRemove) {
    	    // Remove from its parent
    	    if (fromNode.getParent() != null) {
    	        // fromNode.getParent().getChildren().remove(fromNode.getLabel());
    	        fromNode.getParent().removeChild(fromNode);
    	    }
    	    // _nodesHashtable.remove(fromNode.getLabel());
    	    _nodesHashtable.remove(((TypeDescription)fromNode.getObject()).getName());
    	}
    	
    	if ( fromNode.getChildren() == null ) {
    		return;
    	}
    	
    	Iterator iter = fromNode.getChildrenIterator();
    	while (iter.hasNext()) {
    		node = (TypeNode) iter.next();
    		traverseTreeWithRemoveOption (list, node, doRemove, level+1);
    	}
    } // traverseTreeWithRemoveOption
		
    /**
     *  
     * @return ArrayList of TypeDescription
     */
    private void traverseTreeAndCollectObjects (List<TypeDescription> list, TypeNode fromNode, 
                                    boolean includeUIMABuiltInTypes) 
    {
        TypeNode    node;
        
        if ( fromNode.getObject() instanceof TypeDescription ) {
            // Add to list
            if (includeUIMABuiltInTypes 
                    || getBuiltInType (((TypeDescription) fromNode.getObject()).getName()) == null) {
                list.add((TypeDescription) fromNode.getObject());         
            }
        } else {
            // Error
            if ( fromNode.getObject() == null ) {
                String errorText = "??? Node " + fromNode.getLabel() + " WITHOUT associated object (fromNode.getObject() == null)";
                if ( fromNode.getParent() != null ) {
                    errorText += " and Parent: " + fromNode.getParent().getLabel();
                }
                Trace.out (errorText);
            } else {
                Trace.out("??? Unknown node's Type: " + fromNode.getObject().getClass().getName());
            }   
        }        
        
        if ( fromNode.getChildren() == null ) {
            return;
        }
        
        Iterator<TypeNode> iter = fromNode.getChildrenIterator();
        while (iter.hasNext()) {
            node = iter.next();
            traverseTreeAndCollectObjects (list, node, includeUIMABuiltInTypes);
        }
    } // traverseTreeAndCollectItems
            
    /**
     *  
     * @return ArrayList of TreeBaseNode
     */
    private void traverseTreeAndCollectNodes (List<TypeNode> list, TypeNode fromNode, 
                                    boolean includeUIMABuiltInTypes) 
    {
        TypeNode    node;
        
        if ( fromNode.getObject() instanceof TypeDescription ) {
            // Add to list
            if (includeUIMABuiltInTypes 
                    || getBuiltInType (((TypeDescription) fromNode.getObject()).getName()) == null) {
                list.add(fromNode);         
            }
        } else {
            // Error
            if ( fromNode.getObject() == null ) {
                String errorText = "??? Node " + fromNode.getLabel() + " WITHOUT associated object (fromNode.getObject() == null)";
                if ( fromNode.getParent() != null ) {
                    errorText += " and Parent: " + fromNode.getParent().getLabel();
                }
                Trace.out (errorText);
            } else {
                Trace.out("??? Unknown node's Type: " + fromNode.getObject().getClass().getName());
            }   
        }        
        
        if ( fromNode.getChildren() == null ) {
            return;
        }
        
        Iterator<TypeNode> iter = fromNode.getChildrenIterator();
        while (iter.hasNext()) {
            node = iter.next();
            traverseTreeAndCollectNodes (list, node, includeUIMABuiltInTypes);
        }
    } // traverseTreeAndCollectNodes
    
    /*************************************************************************/

	public ArrayList getInheritedFeatures (TypeDescription typeMetadata)
	{	    
        // Find node of typeMetadata
        TypeNode typeNode = (TypeNode) _nodesHashtable.get(typeMetadata.getName());
        if (typeNode == null) {
            return null;
        }
        
        if (typeNode.getObjectType() == IItemTypeConstants.ITEM_TYPE_LABEL_FEATURES) {
            // Skip Lable Feature Node
            typeNode = (TypeNode) typeNode.getParent();
        }
        ArrayList _inheritedFeatures = new ArrayList();
        TypeNode typeParentNode = (TypeNode) typeNode.getParent();
        while (typeParentNode != null) {
            if (typeParentNode.getObjectType() == IItemTypeConstants.ITEM_TYPE_TYPE) {
                typeMetadata = (TypeDescription)typeParentNode.getObject();
                FeatureDescription[] features = typeMetadata.getFeatures();
                if (typeMetadata != null && features.length > 0) {
                    for (int j=0 ; j<features.length; ++j) {
                        _inheritedFeatures.add(features[j]);
                    }                   
                } 
            }
            typeParentNode = (TypeNode) typeParentNode.getParent();
        }        

		return _inheritedFeatures;
	} // getInheritedFeatures

    public List<TypeNode> getNodeListFromHierarchy (boolean includeUIMABuiltInTypes, boolean sortByShortName)
    {       
        return getNodeListFromHierarchy (getRoot(), includeUIMABuiltInTypes, sortByShortName);
    } // getNodeListFromHierarchy   

    private List<TypeNode> getNodeListFromHierarchy (TypeNode fromNode, 
            boolean includeUIMABuiltInTypes, boolean sortByShortName)
    {
        List<TypeNode>       typeNodeList = new ArrayList<TypeNode>();
        traverseTreeAndCollectNodes (typeNodeList, fromNode, includeUIMABuiltInTypes);

        if (typeNodeList.size() > 0) {
            sortTypeNodeList (typeNodeList, sortByShortName);
        }
        
        return typeNodeList;
    } // getNodeListFromHierarchy
    
    public List<TypeDescription> getTypeListFromHierarchy (boolean includeUIMABuiltInTypes, boolean sortByShortName)
    {       
        return getTypeListFromHierarchy (getRoot(), includeUIMABuiltInTypes, sortByShortName);
    } // getTypeMetadataListFromHierarchy   
    
    private List<TypeDescription> getTypeListFromHierarchy (TypeNode fromNode, 
                boolean includeUIMABuiltInTypes, boolean sortByShortName)
    {
        List<TypeDescription> typedescrList = new ArrayList();
        traverseTreeAndCollectObjects (typedescrList, fromNode, false);
        
        // Include UIMA Built-in Types ?
        if (includeUIMABuiltInTypes) {
            List<TypeDescription> uimaTypes = getBuiltInTypeDescriptions();
            typedescrList.addAll(uimaTypes);
        }
        if (typedescrList.size() > 0) {
            sortTypeList (typedescrList, sortByShortName);
        }
        
        return typedescrList;
    } // getTypeListFromHierarchy

    /*************************************************************************/
	/*                         Helpers: SORTING                              */
    /*************************************************************************/
	
	public class MyComparator implements Comparator {
	    boolean byShortName = false;
	    
		public MyComparator(boolean byShortName) {
		    this.byShortName = byShortName;
		}
		
		public int compare(Object d1, Object d2) {
		    TypeDescription type1;
		    TypeDescription type2;
		    if ( d1 instanceof TypeDescription ) {
		        type1 = (TypeDescription) d1;
		        type2 = (TypeDescription) d2;
		    } else {
		        type1 = (TypeDescription) ((TypeNode) d1).getObject();
		        type2 = (TypeDescription) ((TypeNode) d2).getObject();
		    }
		    
            if ( byShortName ) {
                return TypeSystemUtils2.getMyShortName(type1.getName())
                            .compareTo(TypeSystemUtils2.getMyShortName(type2.getName()));
            } else {
                return type1.getName().compareTo(type2.getName());
            }
		}
	}
	
	public List<TypeDescription> sortTypeList (List<TypeDescription> typeDescrList, 
	        				boolean byShortName) {
		// Sort
		if ( typeDescrList.size() > 1 ) {
		    Collections.sort(typeDescrList, new MyComparator(byShortName));
		    return typeDescrList;
		}
	    return null;
	}

	public List<TypeNode> sortTypeNodeList (List<TypeNode> typeNodeList, 
	        boolean byShortName) {
	    if ( typeNodeList.size() > 1 ) {
	        Collections.sort(typeNodeList, new MyComparator(byShortName));
	        return typeNodeList;
	    }
	    return null;
	}
	
	/************************************************************************/
	/*						Generate Xml Descriptor							*/
	/************************************************************************/
	private static void printlnString (PrintWriter out, String text, int level) {
	    for (int i=0 ; i<level ; ++i) {
	        out.print("    ");
	    }
	    out.println(text);
	}
	
	/************************************************************************/

    
} // TypeTree
