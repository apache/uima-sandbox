package org.apache.uima.aae.controller;

public class Controller implements ControllerMBean{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private AnalysisEngineController controller;

  public Controller( AnalysisEngineController aController) {
    controller = aController;
  }
  public void completeProcessingAndStop() {
    System.out.println("************> Controller:"+controller.getComponentName()+" JMX MBean Received Stop Command");
    //controller.quiesceAndStop();
  }

  public void stopNow() {
    controller.terminate();
  }
  
}