package o1.adventure

import scala.util.Random.*

class Battle (attacker: Person, defender: Person)
  private var attackerLength = 100
  private var defenderLength = 100

  def startBattle:String =
    "A BATTLE BETWEEN " + attacker.name + " AND " + defender.name + " BEGINS"
    // pick starter at random

  def use(move: String, user: Person) = ???
