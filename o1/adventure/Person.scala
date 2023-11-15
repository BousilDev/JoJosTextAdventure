package o1.adventure

/**
 * Person
 * @param name
 * @param stand
 * @param health Person's health, can range from 0 to 250 (inclusive)
 */

class Person(val name: String, val stand: Stand, var health: Int):
  private val baseHealth = health
  
  def decreaseHealth(amount: Int):Unit = health -= amount
  
  def resetHealth:Unit =
    health = baseHealth
    
  def heal(amount: Int):Unit = health += amount