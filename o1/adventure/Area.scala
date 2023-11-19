package o1.adventure

import scala.collection.mutable.Buffer
import scala.collection.mutable.Map

/** The class `Area` represents locations in a text adventure game world. A game world
  * consists of areas. In general, an “area” can be pretty much anything: a room, a building,
  * an acre of forest, or something completely different. What different areas have in
  * common is that players can be located in them and that they can have exits leading to
  * other, neighboring areas. An area also has a name and a description.
  * @param name         the name of the area
  * @param description  a basic description of the area (typically not including information about items) */
class Area(var name: String, var description: String):

  private val neighbors = Map[String, Area]()
  private val hiddenNeighbors = Map[String, Area]()
  private var items = Buffer[Item]()
  private var vehicles = Buffer[Item]()
  private var enemies = Buffer[Person]()
  private var hasBattle = false
  private var currentPerson:Option[Person] = None

  /** Returns the area that can be reached from this area by moving in the given direction. The result
    * is returned in an `Option`; `None` is returned if there is no exit in the given direction. */
  def neighbor(direction: String) = this.neighbors.get(direction)

  /** Adds an exit from this area to the given area. The neighboring area is reached by moving in
    * the specified direction from this area. */
  def setNeighbor(direction: String, neighbor: Area) =
    this.hiddenNeighbors += direction -> neighbor

  /** Adds exits from this area to the given areas. Calling this method is equivalent to calling
    * the `setNeighbor` method on each of the given direction–area pairs.
    * @param exits  contains pairs consisting of a direction and the neighboring area in that direction
    * @see [[setNeighbor]] */
  def setNeighbors(exits: Vector[(String, Area)]) =
    this.hiddenNeighbors ++= exits


  def setEnemy(enemy: Person) = this.enemies += enemy

  def getEnemy(enemy: String):Option[Person] = this.enemies.find(_.name == enemy)


  // Sets the value of hasBattle to desired boolean value
  def setBattle(boolean:Boolean) = this.hasBattle = boolean

  def setCurrentPerson(person:Person) = this.currentPerson = Some(person)

  // Sets neighbours "visible". Meant to be used after player has defeated the enemy in current location
  def setAllowNeighbours(boolean:Boolean) = neighbors ++ hiddenNeighbors

  /** Returns a multi-line description of the area as a player sees it. This includes a basic
    * description of the area as well as information about exits and items. If there are no
    * items present, the return value has the form "DESCRIPTION\n\nExits available:
    * DIRECTIONS SEPARATED BY SPACES". If there are one or more items present, the return
    * value has the form "DESCRIPTION\nYou see here: ITEMS SEPARATED BY SPACES\n\nExits available:
    * DIRECTIONS SEPARATED BY SPACES". The items and directions are listed in an arbitrary order. */
  def fullDescription =
    // When there is no battle
    if !hasBattle then
      val exitList = "\n\nExits available: " + this.neighbors.keys.mkString(" ")
      val itemList = "\nYou see here: " + this.items.mkString(" ")
      val enemyList = "\nEnemies here: " + this.enemies.mkString(" ")
      if items.nonEmpty && enemies.nonEmpty then
        this.description + itemList + enemyList + exitList
      else if items.isEmpty && enemies.nonEmpty then
        this.description + enemyList + exitList
      else if items.nonEmpty && enemies.isEmpty then
        this.description + itemList + exitList
      else
        this.description + exitList

    // When a battle is going on
    else
      var moveDescription = "YOUR ABILITIES ARE:\n\n"
      if this.currentPerson.isDefined then
        val moves = this.currentPerson.get.stand.getFightMoves
        val moveNames = moves.map(_.name)
        val moveLengths = moves.map(_.length)
        val moveDamages = moves.map(_.damage)
        for num <- moves.indices do
          moveDescription += moveNames(num) + "     (" + moveLengths(num) + " Energy)" + "   (" + moveDamages(num) + " Damage)\n"
      moveDescription + "\nCHOOSE ONE TO USE!"


  /** Returns a single-line description of the area for debugging purposes. */
  override def toString = this.name + ": " + this.description.replaceAll("\n", " ").take(150)


  def addItem(item: Item): Unit = items += item
  def addVehicle(vehicle: Item): Unit = vehicles += vehicle

  def contains(itemName: String): Boolean = items.exists(_.name==itemName)
  def containsVehicle(vehicleName: String): Boolean = vehicles.exists(_.name==vehicleName)
  def containsEnemy(enemyName: String): Boolean = enemies.exists(_.name == enemyName)

  def remove(itemName: String): Option[Item] =
    val ret = (items++vehicles).find(_.name==itemName)
    items = items.filterNot(_.name==itemName)
    vehicles = vehicles.filterNot(_.name==itemName)
    ret

  def removeEnemy(enemy: Person) =
    enemies = enemies.filterNot(_.name == enemy.name)

end Area

