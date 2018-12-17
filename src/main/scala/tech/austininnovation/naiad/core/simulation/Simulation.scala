package tech.austininnovation.naiad.core
package simulation

import tech.austininnovation.naiad.core.graph.InMemoryGraph

import com.github.nscala_time.time.Imports._

case class Simulation[F[_]](
  graph: InMemoryGraph[F],
  simulationConfig: SimulationConfig) {

  def run(): SimulationResults = {
    SimulationResults(
      java.util.UUID.randomUUID(),
      java.util.UUID.randomUUID(),
      DateTime.now())
  }

}
