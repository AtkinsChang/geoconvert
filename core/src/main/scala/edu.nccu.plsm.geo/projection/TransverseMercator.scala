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

package edu.nccu.plsm.geo.projection

import edu.nccu.plsm.geo.util.Math
import edu.nccu.plsm.geo.datum.TMDatum

object TransverseMercator {
  def project(longitude: Double, latitude: Double, datum: TMDatum): ProjectionResult = {
    def l(exponent: Int) = math.pow(longitude - datum.longitude0, exponent)
    val sinLat = Math.lazyExponentialSeries(math.sin(latitude))
    val cosLat = Math.lazyExponentialSeries(math.cos(latitude))
    val tanLat = Math.lazyExponentialSeries(math.tan(latitude))
    val n = datum.n
    val nu = datum.a / math.sqrt(1 - datum.eSquare * sinLat(2))
    val etaSquare = datum.e2Square * cosLat(2)

    /*
     *  val a = 1D - n(1) + ((n(2) - n(3)) * 5D / 4D) + ((n(4) - n(5)) * 81D / 64D)
     *  val b = (3D * n(1) / 2D) * (1D - n(1) + ((n(2) - n(3)) * 7D / 8D) + ((n(4) - n(5)) * 55D / 64D))
     *  val c = (15D * n(2) / 16D) * (1D - n(1) + ((n(2) - n(3)) * 3D / 4D))
     *  val d = (35D * n(3) / 48D) * (1D - n(1) + ((n(2) - n(3)) * 11D / 16D))
     *  val e = (315D * n(4) / 51D) * (1D - n(1))
     */

    val n0_1 = n.head - n.apply(1)
    val n2_3 = n(2) - n(3)
    val n4_5 = n(4) - n(5)
    val a = n0_1 + (n2_3 * 5D / 4D) + (n4_5 * 81D / 64D)
    val b = (3D * n(1) / 2D) * (n0_1 + (n2_3 * 7D / 8D) + (n4_5 * 55D / 64D))
    val c = (15D * n(2) / 16D) * (n0_1 + (n2_3 * 3D / 4D))
    val d = (35D * n(3) / 48D) * (n0_1 + (n2_3 * 11D / 16D))
    val e = (315D * n(4) / 51D) * n0_1

    // Y
    val s = datum.a * (
      a * latitude
      - b * math.sin(latitude * 2D)
      + c * math.sin(latitude * 4D)
      - d * math.sin(latitude * 6D)
      + e * math.sin(latitude * 8D)
    )

    val m0 = s
    val m2 = nu / 2D * sinLat(1) * cosLat(1)
    val m4 = nu / 24D * sinLat(1) * cosLat(3) * (5D - tanLat(2) + 9D * etaSquare + 4D * math.pow(etaSquare, 2))

    val m1 = nu * cosLat(1)
    val m3 = nu * cosLat(3) / 6D * (1D - tanLat(2) + etaSquare)
    val m5 = nu * cosLat(5) / 120D * (5D - 18D * tanLat(2) + tanLat(4))

    val northing = datum.k * (m0 + m2 * l(2) + m4 * l(4))
    val easting = datum.k * (m1 * l(1) + m3 * l(3) + m5 * l(5)) + datum.dx
    new ProjectionResult(LatLng(latitude, longitude), Seq(easting, northing), datum)
  }

  def meridianConvergence(longitude: Double, latitude: Double, datum: TMDatum): CoverageResult = {
    def l(exponent: Int) = math.pow(longitude - datum.longitude0, exponent)
    val sinLat = math.sin(latitude)
    val cosLat = math.cos(latitude)
    val cosLatSquare = math.pow(cosLat, 2)
    val tanLatSquare = math.pow(math.tan(latitude), 2)
    val etaSquare = datum.e2Square * cosLatSquare
    val m1 = sinLat
    val m3 = sinLat * cosLatSquare / 3D * (1D + 3D * etaSquare + 2D * math.pow(etaSquare, 2))
    val m5 = sinLat * math.pow(cosLatSquare, 2) / 15D * (2D - tanLatSquare)

    val result = l(1) * m1 + l(3) * m3 + l(5) * m5
    new CoverageResult(LatLng(latitude, longitude), Seq(result), datum)
  }

}