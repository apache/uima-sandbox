package org.apache.uima.aae.deployment;

public interface ProcessCasErrors 
{
    final static int        KIND_THRESHOLD_COUNT    = 1;
    final static int        KIND_THRESHOLD_WINDOW   = 2;
    final static int        KIND_THRESHOLD_ACTION   = 3;
    final static int        KIND_TIMEOUT            = 4;
    final static int        KIND_MAX_RETRIES        = 5;
    final static int        KIND_CONTINUE_ON_RETRY  = 6;

    public ProcessCasErrors clone(AsyncAEErrorConfiguration parent);
    
    public AsyncAEErrorConfiguration getParent();

    public void setValueById (int id, Object value);

    /**
     * @return the continueOnRetryFailure
     */
    public boolean isContinueOnRetryFailure();

    /**
     * @param continueOnRetryFailure the continueOnRetryFailure to set
     */
    public void setContinueOnRetryFailure(boolean continueOnRetryFailure);

    /**
     * @return the maxRetries
     */
    public int getMaxRetries();

    /**
     * @param maxRetries the maxRetries to set
     */
    public void setMaxRetries(int maxRetries);

    /**
     * @return the thresholdAction
     */
    public String getThresholdAction();

    /**
     * @param thresholdAction the thresholdAction to set
     */
    public void setThresholdAction(String thresholdAction);

    /**
     * @return the thresholdCount
     */
    public int getThresholdCount();

    /**
     * @param thresholdCount the thresholdCount to set
     */
    public void setThresholdCount(int thresholdCount);

    /**
     * @return the thresholdWindow
     */
    public int getThresholdWindow();

    /**
     * @param thresholdWindow the thresholdWindow to set
     */
    public void setThresholdWindow(int thresholdWindow);

    /**
     * @return the timeout
     */
    public int getTimeout();

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout);

}