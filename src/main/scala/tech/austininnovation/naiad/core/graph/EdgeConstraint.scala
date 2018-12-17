package tech.austininnovation.naiad.core
package graph

sealed trait EdgeConstraint {
  def check(edge: Edge): Either[String, Edge]
}

trait UncontrainedEdges {
  def check(edge: Edge): Either[String, Edge] = Right(edge)
}

trait DirectedEdgeConstraint {
  def check(edge: Edge): Either[String, Edge] = {
    edge match {
      case Edge(_, _, _, -->, _, _) => Right(edge)
      case Edge(_, _, _, <->, _, _) => Right(edge)
      case _ => Left("Cannot add Undirected Edges with DirectedEdgeConstraint")
    }
  }
}
