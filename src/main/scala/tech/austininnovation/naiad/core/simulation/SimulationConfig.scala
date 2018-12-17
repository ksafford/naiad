package tech.austininnovation.naiad.core
package simulation

import graph.Node

case class SimulationConfig(
  terminalNodes: List[Node],
  maxTransitions: Int)
