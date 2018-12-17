package tech.austininnovation.naiad.core
package graph

trait GraphConstraint {
  def edges: Set[Edge]
  def nodes: Set[Node]
}

trait Directed extends GraphConstraint {
  //override abstract def edges(): Set[Edge] = {
  //  super.edges.filter {
  //    e => (List(-->, <->).contains(e.direction))
  //  }
  //}
}
