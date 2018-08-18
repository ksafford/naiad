package tech.austininnovation.naiad.core.graph

import java.util.UUID

import simulacrum._

object Edges {

  //@typeclass
  sealed trait Edge[T] {
    def create(left: Node, right: Node, edgeProperties: Map[String, Any]): T
  }

  case class DirectedEdge(
    id: UUID,
    left: Node,
    right: Node,
    edgeProperties: Map[String, Any])

  object DirectedEdge extends Edge[DirectedEdge] {
    def create(left: Node, right: Node, edgeProperties: Map[String, Any]): DirectedEdge =
      DirectedEdge(
        id = UUID.randomUUID(),
        left = left,
        right = right,
        edgeProperties = edgeProperties)
  }

  case class UndirectedEdge(
    id: UUID,
    left: Node,
    right: Node,
    edgeProperties: Map[String, Any])

  object UndirectedEdge extends Edge[UndirectedEdge] {
    def create(left: Node, right: Node, edgeProperties: Map[String, Any]): UndirectedEdge =
      UndirectedEdge(
        id = UUID.randomUUID(),
        left = left,
        right = right,
        edgeProperties = edgeProperties)
  }
}