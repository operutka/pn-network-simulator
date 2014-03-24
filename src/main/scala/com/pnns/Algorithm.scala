package com.pnns

/**
 * Algorithm trait.
 * 
 * @author Ondrej Perutka
 * 
 * @tparam I input type
 * @tparam S state type
 * @tparam M message type
 */
trait Algorithm[I,S,M] {
  
  /**
   * Create algorithm instance for a given node and input.
   * 
   * @param node a node
   * @param input an input
   * @return algorithm instance
   */
  def createInstance(node: Node, input: I): AlgorithmInstance[S,M]
}