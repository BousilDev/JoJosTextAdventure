package o1.adventure

/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of “hard-coded” information that pertains to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Adventure:

  /** the name of the game */
  val title = "A Forest Adventure"

  private val japan       = Area("Japan", "You are somewhere in the forest. There are a lot of trees here.\nBirds are singing.")
  private val singapore   = Area("Singapore", "You are somewhere in the forest. A tangle of bushes blocks further passage north.\nBirds are singing.")
  private val india       = Area("India", "The forest just goes on and on.")
  private val pakistan    = Area("Pakistan", "You are at a small clearing in the middle of forest.\nNearly invisible, twisted paths lead in many directions.")
  private val northEgypt  = Area("North Egypt", "You are in a dense tangle of bushes. It's hard to see exactly where you're going.")
  private val southEgypt  = Area("South Egypt")
  private val cairo       = Area("Cairo")
  private val dioPalace   = Area("Dio's palace", "Home sweet home! Now the only thing you need is a working remote control.")


  private val destination = home

  middle     .setNeighbors(Vector("north" -> northForest, "east" -> tangle, "south" -> southForest, "west" -> clearing   ))
  northForest.setNeighbors(Vector(                        "east" -> tangle, "south" -> middle,      "west" -> clearing   ))
  southForest.setNeighbors(Vector("north" -> middle,      "east" -> tangle, "south" -> southForest, "west" -> clearing   ))
  clearing   .setNeighbors(Vector("north" -> northForest, "east" -> middle, "south" -> southForest, "west" -> northForest))
  tangle     .setNeighbors(Vector("north" -> northForest, "east" -> home,   "south" -> southForest, "west" -> northForest))
  home       .setNeighbors(Vector(                                                                  "west" -> tangle     ))


  clearing.addItem(Item("battery", "It's a small battery cell. Looks new."))
  southForest.addItem(Item("remote", "It's the remote control for your TV.\nWhat it was doing in the forest, you have no idea.\nProblem is, there's no battery."))

  private val jotaro   = Person("Jotaro Kujo",     Stand("Star Patinum",      5, 5, 3, 5, 5, 5), 150)
  private val kakkioin = Person("Kakyoin Noriaki", Stand("Hierophant Green",  3, 4, 5, 4, 3, 2), 100)
  private val rubber   = Person("Rubber Soul",     Stand("Yellow Temperance", 2, 3, 1, 5, 1, 2), 50)
  private val holHorse = Person("Hol Horse",       Stand("Emperor",           4, 4, 4, 3, 1, 1), 75)
  private val enyaba   = Person("Enyaba",          Stand("Justice",           2, 1, 5, 5, 1, 1), 50)
  private val dio      = Person("DIO",             Stand("THE WORLD",         5, 5, 3, 5, 4, 4), 250)
  //private val persons = Vector[Person](jotaro, kakkioin, rubber)
  /** The character that the player controls in the game. */
  val player = Player(middle, jotaro)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 40


  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = this.player.location == this.destination && this.player.has("remote") && this.player.has("battery")

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "You have just been \"extracted\" from prison where you developed a power called a stand ability.\nYour grandfather and his loyal friend need your help to save your mother from a deadly illness which can be cured if a revived vampire is killed.\nYou set on a 50-day-journey from Japan to Egypt with your grandfather and his friend Avdol (both are also stand users) to defeat the evil vampire Dio.\nGetting to Egypt just isn't going to be that easy because Dio has sent his assassins to kill you and your companions.\nYou must face all threaths victorious and get to Egypt in time to win.\nIf you fail, your mother dies and you lose the game."


  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage =
    if this.isComplete then
      "Home at last... and phew, just in time! Well done!"
    else if this.turnCount == this.timeLimit then
      "Oh no! Time's up. Starved of entertainment, you collapse and weep like a child.\nGame over!"
    else  // game over due to player quitting
      "Quitter!"


  /** Plays a turn by executing the given in-game command, such as “go west”. Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String) =
    val action = Action(command)
    val outcomeReport = action.execute(this.player)
    if outcomeReport.isDefined then
      this.turnCount += 1
    outcomeReport.getOrElse(s"""Unknown command: "$command".""")

end Adventure

