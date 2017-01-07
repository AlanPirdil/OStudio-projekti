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
  val effects = new SoundEffects
  val playerIcon = new Player // Initiate a player
  val sounds = new Music // Initiate the music
  val trumpImg = loadImage(playerIcon.img) // Load the player-icon
  var obstacles = Buffer[Obstacle]() //(new Obstacle(playerIcon.xAxis, 250, 50, 50))
  val wallImgTest = loadImage("src/jumping/wall.png")
  val spikes = loadImage("src/jumping/spikes.png")
  val level1 = Source.fromFile("src/jumping/lvl2.csv")
  val firstX = playerIcon.xAxis
  var obsCount = 0 
  var onTop: Boolean = false
  var gameSpeed = 0
  var pixelsGone = 0
  //obstacles += new Obstacle(firstX + 500, 250, 50, 50)

  private def readLevel() = {
    try {
      var rivinumero = 1
      for (rivi <- level1.getLines) {
        val arrayOfStrings = rivi.split(",")
        val arrayOfInts = arrayOfStrings.map(_.trim().toInt)
        val koko = arrayOfInts.size
        println(arrayOfInts(1))
        for(i<- 0 until koko){
          if(arrayOfInts(i) == 0){
            obstacles += new Obstacle("wall", firstX + i*30,rivinumero*30 + 86,30,30)
        } else if(arrayOfInts(i) == 1){
          obstacles += new Obstacle("spikes", firstX + i*30, rivinumero*30 + 86,30,30)
        }
      }
      rivinumero += 1
    }
 
  }      catch {
        case e: Exception => "juu" 
      }
    finally {
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
    effects.rewind1()
    effects.play1()
    if(playerIcon.vy == 20 || isOnTop) { // Checks if Trump is on the ground
     playerIcon.vy = -15 // Makes Trump jump
    }
  }
  
  
  
  //draw runs 60 time per second
 
  override def draw(): Unit = {
     
   if(gameState == 1) gameScreen
   else if(gameState == 2) mainMenu
   else if(gameState == 3) helpScreen
   else if(gameState == 4) endScreen
  }

  var screenSpeed = playerIcon.xAxis
  var sx = 0
  
  
  def gameScreen = {
    gameSpeed = 4
    pixelsGone += gameSpeed
    obstacles = obstacles.sortBy { _.x}
    var nextObstacle = obstacles(obsCount)
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
    image(trumpImg, playerIcon.xAxis, playerIcon.yAxis, playerIcon.width, playerIcon.height)
    
   //TESTIKUUTIO
    println(gameSpeed)
    if (obstacles.size > 0) {
      for (thisObs <- obstacles) {
      thisObs.x = thisObs.x - gameSpeed
      if(thisObs.obsType == "wall"){
      image(wallImgTest, thisObs.x, thisObs.y, 30, 30)
      } else if(thisObs.obsType == "spikes"){
        image(spikes, thisObs.x, thisObs.y, 30, 30)
      }
      }
    }
    
    if (isOnTop && obstacles(obsCount).obsType == "wall") {
      println("Now on top")
      playerIcon.vy = 0
      playerIcon.yAxis = obstacles(obsCount).y - playerIcon.height
      println( playerIcon.yAxis + " ja toinen arvo on" + obstacles(obsCount).y)
      if(mousePressed) playerIcon.vy = -15
    } else if(isOnTop && obstacles(obsCount).obsType == "spikes"){
      resetProgress()
      pixelsGone = 0
      gameState = 4
    }
    
    if (playerIcon.xAxis > obstacles(obsCount).x - gameSpeed + 30) {
      if (obstacles.length > obsCount + 1)
        obsCount += 1
    }   
    
    
    if (gameEnds) {
      resetProgress()
      pixelsGone = 0
      gameState = 4
    }
    
  }
  
   private def isOnTop: Boolean = {
    var currentX = obstacles(obsCount).x - gameSpeed
    //ONLY FOR TESTING PURPOSES:
    var testi =  playerIcon.xAxis + 30
    var testi2 = currentX + 30
   // println(gameEnds)
   // println(playerIcon.xAxis + 30  > currentX && playerIcon.yAxis + 50 == obstacles(obsCount).y && playerIcon.xAxis < currentX + 30)
   // println(playerIcon.xAxis + 30 + " > " + currentX + " && " + (playerIcon.yAxis + 50) + " == " + obstacles(obsCount).y + " && " + playerIcon.xAxis + " < " + (currentX + 30))
    //THIS, HOWEVER, IS RELEVANT:
    playerIcon.xAxis + obstacles(obsCount).kanta > currentX && playerIcon.yAxis + playerIcon.height > obstacles(obsCount).y && playerIcon.xAxis < currentX + obstacles(obsCount).kanta
  }
  
  //CHECKS IF THE PLAYER "COLLIDES" W/ THE WALL
  private def gameEnds: Boolean = {
    if (playerIcon.xAxis + obstacles(obsCount).kanta == obstacles(obsCount).x - gameSpeed && playerIcon.yAxis + playerIcon.height > obstacles(obsCount).y) {
      true
    } else {
      false
    }
  }
  
  def resetProgress() = {
     for (thisObs <- obstacles) {
      thisObs.x = thisObs.x + pixelsGone
      }
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
    private def endScreen = {
    gameSpeed = 0
    clear()
    background(blurredBg)
    textSize(60)
    fill(0,0,0)
    text("GAME OVER", 100, 100)
    
    val deathDeclaration = "You didn't survive to Mexico.\nCrooked Hillary put you to jail but Soon(tm)\nyou will get your revenge."
    textSize(20)
    text(deathDeclaration, 100, 150)
    text("Your score:", 100, 250)
  }
}