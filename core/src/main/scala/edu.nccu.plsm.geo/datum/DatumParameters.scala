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

package edu.nccu.plsm.geo.datum

import edu.nccu.plsm.geo.util.Math

trait DatumParameters {
  def majorAxis: Double

  def inverseFlattening: Double

  lazy val minorAxis = majorAxis * (inverseFlattening - 1) / inverseFlattening
  lazy val flattening = math.pow(inverseFlattening, -1)

  def a: Double = majorAxis

  def b: Double = minorAxis

  def f: Double = flattening

  lazy val f2 = math.pow(inverseFlattening - 1, -1)
  lazy val f3 = math.pow(inverseFlattening * 2 - 1, -1)
  lazy val n = Math.lazyExponentialSeries(f3)
  lazy val eSquare = f * 2 - math.pow(f, 2)
  lazy val e2Square = eSquare / (1 - eSquare)

  override def toString: String = s"[a=$a, b=$b, 1/f=$inverseFlattening]"
}