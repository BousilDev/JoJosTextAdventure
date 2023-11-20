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
  val title = "JoJo's Text Adventure"

  private val japan           = Area("Tokyo", "Tokyo, the capital of Japan. \nIt is Jotaro Kujo's native city and the starting point for the heroes' journey.")
  private val singapore       = Area("Singapore", "Singapore, a sovereign city-state and island situated right in the middle of South-Asia. \nIt is one of the richest and most developed cities in Asia.")
  private val india           = Area("India", "A crowded pedestrian shopping area in Calcutta. \nIt is precisely there that Jean Pierre Polnareff encounters Hol Horse and begins to fight with him.")
  private val pakistan        = Area("Pakistan", "You are at a small clearing in the middle of forest.\nNearly invisible, twisted paths lead in many directions.")
  private val egyptianDesert  = Area("Egyptian desert", "The Egyptian desert. More dunes and rocky formations.")
  private val nile            = Area("The River Nile", "The River Nile, one of the world's longest rivers and known for being the main artery of Egypt, the main reason of the rise of the Ancient Egyptian civilization. \nThe Joestar Group has to navigate down the Nile to reach Cairo.")
  private val cairo           = Area("Cairo", "Cairo, the capital of Egypt. \nIt is the end goal of the heroes, as it is the city where DIO resides in his mansion. \nCairo is a sprawling city of 2,000,000 habitants at the time of the story and a long history, with labyrinthine streets and unique local customs.")
  private val dioMansion      = Area("Dio's mansion", "Home sweet home! Now the only thing you need is a working remote control.")

  private val home            = Area("Home", "an extravagant three-story mansion made of terracotta, noted to be quite old, situated in one of the older districts of Cairo. \nThe main building is surrounded by a wall and an extensive garden which includes numerous palm trees and other exotic plants.")

  private val destination = home

  japan         .setNeighbors(Vector("north" -> india,                               "south" -> pakistan,  "west" -> singapore        ))
  singapore     .setNeighbors(Vector("north" -> india,          "east" -> japan,     "south" -> pakistan,  "west" -> cairo            ))
  india         .setNeighbors(Vector(                           "east" -> japan,     "south" -> singapore, "west" -> egyptianDesert   ))
  pakistan      .setNeighbors(Vector("north" -> singapore,      "east" -> japan,                           "west" -> nile             ))
  egyptianDesert.setNeighbors(Vector(                           "east" -> india,     "south" -> cairo                                 ))
  nile          .setNeighbors(Vector("north" -> cairo,          "east" -> pakistan                                                    ))
  cairo         .setNeighbors(Vector("north" -> egyptianDesert, "east" -> singapore, "south" -> nile,      "west" -> dioMansion       ))
  dioMansion    .setNeighbors(Vector(                           "east" -> cairo                                                       ))

  japan.addVehicle(Item("airplane", "It's an airplane"))
/*
  clearing.addItem(Item("battery", "It's a small battery cell. Looks new."))
  southForest.addItem(Item("remote", "It's the remote control for your TV.\nWhat it was doing in the forest, you have no idea.\nProblem is, there's no battery."))
*/
  private val jotaro   = Person("Jotaro Kujo",     Stand("Star Patinum",      5, 5, 3, 5, 5, 5), 150)
  private val kakkioin = Person("Kakyoin Noriaki", Stand("Hierophant Green",  3, 4, 5, 4, 3, 2), 100)
  private val rubber   = Person("Rubber Soul",     Stand("Yellow Temperance", 2, 3, 1, 5, 1, 2), 50)
  private val holHorse = Person("Hol Horse",       Stand("Emperor",           4, 4, 4, 3, 1, 1), 75)
  private val enyaba   = Person("Enyaba",          Stand("Justice",           2, 1, 5, 5, 1, 1), 50)
  private val dio      = Person("DIO",             Stand("THE WORLD",         5, 5, 3, 5, 4, 4), 250)
  //private val persons = Vector[Person](jotaro, kakkioin, rubber)

  jotaro.stand.addFightMove(FightMove("Ora",10,75))
  jotaro.stand.addFightMove(FightMove("Star Finger",20,120))
  jotaro.stand.addFightMove(FightMove("ZA WARUDO",50,200))

  kakkioin.stand.addFightMove(FightMove("Emerald Splash",50,140))
  kakkioin.stand.addFightMove(FightMove("Marionette Control",15,90))


  japan     .setEnemy(kakkioin)
  singapore .setEnemy(rubber)
  india     .setEnemy(holHorse)
  pakistan  .setEnemy(enyaba)
  dioMansion.setEnemy(dio)

  /** The character that the player controls in the game. */
  val player = Player(japan, jotaro)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 50


  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = !dioMansion.containsEnemy("DIO")

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit || this.player.isDefeated

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

