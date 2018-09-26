package tech.austininnovation.naiad.core.graph

import java.util.UUID

import cats.Monad
import cats.implicits._

case class InMemoryGraph[F[_]: Monad] private (
  nodes: Set[Node],
  edges: Set[Edge]) extends GraphAlg[F] {

  def getNodeById(uuid: UUID): F[Option[Node]] = {
    Monad[F].pure(nodes.find(_.id == uuid))
  }

  def getEdgeById(uuid: UUID): F[Option[Edge]] = {
    Monad[F].pure(edges.find(_.id == uuid))
  }

  def getNodeEdges(node: Node): F[Set[Edge]] = {
    Monad[F].pure(edges.filter(e => e.left == node | e.right == node))
  }

  def getOutEdges(node: Node): F[Set[Edge]] = {
    val nodeEdges = getNodeEdges(node)

    nodeEdges.map(
      setEdges => setEdges.filter(
        e => (
          (e.direction == <-> & e.right == node) |
          (e.direction == --> & e.left == node))))
  }

  def getInEdges(node: Node): F[Set[Edge]] = {
    val nodeEdges = getNodeEdges(node)

    nodeEdges.map(
      setEdges => setEdges.filter(
        e => (
          (e.direction == <-> & e.left == node) |
          (e.direction == --> & e.right == node))))
  }

}

object InMemoryGraph {
  case class Builder[F[_]: Monad]() {
    var nodes: Set[Node] = Set.empty
    var edges: Set[Edge] = Set.empty
    def addNode(node: Node) = { nodes += node; this }
    def addNodes(ns: Set[Node]) = { nodes ++= ns; this }
    def addEdge(edge: Edge) = { edges += edge; this }
    def addEdges(es: Set[Edge]) = { edges ++= es; this }
    def build(): InMemoryGraph[F] = InMemoryGraph[F](nodes, edges)
  }

}

