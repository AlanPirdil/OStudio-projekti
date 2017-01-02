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
  val blurredBg = loadImage("src/jumping/blurredBg.png")
  val playerIcon = new Player // Initiate a player
  val sounds = new Music // Initiate the music
  val trumpImg = loadImage(playerIcon.img) // Load the player-icon
  val obstacles = Buffer[Obstacle](new Obstacle(playerIcon.xAxis, 250, 1)) // Create a buffer for obstacles
  val wallImgTest = loadImage("src/jumping/wall.png")


  
  //Main setup
  override def setup(): Unit = {
     sounds.musicPlay()
     size(areaWidth, areaHeight)
     noStroke()
    
  }

  override def keyPressed() = {
    if(keyPressed) {
      if(key == 'p' || key == 'P') gameState = 1
      else if(key == 'm' || key == 'M') gameState = 2
      else if(key == 'h' || key == 'H') gameState = 3
      else if(key == 's' || key == 'S') {
        if(sounds.isPaused) sounds.musicPlay()
        else sounds.pause()        
      }
    }
  }
  
  override def mousePressed() = { //What happens when mouse is clicked
    if(playerIcon.vy == 20) { // Checks if Trump is on the ground
     playerIcon.vy = -15 // Makes Trump jump
    }
  }
  
  
  
  //draw runs 60 time per second
 
  override def draw(): Unit = {
     
   if(gameState == 1) gameScreen
   else if(gameState == 2) mainMenu
   else if(gameState == 3) helpScreen
  }

  var sx = 0
  private def gameScreen = {
    clear()
    background(bgImg)
    playerIcon.jump()
    image(trumpImg, playerIcon.xAxis, playerIcon.yAxis, 50, 50)
    
   //TESTIKUUTIO
    rect(obstacles(0).x + 200 - sx,obstacles(0).y, 50, 50)
    image(wallImgTest, obstacles(0).x + 200 - sx, 250, 50, 50)
    sx += 2
  }
    
   
  private def helpScreen = {
    clear()
    background(blurredBg)
    textSize(60)
    //fill(0,0,0)
    text("Help", 100, 100)
    
    val helperText = "Press space to jump\nPress m to go to the main menu\n Press s to mute the soundtrack\n Press e to mute sound effects"
    textSize(20)
    text(helperText, 100, 150)
  }
  
  private def mainMenu = {
    clear()
    background(blurredBg)
    textSize(60)
    //fill(0,0,0)
    text("Trumpoliini", 100, 100)
    
    val instructionText = "Crooked Hillary is chasing you.\nYou must escape her to Mexico by jumping\n over the wall you built.\n Press space to jump\nPress h to get help"
    textSize(20)
    text(instructionText, 100, 150)
  }
}