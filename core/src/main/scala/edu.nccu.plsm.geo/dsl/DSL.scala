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

package edu.nccu.plsm.geo.dsl

import java.util

import edu.nccu.plsm.geo.datum.TMDatum
import edu.nccu.plsm.geo.projection.{ CoverageResult, ProjectionResult, Result, TransverseMercator }
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.language.implicitConversions

trait DSL extends DegreeConversion with DatumDefining {
  protected[this] val logger = LoggerFactory.getLogger(getClass)

  val resultMap = new util.LinkedHashMap[Symbol, Result].asScala
  implicit def symbol2Assignable(s: Symbol): Assignable = new Assignable(s)

  trait Operation[T] {
    def WITH(datum: TMDatum): Result
  }

  final class PROJECT(private val lng: CoordinateUnit, private val lat: CoordinateUnit) extends Operation[(Double, Double)] {
    override def WITH(datum: TMDatum): ProjectionResult = {
      TransverseMercator.project(lng.toRadians, lat.toRadians, datum)
    }
  }

  final class CONVERGENCE(private val lng: CoordinateUnit, private val lat: CoordinateUnit) extends Operation[Double] {
    override def WITH(datum: TMDatum): CoverageResult = {
      TransverseMercator.meridianConvergence(lng.toRadians, lat.toRadians, datum)
    }
  }

  final class Assignable(private val symbol: Symbol) {
    def :=[T](result: Result): Unit = {
      resultMap.put(symbol, result)
        .foreach(result => logger.warn(s"!!! Overwriting result ${symbol.name} = ${result.result}"))
    }
  }

  object PROJECT {
    def apply(lng: CoordinateUnit, lat: CoordinateUnit): PROJECT = new PROJECT(lng, lat)
    def apply(p: (CoordinateUnit, CoordinateUnit)): PROJECT = new PROJECT(p._1, p._2)
  }

  object CONVERGENCE {
    def apply(lng: CoordinateUnit, lat: CoordinateUnit): CONVERGENCE = new CONVERGENCE(lng, lat)
    def apply(p: (CoordinateUnit, CoordinateUnit)): CONVERGENCE = new CONVERGENCE(p._1, p._2)
  }
  /*
  object RESULT {
    def PRINT(): Unit = {
      logger.info(s"""Result:
                     |$RETURN""".stripMargin)
    }

    def RETURN(): String = resultMap.map { case (symbol, result) => f"${symbol.name}%-10s = $result" }.mkString("\n")

    def CLEAR(): Unit = resultMap.clear()
  }
  */

}