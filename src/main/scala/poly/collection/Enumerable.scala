package poly.collection

import poly.algebra.hkt._
import poly.collection.mut._

/**
 * `Enumerable` is the basic trait for all collections that exposes an enumerator.
 * `Enumerable`s differ from `Traversable`s in that the iteration process can be controlled:
 * It can be paused or resumed by the user.
 *
 * This trait is created to replace the `Iterable` Java interface or the `Iterable` Scala
 * trait in Poly-collection.
 *
 * @author Tongfei Chen (ctongfei@gmail.com).
 */
trait Enumerable[+T] extends Traversable[T] { self =>

  import Enumerable._

  /** Returns a new enumerator that can be used to enumerate through this collection. */
  def newEnumerator: Enumerator[T]


  //region HELPER FUNCTIONS

  def foreach[V](f: T => V) = newEnumerator.foreach(f)

  override def map[U](f: T => U) = ofEnumerator(self.newEnumerator.map(f))

  def flatMap[U](f: T => Enumerable[U]) = ofEnumerator(self.newEnumerator.flatMap(x => f(x).newEnumerator))

  def cartesianProduct[U](that: Enumerable[U]): Enumerable[(T, U)] = self.flatMap(t => that.map(u => (t, u)))

  override def filter(f: T => Boolean) = ofEnumerator(self.newEnumerator.filter(f))

  override def filterNot(f: T => Boolean) = ofEnumerator(self.newEnumerator.filterNot(f))

  def concat[U >: T](that: Enumerable[U]): Enumerable[U] = ofEnumerator(self.newEnumerator concat that.newEnumerator)

  override def prepend[U >: T](u: U): Enumerable[U] = ofEnumerator(self.newEnumerator prepend u)

  override def append[U >: T](u: U): Enumerable[U] = ofEnumerator(self.newEnumerator append u)

  override def tail: Enumerable[T] = ofEnumerator(self.newEnumerator.tail)

  override def take(n: Int): Enumerable[T] = ofEnumerator(self.newEnumerator.take(n))

  override def drop(n: Int): Enumerable[T] = ofEnumerator(self.newEnumerator.drop(n))

  override def slice(i: Int, j: Int): Enumerable[T] = ofEnumerator(self.newEnumerator.slice(i, j))

  def distinct: Enumerable[T] = ???

  def zip[U](that: Enumerable[U]): Enumerable[(T, U)] = ofEnumerator(self.newEnumerator zip that.newEnumerator)

  def zip3[U, V](us: Enumerable[U], vs: Enumerable[V]): Enumerable[(T, U, V)] = ofEnumerator {
    new Enumerator[(T, U, V)] {
      val ti = self.newEnumerator
      val ui = us.newEnumerator
      val vi = vs.newEnumerator
      def advance(): Boolean = ti.advance() && ui.advance() && vi.advance()
      def current: (T, U, V) = (ti.current, ui.current, vi.current)
    }
  }

  def interleave[U >: T](that: Enumerable[U]): Enumerable[U] =
    Enumerable.ofEnumerator(self.newEnumerator interleave that.newEnumerator)

  def sliding(windowSize: Int, step: Int = 1) = ofEnumerator(self.newEnumerator.sliding(windowSize, step))

  //endregion

}

object Enumerable {

  object empty extends Enumerable[Nothing] {
    def newEnumerator: Enumerator[Nothing] = Enumerator.empty
  }

  /** Creates an enumerable sequence based on an existing enumerator. */
  def ofEnumerator[T](e: => Enumerator[T]): Enumerable[T] = new AbstractEnumerable[T] {
    def newEnumerator = e // call-by-name parameter!
  }

  /**
   * Constructs an infinite sequence that is generated by repeatedly applying a given function to
   * a start value. $LAZY
   * @param s Start value
   * @param next Function
   * @return An infinite sequence
   */
  def iterate[T](s: T)(next: T => T) = ofEnumerator(Enumerator.iterate(s)(next))

  /** Returns the natural monad on Enumerables. */
  implicit object Monad extends Monad[Enumerable] {
    def flatMap[X, Y](mx: Enumerable[X])(f: (X) => Enumerable[Y]): Enumerable[Y] = mx.flatMap(f)
    def id[X](u: X): Enumerable[X] = ListSeq(u)
  }

  implicit def optionAsEnumerable[T](o: Option[T]): Enumerable[T] = new AbstractEnumerable[T] {
    def newEnumerator = o match {
      case Some(x) => Enumerator.single(x)
      case None => Enumerator.empty
    }
  }
}

abstract class AbstractEnumerable[+T] extends AbstractTraversable[T] with Enumerable[T]
