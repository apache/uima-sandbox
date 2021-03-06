<?xml version="1.0" encoding="UTF-8"?>

<!--
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version='1.0'>
   
  <!-- S A M P L E  -->
  
  <!-- note that xsl:import elements must appear FIRST
       and their href strings must be literal (no variables allowed) -->
    
  <!-- import docbook system from the shared uima-docbook-tool project -->  
  <xsl:import href="../../../../uima-docbook-tool/tools/docbook-versions/docbook-xsl-1.72.0/html/chunk.xsl" />
  
  <!-- import the standard uima-style of formatting from the shared uima-docbook-tool project -->
  <xsl:import href="../../../../uima-docbook-tool/styles/uima-style/top/html-chunked.xsl" />  
    
  <!-- The script will generate a title page in ${local.titlepage.dir} -->
  <!-- this generate xsl file needs to be included here -->
  <!-- Make sure the path agrees with your setting of ${local.titlepage.dir} in local.docbook.properties -->
  <xsl:include href="../titlepage/titlepage-html.xsl"/>
   
</xsl:stylesheet>
