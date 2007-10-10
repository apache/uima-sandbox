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
package org.apache.uima.annotator.regex.extension.impl;

import org.apache.uima.annotator.regex.extension.Validation;

/**
 * credit card number validator
 * 
 */
public class CreditCardNumberValidator implements Validation {

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.regex.extension.Validation#validate(java.lang.String,
    *      java.lang.String)
    */
   public boolean validate(String coveredText, String ruleID) throws Exception {

      // cleanup '-' and ' ' signs from the covered text to check the credit
      // card number
      coveredText = coveredText.replaceAll("-", "");
      coveredText = coveredText.replaceAll(" ", "");

      // get character array of credit card number digits
      char[] creditCardCharArray = coveredText.toCharArray();

      // get credit card number length
      int creditCardLength = creditCardCharArray.length;

      // calculate credit card check digit using the Luhn algorithm
      boolean isSecond = false;
      int sum = 0;
      for (int i = creditCardLength - 1; i >= 0; i--) {
         int digit = Character.digit(creditCardCharArray[i], 10);
         if (isSecond) {
            // we have to double the digit
            int doubledDigit = digit * 2;
            // if the digit is greater than 9 we have to compute the crossfoot
            if (doubledDigit > 9) {
               // calculate crossfoot
               int crossFoot = (doubledDigit % 10) + 1;
               // add crossfoot to sum
               sum = sum + crossFoot;
            } else {
               sum = sum + doubledDigit;
            }
         } else {
            sum = sum + digit;
         }
         isSecond = !isSecond;
      }
      // calculate Luhn result
      int result = sum % 10;

      // in case the result == 0 the Luhn algorithm was correct and the credit
      // card number is valid
      if (result == 0) {
         return true;
      }

      return false;
   }
}
