package o1.adventure

import scala.collection.mutable.Buffer

/**
 * Represents a persons Stand ability. Stats range from 1 to 5 (inclusive).
 * @param name Stand's name
 * @param power
 * @param speed
 * @param range
 * @param stamina
 * @param precision
 * @param developmentPotential
 */

class Stand (val name: String, val power: Int, val speed: Int, val range: Int, val stamina: Int, val precision: Int, val developmentPotential: Int):
  private var fightMoves = Buffer[FightMove]()
  
  def addFightMove(move: FightMove) = fightMoves += move
  
  def getFightMove(name: String):Option[FightMove] = this.fightMoves.find(_.name == name)