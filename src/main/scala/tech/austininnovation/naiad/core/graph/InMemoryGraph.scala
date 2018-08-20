package tech.austininnovation.naiad.core.graph

import java.util.UUID

/**
 * Graph structure consisting of Nodes (vertecies) and Edges
 * Edges can be directed or undirected. By convention a directed edge goes from left to right.
 *
 * @param nodes: Optional Set of Nodes in the Graph
 * @param edges: Optianl Set of Undirected Edges in the Graph
 */

case class InMemoryGraph(
  nodes: Option[Set[Node]],
  edges: Option[Set[Edge]]) {

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

  def addEdge(edge: Edge): InMemoryGraph = {
    val newEdgeSet = this.edges match {
      case None => Set(edge)
      case Some(es) => es + edge
    }
    this.copy(edges = Some(newEdgeSet))
  }

  def getNodeById(uuid: UUID): Option[Node] = nodes.flatMap(s => s.find(_.id == uuid))

  def getEdgeById(uuid: UUID): Option[Edge] = {
    edges.flatMap(s => s.find(_.id == uuid))
  }

}

