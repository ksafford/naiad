package tech.austininnovation.naiad.core.graph

import org.scalatest._
import cats.effect.IO
import cats.Id
import cats.syntax.functor
import cats.implicits._

import tech.austininnovation.naiad.core.graph.EdgeValueImplicits._
import tech.austininnovation.naiad.core.graph.NodeValueImplicits._

class TestInMemoryGraph extends FlatSpec with Matchers {

  val nProp1 = Map("Name" -> "A new Node")
  val node1 = Node.create("First Node", nProp1)

  val nProp2 = Map("Name" -> "An even newer node")
  val node2 = Node.create("Second Node", nProp2)

  val eProp1 = Map("Relationship" -> "Is older than")
  val eProp2 = Map("Relationship" -> "Is connected to")
  val eProp3 = Map("Relationship" -> "Is connected to")
  val edge1 = Edge.create("First Edge", node1, -->, node2, eProp1)
  val edge2 = Edge.create("Second Edge", node1, :-:, node1, eProp2)
  val edge3 = Edge.create("Self Edge", node2, <->, node2, eProp3)

  val testGraph = InMemoryGraph.Builder[Id]
    .addNode(node1)
    .addNode(node2)
    .addEdge(edge1)
    .addEdge(edge2)
    .addEdge(edge3)
    .build()

  "A graph " should "be able to have nodes added" in {
    testGraph.nodes shouldBe Set(node1, node2)
  }

  it should " be able to have edged added" in {
    testGraph.edges shouldBe Set(edge1, edge2, edge3)
  }

  it should " be able to return nodes and edges by their ids" in {
    testGraph.getNodeById(node1.id) shouldBe Some(node1)
    testGraph.getEdgeById(edge1.id) shouldBe Some(edge1)
    testGraph.getNodeById(java.util.UUID.randomUUID()) shouldBe None
  }

  it should "be able to get edges connected to a node" in {
    testGraph.getOutEdges(node1) shouldBe Set(edge1)
    testGraph.getInEdges(node2) shouldBe Set(edge1, edge3)
    testGraph.getNodeEdges(node1) shouldBe Set(edge1, edge2)
    testGraph.getNodeEdges(node2) shouldBe Set(edge1, edge3)

  }

  it should "be able to find parent nodes of a given node" in {
    testGraph.getParentNodes(node2) shouldBe Set(node1, node2)
    testGraph.getParentNodes(node1) shouldBe Set.empty
  }

  it should "be able to find the child nodes of a given node" in {
    testGraph.getChildNodes(node1) shouldBe Set(node2)
    testGraph.getChildNodes(node2) shouldBe Set(node2)

  }

  it should "be able to find the sibling nodes of a given node" in {
    testGraph.getSiblingNodes(node1) shouldBe Set.empty
    //testGraph.getSiblingNodes(node2) shouldBe Set.empty
  }

}
