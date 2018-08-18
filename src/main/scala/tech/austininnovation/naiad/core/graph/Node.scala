package tech.austininnovation.naiad.core.graph

import java.util.UUID

case class Node(id: UUID, nodeProperties: Map[String, Any])

object Node {
  def create(props: Map[String, Any]): Node = Node(
    id = UUID.randomUUID(),
    nodeProperties = props)
}
