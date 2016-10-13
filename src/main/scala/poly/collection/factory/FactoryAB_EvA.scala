package poly.collection.factory

import poly.collection._
import poly.collection.conversion.FromScala._
import poly.collection.builder._
import scala.language.higherKinds

/**
 * @author Tongfei Chen
 */
trait BuilderFactoryAB_EvA[+C[_, _], Ev[_]] extends FactoryAB_EvA[C, Ev] {

  implicit def newBuilder[A: Ev, B]: Builder[(A, B), C[A, B]]

  override def empty[A: Ev, B] = newBuilder[A, B].result

  def from[A: Ev, B](xs: Traversable[(A, B)]) = {
    val b = newBuilder[A, B]
    if (xs.sizeKnown) b.sizeHint(xs.size)
    b addAll xs
    b.result
  }
}

trait FactoryAB_EvA[+C[_, _], Ev[_]] {

  def empty[A: Ev, B]: C[A, B] = from(Traversable.empty)

  def apply[A: Ev, B](xs: (A, B)*): C[A, B] = from(xs)

  def from[A: Ev, B](xs: Traversable[(A, B)]): C[A, B]
}
