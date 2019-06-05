package tech.austininnovation.naiad.core
package graph

trait GraphConstraint[T[_]] {
  def edges: T[Edge]
  def nodes: T[Node]
}

trait Directed[T[_]] extends GraphConstraint[T] {
  //override abstract def edges(): Set[Edge] = {
  //  super.edges.filter {
  //    e => (List(-->, <->).contains(e.direction))
  //  }
  //}
}
