package jumping
import scala.math._
import processing.core._
import ddf.minim._
import scala.collection.mutable.Buffer
import scala.io.Source

object Window extends PApplet{
  
  def main(args:Array[String]) {
    PApplet.main(Array[String]("jumping.Window"))
  }

}

class Window extends PApplet {
  // useful variables
  val areaWidth = 640
  val areaHeight = 320
  
  var gameState = 2
  
  def situation = this.gameState
  
  val bgImg = loadImage("src/jumping/BG.png") // Load the background image
  val blurredBg = loadImage("src/jumping/blurredBg.png")
  val playerIcon = new Player // Initiate a player
  val sounds = new Music // Initiate the music
  val trumpImg = loadImage(playerIcon.img) // Load the player-icon
  val obstacles = Buffer[Obstacle]() //(new Obstacle(playerIcon.xAxis, 250, 50, 50))
  val wallImgTest = loadImage("src/jumping/wall.png")
  val level1 = Source.fromFile("src/jumping/lvl1.csv")
  val firstX = playerIcon.xAxis
  var obsCount = 0 
  var onTop: Boolean = false
  //obstacles += new Obstacle(firstX + 500, 250, 50, 50)

  private def readLevel() = {
    try {
      var rivinumero = 1
      for (rivi <- level1.getLines) {
        val aabel = rivi.split(",")
        val baabel = aabel.map(_.toInt)
        val koko = baabel.size
        for(i<- 0 until koko){
          if(baabel(i) == 0){
            obstacles += new Obstacle(firstX + i*30,rivinumero*30 + 86,30,30)
        }
      }
      rivinumero += 1
    }
 
  } finally {
    level1.close()
  }
 }
  
  //Main setup
  override def setup(): Unit = {
     sounds.musicPlay()
     size(areaWidth, areaHeight)
     noStroke()
     readLevel()
     frameRate(45)
  }

  override def keyPressed() = {
    if(keyPressed) {
      if(key == 'p' || key == 'P') gameState = 1
      else if(key == 'm' || key == 'M') gameState = 2
      else if(key == 'h' || key == 'H') gameState = 3
      else if(key == 's' || key == 'S') {
        if(sounds.isPaused) sounds.musicPlay()
        else sounds.pause()        
      } else if(key == ' '){
            if(playerIcon.vy == 20 || isOnTop) { // Checks if Trump is on the ground
               playerIcon.vy = -15 // Makes Trump jump
              }
      }
    }
  }
  
  override def mousePressed() = { //What happens when mouse is clicked
    if(playerIcon.vy == 20 || isOnTop) { // Checks if Trump is on the ground
     playerIcon.vy = -15 // Makes Trump jump
    }
  }
  
  
  
  //draw runs 60 time per second
 
  override def draw(): Unit = {
     
   if(gameState == 1) gameScreen
   else if(gameState == 2) mainMenu
   else if(gameState == 3) helpScreen
  }

  var screenSpeed = playerIcon.xAxis
  var sx = 0
  var gameSpeed = 0
  def gameScreen = {
    clear()
    
    image(bgImg, screenSpeed, 0)
    image(bgImg, screenSpeed - areaWidth, 0)
    image(bgImg, screenSpeed + areaWidth, 0)
    screenSpeed -= 2
    if(abs(screenSpeed) > areaWidth) screenSpeed= 0
    sx += 2
    
    textSize(20)
    fill(255, 15, 15)
    text("Your Score: " + millis() / 100 , 250, 40)
    playerIcon.jump()
    image(trumpImg, playerIcon.xAxis, playerIcon.yAxis, 50, 50)
    
   //TESTIKUUTIO
    var gameSpeed = 0
    gameSpeed += 2
    if (obstacles.size > 0) {
      for (thisObs <- obstacles) {
      thisObs.x = thisObs.x - gameSpeed
      image(wallImgTest, thisObs.x, thisObs.y, 30, 30)
      }
    }
    
    if (isOnTop) {
      println("Now on top")
      playerIcon.vy = 0
      playerIcon.yAxis = obstacles(obsCount).y - 41
      if(mousePressed) playerIcon.vy = -15
    }
    
    if (playerIcon.xAxis > obstacles(obsCount).x - gameSpeed + 30) {
      if (obstacles.length > obsCount + 1)
        obsCount += 1
    }   
  }
  
  private def isOnTop: Boolean = {
    var currentX = obstacles(obsCount).x - gameSpeed
    var testi =  playerIcon.xAxis + 30
    var testi2 = currentX + 30
    println(playerIcon.xAxis + 30  > currentX && playerIcon.yAxis + 30 == obstacles(obsCount).y && playerIcon.xAxis < currentX + 30)
    println(playerIcon.xAxis + 30 + " > " + currentX + " && " + (playerIcon.yAxis + 30) + " == " + obstacles(obsCount).y + " && " + playerIcon.xAxis + " < " + (currentX + 30))
    playerIcon.xAxis + 30  > currentX && playerIcon.yAxis + 30 == obstacles(obsCount).y && playerIcon.xAxis < currentX + 30
  }
     
     
//     Make Trump jump
     
 //END OF gameScreen
   
  private def helpScreen = {
    clear()
    background(blurredBg)
    textSize(60)
    fill(0,0,0)
    text("Help", 100, 100)
    
    val helperText = "Press space to jump\nPress m to go to the main menu\nPress s to mute the soundtrack\nPress e to mute sound effects"
    textSize(20)
    text(helperText, 100, 150)
  }
  
  private def mainMenu = {
    clear()
    background(blurredBg)
    textSize(60)
    fill(0,0,0)
    text("Trumpoliini", 100, 100)
    
    val instructionText = "Crooked Hillary is chasing you.\nYou must escape her to Mexico by jumping\nover the wall you built.\nPress space to jump\nPress h to get help"
    textSize(20)
    text(instructionText, 100, 150)
  }
}