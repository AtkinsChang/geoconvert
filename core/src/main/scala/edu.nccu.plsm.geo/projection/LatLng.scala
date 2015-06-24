package edu.nccu.plsm.geo.projection

import scala.language.postfixOps

case class LatLng(
    lat: Double,
    lng: Double
) {
  private[this] def format(degree: Double) = {
    val d = degree toInt
    val m = ((degree - d) * 60) toInt
    val s = BigDecimal(degree * 3600 % 60).setScale(5, BigDecimal.RoundingMode.HALF_UP)
    f"""$d%3s° $m%2s' $s%8s""""
  }
  def latDegree = math.toDegrees(lat)
  def lngDegree = math.toDegrees(lng)
  def latitude = format(latDegree)
  def longitude = format(lngDegree)
}
