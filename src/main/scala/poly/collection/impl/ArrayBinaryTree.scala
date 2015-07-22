package poly.collection.impl

import poly.collection._
import poly.collection.exception._
import poly.collection.mut._
import poly.collection.node._
import poly.collection.tree._

/**
 * A binary tree stored in an array.
 * @author Tongfei Chen (ctongfei@gmail.com).
 */
class ArrayBinaryTree[T] (
  private[poly] val data: ResizableArray[T],
  private[poly] val state: ResizableArray[Boolean]
) extends BinaryTree[T] { self =>

  import BinaryTree._

  class Node (val i: Int) extends BiBinaryTreeNode[T] {
    def data = if (state(i)) self.data(i) else throw new NoSuchElementException
    def data_=(x: T) = {
      self.state(i) = true
      self.data(i) = x
    }
    def parent = if (i > 0) new Node((i - 1) / 2) else null
    def left = if (nodeExists(2 * i + 1)) new Node(2 * i + 1) else null
    def right = if (nodeExists(2 * i + 2)) new Node(2 * i + 2) else null

    override def equals(that: Any) = that match {
      case that: Node => this.i == that.i
      case _ => false
    }
  }

  override def apply(i: Int) = if (nodeExists(i)) data(i) else throw new NoSuchElementException

  def rootNode = new Node(0)

  def update(i: Int, x: T) = {
    state(i) = true
    data(i) = x
  }

  def insertAt(i: Int, x: T, direction: Boolean) = ???


  @inline private[this] def nodeExists(i: Int) = {
    i >= 0 && i < data.length && state(i)
  }

}

object ArrayBinaryTree {


}
