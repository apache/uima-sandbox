/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.simpleserver.config;

import org.apache.uima.InternationalizedException;

/**
 * Simple server exception class.
 * <p>
 * 
 * Message keys are defined as public string constants. Note: to know how many arguments a message
 * takes, you need to look at the message properties file. This information is not captured in this
 * class.
 */
public class SimpleServerException extends InternationalizedException {

  public static final String incorrect_path_syntax = "incorrect_path_syntax";

  public static final String incorrect_feature_syntax = "incorrect_feature_syntax";

  private static final String messages = "org.apache.uima.simpleserver.exception";

  /**
   * Constructor for creating exception from scratch.
   * 
   * @param messageKey
   *                The message key. Use one of the constants defined in this class.
   * @param arguments
   *                An array of message arguments. Check the message properties file to see how many
   *                and what kind of arguments are expected.
   */
  public SimpleServerException(String messageKey, Object[] arguments) {
    super(messages, messageKey, arguments);
  }

  /**
   * Constructor for creating exception from existing exception (for rethrow).
   * 
   * @param messageKey
   *                The message key. Use one of the constants defined in this class.
   * @param arguments
   *                An array of message arguments. Check the message properties file to see how many
   *                and what kind of arguments are expected.
   * @param cause
   *                Root cause of this exception.
   */
  public SimpleServerException(String messageKey, Object[] arguments, Throwable cause) {
    super(messages, messageKey, arguments, cause);
  }

}
