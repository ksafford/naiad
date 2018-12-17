package tech.austininnovation.naiad.core
package graph

import cats.{ Monad, InvariantMonoidal }
import cats.implicits._

import java.util.UUID

case class InMemorySetBackend[F[_]](nodes: Set[Node], edges: Set[Edge]) extends GraphBackend[F, Set] {

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

    nodeEdges.map(
      setEdges => setEdges.filter(
        e => (
          (e.direction == <-> & e.right == node) |
          (e.direction == --> & e.left == node))))
  }

  def getInEdges(node: Node)(implicit m: Monad[F]): F[Set[Edge]] = {
    val nodeEdges = getNodeEdges(node)(m)

    nodeEdges.map(
      setEdges => setEdges.filter(
        e => (
          (e.direction == <-> & e.left == node) |
          (e.direction == --> & e.right == node))))
  }

  //def addNode(node: Either[String, Node]): Set[Node]
  //def addEdge(edge: Either[String, Edge]): Set[Edge]

  /**
   * Return all edges between two given nodes. Node order doesn't matter.
   *
   * @param n1 Node
   * @param n2 Node: Can be the same as n1
   * @param m implicit Monad: needed to allow the return value of getNodeEdges
   * to be mappable and filterable.
   * @return F[Set[Edge]]: All edges with the given nodes at their endpoints.
   */
  def getEdgesBetween(n1: Node, n2: Node)(implicit m: Monad[F]): F[Set[Edge]] = {
    getNodeEdges(n1)(m).map(e1s =>
      e1s.filter(e => Set(e.left, e.right) == Set(n1, n2)))
  }

  /**
   * Check if n1 is the parent of n2. Here, a parent requires and edge from n1
   * to n2. E.g: n1 --> n2. Undirected edges create an adjacent relationship,
   * not parent/child. In the case n1 <-> n2 n1 is both parent and child to n2
   *
   * We could use a typeclass to make this infix
   *
   * @param n1 Node: The node we are checking for parenthood: is n1 a parent of
   * n2?
   * @param n2 Node: The node we are checking against: is n2 a child of n1?
   * @param f implicit Functor: To allow for map operations.
   * @return F[Boolean]
   */
  def isParent(n1: Node, n2: Node)(implicit m: Monad[F]): F[Boolean] = {
    val out: F[Set[Edge]] = getOutEdges(n1)(m)
    out.map(es => !es.filter(_.right == n2).isEmpty)
  }

  /**
   * Inverse of isParent. Check if n1 is the child of n2. Here, a child requires
   * and edge from n2 to n1. E.g: n2 --> n1. Undirected edges create an adjacent
   * relationship, not parent/child. In the case n1 <-> n2 n1 is both parent and
   * child to n2
   *
   * We could use a typeclass to make this infix
   *
   * @param n1 Node: The node we are checking for childhood: is n1 a child of n2?
   * @param n2 Node: The node we are checking against: is n2 a parent of n1?
   * @param f implicit Functor: To allow for map operations.
   * @return F[Boolean]
   */
  override def isChild(n1: Node, n2: Node)(implicit m: Monad[F]): F[Boolean] = isParent(n2, n1)(m)

  /**
   * Check if two nodes are connected by any kind of edge.
   *
   * @param n1 Node
   * @param n2 Node
   * @param f implicit Functor: To allow for map operations.
   * @return F[Boolean]: Are these two nodes connected by and edge?
   */
  def isAdjacent(n1: Node, n2: Node)(implicit m: Monad[F]): F[Boolean] = {
    val nodeEdges = getNodeEdges(n1)(m)
    nodeEdges.map(es => !es.filter(e => e.right == n2 || e.left == n2).isEmpty)
  }

  /**
   * Check if two nodes are connected by a bi-directional edge.
   * E.g. n1 <-> n2
   *
   * @param n1 Node
   * @param n2 Node
   * @param f implicit Functor: To allow for map operations.
   * @return F[Boolean]: Are these nodes connected by a bi-directional edge?
   */
  //TODO: Ugly. But can't seem to use filterNot correctly.
  def isSibling(n1: Node, n2: Node)(implicit m: Monad[F]): F[Boolean] = {
    val nodeEdges = getNodeEdges(n1)(m)
    nodeEdges.map(
      es => es.filter(
        e => (List(e.left, e.right).contains(n2) && (e.direction == <->))).isEmpty).map(e => !e)
  }

  /**
   * Get all nodes connected to the given node, via any edge.
   *
   * @param node Node
   * @param implicit im InvariantMonoidal: Provides point, to lift a value to
   * the return type F
   * @param f implicit Functor: To allow for map operations.
   * @return F[Set[Node]]: All nodes connected to a given node, by any edge.
   */
  def getAdjacentNodes(node: Node)(implicit im: InvariantMonoidal[F], m: Monad[F]): F[Set[Node]] = {
    val nodeEdges = getNodeEdges(node)(m)
    val nodes = nodeEdges.map(es => es.flatMap(e => Set(e.left, e.right)))
    nodes.map(
      ns => ns.map(
        n => (n, isAdjacent(n, node))).collect {
          case (n, fb) if fb == im.point(true) => n
        })
  }

  /**
   * Get all children of the given node. Children are connected by a directed
   * edge, pointing away from the given node. This will include both --> and <->
   * edges.
   *
   * @param node Node
   * @param implicit im InvariantMonoidal: Provides point, to lift a value to
   * the return type F
   * @param f implicit Functor: To allow for map operations.
   * @return F[Set[Node]]: All child nodes of a given node.
   */
  def getChildNodes(node: Node)(implicit im: InvariantMonoidal[F], m: Monad[F]): F[Set[Node]] = {
    val adjNodes = getAdjacentNodes(node)(im, m)
    adjNodes.map(
      ns => ns.map(
        n => (n, isChild(n, node))).collect {
          case (n, fb) if fb == im.point(true) => n
        })
  }

  /**
   * Get all parents of the given node. Parents are connected by a directed
   * edge, pointing toward from the given node. This will include both --> and
   * <-> edges.
   *
   * @param node Node
   * @param implicit im InvariantMonoidal: Provides point, to lift a value to
   * the return type F
   * @param f implicit Functor: To allow for map operations.
   * @return F[Set[Node]]: All parent nodes of a given node.
   */
  def getParentNodes(node: Node)(implicit im: InvariantMonoidal[F], m: Monad[F]): F[Set[Node]] = {
    val adjNodes = getAdjacentNodes(node)(im, m)
    adjNodes.map(
      ns => ns.map(n => (n, isParent(n, node))).collect {
        case (n, fb) if fb == im.point(true) => n
      })
  }

  /**
   * Get all siblings of the given node. Siblings are connected by a bi-directional edges, <->
   *
   * @param node Node
   * @param implicit im InvariantMonoidal: Provides point, to lift a value to
   * the return type F
   * @param f implicit Functor: To allow for map operations.
   * @return F[Set[Node]]: All sibling nodes of a given node.
   */
  def getSiblingNodes(node: Node)(implicit im: InvariantMonoidal[F], m: Monad[F]): F[Set[Node]] = {
    val adjNodes = getAdjacentNodes(node)(im, m)
    adjNodes.map(
      ns => ns.map(n => (n, isSibling(n, node))).collect {
        case (n, fb) if (fb == im.point(true)) => n
      })
  }

  // Something clean and re-usable like this would be nicer. The implicit
  // parameters make it hard.
  //
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
