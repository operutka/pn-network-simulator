package com.pnns.bmm

import org.junit._
import Assert._
import com.pnns.NetworkBuilder

@Test
class BmmTest {

  @Test
  def testBmmAlgorithm() {
    val nb = new NetworkBuilder(8)
    nb.addLink(0, 0, 1, 1)
    nb.addLink(0, 1, 5, 0)
    nb.addLink(1, 0, 2, 0)
    nb.addLink(2, 1, 3, 0)
    nb.addLink(2, 2, 7, 0)
    nb.addLink(4, 0, 5, 1)
    nb.addLink(5, 2, 6, 0)
    nb.addLink(6, 1, 7, 1)
    
    val network = nb.createNetwork
    val input = Vector(false, true, false, true, false, true, false, true)
    val matching = BmmAlgorithm(input, network)
    val expected = Vector((1, 2), (5, 4), (7, 6))
    
    assertTrue(expected.equals(matching))
  }
}