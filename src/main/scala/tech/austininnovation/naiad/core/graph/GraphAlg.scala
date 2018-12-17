package tech.austininnovation.naiad.core
package graph

/**
 * The fundamental graph algebra. We want to limit the number of abstract
 * methods here so that writing an implementation is simple.
 *
 * As it stands, writing a new graph implementation only requires an
 * implementation of the basic node and edge getters.
 *
 * @tparam F A wrapper monad to contain side-effects
 *           Likely candidates are Future, IO, Task
 *           Id should be used for testing.
 *
 *           This style is called 'tagless final'
 */

trait GraphAlg[F[_], T[_]] {
  def graph: GraphBackend[F, T]
  //def depthFirstPropertySearch(prop: Map[String, String]): F[T[Node]] = {
  //
  //}

}
