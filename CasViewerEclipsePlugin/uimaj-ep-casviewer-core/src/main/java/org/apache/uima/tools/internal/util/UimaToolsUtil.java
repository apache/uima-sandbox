package org.apache.uima.tools.internal.util;

public class UimaToolsUtil {

    /*************************************************************************/

    // Helper
    static public String getMyShortName (String fullName) {
        if (fullName == null) return null;
        
        int index = fullName.lastIndexOf('.');
        if ( index < 0 ) {
            return fullName;
        } else {
            return fullName.substring(index+1);
        }
    }
    
    /**
     * Remove name space from string (e.g., remove "ns:" from "ns:myname")
     * 
     * @param fullName
     * @return String
     */
    static public String removeNamespace (String fullName) {
        return fullName.substring(fullName.indexOf(':')+1);
    }
    

}
