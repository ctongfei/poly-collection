package poly.collection

/**
 * @author Tongfei Chen (ctongfei@gmail.com).
 */
trait StructureMutableGraph[K, V, E] extends DataMutableGraph[K, V, E] {

  def addVertex(i: K, v: V): Unit
  def removeVertex(i: K): Unit

  def addEdge(i: K, j: K, e: E): Unit
  def removeEdge(i: K, j: K): Unit

}
