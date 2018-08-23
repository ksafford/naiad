package tech.austininnovation.naiad.core.graph

import java.util.UUID

trait GraphAlg[F[_]] {

  def nodes: Option[Set[Node]]
  def edges: Option[Set[Edge]]

  def addNode(node: Node): InMemoryGraph[F]
  def addEdge(edge: Edge): InMemoryGraph[F]

  def getNodeById(uuid: UUID): F[Option[Node]]
  def getEdgeById(uuid: UUID): F[Option[Edge]]

  def getNodeEdges(node: Node): F[Option[Set[Edge]]]
  def getOutEdges(node: Node): F[Option[Set[Edge]]]
  def getInEdges(node: Node): F[Option[Set[Edge]]]

  def getParentNodes(node: Node): F[Option[Set[Node]]]
  def getSiblingNodes(node: Node): F[Option[Set[Node]]]
  def getChildNodes(node: Node): F[Option[Set[Node]]]
  def getAdjacentNodes(node: Node): F[Option[Set[Node]]]
  def getRootNodes(): F[Option[Set[Node]]]
  def getLeafNodes(): F[Option[Set[Node]]]
}
