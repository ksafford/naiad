package tech.austininnovation.naiad.core.graph

import org.scalatest._

class TestGraph extends FlatSpec with Matchers {

  val node1 = Node.create(Map("Name" -> "A new Node"))
  val node2 = Node.create(Map("Name" -> "And even newer node"))
  val edge = Edges.DirectedEdge.create(node1, node2, Map("Relationship" -> "Is older than"))

  val graph = Graph(None, None, None)

  val testGraph = graph.addNode(node1)
    .addNode(node2)
    .addDirectedEdge(edge)

  println(s"Graph has nodes: ${testGraph.nodes.getOrElse("No Nodes!")}")
  println(s"Graph has edges: ${testGraph.uEdges.getOrElse("No Undirected Edges!")}, " +
    s"${testGraph.dEdges.getOrElse("No Directed Edges!")}")

  "A graph " should "be able to have nodes added" in {
    testGraph.nodes shouldBe Some(Set(node1, node2))
  }
}
