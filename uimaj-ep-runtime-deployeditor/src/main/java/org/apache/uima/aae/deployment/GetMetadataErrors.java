package org.apache.uima.aae.deployment;

public interface GetMetadataErrors 
{
    final static int        KIND_MAX_RETRIES    = 1;
    final static int        KIND_TIMEOUT        = 2;
    final static int        KIND_ERRORACTION    = 3;

    public GetMetadataErrors clone(AsyncAEErrorConfiguration parent);
    
    public AsyncAEErrorConfiguration getParent();
    
    public void setValueById (int id, Object value);
    
    /**
     * @return the errorAction
     */
    public String getErrorAction();

    /**
     * @param errorAction the errorAction to set
     */
    public void setErrorAction(String errorAction);

    /**
     * @return the maxRetries
     */
    public int getMaxRetries();

    /**
     * @param maxRetries the maxRetries to set
     */
    public void setMaxRetries(int maxRetries);

    /**
     * @return the timeout
     */
    public int getTimeout();

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout);

}