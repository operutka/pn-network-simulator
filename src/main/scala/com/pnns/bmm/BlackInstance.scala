package com.pnns.bmm

import com.pnns.Node
import scala.collection.immutable.HashSet

/**
 * BMM instance for black nodes.
 */
class BlackInstance(node: Node) extends AbstractInstance(node) {
  private var m: List[Int] = Nil
  private var x = HashSet((0 until node.ports.length): _*)

  override def send() = {
    val pcount = node.ports.length

    _state match {
      case UR() if round % 2 == 1 => urSend
      case _ => Vector.fill(pcount)(null)
    }
  }

  /**
   * Generate outgoing messages for the UR state.
   */
  private def urSend() = {
    val pcount = node.ports.length

    if (m.isEmpty) {
      if (x.isEmpty)
        _state = US()
      Vector.fill(pcount)(null)
    } else {
      val proposal = m.min
      _state = MS(proposal)
      Vector.tabulate(pcount)(i => {
        if (i == proposal)
          "accept"
        else
          null
      })
    }
  }

  override def receiveInternal(msgs: Vector[String]) {
    _state match {
      case UR() if round % 2 == 0 => urReceive(msgs)
      case _ =>
    }
  }

  /**
   * Message processing for the UR state.
   */
  private def urReceive(msgs: Vector[String]) {
    msgs.zipWithIndex foreach {
      case (msg, port) => receiveMsg(msg, port)
    }
  }

  /**
   * Helper method for message processing.
   */
  private def receiveMsg(msg: String, port: Int) {
    msg match {
      case "matched" => x -= port
      case "proposal" => m = port :: m
      case _ =>
    }
  }
}
