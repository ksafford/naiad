package tech.austininnovation.naiad.core
package transition

trait CompilePlease

// import org.scalatest._
// import cats.Id
//
// import graph._
//
// class TestTransitionGraph extends FlatSpec with Matchers {
//
//   val nProp1 = Map("Name" -> "A new Node")
//   val node1 = Node.create("First Node", nProp1)
//
//   val nProp2 = Map("Name" -> "An even newer node")
//   val node2 = Node.create("Second Node", nProp2)
//
//   val eProp1 = Map("Relationship" -> "Is older than")
//   val eProp2 = Map("Relationship" -> "Is connected to")
//   val eProp3 = Map("Relationship" -> "Is connected to")
//   val edge1 = Edge.create("edge1", node1, -->, node2, eProp1)
//   val edge2 = Edge.create("edge2", node1, -->, node2, eProp1)
//   val edge3 = Edge.create("edge3", node1, :-:, node1, eProp2)
//   val edge4 = Edge.create("edge4", node2, <->, node2, eProp3)
//
//   val g = InMemoryGraph.Builder[Id]
//     .addNode(node1)
//     .addNode(node2)
//     .addEdge(edge1)
//     .addEdge(edge2)
//     .addEdge(edge3)
//     .addEdge(edge4)
//     .build()
//
//   val testGraph = TransitionGraph(g)
//
//   "A transition graph " should "have nodes in the underlyingGraph" in {
//     testGraph.underlyingGraph.nodes shouldBe Set(node1, node2)
//   }
//
//   it should " have edges in the underlyingGraph" in {
//     testGraph.underlyingGraph.edges shouldBe Set(edge1, edge2, edge3, edge4)
//   }
//   "A graph" should "have nodes in the aggregateGraph" in {
//     testGraph.aggregateGraph.nodes shouldBe Set(node1, node2)
//   }
//
//   it should "have edges in the aggregateGraph" in {
//     testGraph.aggregateGraph.edges.size shouldBe 3
//   }
//
//   it should "correctly aggregate the number of edges between nodes" in {
//     testGraph.aggregateGraph.getOutEdges(node1).size shouldBe 2
//     testGraph.aggregateGraph.getOutEdges(node2).size shouldBe 1
//
//     testGraph.aggregateGraph.getEdgesBetween(node1, node2).size shouldBe 1
//     testGraph.aggregateGraph.getEdgesBetween(node1, node2).head.edgeProperties.getOrElse("occurances", None) shouldBe 2
//   }
//
//   it should "be able to return nodes and edges by their ids" in {
//     testGraph.aggregateGraph.getNodeById(node1.id) shouldBe Some(node1)
//     testGraph.underlyingGraph.getEdgeById(edge1.id) shouldBe Some(edge1)
//     testGraph.aggregateGraph.getNodeById(java.util.UUID.randomUUID()) shouldBe None
//   }
// }
