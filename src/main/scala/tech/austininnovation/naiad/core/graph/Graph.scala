package tech.austininnovation.naiad.core.graph

import java.util.UUID

import tech.austininnovation.naiad.core.graph.Edges.{ DirectedEdge, UndirectedEdge }

/**
 * Graph structure consisting of Nodes (vertecies) and Edges
 * Edges can be directed or undirected. By convention a directed edge goes from left to right.
 *
 * @param nodes: Optional Set of Nodes in the Graph
 * @param uEdges: Optianl Set of Undirected Edges in the Graph
 * @param dEdges: Optional Set of Directed Edges in the Graph
 */

case class Graph(
  nodes: Option[Set[Node]],
  uEdges: Option[Set[UndirectedEdge]],
  dEdges: Option[Set[DirectedEdge]]) {

  /**
   * @param node: A Node to add to the graph
   * @return: A copy (shallow, deep?) of the graph with this node added
   */

  def addNode(node: Node): Graph = {
    this.copy(
      nodes = nodes match {
        case Some(ns) => Some(ns + node)
        case None => Some(Set(node))
      })
  }

  def addDirectedEdge(edge: DirectedEdge): Graph = {
    this.copy(dEdges = this.dEdges.map(es => es + edge))
  }

  // private val addDEdge(_) = addDirectedEdge(_)

  def addUndirectedEdge(edge: UndirectedEdge): Graph = {
    this.copy(uEdges = this.uEdges.map(es => es + edge))
  }

  // private val addUEdge(_) = addUndirectedEdge(_)

  def getNodeById(uuid: UUID): Option[Node] = nodes.flatMap(s => s.find(_.id == uuid))

  def getEdgeById(uuid: UUID) {
    for {
      des <- dEdges
      ues <- uEdges
      val dEdge = des.find(_.id == uuid)
      val uEdge = ues.find(_.id == uuid)

      val edge = dEdge match {
        case Some(e) => Some(e)
        case None => uEdge
      }

    } yield edge
  }
}

