package tech.austininnovation.naiad.core
package graph

import cats.{ InvariantMonoidal, Monad }
import java.util.UUID
import scala.collection.GenTraversable
import scala.collection.generic.GenericTraversableTemplate


case class InvalidEdgeException(message: String) extends Exception(message)
case class InvalidNodeException(message: String) extends Exception(message)

trait GraphBackend[T[A] <: GenTraversable[A] with GenericTraversableTemplate[A, T], F[T]] {

  /**
    *  We need a filter that returns T[A], not a GenTraversable. We need a collections builder for that.
    */
  def filter[A](xs: T[A])(pred: A => Boolean) = {
    val builder = xs.genericBuilder[A]
    xs.foreach(x => if (pred(x)) { builder += x })
    builder.result()
  }

  def nodes: T[Node]
  def edges: T[Edge]

  //  def liftEdge(e: Edge) = Monad[F].pure(Monad[T].pure(e))
  //  def liftNode(n: Node) = Monad[F].pure(Monad[T].pure(n))

  //  def liftEdges(es: T[Edge]) = Monad[F].pure(T[Edge])
  //  def liftNodes(ns: T[Node]) = Monad[F].pure(T[Node])

  //  def addNode(node: Either[String, Node]): T[Node]
  //  def addEdge(edge: Either[String, Edge]): T[Edge]

  /**
   * Abstract method
   *
   * @param uuid java.util.UUID
   * @return F[Option[Node]
   */
  def getNodeById(uuid: UUID)(implicit m: Monad[F]): F[Option[Node]]

  /**
   * Abstract method
   *
   * @param uuid java.util.UUID
   * @return F[Option[Edge]
   */
  def getEdgeById(uuid: UUID)(implicit m: Monad[F]): F[Option[Edge]]

  /**
   * Abstract method to retrieve all edges connected to a given node
   *
   * @param node Node
   * @return F[T[Node]]: Will return an empty set if there are no edges
   */
  def getNodeEdges(node: Node)(implicit m: Monad[F]): F[T[Edge]]

  /**
   * Abstract method to retrieve all edges connected to a given node that are
   * directed away from the node. This should include both edges with
   * direction --> and <->
   *
   * @param node
   * @return F[T[Node]] Will return an empty set if there are no edges
   */
  def getOutEdges(node: Node)(implicit m: Monad[F]): F[T[Edge]]

  /**
   * Abstract method to retrieve all edges connected to a given node that are
   * directed toward the node. This should include both edges with direction <--
   * and <->
   *
   * @param node Node:
   * @return F[T[Node]]: Will return an empty set if there are no edges
   *
   */
  def getInEdges(node: Node)(implicit m: Monad[F]): F[T[Edge]]


  /**
    * Return all edges between two given nodes. Node order doesn't matter.
    *
    * @param n1 Node
    * @param n2 Node: Can be the same as n1
    * @param m implicit Monad[F]: needed for a call to m.map2.
    * @return F[T[Edge]]: All edges with the given nodes at their endpoints.
    */
  def getEdgesBetween(n1: Node, n2: Node)(implicit m: Monad[F]): F[T[Edge]] = {

    val n1es = getNodeEdges(n1)

    val n2es = getNodeEdges(n2)

    def f(xs: T[Edge], ys: T[Edge]): T[Edge] =
      filter(xs)(x => (ys.exists(y => y == x) && Set(n1, n2).contains(x.right) && Set(n1, n2).contains(x.left)))

    m.map2(n1es, n2es)(f)

  }

