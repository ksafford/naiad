package tech.austininnovation.naiad.core.graph

import org.scalatest._

class TestInMemoryGraph extends FlatSpec with Matchers {

  val node1 = Node.create(Map("Name" -> "A new Node"))
  val node2 = Node.create(Map("Name" -> "And even newer node"))
  val edge1 = Edges.DirectedEdge.create(node1, node2, Map("Relationship" -> "Is older than"))
  val edge2 = Edges.UndirectedEdge.create(node1, node1, Map("Relationship" -> "Is connected to"))

  val graph = InMemoryGraph(None, None, None)

  val testGraph = graph.addNode(node1)
    .addNode(node2)
    .addDirectedEdge(edge1)
    .addUndirectedEdge(edge2)

  "A graph " should "be able to have nodes added" in {
    testGraph.nodes shouldBe Some(Set(node1, node2))
  }

  it should " be able to have edged added" in {
    testGraph.dEdges shouldBe Some(Set(edge1))
    testGraph.uEdges shouldBe Some(Set(edge2))
  }

  it should " be able to return nodes and edged by their ids" in {
    testGraph.getNodeById(node1.id) shouldBe Some(node1)
    testGraph.getEdgeById(edge1.id) shouldBe Some(edge1)
    testGraph.getNodeById(java.util.UUID.randomUUID()) shouldBe None
  }

  it should " be able to find root and leaf nodes" in {
    testGraph.getLeafNodes() shouldBe Some(Set(node2))
    testGraph.getRootNodes() shouldBe None
  }

  it should " be able to find all children and siblings of a given node" in {
    testGraph.getConnectedNodes(node1) shouldBe Some(Set(node1, node2))
    testGraph.getChildNodes(node1) shouldBe Some(Set(node2))
    testGraph.getSiblingNodes(node1) shouldBe Some(Set(node1))
    testGraph.getParentNodes(node1) shouldBe None
    testGraph.getParentNodes(node2) shouldBe Some(Set(node1))
  }

  /*
  TODO:
  Illegal states should be unrepresentable.
  """testGraph.addNode(node1.copy(nodeProperties = Map("Malicious node" -> "Because it's id matches another node's")))""" shouldNot compile
   */
}
