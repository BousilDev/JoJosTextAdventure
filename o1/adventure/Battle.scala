package o1.adventure

import scala.util.Random
import scala.collection.mutable.Buffer
import scala.collection.mutable.Map

class Battle (val attacker: Person, val defender: Person):

/** These "lengths" describe the amount of time each contestant has per turn.
 * For example, if there are two fight moves available, one costs 50 "length" and another 70 "length",
 * and the user has 100 "lengths" on his turn, he may use two times the 50 length move or once the 70 length move.
 * If user uses 2x50 length moves, his length in the next turn will be "base value per turn" + ("lastLength" - "move costs") = 100 + (100-2x50) = 100
 * If user uses 1x70 length move, his length in the next turn will be "base value per turn" + ("lastLength" - "move costs") = 100 + (100-1x70) = 130
 * The latter case allows the utilization of larger combos (say there's a move that costs 120 lengths and is super powerful)
 */
  private var bothLengths:Map[Person,Int] = Map(attacker -> 100, defender -> 100)
  private var participants:Buffer[Person] = Buffer(attacker, defender)
  private var nextInTurn:Person = attacker
  private var battleEnded = false

  def startBattle:String =
    var returnLine = "A BATTLE BETWEEN " + attacker.name + " AND " + defender.name + " BEGINS !\n"
                     + defender.name + " IS SAYING SOMETHING: " + defender.battleDescription + '\n'
                     + attacker.name + " RESPONDS: " + attacker.battleDescription + '\n'
    // pick starter at random
    if Random.nextInt(2) == 1 then
      nextInTurn = defender
    returnLine += "IT HAS BEEN DECIDED BY FAITH THAT " + nextInTurn.name + " WILL START THE BATTLE !\n"
    while nextInTurn == defender do
      returnLine += defenderTurn

    returnLine

  def use(move: String, user: Person):String =
    val selectedMove = user.stand.getFightMove(move)
    val enemy = participants.find(_ != user).head
    if selectedMove.isEmpty then
      "YOU DON'T HAVE A FIGHT MOVE CALLED " + move + "!"
    else
      var returnLine = ""
      if selectedMove.get.length < bothLengths(user) && nextInTurn == user then

        // Stands power stat gets used here
        val damage = selectedMove.get.damage * user.stand.power

        enemy.decreaseHealth(damage)

        bothLengths(user) = bothLengths(user) - selectedMove.get.length
        returnLine += user.name + " DEALT " + damage + " DAMAGE TO " + enemy.name + "!" + '\n'

        if enemy.health <= 0 then
          returnLine += enemy.name + " HAS BEEN DEFEATED BY " + user.name + "!" + '\n'
          enemy.isDefeated = true
        else
          returnLine += healthPoints(enemy)
          if movesRemaining(user,bothLengths(user)) then
            returnLine += user.name + " HAS STILL GOT SOME ENERGY TO USE!" +'\n'
            if user == defender then
              returnLine += defenderTurn
          else
            nextInTurn = enemy

            // Here stand speed is used
            val lengthBefore = bothLengths(user)
            // Stores the remaining "length" to be used in the next turn
            bothLengths(user) = 80 + user.stand.speed*10 + bothLengths(user)

            returnLine += user.name + " DOESN'T HAVE ENOUGH ENERGY (" + lengthBefore + ") FOR ANOTHER MOVE AND GIVES TURN TO " + enemy.name + "!" + '\n'
            if nextInTurn == defender then
              returnLine += defenderTurn

      else
        returnLine += "YOU ONLY HAVE " + bothLengths(user) + " ENERGY LEFT!" + '\n' + "TRY TO PICK ANOTHER MOVE!"

      if participants.exists(_.isDefeated) then
        battleEnded = true

      returnLine


  def hasEnded = battleEnded

  def skipTurn(user:Person):String =
    val otherPerson = participants.filterNot(_ == user).head
    nextInTurn = otherPerson
    var returnLine = "\n" + user.name + " DECIDES TO OBSERVE THE SITUATION AND GIVES TURN TO " + otherPerson.name + "!\n"
    bothLengths(user) = 80 + user.stand.speed*10 + bothLengths(user)
    if nextInTurn == defender then
      returnLine += defenderTurn
    returnLine

  // Represents the turn of the enemy/defender
  private def defenderTurn:String =
    if movesRemaining(defender,bothLengths(defender)) then
      var moveCounter = 0
      var move = defender.stand.getRandomFightMoveName
      while defender.stand.getFightMove(move).get.length > bothLengths(defender) && moveCounter < 3 do
        move = defender.stand.getRandomFightMoveName
        moveCounter += 1
      if moveCounter >= 3 then
        skipTurn(defender)
      else
        this.use(move, defender)

    else
      nextInTurn = attacker
      defender.name + " HAS LOWERED HIS ATTACK, NOW IS YOUR CHANCE !" + "\n\n"


  // Returns a boolean whether the user can still use moves on this turn
  private def movesRemaining(user: Person, lengthRemaining: Int):Boolean =
    user.stand.getFightMoves.exists(_.length <= lengthRemaining)

  private def healthPoints(person: Person):String =
    var healthBar = " ["
    for health <- 1 to person.health / 10 do
      healthBar += "#"
    for notHealth <- (person.health / 10) + 1 to person.baseHealth / 10 do
      healthBar += " "
    healthBar += "] "
    person.name + ":" + healthBar + person.health + " HP\n"