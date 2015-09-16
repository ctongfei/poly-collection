package poly.collection.mut

import poly.collection._
import poly.collection.factory._
import poly.collection.impl._
import poly.collection.node._

/**
 * @author Tongfei Chen (ctongfei@gmail.com).
 */
class ListBiSeq[T] private(private val data: DoublyLinkedList[T]) extends BiSeq[T] with KeyMutableSeq[T] {

  def headNode: BiSeqNode[T] = data.dummy.next
  def lastNode: BiSeqNode[T] = data.dummy.prev

  def length = data.length

  def appendInplace(x: T) = data.appendInplace(x)

  def insertAt(i: Int, x: T) = data.insertAt(i, x)

  def clear() = data.clear()

  def prependInplace(x: T) = data.prependInplace(x)

  def deleteAt(i: Int) = data.deleteAt(i)

  def update(i: Int, x: T) = data(i) = x

  def apply(i: Int) = data.apply(i)
}

object ListBiSeq extends SeqFactory[ListBiSeq] {

  implicit def newBuilder[T]: Builder[T, ListBiSeq[T]] = new Builder[T, ListBiSeq[T]] {
    val a = new DoublyLinkedList[T]()
    def +=(x: T) = a appendInplace x
    def result = new ListBiSeq[T](a)
    def sizeHint(n: Int) = {}
  }

}