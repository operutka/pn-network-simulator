package com.pnns.vc3

import com.pnns.Algorithm
import com.pnns.Node
import com.pnns.Network
import java.util.Scanner
import com.pnns.NetworkBuilder

/**
 * VC3 algorithm implementation.
 */
class Vc3Algorithm extends Algorithm[Any, (State, State), (String, String)] {

  override def createInstance(node: Node, input: Any) = {
    new Vc3Instance(node)
  }
}

/**
 * VC3 algorithm.
 */
object Vc3Algorithm {
  
  def apply(network: Network): List[Int] = {
    val alg = new Vc3Algorithm
    val ncount = network.size
    val input = Vector.fill(ncount)(null)
    val instances = network.simulate(alg, input)
    val states = instances.map(i => i.state)
    
    states.zipWithIndex.foldLeft(List.empty[Int])((list, x) => x match {
      case ((MS(), _), i) => i :: list
      case ((_, MS()), i) => i :: list
      case _ => list
    }).sorted
  }
  
  def main(args: Array[String]) {
    val scanner = new Scanner(System.in)
    
    println("enter number of nodes:")
    val ncount = scanner.nextInt()
    
    val nbuilder = new NetworkBuilder(ncount)
    for (u <- 0 until ncount) {
      println("enter number of ports for node " + u + ":")
      val pcount = scanner.nextInt()
      
      println("enter port destinations in form:\nnode port")
      for (uport <- 0 until pcount) {
        val v = scanner.nextInt()
        val vport = scanner.nextInt()
        nbuilder.addLink(u, uport, v, vport)
      }
    }
    
    val network = nbuilder.createNetwork
    val vCover = apply(network)
    
    if (vCover.isEmpty)
      println("the algorithm did not find any vertex cover")
    else {
      println("the algorithm found the following vertex cover:")
      println(vCover.mkString(", "))
    }
  }
}
