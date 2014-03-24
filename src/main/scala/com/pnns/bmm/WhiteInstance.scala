package com.pnns.bmm

import com.pnns.Node

/**
 * BMM instance for white nodes.
 */
class WhiteInstance(node: Node) extends AbstractInstance(node) {
  
  override def send() = {
    val pcount = node.ports.length

    _state match {
      case UR() if round % 2 == 0 => urSend
      case MR(i) if round % 2 == 0 => mrSend(i)
      case _ => Vector.fill(pcount)(null)
    }
  }

  /**
   * Generate outgoing messages for the UR state.
   */
  private def urSend() = {
    val pcount = node.ports.length
    val k = (round + 1) / 2

    if (k < pcount) {
      Vector.tabulate(pcount)(pid => {
        if (pid == k)
          "proposal"
        else
          null
      })
    } else {
      _state = US()
      Vector.fill(pcount)(null)
    }
  }

  /**
   * Generate outgoing messages for the MR(i) state.
   */
  private def mrSend(i: Int) = {
    val pcount = node.ports.length

    _state = MS(i)
    Vector.fill(pcount)("matched")
  }

  override def receiveInternal(msgs: Vector[String]) {
    _state match {
      case UR() if round % 2 == 1 => urReceive(msgs)
      case _ =>
    }
  }

  /**
   * Message processing for the UR state.
   */
  private def urReceive(msgs: Vector[String]) {
    val pcount = node.ports.length
    val accept = msgs.indexWhere(msg => msg == "accept")

    if (accept >= 0)
      _state = MR(accept)
  }
}
