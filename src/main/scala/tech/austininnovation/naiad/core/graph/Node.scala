package tech.austininnovation.naiad.core.graph

import java.util.UUID

case class Node(id: UUID, nodeProperties: NodeProperties)

object Node {
  def create(props: NodeProperties): Node = Node(
    id = UUID.randomUUID(),
    nodeProperties = props)
}
