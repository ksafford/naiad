package tech.austininnovation.naiad.core

import scalax.collection.Graph // or scalax.collection.mutable.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._

/*
 A useful graph for testing PGMs. Classic example from Daphnie Koller's course and text
 */

case class TestNode(name: String)

object StudentGraph {

  val S = TestNode("SAT")
  val D = TestNode("Difficulty")
  val I = TestNode("Intelligent")
  val G = TestNode("Grade")
  val L = TestNode("Letter")

  val studentGraph = for (
    g <- GraphBuilder.new()
    _ <- g.addNode(S)
    _ <- g.addNode(D)
    _ <- g.addNode(I)
    _ <- g.addNode(G)
    _ <- g.addNode(L)
    _ <- g.addEdge(I --> S)
    _ <- g.addEdge(I --> G)
    _ <- g.addEdge(D --> G)
    _ <- g.addEdge(G --> L)
  ) yield g

  studentGraph.run()

}
