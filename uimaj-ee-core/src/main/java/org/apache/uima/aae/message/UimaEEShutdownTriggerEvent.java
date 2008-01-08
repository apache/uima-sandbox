/**
 * 
 */
package org.apache.uima.aae.message;

import org.apache.uima.aae.controller.AnalysisEngineController;
import org.springframework.context.ApplicationEvent;

/**
 *
 */
public class UimaEEShutdownTriggerEvent extends ApplicationEvent
{
    private AnalysisEngineController targetController = null;
	/**
	 * @param source
	 */
	public UimaEEShutdownTriggerEvent(Object source)
	{
		super(source);
	}
	public AnalysisEngineController getTargetController()
	{
		return targetController;
	}
	public void setTargetController(AnalysisEngineController targetController)
	{
		this.targetController = targetController;
	}

	
}
