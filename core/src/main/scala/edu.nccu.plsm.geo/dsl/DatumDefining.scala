package edu.nccu.plsm.geo.dsl

import edu.nccu.plsm.geo.datum.Datum

trait DatumDefining {
  object SET {
    def MAJORAXIS(majorAxis: Double) = new {
      def INVERSEFLATTENING(inverseFlattening: Double) = Datum("anon", majorAxis, inverseFlattening)
    }
  }

  object BASE {
    def ON(datum: Datum) = new {
      def CENTRAL(cm: DegreeConversion#CoordinateUnit) = new {
        def SCALE_FACTOR(k: Double) = new {
          def X_TRANSLATION(x: Double) = Datum("anon", datum, cm.toRadians, k, x)
        }
      }
    }
  }
}
