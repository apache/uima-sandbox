package org.apache.uima.aae.deployment.impl;

public interface InputQueue {

    /**
     * @return the brokerURL
     */
    public String getBrokerURL();

    /**
     * @param brokerURL the brokerURL to set
     */
    public void setBrokerURL(String brokerURL);

    /**
     * @return the endPoint
     */
    public String getEndPoint();

    /**
     * @param endPoint the endPoint to set
     */
    public void setEndPoint(String endPoint);

}