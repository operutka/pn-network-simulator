package com.pnns.vc3

import org.junit._
import Assert._
import com.pnns.NetworkBuilder

@Test
class Vc3Test {

  @Test
  def testVc3Algorithm() {
    val nb = new NetworkBuilder(4)
    nb.addLink(0, 0, 2, 1)
    nb.addLink(0, 1, 1, 0)
    nb.addLink(1, 1, 2, 0)
    nb.addLink(0, 0, 2, 1)
    nb.addLink(2, 2, 3, 0)
    
    val network = nb.createNetwork
    val vCover = Vc3Algorithm(network)
    val expected = List(0, 1, 2)
    
    assertTrue(expected.equals(vCover))
  }
}