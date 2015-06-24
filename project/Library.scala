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

import play.core.PlayVersion
import sbt.Keys._
import sbt._

trait BasicLibrary extends VersionHelper {
  protected[this] def javaLibrary(group: String, version: String) = {
    (module: String) => group % module % v"$version"
  }

  protected[this] def scalaLibrary(group: String, version: String) = {
    (module: String) => group %% module % v"$version"
  }
}

trait Slf4jLibrary extends BasicLibrary {
  lazy val slf4jApi = module("slf4j-api")
  lazy val osgiOverSlf4j = module("osgi-over-slf4j")
  lazy val log4jOverSlf4j = module("log4j-over-slf4j")
  lazy val jclOverSlf4j = module("jcl-over-slf4j")
  lazy val julToSlf4j = module("jul-to-slf4j")

  private[this] def module = javaLibrary("org.slf4j", Version.slf4j)
}

trait LogbackLibrary extends BasicLibrary {
  lazy val logbackCore = module("logback-core")
  lazy val logbackClassic = module("logback-classic")

  private[this] def module = javaLibrary("ch.qos.logback", Version.logback)
}

trait AkkaLibrary extends BasicLibrary {
  lazy val akkaClusterSharding = module("akka-cluster-sharding")
  lazy val akkaContrib = module("akka-contrib")
  lazy val akkaClusterMetrics = module("akka-cluster-metrics")
  lazy val akkaClusterTools = module("akka-cluster-tools")
  lazy val akkaPersistenceExperimentalTck = module("akka-persistence-experimental-tck")
  lazy val akkaCluster = module("akka-cluster")
  lazy val akkaMultiNodeTestkit = module("akka-multi-node-testkit") % Test
  lazy val akkaCamel = module("akka-camel")
  lazy val akkaTypedExperimental = module("akka-typed-experimental")
  lazy val akkaPersistenceExperimental = module("akka-persistence-experimental")
  lazy val akkaRemote = module("akka-remote")
  lazy val akkaSlf4j = module("akka-slf4j")
  lazy val akkaOsgi = module("akka-osgi")
  lazy val akkaTestkit = module("akka-testkit") % Test
  lazy val akkaKernel = module("akka-kernel")
  lazy val akkaRemoteTests = module("akka-remote-tests")
  lazy val akkaActor = module("akka-actor")
  lazy val akkaActorTests = module("akka-actor-tests")

  private[this] def module = scalaLibrary("com.typesafe.akka", Version.akka)
}

trait ToolLibrary extends BasicLibrary {
  lazy val scalaReflect       = Def.setting { "org.scala-lang"      %   "scala-reflect"        %   scalaVersion.value}
  lazy val scalaCompiler      = Def.setting { "org.scala-lang"      %   "scala-compiler"       %   scalaVersion.value}
  lazy val javassit           =               "org.javassist"       %   "javassist"            %  v"3.19.0-GA"
}

trait TestingLibrary extends BasicLibrary {
  lazy val mockito            =               "org.mockito"         %   "mockito-core"         %  v"2.0.16-beta"   %  Test
  lazy val scalatest          =               "org.scalatest"       %%  "scalatest"            %  v"2.2.5"         %  Test
}

trait PlayLibrary extends BasicLibrary {
  lazy val playSlick        = "com.typesafe.play"      %% "play-slick"            %  v"1.0.0"
  lazy val slickExtensions  = "com.typesafe.slick"     %% "slick-extensions"      %  v"3.0.0"
  lazy val oracleConnector  = "com.oracle"             %  "ojdbc7dms_g"           %  v"12.1.0.2"
  lazy val hikariCP         = "com.zaxxer"             %  "HikariCP"              %  v"2.3.8"
  lazy val xalanSerializer  = "xalan"                  %  "serializer"            %  v"2.7.2"
  lazy val playApi          = "com.typesafe.play"      %% "play"                  %   PlayVersion.current          % Provided

}

trait Library extends Slf4jLibrary with LogbackLibrary with ToolLibrary with TestingLibrary with PlayLibrary with AkkaLibrary
