package tech.austininnovation.naiad.core.graph

import java.util.UUID

import cats.{ Functor, InvariantMonoidal }
import cats.syntax.functor
import cats.implicits._

trait GraphAlg[F[_]] {

  def nodes: Set[Node]
  def edges: Set[Edge]

  def getNodeById(uuid: UUID): F[Option[Node]]
  def getEdgeById(uuid: UUID): F[Option[Edge]]

  def getNodeEdges(node: Node): F[Set[Edge]]
  def getOutEdges(node: Node): F[Set[Edge]]
  def getInEdges(node: Node): F[Set[Edge]]

  def isParent(n1: Node, n2: Node)(implicit f: Functor[F]): F[Boolean] = {
    val out: F[Set[Edge]] = getOutEdges(n1)
    out.map(es => !es.filter(_.right == n2).isEmpty)
  }

  def isChild(n1: Node, n2: Node)(implicit f: Functor[F]): F[Boolean] = isParent(n2, n1)

  def isAdjacent(n1: Node, n2: Node)(implicit f: Functor[F]): F[Boolean] = {
    val nodeEdges = getNodeEdges(n1)
    nodeEdges.map(es => es.filter(e => e.right == n2 || e.left == n2).isEmpty)
  }

  def isSibling(n1: Node, n2: Node)(implicit im: InvariantMonoidal[F], f: Functor[F]): F[Boolean] = {
    val nodeEdges = getNodeEdges(n1)
    nodeEdges.map(es => !es.filter(e => List(e.left, e.right).contains(n2) && (e.direction == <->)).isEmpty)
  }

  def getAdjacentNodes(node: Node)(implicit im: InvariantMonoidal[F], f: Functor[F]): F[Set[Node]] = {
    val adjacentNodes = nodes.map(n => (n, isAdjacent(n, node))).collect {
      case (n, fb) if fb == im.point(true) => n
    }
    im.point(adjacentNodes)
  }

  def getChildNodes(node: Node)(implicit im: InvariantMonoidal[F], f: Functor[F]): F[Set[Node]] = {
    val childNodes = nodes.map(n => (n, isChild(n, node))).collect {
      case (n, fb) if fb == im.point(true) => n
    }
    im.point(childNodes)
  }

  def getParentNodes(node: Node)(implicit im: InvariantMonoidal[F], f: Functor[F]): F[Set[Node]] = {
    val parentNodes = nodes.map(n => (n, isParent(n, node))).collect {
      case (n, fb) if fb == im.point(true) => n
    }
    im.point(parentNodes)
  }

  def getSiblingNodes(node: Node)(implicit im: InvariantMonoidal[F], f: Functor[F]): F[Set[Node]] = {
    val siblingNodes = nodes.map(n => (n, isSibling(n, node))).collect {
      case (n, fb) if (fb == im.point(true)) => n
    }
    im.point(siblingNodes)
  }

  // Something clean and re-usable like this would be nicer. The implicit parameters make it hard.
  //def getNodesConditionally(node: Node, pred: (Node, Node) => F[Boolean]): F[Set[Node]] = {
  //  val conditionalNodes = nodes.map(n => (n, pred(n, node))).collect {
  //    case (n, fb) if fb == im.point(true) => n
  //  }
  //  im.point(conditionalNodes)
  //}
  //
  //
  //  def getAdjacentNodes(implicit im: InvariantMonoidal[F], f: Functor[F]) = getNodesConditionally(_: Node, isAdjacent)
  //  def getChildNodes(implicit im: InvariantMonoidal[F], f: Functor[F]) = getNodesConditionally(_: Node, isChild)
  //  def getParentNodes(implicit im: InvariantMonoidal[F], f: Functor[F]) = getNodesConditionally(_: Node, isParent)
  //  def getSiblingNodes = getNodesConditionally(n: Node, n1 => n2 => implicit im => f => isSibling(n1, n2)(im, f))

}
