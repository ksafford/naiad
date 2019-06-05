package tech.austininnovation.naiad.core
package transition

// import cats.{ Monad }
// import graph.{ GraphAlg }
// import tech.austininnovation.naiad.core.graph.{ -->, Edge, InMemoryGraph, Node }

trait TransitionGraphAlg[F[_]]

// case class TransitionGraph[F[_]: Monad] private (
//   underlyingGraph: GraphAlg[F],
//   aggregateGraph: GraphAlg[F]) {
// }
//
// object TransitionGraph {
//   def apply[F[_]: Monad](underlyingGraph: GraphAlg[F]): TransitionGraph[F] = {
//     val edgeCounter: scala.collection.mutable.Map[(Node, Node), Int] = scala.collection.mutable.Map()
//     underlyingGraph.edges.foreach {
//       e =>
//         edgeCounter.update(
//           (e.left, e.right), edgeCounter.get((e.left, e.right)).getOrElse(0) + 1)
//     }
//
//     val aggregateEdges: Set[Edge] = edgeCounter.map {
//       case ((left, right), count) => Edge.create(
//         "Aggregate Edge",
//         left,
//         -->,
//         right,
//         Map("occurances" -> count))
//     }.toSet
//
//     /*
//      This is not very flexible.
//
//      TODO: Find a way to generalize the
//      TransitionGraph over different graph implimentations so we aren't
//      hard-coding the InMemoryGraph.Builder here, but instead a generic
//      GraphAlg.Builder or something.
//      */
//     val aggregateGraph = InMemoryGraph.Builder[F]
//       .addNodes(underlyingGraph.nodes)
//       .addEdges(aggregateEdges)
//       .build()
//     TransitionGraph(underlyingGraph, aggregateGraph)
//   }
// }
//
