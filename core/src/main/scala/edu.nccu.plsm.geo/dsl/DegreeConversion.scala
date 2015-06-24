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

import scala.language.implicitConversions

trait DegreeConversion {
  implicit def double2Degree(d: Double): Degree = new Degree(d)

  implicit def int2Degree(d: Int): Degree = new Degree(d)

  implicit def long2Degree(d: Long): Degree = new Degree(d)

  final class Degree(private val _d: Double) {
    def °(m: Double): Minute = d(m)

    def d(m: Double): Minute = new Minute(_d, m)

    def ° : CoordinateUnit = d

    def d: CoordinateUnit = new CoordinateUnit(math.toRadians(_d))
  }

  final class Minute(private val _d: Double, private val _m: Double) {
    def m(s: Double): Second = new Second(_d, _m, s)

    def m: CoordinateUnit = new CoordinateUnit(math.toRadians(_d + _m / 60D))
  }

  final class Second(private val _d: Double, private val _m: Double, private val _s: Double) {
    def s: CoordinateUnit = new CoordinateUnit(math.toRadians(_d + _m / 60D + _s / 3600D))
  }

  final class Radian(private val _rad: Double) {
    def rad: CoordinateUnit = new CoordinateUnit(_rad)
  }

  final class CoordinateUnit(private val v: Double) {
    def toRadians: Double = v

    def toDegrees: Double = math.toDegrees(v)
  }

}