package tech.austininnovation.naiad.core.graph

sealed trait NodeValue
case class NString(value: String) extends NodeValue
case class NDouble(value: Double) extends NodeValue
case class NInt(value: Int) extends NodeValue

object NodeValueImplicits {

  implicit def strToNodeVal(str: String): NodeValue = NString(str)

  implicit def dblToNodeVal(dbl: Double): NodeValue = NDouble(dbl)

  implicit def intToNodeVal(int: Int): NodeValue = NInt(int)

}
