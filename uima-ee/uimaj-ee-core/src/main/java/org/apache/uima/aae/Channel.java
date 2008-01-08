package org.apache.uima.aae;

public interface Channel
{
	public void stop() throws Exception;
	public String getName();

	static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
