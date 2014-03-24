package com.pnns.bmm

import com.pnns.AlgorithmInstance
import com.pnns.Node
import com.pnns.Algorithm
import com.pnns.AlgorithmInstance
import scala.collection.immutable.HashSet
import com.pnns.Network
import java.util.Scanner
import com.pnns.NetworkBuilder

/**
 * BMM algorithm implementation.
 */
class BmmAlgorithm extends Algorithm[Boolean, State, String] {

  override def createInstance(node: Node, input: Boolean) = {
    if (input)
      new BlackInstance(node)
    else
      new WhiteInstance(node)
  }
}

/**
 * BMM algorithm.
 */
object BmmAlgorithm {
  
  def apply(input: Vector[Boolean], network: Network) = {
    val alg = new BmmAlgorithm
    val instances = network.simulate(alg, input)
    val states = instances.map(i => i.state)
    val nodes = network.nodes
    
    input.zipWithIndex.zip(states) collect {
      case ((true, u), MS(v)) => (u, nodes(u).ports(v)._1) 
    }
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
    
    var input = Vector.empty[Boolean]
    println("for each node enter whether it is white (0) or black (otherwise):")
    for (u <- 0 until ncount)
      input :+= scanner.nextInt() != 0
        
    val network = nbuilder.createNetwork
    val matching = apply(input, network)
    
    if (matching.isEmpty)
      println("the algorithm did not find any matching")
    else {
      println("the algorithm found the following maximal matching:")
      matching foreach {
        case (u, v) => println("(" + u + ", " + v + ")")
      }
    }
  }
}