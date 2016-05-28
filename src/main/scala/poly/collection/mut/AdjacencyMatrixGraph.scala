package poly.collection.mut

import poly.algebra._
import poly.algebra.syntax._
import poly.collection._
import poly.collection.impl.specialized._

/**
 * Represents a graph whose underlying representation is an adjacency matrix.
 *
 * The keys of an adjacency matrix graph is restricted to the integer set '''Z''',,''n'',, = {0, 1, ..., ''n''-1}
 * where ''n'' is the number of nodes in this graph.
 * @author Tongfei Chen
 * @since 0.1.0
 */
class AdjacencyMatrixGraph[E] private(
  override val numNodes: Int,
  private val edgeExists: SpArrayTable[Boolean],
  private val edgeData: ValueMutableTable[E]
) extends AbstractBiGraph[Int, E] with ValueMutableGraph[Int, E] {

  def incomingKeySet(j: Int) = keySet filterKeys { i ⇒ edgeExists(i, j) }
  def outgoingKeySet(i: Int) = keySet filterKeys { j ⇒ edgeExists(i, j) }

  def keys = Range(numNodes)

  override def keySet = Range(numNodes).asSet

  def containsKey(i: Int) = 0 <= i && i < numNodes

  def eqOnKeys = Eq[Int]

  override def containsArc(i: Int, j: Int) = edgeExists(i, j)

  def apply(i: Int, j: Int) = edgeData(i, j)

  def update(i: Int, j: Int, e: E) = {
    edgeExists(i, j) = true
    edgeData(i, j) = e
  }

  // TODO: arcMap should be overridden

  def adjacencyMatrix: Table[Option[E]] = new AbstractTable[Option[E]] {
    def apply(i: Int, j: Int) = if (edgeExists(i, j)) Some(edgeData(i, j)) else None
    def numRows = numNodes
    def numCols = numNodes
  }

}
