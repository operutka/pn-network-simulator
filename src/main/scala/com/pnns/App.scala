package com.pnns

import scala.annotation.tailrec
import scala.collection.immutable.HashSet
import com.pnns.bmm.BmmAlgorithm
import com.pnns.vc3.Vc3Algorithm

/**
 * Main application object. It makes this application usable from command line.
 * 
 * @author Ondrej Perutka
 */
object App {
  val algs = List("bmm", "vc3")
  
  def main(args: Array[String]) {
    if (args.length < 1)
      printUsage()
    else args(0) match {
      case "bmm" => BmmAlgorithm.main(args)
      case "vc3" => Vc3Algorithm.main(args)
      case _ => printUsage()
    }
  }
  
  private def printUsage() {
    println("USAGE: java -jar pnns.jar algorithm [params]\n")
    println("    algorithm  may be one of {" + algs.mkString(", ") + "}\n")
  }
}
