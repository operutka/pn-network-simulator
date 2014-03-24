package com.pnns.bmm

abstract class State
case class UR() extends State
case class US() extends State
case class MR(val i: Int) extends State
case class MS(val i: Int) extends State
