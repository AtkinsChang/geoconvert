package edu.nccu.plsm.geo.projection

import edu.nccu.plsm.geo.datum.TMDatum

case class ProjectionResult(
    from: LatLng,
    result: Seq[Double],
    datum: TMDatum
) extends Result {
  override def unit: String = "m"
}