package tech.austininnovation.naiad.core.graph

import java.util.UUID

import cats.Monad

case class InMemoryGraph[F[_]: Monad] (
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
    implicitly[Monad[F]].pure(nodes.flatMap(s => s.find(_.id == uuid)))
  }

  def getEdgeById(uuid: UUID): F[Option[Edge]] = {
    implicitly[Monad[F]].pure(edges.flatMap(s => s.find(_.id == uuid)))
  }

  def getNodeEdges(node: Node): F[Option[Set[Edge]]] = for {
    oSetOfEdges: Option[Set[Edge]] <- edges
    setOfEdges: Set[Edge] <- oSetOfEdges
    nodeEdges: Set[Edge] =  setOfEdges.filter(e => e.left == node | e.right == node)
  } yield implicitly[Monad[F]].pure(nodeEdges)

  def getOutEdges(node: Node): F[Option[Set[Edge]]] = {
    for {
      fe <- getNodeEdges(node)
      oSetOfEdges: Option[Set[Edge]] <- fe
      setOfEdges: Set[Edge] <- oSetOfEdges
      outEdges: Set[Edge] = setOfEdges.filter(_.direction == -->)
    } yield implicitly[Monad[F]].pure(outEdges)
  }

  def getInEdges(node: Node): F[Option[Set[Edge]]] = {
    for {
      fe <- getNodeEdges(node)
      oSetOfEdges: Option[Set[Edge]] <- fe
      setOfEdges: Set[Edge] <- oSetOfEdges
      outEdges: Set[Edge] = setOfEdges.filter(_.direction == <--)
    } yield implicitly[Monad[F]].pure(outEdges)
  }

  def getParentNodes(node: Node): F[Option[Set[Node]]] = {
    for {
      oSetOfEdges: Option[Set[Edge]] <- getNodeEdges(node)
      setOfEdges: Set[Edge] <- oSetOfEdges
      toParents: Set[Edge] = setOfEdges.filter(_.direction == <--)
      parents = toParents.map(_.left)
    } implicitly[Monad[F]].pure(parents)
  }

  def getChildNodes(node: Node): F[Option[Set[Node]]] = ???

  def getSiblingNodes(node: Node): F[Option[Set[Node]]] = ???

  def getAdjacentNodes(node: Node): F[Option[Set[Node]]] = ???

  def getRootNodes(): F[Option[Set[Node]]] = ???

  override def getLeafNodes(): F[Option[Set[Node]]] = ???

}

