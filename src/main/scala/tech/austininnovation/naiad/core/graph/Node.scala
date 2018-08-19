package tech.austininnovation.naiad.core.graph

import java.util.UUID

case class Node(id: UUID, label: String, nodeProperties: NodeProperties)

object Node {
  def create(label: String, props: NodeProperties): Node = Node(
    id = UUID.randomUUID(),
    label,
    nodeProperties = props)
}