  /**
    * Check if n1 is the parent of n2. Here, a parent requires and edge from n1
    * to n2. E.g: n1 --> n2. Undirected edges create an adjacent relationship,
    * not parent/child. In the case n1 <-> n2 n1 is both parent and child to n2
    *
    * @param n1 Node: The node we are checking for parenthood: is n1 a parent of
    * n2?
    * @param n2 Node: The node we are checking against: is n2 a child of n1?
    * @param f implicit Functor: To allow for map operations.
    * @return F[Boolean]
    */
  def isParent(n1: Node, n2: Node)(implicit m: Monad[F]): F[Boolean] = {
    val out: F[T[Edge]] = getOutEdges(n1)(m)

    m.map(out)(es => filter(es)(e => e.asInstanceOf[Edge].right == n2).nonEmpty)

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
  def isChild(n1: Node, n2: Node)(implicit m: Monad[F]) = isParent(n2, n1)

  /**
   * Check if two nodes are connected by any kind of edge.
   *
   * @param n1 Node
   * @param n2 Node
   * @param f implicit Functor: To allow for map operations.
   * @return F[Boolean]: Are these two nodes connected by an edge?
   */
  def isAdjacent(n1: Node, n2: Node)(implicit m: Monad[F]): F[Boolean] = {
    val nodeEdges = getNodeEdges(n1)(m)
    m.map(nodeEdges){
      es => filter(es)(e => e.asInstanceOf[Edge].right == n2 || e.asInstanceOf[Edge].left == n2).nonEmpty
    }
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

  def isSibling(n1: Node, n2: Node)(implicit m: Monad[F]): F[Boolean] = {
    val nodeEdges = getNodeEdges(n1)(m)
    m.map(nodeEdges){
      es =>
      filter(es)(e =>
          // One of the connected edges is n2. We wrap in a list to get access to contains(). Then check for direction type.
          List(e.asInstanceOf[Edge].left, e.asInstanceOf[Edge].right).contains(n2) && e.asInstanceOf[Edge].direction == <->
        )
        .nonEmpty
    }
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
  def getAdjacentNodes(node: Node)(implicit im: InvariantMonoidal[F], m: Monad[F]): F[T[Node]] = {
    val nodeEdges = getNodeEdges(node)(m)
    val nodes = m.map(nodeEdges)(es => es.flatMap(e => Set(e.asInstanceOf[Edge].left, e.asInstanceOf[Edge].right)))
    m.map(nodes)(
      ns => ns.map(
        n => (n, isAdjacent(n, node))).collect {
        case (n, fb) if fb == im.point(true) => n
      }).asInstanceOf[F[T[Node]]]
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
  def getChildNodes(node: Node)(implicit im: InvariantMonoidal[F], m: Monad[F]): F[T[Node]]  = {
    val adjNodes = getAdjacentNodes(node)(im, m)
    m.map(adjNodes)(
      ns => ns.map(
        n => (n, isChild(n.asInstanceOf[Node], node))).collect {
        case (n, fb) if fb == im.point(true) => n
      }).asInstanceOf[F[T[Node]]]
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
  def getParentNodes(node: Node)(implicit im: InvariantMonoidal[F], m: Monad[F]): F[T[Node]] = {
    val adjNodes = getAdjacentNodes(node)(im, m)
    m.map(adjNodes)(
      ns => ns.map(n => (n, isParent(n.asInstanceOf[Node], node))).collect {
        case (n, fb) if fb == im.point(true) => n
      }).asInstanceOf[F[T[Node]]]
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
  def getSiblingNodes(node: Node)(implicit im: InvariantMonoidal[F], m: Monad[F]): F[T[Node]] = {
    val adjNodes = getAdjacentNodes(node)(im, m)
    m.map(adjNodes)(
      ns => ns.map(n => (n, isSibling(n.asInstanceOf[Node], node))).collect {
        case (n, fb) if (fb == im.point(true)) => n
      }).asInstanceOf[F[T[Node]]]
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

// object Implicits {
//   implicit val GraphBackendInMemorySet[F]: GraphBackend[F, T] = new GraphBackend[F, T] {
//
//     def nodes: scala.collection.immutable.T[Node] = T[Node]()
//     def edges: scala.collection.immutable.T[Edge] = T[Edge]()
//
//     def addNode(node: Either[String, Node]): T[Node] = {
//       node match {
//         case Right(n) => nodes + n
//         case Left(message) => throw InvalidNodeException(message)
//       }
//     }
//
//     def addEdge(edge: Either[String, Edge]): T[Edge] = {
//       edge match {
//         case Right(e) => edges + e
//         case Left(message) => throw InvalidEdgeException(message)
//       }
//     }
//   }
// }
