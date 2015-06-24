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
lazy val rootPath = file(".")

lazy val root = (project in rootPath)
  .enablePlugins(BuildInfoPlugin)
  .settings(Settings.default: _*)
  .settings(Settings.plugin: _*)
  .settings(
    name := "convert-root"
  ).aggregate(util, core, web)

lazy val util = (project in rootPath / "util")
  .enablePlugins(BuildInfoPlugin)
  .settings(Settings.default: _*)
  .settings(Settings.plugin: _*)
  .settings(
    name := "convert-util",
    libraryDependencies ++= Dependencies.scalaTools.value,
    libraryDependencies ++= Dependencies.utilProject
  )

lazy val core = (project in rootPath / "core")
  .enablePlugins(BuildInfoPlugin)
  .settings(Settings.default: _*)
  .settings(Settings.plugin: _*)
  .settings(
    name := "convert-core",
    libraryDependencies ++= Dependencies.scalaTools.value,
    libraryDependencies ++= Dependencies.coreProject
  )

lazy val web = (project in rootPath / "web")
  .enablePlugins(PlayScala, SbtWeb, BuildInfoPlugin)
  .settings(Settings.default: _*)
  .settings(Settings.plugin: _*)
  .settings(Settings.play: _*)
  .settings(
    name := "convert-web",
    libraryDependencies ++= Dependencies.scalaTools.value,
    libraryDependencies ++= Dependencies.webProject
  ).dependsOn(core, util)