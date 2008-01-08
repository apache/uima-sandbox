package org.apache.uima.aae.deployment;

public interface CollectionProcessCompleteErrors 
{
    final static public int        KIND_TIMEOUT                = 1;
    final static public int        KIND_ADDITIONA_ERROR_ACTION = 2;
    
    public CollectionProcessCompleteErrors clone(AsyncAEErrorConfiguration parent);
    
    public AsyncAEErrorConfiguration getParent();

    public void setValueById (int id, Object value);

    /**
     * @return the additionalErrorAction
     */
    public String getAdditionalErrorAction();

    /**
     * @param additionalErrorAction the additionalErrorAction to set
     */
    public void setAdditionalErrorAction(String additionalErrorAction);

    /**
     * @return the timeout
     */
    public int getTimeout();

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout);

}