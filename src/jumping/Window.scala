package jumping
import scala.math._
import processing.core._
import ddf.minim._
import scala.collection.mutable.Buffer

object Window extends PApplet{
  
  def main(args:Array[String]) {
    PApplet.main(Array[String]("jumping.Window"))
  }

}

class Window extends PApplet {
  // useful variables
  val areaWidth = 640
  val areaHeight = 320
  
  var gameState = 1
  
  val bgImg = loadImage("src/jumping/BG.png") // Load the background image
  val playerIcon = new Player // Initiate a player
  val sounds = new Music // Initiate the music
  val trumpImg = loadImage(playerIcon.img) // Load the player-icon
  val obstacles = Buffer[Obstacle](new Obstacle(playerIcon.xAxis, 250, 1)) // Create a buffer for obstacles

  
  
  
  //Main setup
  override def setup(): Unit = {
     sounds.musicSetup()
     size(areaWidth, areaHeight)
     noStroke()
    
  }

  
  override def mousePressed() = { //What happens when mouse is clicked
    if(playerIcon.vy == 20) { // Checks if Trump is on the ground
     playerIcon.vy = -15 // Makes Trump jump
    }
   }
  
  
  
  //draw runs 60 time per second
   var vy = 0
   var sx = 0
  override def draw(): Unit = {
     
   if(gameState == 1) {
    clear()
    background(bgImg)
    playerIcon.jump()
    image(trumpImg, playerIcon.xAxis, playerIcon.yAxis, 50, 50)
    
    
   //TESTIKUUTIO
    rect(obstacles(0).x+200-sx,obstacles(0).y,50,50)
    sx += 2
   }
  }
}