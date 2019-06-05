package tech.austininnovation.naiad.core
package graph

//import cats.Monad


final case class InMemoryGraph[F[_]] private (
  graph: InMemorySetBackend[F]) extends GraphAlg[Set, F]

object InMemoryGraph {
  case class Builder[F[_]]() {
    var nodes: Set[Node] = Set.empty
    var edges: Set[Edge] = Set.empty
    def addNode(node: Node) = { nodes += node; this }
    def addNodes(ns: Set[Node]) = { nodes ++= ns; this }
    def addEdge(edge: Edge) = { edges += edge; this }
    def addEdges(es: Set[Edge]) = { edges ++= es; this }
    def build(): InMemoryGraph[F] = {
      val backend = InMemorySetBackend[F](nodes, edges)
      InMemoryGraph(backend)
    }
  }

}

