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

trait PlayPlugin extends VersionHelper {
  lazy val playVersion = "2.4.0"

  lazy val play         = "com.typesafe.play"        %  "sbt-plugin"            %  playVersion
  lazy val playDocs     = "com.typesafe.play"        %  "play-docs-sbt-plugin"  %  playVersion
  lazy val forkRun      = "com.typesafe.play"        %  "sbt-fork-run-plugin"   %  playVersion
  lazy val coffeescript = "com.typesafe.sbt"         %  "sbt-coffeescript"      %  v"1.0.0"
  lazy val less         = "com.typesafe.sbt"         %  "sbt-less"              %  v"1.0.6"
  lazy val jshint       = "com.typesafe.sbt"         %  "sbt-jshint"            %  v"1.0.3"
  lazy val rjs          = "com.typesafe.sbt"         %  "sbt-rjs"               %  v"1.0.7"
  lazy val digest       = "com.typesafe.sbt"         %  "sbt-digest"            %  v"1.1.0"
  lazy val mocha        = "com.typesafe.sbt"         %  "sbt-mocha"             %  v"1.1.0"
  lazy val uglify       = "com.typesafe.sbt"         %  "sbt-uglify"            %  "1.0.3"
  lazy val gzip         = "com.typesafe.sbt"         %  "sbt-gzip"              %  v"1.0.0"

  lazy val playPlugins = Seq(
    play,
    playDocs,
    coffeescript,
    less,
    jshint,
    rjs,
    digest,
    mocha,
    uglify,
    gzip
  )
}
