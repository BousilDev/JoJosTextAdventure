package o1.adventure

import scala.collection.mutable.Buffer
import scala.util.Random

/**
 * Represents a person's Stand ability. Stats range from 1 to 5 (inclusive).
 * @param name Stand's name
 * @param power Power stat gets used when calculating the damage of the fight move
 * @param speed Speed determines how much length/energy the user gets between turns
 * @param range Not used in the game (for now)
 * @param stamina Not used in the game (for now)
 * @param precision Not used in the game (for now)
 * @param developmentPotential Not used in the game (for now)
 */

class Stand (val name: String, val power: Int, val speed: Int, val range: Int, val stamina: Int, val precision: Int, val developmentPotential: Int):
  private var fightMoves = Buffer[FightMove]()
  private var hiddenFightMoves = Buffer[FightMove]()
  
  def addFightMove(move: FightMove) = fightMoves += move
  
  def addHiddenFightMove(move: FightMove) = hiddenFightMoves += move
  
  def getFightMove(name: String):Option[FightMove] = this.fightMoves.find(_.name == name)
  
  def getFightMoves:Buffer[FightMove] =
    val retMoves = fightMoves
    retMoves
  
  def getRandomFightMoveName:String = fightMoves(Random.nextInt(fightMoves.size)).name
  
  def learnHiddenMoves = fightMoves ++= hiddenFightMoves