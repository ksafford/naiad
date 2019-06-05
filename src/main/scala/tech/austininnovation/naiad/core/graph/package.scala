package tech.austininnovation.naiad.core

import cats.Monad

package object graph {
  implicit def alg2Backend[F[_] <: Monad[F]](a: graph.InMemoryGraph[F]): graph.InMemorySetBackend[F] = a.graph
}
