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
package org.apache.uima.pear.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.uima.pear.tools.InstallationController;
import org.apache.uima.pear.tools.PackageCreator;
import org.apache.uima.pear.tools.PackageCreatorException;

/**
 * Goal which generates a UIMA PEAR package
 * 
 * @goal package
 * 
 * @phase package
 */
public class PearPackagingMojo extends AbstractMojo {

   /**
    * Root directory of the UIMA project that contains the pear structure
    * 
    * @parameter expression="${project.build.directory}"
    * @required
    */
   private String mainComponentDir = null;

   /**
    * Required classpath settings for the pear component.
    * 
    * @parameter
    * @required
    */
   private String classpath = null;

   /**
    * Main Component Descriptor
    * 
    * @parameter
    * @required
    */
   private String mainComponentDesc = null;

   /**
    * pear component ID
    * 
    * @parameter
    * @required
    */
   private String componentId = null;

   /**
    * target directory for the pear file
    * 
    * @parameter expression="${project.build.directory}/target"
    * @required
    */
   private String targetDir = null;

   /**
    * compiled classes directory
    * 
    * @parameter expression="${project.build.directory}/target/classes"
    * @required
    */
   private String compiledClassesDir = null;

   /**
    * Required UIMA datapath settings for the pear component
    * 
    * @parameter default-value="$main_root/resources"
    */
   private String datapath = null;

   /**
    * environment variables
    * 
    * @parameter
    */
   private Properties props = null;

   /**
    * The maven project.
    * 
    * @parameter expression="${project}"
    * @required
    * @readonly
    * @description "the maven project to use"
    */
   private MavenProject project;

   // the pear packaging directory contains all the stuff that is packaged to
   // the PEAR package
   private File pearPackagingDir;

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.maven.plugin.AbstractMojo#execute()
    */
   public void execute() throws MojoExecutionException {
      this.pearPackagingDir = new File(this.targetDir, "pearPackaging");

      Log log = getLog();
      log.debug("UIMA PEAR INFORMATION ");
      log.debug("======================");
      log.debug("main component dir:   " + this.mainComponentDir);
      log.debug("main component desc:  " + this.mainComponentDesc);
      log.debug("component id:         " + this.componentId);
      log.debug("classpath:            " + this.classpath);
      log.debug("datapath:             " + this.datapath);
      log.debug("target dir:           " + this.targetDir);
      log.debug("pear packaging dir:   "
            + this.pearPackagingDir.getAbsolutePath());

      try {
         // copies the PEAR data to the pear packaging directory
         copyPearData();

         // update classpath with compiled classes
         String classpathExtension = ";$main_root/"
               + InstallationController.PACKAGE_BIN_DIR;
         this.classpath = this.classpath + classpathExtension;

         // create the PEAR package
         createPear();

         File finalFile = new File(this.targetDir, this.componentId + ".pear");
         this.project.getArtifact().setFile(finalFile);

      } catch (PackageCreatorException e) {
         log.debug(e.getMessage());
         throw new MojoExecutionException(e.getMessage());
      } catch (IOException e) {
         log.debug(e.getMessage());
         throw new MojoExecutionException(e.getMessage());
      }

   }

   /**
    * copies the given directory if it exists to the PEAR packaging directory
    * 
    * @param directory
    *           directory to copy
    * 
    * @throws IOException
    */
   private void copyDirIfAvailable(String directory) throws IOException {

      // check if directory exist
      File dirToCopy = new File(this.mainComponentDir, directory);
      if (dirToCopy.exists()) {
         File target = new File(this.pearPackagingDir, directory);
         FileUtils.copyDirectory(dirToCopy, target);
         // remove directories that begins with a "." -> e.g. .SVN
         removeDotDirectories(target);
      }
   }

   /**
    * removes recursively all directories that begins with a ".". E.g. ".SVN"
    * 
    * @param dir
    *           directory to check for Dot-directories
    * 
    * @throws IOException
    */
   private void removeDotDirectories(File dir) throws IOException {
      ArrayList subdirs = org.apache.uima.util.FileUtils.getSubDirs(dir);

      for (int i = 0; i < subdirs.size(); i++) {
         File current = (File) subdirs.get(i);
         if (current.getName().startsWith(".")) {
            FileUtils.deleteDirectory(current);
         } else {
            removeDotDirectories(current);
         }
      }
   }

   /**
    * copies all the necessary PEAR data to the PEAR packaging directory
    * 
    * @throws IOException
    */
   private void copyPearData() throws IOException {

      // select all necessary PEAR package directories that have to be copied
      String[] dirsToCopy = new String[] {
            InstallationController.PACKAGE_CONF_DIR,
            InstallationController.PACKAGE_DATA_DIR,
            InstallationController.PACKAGE_DESC_DIR,
            InstallationController.PACKAGE_DOC_DIR,
            InstallationController.PACKAGE_LIB_DIR,
            InstallationController.PACKAGE_METADATA_DIR,
            InstallationController.PACKAGE_RESOURCES_DIR,
            /* InstallationController.PACKAGE_SOURCES_DIR , */InstallationController.PACKAGE_BIN_DIR, };

      // copies the selected directories if they exists
      for (int i = 0; i < dirsToCopy.length; i++) {
         copyDirIfAvailable(dirsToCopy[i]);
      }

      // copy compiled classes to the PEAR packaging directory
      File dirToCopy = new File(this.compiledClassesDir);
      if (dirToCopy.exists()) {
         File target = new File(this.pearPackagingDir,
               InstallationController.PACKAGE_BIN_DIR);
         FileUtils.copyDirectory(dirToCopy, target);
         removeDotDirectories(target);
      }
   }

   /**
    * create a PEAR package with
    * 
    * @throws PackageCreatorException
    */
   private void createPear() throws PackageCreatorException {
      PackageCreator
            .generatePearPackage(this.componentId, this.mainComponentDesc,
                  this.classpath, this.datapath, this.pearPackagingDir
                        .getAbsolutePath(), this.targetDir, this.props);
   }
}
