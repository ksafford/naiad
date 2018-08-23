package tech.austininnovation.naiad.core.graph

import org.scalatest._

import tech.austininnovation.naiad.core.graph.EdgeValueImplicits._
import tech.austininnovation.naiad.core.graph.NodeValueImplicits._

class TestEdge extends FlatSpec with Matchers {

  val nProp1 = List(NodeProperty("Name", "A new Node"))
  val node1 = Node.create("FirstNode", nProp1)

  val nProp2 = List(NodeProperty("Name", "An even newer node"))
  val node2 = Node.create("SecondNode", nProp2)

  val eProp1 = List(EdgeProperty("Relationship", "Is older than"))
  val eProp2 = List((EdgeProperty("Relationship", "Is connected to")))

  val edge1 = Edge.create("First Edge", node1, -->, node2, eProp1)
  val edge2 = Edge.create("Second Edge", node1, ---, node1, eProp2)

  "An edge" should "have properties" in {
    edge1.edgeProperties shouldBe List(EdgeProperty("Relationship", "Is older than"))
  }

  it should "be able to get the names and value of properties" in {
    val props = edge1.edgeProperties.head
    props.name shouldBe "Relationship"
    props.value shouldBe "Is older than"
  }

  it should "be able to find properties by name" in {
    edge1.get("Relationship").getOrElse(None) shouldBe Some("Is older than")
  }
}