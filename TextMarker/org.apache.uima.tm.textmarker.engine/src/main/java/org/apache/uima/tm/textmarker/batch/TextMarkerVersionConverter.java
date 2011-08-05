/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/

package org.apache.uima.tm.textmarker.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

public class TextMarkerVersionConverter {

  public static void main(String[] args) {
    File dir = new File(args[0]);
    TextMarkerVersionConverter converter = new TextMarkerVersionConverter();
    for (File file : dir.listFiles(new FilenameFilter() {

      public boolean accept(File dir, String name) {
        return !name.endsWith("tm");
      }

    })) {
      File output = new File(file.getParentFile(), file.getName() + ".tm");
      try {
        converter.convertOldToNew(file, output);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void convertOldToNew(File inputFile, File outputFile) throws IOException {
    BufferedReader in = new BufferedReader(new FileReader(inputFile));
    BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));

    String line = null;
    while ((line = in.readLine()) != null) {
      line = line.replaceAll("DUMMY", "Document");
      line = line.replaceAll("CAPLETTERS", "CAP");
      if (line.startsWith("/**")) {
        continue;
      }
      if (!line.startsWith("//") && !line.startsWith("/*") && !"".equals(line.trim())) {
        // if(line.startsWith("BASEDIR")) {
        // continue;
        // }
        if (line.startsWith("DECLARE")) {
          line = line.replaceAll(" ", ", ");
          line = line.replaceFirst(", ", " ");
          if (line.endsWith(", ")) {
            line = line.substring(0, line.length() - 2);
          }
        } else {

        }
        if (line.indexOf("//") == -1) {
          line = line + ";";
        } else {
          line = line.replaceAll("//", ";//");
        }
        line = line.replaceAll("\'", "\"");
        if (line.contains("BASEDIR") || line.contains("FILLOBJECT")) {
          line = "//" + line;
        }
      }
      if (!line.endsWith("\n")) {
        line = line + "\n";
      }
      out.write(line);
    }
    out.close();
    in.close();
  }

}