package org.apache.uima.aae.deployment.impl;

import org.apache.uima.util.InvalidXMLException;

public class DDEInvalidXMLException extends InvalidXMLException 
{
    /**
     * The name of the {@link java.util.ResourceBundle ResourceBundle} containing the standard DDE
     * Exception messages.
     */
    public static final String STANDARD_MESSAGE_CATALOG = "org.apache.uima.aae.deployment.DDEException_Messages";

    /**
     * Message key for a standard UIMA exception message: "Expected an element of type {0}, but found
     * an element of type {1}."
     */
    public static final String INVALID_ELEMENT_TYPE     = "invalid_element_type";
    
    public static final String DELEGATE_KEY_NOT_FOUND   = "delegate_key_not_found";

    
    protected DDEInvalidXMLException() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected DDEInvalidXMLException(String aMessageKey, Object[] aArguments, Throwable aCause) {
        super(aMessageKey, aArguments, aCause);
    }

    public DDEInvalidXMLException(String aMessageKey, Object[] aArguments) {
        super(STANDARD_MESSAGE_CATALOG, aMessageKey, aArguments);
    }

    protected DDEInvalidXMLException(String arg0, String arg1, Object[] arg2, Throwable arg3) {
        super(arg0, arg1, arg2, arg3);
        // TODO Auto-generated constructor stub
    }

    protected DDEInvalidXMLException(String arg0, String arg1, Object[] arg2) {
        super(arg0, arg1, arg2);
        // TODO Auto-generated constructor stub
    }

    protected DDEInvalidXMLException(Throwable arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

}
