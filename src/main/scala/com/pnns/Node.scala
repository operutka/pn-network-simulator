package com.pnns

/**
 * Representation of a single network node.
 * 
 * @author Ondrej Perutk
 * 
 * @constructor Create a new node using a given node id and node ports.
 * @param id a node id
 * @param ports node ports
 */
class Node(val id: Int, val ports: Vector[(Int, Int)])
