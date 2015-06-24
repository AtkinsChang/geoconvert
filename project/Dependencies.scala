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

import play.sbt.PlayImport._
import sbt._

trait Dependencies {
  object Library extends Library {
    override protected[this] def version = NotSpecific
  }
  import Library._

  lazy val logback = Seq(
    logbackCore,
    logbackClassic
  )

  lazy val slf4j = Seq(
    slf4jApi
  )

  lazy val akka = Seq(
    akkaSlf4j,
    akkaActor
  )
  lazy val tool = Seq(
    javassit
  )

  lazy val scalaTools = Def.setting{
    Seq(scalaCompiler.value, scalaReflect.value)
  }

  lazy val testing = Seq(
    scalatest,
    mockito
  )

  lazy val play = Seq(
    jdbc,
    cache,
    ws,
    filters,
    json,
    evolutions,
    playSlick,
    hikariCP,
    oracleConnector,
    slickExtensions,
    xalanSerializer
  )

  lazy val playTesting = Seq(
    specs2 % Test
  )

  lazy val utilProject = slf4j.map(_ % Provided) :+ playApi
  lazy val coreProject = slf4j.map(_ % Provided) ++ logback.map(_ % Provided) ++ testing
  lazy val webProject = slf4j ++ logback ++ tool ++ akka ++ play ++ testing ++ playTesting
}

object Dependencies extends Dependencies