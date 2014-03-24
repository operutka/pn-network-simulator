package com.pnns

/**
 * Network builder class. It allows to incrementaly build simple networks.
 * 
 * @author Ondrej Perutka
 * 
 * @constructor Create a new network builder for a network of a given size.
 * @param nodeCount number of network nodes
 */
class NetworkBuilder(nodeCount: Int) {
  private var ports = Vector.fill(nodeCount)(Vector.empty[(Int, Int)])
  
  /**
   * Add a link into the network.
   * 
   * @param u u node (node index)
   * @param uport port of the u node (port index)
   * @param v v node
   * @param vport port of the v node
   */
  def addLink(u: Int, uport: Int, v: Int, vport: Int) {
    var uports = ports(u)
    var vports = ports(v)
    
    uports = updatePort(uports, u, uport, v, vport)
    vports = updatePort(vports, v, vport, u, uport)
    
    ports = ports.updated(u, uports).updated(v, vports)
  }
  
  /**
   * Update a given node port table.
   * 
   * @param uports u node port table
   * @param u u node (node index)
   * @param uport port of the u node (port index)
   * @param v v node
   * @param vport port of the v node
   * @return updated port table
   */
  private def updatePort(
      uports: Vector[(Int, Int)],
      u: Int, uport: Int,
      v: Int, vport: Int) = {
    if (u == v) // is it loop?
      throw new IllegalArgumentException("loops are not allowed")
    if (hasAnotherLink(uports, v, vport)) // is there another link to the same node?
      throw new IllegalArgumentException("there is already a different link between nodes " + u + " and " + v)
    
    if (uport == uports.length)
      uports :+ (v, vport)
    else if (uport > uports.length)
      Vector.tabulate(uport + 1)(i => {
        if (i < uports.length)
          uports(i)
        else if (i == uport)
          (v, vport)
        else
          null
      })
    else if (uports(uport) == null)
      uports.updated(uport, (v, vport))
    else if (uports(uport) != (v, vport)) // is the port occupied by a different link?
      throw new IllegalArgumentException("port nr. " + uport + " of node " + u + " is occupied")
    else
      uports
  }
  
  /**
   * Check if there is another link in a given port table to a given destination
   * node.
   * 
   * @param ports a port table
   * @param v a destination node
   * @param vport a destination port
   * @return true if there is another link, false otherwise
   */
  private def hasAnotherLink(ports: Vector[(Int, Int)], v: Int, vport: Int) = {
    ports exists {
      case (z, zport) => (z == v) && (zport != vport)
      case _ => false
    }
  }
  
  /**
   * Check if all ports in a given port table are connected.
   * 
   * @param a port table
   * @return true if all ports are connected, false otherwise
   */
  private def checkPorts(ports: Vector[(Int, Int)]) = {
    ports.forall(p => p != null)
  }
  
  /**
   * Create a network.
   * 
   * @return a network
   */
  def createNetwork() = {
    val nodes = ports.zipWithIndex map {
      case (nodePorts, nodeId) => {
        if (!checkPorts(nodePorts))
          throw new IllegalStateException("there is an unconnected port on node " + nodeId)
        else
          new Node(nodeId, nodePorts)
      }
    }
    
    new Network(nodes)
  }
}
