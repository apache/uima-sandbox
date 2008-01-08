package org.apache.uima.aae;

public interface UimaEEAdminContext
{
	public void shutdown();
	public void stopListener(String anEndpoint );

    static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
