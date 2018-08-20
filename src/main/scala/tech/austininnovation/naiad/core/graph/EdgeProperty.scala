package tech.austininnovation.naiad.core.graph

import shapeless.HList

object EdgeValues {
  sealed trait EdgeValue
  case class EString(value: String) extends EdgeValue
  case class EDouble(value: Double) extends EdgeValue

  implicit def strToEdgeVal(str: String): EdgeValue = EString(str)
  implicit def dblToEdgeVal(dbl: Double): EdgeValue = EDouble(dbl)

}

case class EdgeProperty(name: String, value: EdgeValues.EdgeValue)
//sealed trait EdgeProperty
//case class EdgePropertyD(name: String, value: Double) extends EdgeProperty
//case class EdgePropertyS(name: String, value: String) extends EdgeProperty

