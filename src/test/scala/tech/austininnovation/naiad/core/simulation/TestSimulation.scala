package tech.austininnovation.naiad.core
package simulation

import org.scalatest._
import cats.Id
import com.github.nscala_time.time.Imports._

import graph.InMemoryGraph
import tech.austininnovation.naiad.core.graph.Node

class TestSimulation extends FlatSpec with Matchers {

  val graph = InMemoryGraph.Builder[Id].build()
  val config = SimulationConfig(List(): List[Node], 0)

  "A simulation run" should "return a simulation result" in {
    // val sim = Simulation[Id](graph, config).run()
    val res = SimulationResults(
      java.util.UUID.randomUUID(),
      java.util.UUID.randomUUID(),
      DateTime.now())

    res.runTime should be < (new DateTime).withYear(3018)
      .withMonthOfYear(12)
      .withDayOfMonth(8)
  }
}
