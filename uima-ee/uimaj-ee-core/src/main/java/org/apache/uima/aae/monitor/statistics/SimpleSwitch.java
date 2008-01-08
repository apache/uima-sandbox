package org.apache.uima.aae.monitor.statistics;

public class SimpleSwitch implements SimpleSwitchMBean
{
	private int state;
	
	public SimpleSwitch() { state = 0;}
	
//	public SimpleSwitch(int aState) { state = aState;}

	public void flip()
	{
		state = (state ==0) ? 1 : 0;
	}

	public int getState()
	{
		return state;
	}


private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
