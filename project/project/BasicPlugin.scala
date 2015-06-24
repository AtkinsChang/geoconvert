/*
 * Licensed to the Programming Language and Software Methodology Lab (PLSM)
 * under one or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership.
 * The PLSM licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import sbt._

trait BasicPlugin extends VersionHelper {
  lazy val updates      = "com.timushev.sbt"         %  "sbt-updates"           %  v"0.1.8"
  lazy val buildInfo    = "com.eed3si9n"             %  "sbt-buildinfo"         %  v"0.4.0"
  lazy val scoverage    = "org.scoverage"            %  "sbt-scoverage"         %  v"1.1.0"
  lazy val assembly     = "com.eed3si9n"             %  "sbt-assembly"          %  v"0.13.0"
  lazy val multiJvm     = "com.typesafe.sbt"         %  "sbt-multi-jvm"         %  v"0.3.11"
  lazy val cpd          = "de.johoop"                %  "cpd4sbt"               %  v"1.1.5"
  lazy val scalastyle   = "org.scalastyle"           %% "scalastyle-sbt-plugin" %  v"0.7.0"
  lazy val scalariform  = "com.typesafe.sbt"         %  "sbt-scalariform"       %  v"1.3.0"
  lazy val stats        = "com.orrsella"             %  "sbt-stats"             %  v"1.0.5"
  lazy val gitStamp     = "com.atlassian.labs"       %  "sbt-git-stamp"         %  v"0.1.2"
  lazy val meow         = "com.37pieces"             %  "sbt-meow"              %  v"0.1"

  lazy val eclipse      = "com.typesafe.sbteclipse"  %  "sbteclipse-plugin"     %  v"4.0.0-RC2"
  lazy val idea         = "com.github.mpeltonen"     %  "sbt-idea"              %  v"1.6.0"

  lazy val echo         = "com.typesafe.sbt"         %  "sbt-echo"              %  v"0.1.15"
  lazy val sbtCoreNext  = "org.scala-sbt"            %  "sbt-core-next"         %  "0.1.1"
}
