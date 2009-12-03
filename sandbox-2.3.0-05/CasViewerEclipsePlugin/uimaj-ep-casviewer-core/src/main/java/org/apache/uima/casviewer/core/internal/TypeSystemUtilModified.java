package org.apache.uima.casviewer.core.internal;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.util.TypeSystemUtil;

public class TypeSystemUtilModified extends TypeSystemUtil
{

  public static TypeDescription type2TypeDescription(Type aType, TypeSystem aTypeSystem)
  {
      Type superType = aTypeSystem.getParent(aType);
      if (superType == null) {
        return null;
      }
      
      Type stringType = aTypeSystem.getType(CAS.TYPE_NAME_STRING);
      if ( stringType.equals(aType) ) {
          TypeDescription typeDesc = UIMAFramework.getResourceSpecifierFactory().createTypeDescription();
          typeDesc.setName(CAS.TYPE_NAME_STRING);
          typeDesc.setSupertypeName(CAS.TYPE_NAME_TOP);
          return typeDesc;
      }

      return TypeSystemUtil.type2TypeDescription(aType, aTypeSystem);
  }

}
