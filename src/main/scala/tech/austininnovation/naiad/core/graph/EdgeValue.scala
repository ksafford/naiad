package tech.austininnovation.naiad.core.graph

sealed trait EdgeValue
case class EString(value: String) extends EdgeValue
case class EDouble(value: Double) extends EdgeValue
case class EInt(value: Int) extends EdgeValue

object EdgeValueImplicits {

  implicit def strToEdgeVal(str: String): EdgeValue = EString(str)

  implicit def dblToEdgeVal(dbl: Double): EdgeValue = EDouble(dbl)

  implicit def intToEdgeVal(int: Int): EdgeValue = EInt(int)

}