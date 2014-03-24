package com.pnns.bmm

import com.pnns.AlgorithmInstance
import com.pnns.Node

/**
 * Common ancestor for BMM algorithm instances.
 */
abstract class AbstractInstance(val node: Node) 
  extends AlgorithmInstance[State, String] {
  protected var _state: State = UR()
  private var _round = 0

  /**
   * Get number of the current round.
   * 
   * @return round number
   */
  final def round = _round

  final override def receive(msgs: Vector[String]) {
    receiveInternal(msgs)
    _round += 1
  }

  /**
   * Internal method for receiving messages.
   * 
   * @param msgs a message vector
   */
  protected def receiveInternal(msgs: Vector[String])

  override def hasStopped() = {
    _state match {
      case US() => true
      case MS(_) => true
      case _ => false
    }
  }

  override def state() = _state
}
