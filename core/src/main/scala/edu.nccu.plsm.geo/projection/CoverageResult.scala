package edu.nccu.plsm.geo.projection

import edu.nccu.plsm.geo.datum.TMDatum

case class CoverageResult(
    from: LatLng,
    result: Seq[Double],
    datum: TMDatum
) extends Result {
  override def unit: String = "rad"
}
