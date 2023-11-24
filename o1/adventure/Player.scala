package o1.adventure

import scala.collection.mutable.Map

/** A `Player` object represents a player character controlled by the real-life user
  * of the program.
  *
  * A player object’s state is mutable: the player’s location and possessions can change,
  * for instance.
  *
  * @param startingArea  the player’s initial location
 *  @param startingPerson the player's initial person, this has stores information of player's Stand as well*/
class Player(startingArea: Area, startingPerson: Person):

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private var items = Map[String,Item]()
  private var vehicles = Map[String,Item]()
  private var currentPerson = startingPerson
  private var newBattle:Option[Battle] = None

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  /** Returns the player’s current location. */
  def location = this.currentLocation

  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player’s current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def go(direction: String) =
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if destination.isDefined then "You drive to " + direction + " with your " + this.vehicles.last._1 + "." else "You can't go " + direction + "."

  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def rest() =
    "You rest for a while. Better get a move on, though."

  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() =
    this.quitCommandGiven = true
    ""

  def isDefeated = this.currentPerson.isDefeated

  /** Returns a brief description of the player’s state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name

  def drop(itemName: String):String =
    if has(itemName) then
      this.currentLocation.addItem(items.values.find(_.name==itemName).get)
      items.remove(itemName)
      "You drop the " + itemName + "."
    else
      "You don't have that!"

  def examine(itemName: String): String =
    if has(itemName) then
      val desc = items.values.find(_.name==itemName).get.description
      "You look closely at the "+itemName+".\n"+desc
    else
      "If you want to examine something, you need to pick it up first."

  def get(itemName: String): String =
    if this.currentLocation.contains(itemName) then
      val newItem = this.currentLocation.remove(itemName).get
      items += (newItem.name,newItem)
      if itemName == "map" then
        currentLocation.allowNeighbours
      "You pick up the " + newItem.name
    else
      "There is no " + itemName + " here to pick up."

  def has(itemName: String): Boolean = items.keys.exists(_==itemName)

  def inventory: String =
    if items.nonEmpty then
      "You are carrying:\n" + items.keys.mkString("\n")
    else
      "You are empty-handed."

  def use(fightMove: String): String =
    if newBattle.isDefined then
      if !newBattle.get.hasEnded then
        val returnLine = newBattle.get.use(fightMove,this.currentPerson)
        this.checkBattleStatus
        returnLine
      else
        "Your previous battle has already ended..." + "\n"
    else
      "There's no active battle right now..." + "\n"

  def battle(enemy: String): String =
    if currentLocation.containsEnemy(enemy) then
      val enemyPerson = currentLocation.getEnemy(enemy).get
      newBattle = Some(Battle(currentPerson, enemyPerson))
      currentLocation.setBattle(newBattle)
      currentLocation.setCurrentPerson(this.currentPerson)

      if enemyPerson.name == "dio" then
        currentPerson.stand.learnHiddenMoves

      val returnLine = newBattle.get.startBattle
      this.checkBattleStatus
      returnLine
    else
      "No enemy called " + enemy + " found..." + "\n"

  private def checkBattleStatus: Unit =
    if newBattle.get.hasEnded then
      currentPerson.resetHealth
      currentLocation.removeEnemy(newBattle.get.defender)
      currentLocation.allowVehicles

  def drive(vehicle: String): String =
    if this.currentLocation.containsVehicle(vehicle) then
      val newVehicle = this.currentLocation.remove(vehicle).get
      vehicles = Map(newVehicle.name -> newVehicle)
      if !(vehicle == "defender") then
        currentLocation.allowNeighbours
      "You hop on " + newVehicle.name + "\n" + newVehicle.description
    else
      "No vehicle called " + vehicle + " found here..."

  def skip:String =
    if newBattle.isDefined then
      if !newBattle.get.hasEnded then
        val returnLine = newBattle.get.skipTurn(this.currentPerson)
        this.checkBattleStatus
        returnLine
      else
        "Your previous battle has already ended..." + "\n"
    else
      "There's no active battle right now..." + "\n"

  def help:String =
    "You can use the following commands in addition to the original commands:\nbattle \"enemy\"\nuse \"fightMove\"\nskip\ndrive \"vehicle\""

end Player

