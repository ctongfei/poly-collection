package poly.collection.mut

/**
  * Represents a structurally-mutable sequence.
 *
 * @tparam T
 */
trait KeyMutableSeq[T] extends ValueMutableSeq[T] {
  /**
   * Prepends the specific element to the start of the sequence.
   * @param x The element to be prepended
   */
  def prependInplace(x: T): Unit

  /**
   * Appends the specific element to the end of the sequence.
   * @param x The element to be appended
   */
  def appendInplace(x: T): Unit

  /**
   * Clears this sequence.
   */
  def clear(): Unit

  /**
   * Inserts an element at the specific position.
   * @param i Position for insertion
   * @param x The element to be inserted
   */
  def insertAt(i: Int, x: T): Unit

  /**
   * Deletes the element at the specific position.
   * @param i Position for deletion
   */
  def deleteAt(i: Int): Unit

}
