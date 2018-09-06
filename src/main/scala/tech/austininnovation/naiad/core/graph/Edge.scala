package tech.austininnovation.naiad.core.graph

import java.util.UUID

sealed trait EdgeDirection
case object :-: extends EdgeDirection
case object --> extends EdgeDirection
case object <-> extends EdgeDirection

case class Edge(
  id: UUID,
  label: String,
  left: Node,
  direction: EdgeDirection,
  right: Node,
  edgeProperties: Map[String, Any]) {
}

object Edge {
  def create(label: String, left: Node, direction: EdgeDirection, right: Node, edgeProperties: Map[String, Any]): Edge =
    Edge(
      id = UUID.randomUUID(),
      label,
      left = left,
      direction = direction,
      right = right,
      edgeProperties = edgeProperties)
}
