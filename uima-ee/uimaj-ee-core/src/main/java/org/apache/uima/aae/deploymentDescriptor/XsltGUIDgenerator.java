/**
 * 
 */
package org.apache.uima.aae.deploymentDescriptor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.server.UID;

/**
 *
 */
public class XsltGUIDgenerator {

  public static String getGUID() {
    String localhost = "unknown_local_host";
    try {
      localhost = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
    }
    return localhost + new UID().toString();
  }

private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
