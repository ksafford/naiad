package tech.austininnovation.naiad.core.graph

import org.scalatest._

class TestNode extends FlatSpec with Matchers {
  val nProp1 = Map("Name" -> "A new Node")
  val node1 = Node.create("FirstNode", nProp1)

  val nProp2 = Map("Name" -> "An even newer node")
  val node2 = Node.create("SecondNode", nProp2)

  val eProp1 = Map("Relationship" -> "Is older than")
  val eProp2 = Map("Relationship" -> "Is connected to")

  val edge1 = Edge.create("First Edge", node1, -->, node2, eProp1)
  val edge2 = Edge.create("Second Edge", node1, :-:, node1, eProp2)

  "A node " should "create edges" in {
    (node1 ---- node2)("Infix Edge", eProp1).left shouldBe node1
    (node1 ---- node2)("Infix Edge", eProp1).right shouldBe node2
    (node1 ---- node2)("Infix Edge", eProp1).direction shouldBe :-:

    (node1 ---> node2)("Infix Edge", eProp1).left shouldBe node1
    (node1 ---> node2)("Infix Edge", eProp1).right shouldBe node2
    (node1 ---> node2)("Infix Edge", eProp1).direction shouldBe -->

    (node1 <--> node2)("Infix Edge", eProp1).left shouldBe node1
    (node1 <--> node2)("Infix Edge", eProp1).right shouldBe node2
    (node1 <--> node2)("Infix Edge", eProp1).direction shouldBe <->

  }
}
