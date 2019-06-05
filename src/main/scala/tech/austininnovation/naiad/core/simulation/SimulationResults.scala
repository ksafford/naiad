package tech.austininnovation.naiad.core
package simulation

import java.util.UUID
import com.github.nscala_time.time.Imports._

case class SimulationResults(
  id: UUID,
  graphId: UUID,
  runTime: DateTime)
