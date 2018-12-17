package tech.austininnovation.naiad.core

package object graph {
  implicit def alg2Backend[F[_]](a: graph.InMemoryGraph[F]): graph.InMemorySetBackend[F] = a.graph
}
