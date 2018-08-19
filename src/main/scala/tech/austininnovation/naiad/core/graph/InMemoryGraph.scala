package tech.austininnovation.naiad.core.graph

import java.util.UUID

import cats.Monad
import cats.implicits._

case class InMemoryGraph[F[_]: Monad](
  nodes: Option[Set[Node]],
  edges: Option[Set[Edge]]) extends GraphAlg[F] {

  def addNode(node: Node): InMemoryGraph[F] = {
    this.copy(
      nodes = nodes match {
        case Some(ns) => Some(ns + node)
        case None => Some(Set(node))
      })
  }

  def addEdge(edge: Edge): InMemoryGraph[F] = {
    val newEdgeSet = this.edges match {
      case None => Set(edge)
      case Some(es) => es + edge
    }
    this.copy(edges = Some(newEdgeSet))
  }

  def getNodeById(uuid: UUID): F[Option[Node]] = {
    Monad[F].pure(nodes.flatMap(s => s.find(_.id == uuid)))
  }

  def getEdgeById(uuid: UUID): F[Option[Edge]] = {
    Monad[F].pure(edges.flatMap(s => s.find(_.id == uuid)))
  }

  def getNodeEdges(node: Node): F[Option[Set[Edge]]] = {
    val nes = for {
      setOfEdges: Set[Edge] <- edges
      nodeEdges: Set[Edge] = setOfEdges.filter(e => e.left == node | e.right == node)
    } yield nodeEdges

    Monad[F].pure(nes)
  }

  def getOutEdges(node: Node): F[Option[Set[Edge]]] = {
    val nodeEdges = getNodeEdges(node)

    nodeEdges.map(
      optionSetEdges => optionSetEdges.map(
        setEdges => setEdges.filter(
          e => (
            (e.direction == <-- & e.right == node) |
            (e.direction == --> & e.left == node)))))
  }

  def getInEdges(node: Node): F[Option[Set[Edge]]] = {
    val nodeEdges = getNodeEdges(node)

    nodeEdges.map(
      optionSetEdges => optionSetEdges.map(
        setEdges => setEdges.filter(
          e => (
            (e.direction == <-- & e.left == node) |
            (e.direction == --> & e.right == node)))))
  }

  def getParentNodes(node: Node): F[Option[Set[Node]]] = {
    val inEdges = getInEdges(node)
    inEdges.map(
      optionSetEdges => optionSetEdges.map(
        setEdges => setEdges.map(_.left)))
  }

  def getChildNodes(node: Node): F[Option[Set[Node]]] = {
    val outEdges = getOutEdges(node)
    outEdges.map(
      optionSetEdges => optionSetEdges.map(
        setEdges => setEdges.map(_.right)))
  }

  def getSiblingNodes(node: Node): F[Option[Set[Node]]] = {
    val nodeEdges = getNodeEdges(node)
    val sibNodes = nodeEdges.map(
      optionSetEdges => optionSetEdges.map(
        setEdges => setEdges.filter(_.direction == ---).flatMap(
          edge => Set(edge.left, edge.right))))

    // At this point, uEdges also contains the original node so filter it
    sibNodes.map(optionSetNodes => optionSetNodes.map(
      setNodes => setNodes - node))
  }

  def getAdjacentNodes(node: Node): F[Option[Set[Node]]] = ???

  def getRootNodes(): F[Option[Set[Node]]] = ???

  override def getLeafNodes(): F[Option[Set[Node]]] = ???

}

