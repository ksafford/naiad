package tech.austininnovation.naiad.core
package graph

import scala.collection.GenTraversable
import scala.collection.generic.GenericTraversableTemplate


//import cats.Monad


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

trait GraphAlg[T[A] <: GenTraversable[A] with GenericTraversableTemplate[A, T], F[T]] {
  def graph: GraphBackend[T, F]
  //def depthFirstPropertySearch(prop: Map[String, String]): F[T[Node]] = {
  //
  //}

}
