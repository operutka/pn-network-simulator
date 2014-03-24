package com.pnns.vc3

import com.pnns.AlgorithmInstance
import com.pnns.Node
import scala.collection.immutable.HashSet

/**
 * VC3 algorithm instance.
 */
class Vc3Instance(val node: Node) extends AlgorithmInstance[(State, State), (String, String)] {
  private var wState: State = UR()
  private var bState: State = UR()
  private var round = 0
  private val pcount = node.ports.length
  private var m: List[Int] = Nil
  private var x = HashSet((0 until pcount): _*)
  
  override def hasStopped() = {
    isStoppingState(wState) && isStoppingState(bState)
  }
  
  private def isStoppingState(state: State) = {
    state match {
      case US() => true
      case MS() => true
      case _ => false
    }
  }
  
  override def state() = (wState, bState)
  
  override def send() = {
    wSend.zip(bSend)
  }
  
  /**
   * Generate outgoing messages for the black node.
   */
  private def bSend() = {
    bState match {
      case UR() if round % 2 == 1 => burSend
      case _ => Vector.fill(pcount)(null)
    }
  }
  
  /**
   * Generate outgoing messages for the black node in UR state.
   */
  private def burSend() = {
    if (m.isEmpty) {
      if (x.isEmpty)
        bState = US()
      Vector.fill(pcount)(null)
    } else {
      val proposal = m.min
      bState = MS()
      Vector.tabulate(pcount)(i => {
        if (i == proposal)
          "accept"
        else
          null
      })
    }
  }
  
  /**
   * Generate outgoing messages for the white node.
   */
  private def wSend() = {
    wState match {
      case UR() if round % 2 == 0 => wurSend
      case MR() if round % 2 == 0 => wmrSend
      case _ => Vector.fill(pcount)(null)
    }
  }
  
  /**
   * Generate outgoing messages for the white node in UR state.
   */
  private def wurSend() = {
    val k = (round + 1) / 2

    if (k < pcount) {
      Vector.tabulate(pcount)(pid => {
        if (pid == k)
          "proposal"
        else
          null
      })
    } else {
      wState = US()
      Vector.fill(pcount)(null)
    }
  }
  
  /**
   * Generate outgoing messages for the white node in MR state.
   */
  private def wmrSend() = {
    wState = MS()
    Vector.fill(pcount)("matched")
  }
  
  override def receive(msgs: Vector[(String, String)]) {
    wReceive(msgs map {
      case (_, w) => w
    })
    
    bReceive(msgs map {
      case (b, _) => b
    })
    
    round += 1
  }
  
  /**
   * Message processing for the black node.
   */
  private def bReceive(msgs: Vector[String]) {
    bState match {
      case UR() if round % 2 == 0 => burReceive(msgs)
      case _ =>
    }
  }
  
  /**
   * Message processing for the black node in UR state.
   */
  private def burReceive(msgs: Vector[String]) {
    msgs.zipWithIndex foreach {
      case (msg, port) => breceiveMsg(msg, port)
    }
  }
  
  /**
   * Helper method for black node message processing.
   */
  private def breceiveMsg(msg: String, port: Int) {
    msg match {
      case "matched" => x -= port
      case "proposal" => m = port :: m
      case _ =>
    }
  }
  
  /**
   * Message processing for the white node.
   */
  private def wReceive(msgs: Vector[String]) {
    wState match {
      case UR() if round % 2 == 1 => wurReceive(msgs)
      case _ =>
    }
  }
  
  /**
   * Message processing for the white node in UR state.
   */
  private def wurReceive(msgs: Vector[String]) {
    val accept = msgs.indexWhere(msg => msg == "accept")

    if (accept >= 0)
      wState = MR()
  }
}
