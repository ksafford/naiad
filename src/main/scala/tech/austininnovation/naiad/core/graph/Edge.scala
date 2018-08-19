package tech.austininnovation.naiad.core.graph

import java.util.UUID

sealed trait EdgeDirection
case object --- extends EdgeDirection
case object <-- extends EdgeDirection
case object --> extends EdgeDirection

case class Edge(
  id: UUID,
  label: String,
  left: Node,
  direction: EdgeDirection,
  right: Node,
  edgeProperties: EdgeProperties) {

  def get[T](name: String): Option[Any] = {
    edgeProperties.find(p => p.name == name).map {
      case EdgeProperty(_, EString(v)) => Some(v)
      case EdgeProperty(_, EDouble(v)) => Some(v)
      case EdgeProperty(_, EInt(v)) => Some(v)
      case _ => None
    }
  }
}

object Edge {
  def create(label: String, left: Node, direction: EdgeDirection, right: Node, edgeProperties: EdgeProperties): Edge =
    Edge(
      id = UUID.randomUUID(),
      label,
      left = left,
      direction = direction,
      right = right,
      edgeProperties = edgeProperties)
}