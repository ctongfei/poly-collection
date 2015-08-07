package poly.collection

import poly.algebra._
import poly.collection.exception._
import poly.collection.factory._
import poly.collection.mut._
import poly.collection.node._
import scala.annotation.unchecked.{uncheckedVariance => uV}

/**
 * Trait for sequences.
 * @author Tongfei Chen (ctongfei@gmail.com).
 */
trait Seq[+T] extends Enumerable[T] with Map[Int, T] { self =>

  /**
   * Returns the length of this sequence.
   * @return The length of this sequence
   */
  def length: Int

  /**
   * Returns the ''i''-th element of this sequence.
   * @param i Index
   * @return The ''i''-th element of this sequence
   */
  def apply(i: Int): T

  override def size = length

  def headNode: SeqNode[T]

  def newEnumerator: Enumerator[T] = new Enumerator[T] {
    var node: SeqNode[T] = null
    var first = true
    def advance() = {
      if (first) {
        first = false
        node = headNode
      }
      else node = node.next
      node ne null
    }
    def current = node.data
  }

  override def isDefinedAt(i: Int) = i >= 0 && i < size

  def ?(i: Int) = if (isDefinedAt(i)) Some(this(i)) else None

  def containsKey(i: Int) = isDefinedAt(i)

  def pairs = ??? //TODO: zipWithIndex.map(_.swap)

  override def map[U](f: T => U): Seq[U] = new Seq[U] {
    def apply(i: Int): U = f(self(i))
    def length: Int = self.length
    def headNode: SeqNode[U] = self.headNode.map(f)
    override def newEnumerator: Enumerator[U] = self.newEnumerator.map(f)
  }

  /**
   * Pretends that this sequence is sorted under the given order.
   * @param O The implicit order
   * @return A sorted order (WARNING: Actual orderedness is not guaranteed! The user should make sure that it is sorted.)
   */
  def asIfSorted[U >: T](implicit O: WeakOrder[U]): SortedSeq[U] = new SortedSeq[U] {
    val order: WeakOrder[U] = O
    def length: Int = self.length
    def apply(i: Int): T = self.apply(i)
    override def newEnumerator: Enumerator[T] = self.newEnumerator
    def headNode: SeqNode[T] = self.headNode
  }

  override def equals(that: Any) = that match {
    case that @ (other: Seq[T]) => ??? //TODO!!!
    case _ => false
  }

  override def toString() = {
    val len = length
    if (len > Settings.MaxElemToString)
      s"[$len] " + this.take(Settings.MaxElemToString).buildString(", ") + "..."
    else s"[$len] " + this.buildString(", ")
  }

  override def hashCode = ???



}

object Seq {

  object empty extends Seq[Nothing] {
    def apply(i: Int): Nothing = throw new NoSuchElementException
    def length: Int = 0
    def headNode: SeqNode[Nothing] = throw new NoSuchElementException
  }

  def tabulate[T](n: Int)(f: Int => T) = IndexedSeq.tabulate(n)(f)

}
