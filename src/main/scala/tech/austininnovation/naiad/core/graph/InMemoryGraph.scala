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

case class InMemoryGraph(
  nodes: Option[Set[Node]],
  uEdges: Option[Set[UndirectedEdge]],
  dEdges: Option[Set[DirectedEdge]]) {

  def getParentNodes(node: Node): Option[Set[Node]] = {
    None
  }

  def getSiblingNodes(node: Node): Option[Set[Node]] = {
    None
  }

  def getChildNodes(node: Node): Option[Set[Node]] = {
    None
  }

  def getConnectedNodes(node: Node): Option[Set[Node]] = {
    None
  }

  def getRootNodes(): Option[Set[Node]] = {
    None
  }

  def getLeafNodes(): Option[Set[Node]] = {
    None
  }

  /**
   * @param node: A Node to add to the graph
   * @return: A copy (shallow, deep?) of the graph with this node added
   */

  def addNode(node: Node): InMemoryGraph = {
    this.copy(
      nodes = nodes match {
        case Some(ns) => Some(ns + node)
        case None => Some(Set(node))
      })
  }

  def addDirectedEdge(edge: DirectedEdge): InMemoryGraph = {
    val newEdgeSet = this.dEdges match {
      case None => Set(edge)
      case Some(es) => es + edge
    }
    this.copy(dEdges = Some(newEdgeSet))
  }

  // private val addDEdge(_) = addDirectedEdge(_)

  def addUndirectedEdge(edge: UndirectedEdge): InMemoryGraph = {
    val newEdgeSet = this.uEdges match {
      case None => Set(edge)
      case Some(es) => es + edge
    }
    this.copy(uEdges = Some(newEdgeSet))
  }

  // private val addUEdge(_) = addUndirectedEdge(_)

  def getNodeById(uuid: UUID): Option[Node] = nodes.flatMap(s => s.find(_.id == uuid))

  private def getDEdgeById(uuid: UUID): Option[DirectedEdge] = {
    dEdges.flatMap(s => s.find(_.id == uuid))
  }

  private def getUEdgeById(uuid: UUID): Option[UndirectedEdge] = {
    uEdges.flatMap(s => s.find(_.id == uuid))
  }

  def getEdgeById(uuid: UUID): Option[Product] = {
    this.getUEdgeById(uuid) orElse this.getDEdgeById(uuid)
  }
}

