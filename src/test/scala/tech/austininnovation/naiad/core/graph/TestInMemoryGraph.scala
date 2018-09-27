package tech.austininnovation.naiad.core.graph

import org.scalatest._
import cats.Id

class TestInMemoryGraph extends FlatSpec with Matchers {

  val nProp1 = Map("Name" -> "A new Node")
  val node1 = Node.create("node1", nProp1)

  val nProp2 = Map("Name" -> "An even newer node")
  val node2 = Node.create("node2", nProp2)

  val eProp1 = Map("Relationship" -> "Is older than")
  val eProp2 = Map("Relationship" -> "Is connected to")
  val eProp3 = Map("Relationship" -> "Is connected to")
  val eProp4 = Map("Relationship" -> "Is connected to")
  val edge1 = Edge.create("edge1", node1, -->, node2, eProp1)
  val edge2 = Edge.create("edge2", node1, :-:, node1, eProp2)
  val edge3 = Edge.create("edge3", node2, <->, node2, eProp3)
  val edge4 = Edge.create("edge4", node1, :-:, node2, eProp4)

  val testGraph = InMemoryGraph.Builder[Id]
    .addNode(node1)
    .addNode(node2)
    .addNode(node2)
    .addEdges(Set(edge1, edge2))
    .addEdge(edge2)
    .addEdge(edge3)
    .addEdge(edge4)
    .build()

  "An in-memory graph " should "correctly add nodes, without duplicates" in {
    testGraph.nodes shouldBe Set(node1, node2)
  }

  it should " correctly add edges, without duplicaes" in {
    testGraph.edges shouldBe Set(edge1, edge2, edge3, edge4)
  }

  it should " be able to return nodes and edges by their ids" in {
    testGraph.getNodeById(node1.id) shouldBe Some(node1)
    testGraph.getEdgeById(edge1.id) shouldBe Some(edge1)
    testGraph.getNodeById(java.util.UUID.randomUUID()) shouldBe None
  }

  it should "be able to get edges connected to a node" in {
    testGraph.getOutEdges(node1) shouldBe Set(edge1)
    testGraph.getInEdges(node2) shouldBe Set(edge1, edge3)
    testGraph.getNodeEdges(node1) shouldBe Set(edge1, edge2, edge4)
    testGraph.getNodeEdges(node2) shouldBe Set(edge1, edge3, edge4)

  }

  it should "corecctly identify the relationship between nodes" in {
    testGraph.isParent(node1, node2) shouldBe true
    testGraph.isChild(node2, node1) shouldBe true
    testGraph.isSibling(node1, node2) shouldBe false
    testGraph.isAdjacent(node1, node2) shouldBe true

    testGraph.isParent(node2, node1) shouldBe false
    testGraph.isChild(node1, node2) shouldBe false
    testGraph.isSibling(node2, node1) shouldBe false
    testGraph.isAdjacent(node2, node1) shouldBe true

    testGraph.isParent(node1, node1) shouldBe false
    testGraph.isParent(node2, node2) shouldBe true
    testGraph.isChild(node2, node2) shouldBe true
    testGraph.isSibling(node2, node2) shouldBe true
    testGraph.isAdjacent(node2, node2) shouldBe true

  }

  it should "be able to get edges between two nodes" in {
    testGraph.getEdgesBetween(node1, node2).size shouldBe 2
    testGraph.getEdgesBetween(node1, node1).size shouldBe 1
    testGraph.getEdgesBetween(node2, node2).size shouldBe 1

    testGraph.getEdgesBetween(node1, node2) shouldBe Set(edge1, edge4)
    testGraph.getEdgesBetween(node2, node1) shouldBe Set(edge1, edge4)
    testGraph.getEdgesBetween(node1, node1) shouldBe Set(edge2)
    testGraph.getEdgesBetween(node2, node2) shouldBe Set(edge3)

  }

  it should "be able to find parent nodes of a given node" in {
    testGraph.getParentNodes(node2).map(_.label) shouldBe Set(node1, node2).map(_.label)
    testGraph.getParentNodes(node1) shouldBe Set.empty
  }

  it should "be able to find the child nodes of a given node" in {
    testGraph.getChildNodes(node1) shouldBe Set(node2)
    testGraph.getChildNodes(node2) shouldBe Set(node2)

  }

  it should "be able to find the sibling nodes of a given node" in {
    testGraph.getSiblingNodes(node1) shouldBe Set.empty
    testGraph.getSiblingNodes(node2) shouldBe Set(node2)
  }

  it should "be able to find adjacent nodes of a given node" in {
    testGraph.getAdjacentNodes(node1) shouldBe Set(node1, node2)
  }

}
