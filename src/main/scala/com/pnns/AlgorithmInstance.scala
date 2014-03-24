package com.pnns

/**
 * Algorithm instance trait.
 * 
 * @author Ondrej Perutka
 * 
 * @tparam S state type
 * @tparam M message type
 */
trait AlgorithmInstance[S,M] {
  
  /**
   * Get node on which this algorithm instance is running.
   * 
   * @return a node
   */
  def node: Node
  
  /**
   * Check if the algorithm instance has stopped.
   * 
   * @return true if the instance has stopped, false otherwise
   */
  def hasStopped(): Boolean
  
  /**
   * Send messages.
   * 
   * @return vector with messages for all ports
   */
  def send(): Vector[M]
  
  /**
   * Process given messages.
   * 
   * @param msgs a vector of messages from all ports
   */
  def receive(msgs: Vector[M])
  
  /**
   * Get state of this algorithm instance.
   * 
   * @return a state
   */
  def state(): S
}