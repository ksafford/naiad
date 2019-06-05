package tech.austininnovation.naiad.core
package graph

import cats.Monad

import java.util.UUID

final case class InMemorySetBackend[F[_]](nodes: Set[Node], edges: Set[Edge]) extends GraphBackend[Set, F] {

  def getNodeById(uuid: UUID)(implicit m: Monad[F]): F[Option[Node]] = {
    Monad[F].pure(nodes.find(_.id == uuid))
  }

  def getEdgeById(uuid: UUID)(implicit m: Monad[F]): F[Option[Edge]] = {
    Monad[F].pure(edges.find(_.id == uuid))
  }

  def getNodeEdges(node: Node)(implicit m: Monad[F]): F[Set[Edge]] = {
    Monad[F].pure(edges.filter(e => e.left == node | e.right == node))
  }

  def getOutEdges(node: Node)(implicit m: Monad[F]): F[Set[Edge]] = {
    val nodeEdges = getNodeEdges(node)(m)

    m.map(nodeEdges)(
      setEdges => setEdges.filter(
        e => (
          (e.direction == <-> & e.right == node) |
          (e.direction == --> & e.left == node))))
  }

  def getInEdges(node: Node)(implicit m: Monad[F]): F[Set[Edge]] = {
    val nodeEdges = getNodeEdges(node)(m)

    m.map(nodeEdges)(
      setEdges => setEdges.filter(
        e => (
          (e.direction == <-> & e.left == node) |
          (e.direction == --> & e.right == node))))
  }

  //def addNode(node: Either[String, Node]): Set[Node]
  //def addEdge(edge: Either[String, Edge]): Set[Edge]

}
