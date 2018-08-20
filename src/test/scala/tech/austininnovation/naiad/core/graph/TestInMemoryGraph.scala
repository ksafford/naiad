package tech.austininnovation.naiad.core.graph

import org.scalatest._
import tech.austininnovation.naiad.core.graph.EdgeValueImplicits._
import tech.austininnovation.naiad.core.graph.NodeValueImplicits._

class TestInMemoryGraph extends FlatSpec with Matchers {

  val nProp1 = List(NodeProperty("Name", "A new Node"))
  val node1 = Node.create(nProp1)

  val nProp2 = List(NodeProperty("Name", "An even newer node"))
  val node2 = Node.create(nProp2)

  val eProp1 = List(EdgeProperty("Relationship", "Is older than"))
  val eProp2 = List(EdgeProperty("Relationship", "Is connected to"))
  val edge1 = Edge.create(node1, -->, node2, eProp1)
  val edge2 = Edge.create(node1, ---, node1, eProp2)

  val graph = InMemoryGraph(None, None)

  val testGraph = graph.addNode(node1)
    .addNode(node2)
    .addEdge(edge1)
    .addEdge(edge2)

  "A graph " should "be able to have nodes added" in {
    testGraph.nodes shouldBe Some(Set(node1, node2))
  }

  it should " be able to have edged added" in {
    testGraph.edges shouldBe Some(Set(edge1, edge2))
  }

  it should " be able to return nodes and edges by their ids" in {
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
