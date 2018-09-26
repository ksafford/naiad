package tech.austininnovation.naiad.core
package graph

import java.util.UUID

case class Node(id: UUID, label: String, nodeProperties: Map[String, Any]) {
  def ----(n2: Node) = Edge.create(_: String, this, :-:, n2, _: Map[String, Any])
  def --->(n2: Node) = Edge.create(_: String, this, -->, n2, _: Map[String, Any])
  def <-->(n2: Node) = Edge.create(_: String, this, <->, n2, _: Map[String, Any])
}

object Node {
  def create(label: String, props: Map[String, Any]): Node = Node(
    id = UUID.randomUUID(),
    label,
    nodeProperties = props)
}
