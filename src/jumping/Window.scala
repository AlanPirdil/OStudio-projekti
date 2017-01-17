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
  val level1 = Source.fromFile("src/jumping/level2.csv") 
  val level1Logo = loadImage("src/jumping/firstStatic.png")
  val level2Logo = loadImage("src/jumping/secondStatic.png")
  val level3Logo = loadImage("src/jumping/thirdStatic.png")
  val mouseOnLvl1 = loadImage("src/jumping/firstHover.png")
  val mouseOnLvl2 = loadImage("src/jumping/secondHover.png")
  val mouseOnLvl3 = loadImage("src/jumping/thirdHover.png")
  val playbutton = loadImage("src/jumping/playStatic.png")
  val helpbutton = loadImage("src/jumping/helpStatic.png")
  val mouseOnPlaybutton = loadImage("src/jumping/playHover.png")
  val mouseOnHelpbutton = loadImage("src/jumping/helpHover.png")
  val firstX = playerIcon.xAxis
  var obsCount = 0 
  var onTop: Boolean = false
  var gameSpeed = 0
  var pixelsGone = 0

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
     frameRate(60)
  }

  override def keyPressed() = {
    if(keyPressed) {
      if(key == 'p' || key == 'P') gameState = 5
      else if(key == 'm' || key == 'M') gameState = 2
      else if(key == 'h' || key == 'H') gameState = 3
      else if(key == 'n' || key == 'N') {
        sounds.nextSong()
        sounds.musicPlay()
      }
      else if(key == 's' || key == 'S') {
        if(sounds.isPaused) sounds.musicPlay()
        else sounds.pause()        
      } else if(key == ' ') {
            if(playerIcon.vy == 20 || isOnTop) { // Checks if Trump is on the ground
               playerIcon.vy = -15 // Makes Trump jump
              }
      }
      if (gameState == 5 && key == '1') {
        gameState = 1
      }
    }
  }
  
  override def mousePressed() = { //What happens when mouse is clicked
    effects.rewind1()
    effects.play1()
    
    if (playerIcon.vy == 20 || isOnTop && gameState == 1) { // Checks if Trump is on the ground
     playerIcon.vy = -15 // Makes Trump jump
    } 
    
    if (gameState == 5 && mouseX >= 85 && mouseX <= 185 && mouseY <= 245 && mouseY >= 145) {
    gameState = 1
    }
    
    if (gameState == 2 && mouseX >= 80 && mouseX <= 280 && mouseY >= 150 && mouseY <= 250) {
      gameState = 5
    }
    
    if (gameState == 2 && mouseX >= 360 && mouseX <= 560 && mouseY >= 150 && mouseY <= 250) {
      gameState = 3
    }
  }
  
  
  
  //draw runs 60 time per second
 
  override def draw(): Unit = {
     
   if(gameState == 1) gameScreen
   else if(gameState == 2) mainMenu
   else if(gameState == 3) helpScreen
   else if(gameState == 4) endScreen
   else if(gameState == 5) levelSelect
  }

  var screenSpeed = playerIcon.xAxis

  //Start of gameScreen
  def gameScreen = {
    gameSpeed = 4
    pixelsGone += gameSpeed
    println(pixelsGone)
    sounds.gameMusic()
    obstacles = obstacles.sortBy { _.x}
    var nextObstacle = obstacles(obsCount)
    image(bgImg, screenSpeed, 0)
    image(bgImg, screenSpeed - areaWidth, 0)
    image(bgImg, screenSpeed + areaWidth, 0)
    screenSpeed -= 2
    if(abs(screenSpeed) > areaWidth) screenSpeed= 0
    
    textSize(20)
    fill(255, 15, 15)
    text("Your Score: " + millis(), 250, 40)
    playerIcon.jump()
    image(trumpImg, playerIcon.xAxis, playerIcon.yAxis, playerIcon.width, playerIcon.height)

    // Draws the obstacles
    if (obstacles.size > 0) {
      for (thisObs <- obstacles) {
      if (thisObs.obsType == "wall") {
      image(wallImgTest, thisObs.x - pixelsGone, thisObs.y, 30, 30)
      } else if (thisObs.obsType == "spikes") {
        image(spikes, thisObs.x - pixelsGone, thisObs.y, 30, 30)
      }
      }
    }
    
    //When the player is on a wall, he's able to jump
    if (isOnTop && nextObstacle.obsType == "wall") {
      playerIcon.vy = 0
      playerIcon.yAxis = obstacles(obsCount).y - playerIcon.height
      if(mousePressed) playerIcon.vy = -15
    } 
    //If the player lands on a spike he dies
    else if(isOnTop && obstacles(obsCount).obsType == "spikes"){
      resetProgress()
      gameState = 4
    }

    //Chooses the next obstacle after moving past one
    if (playerIcon.xAxis > obstacles(obsCount).x - pixelsGone + 30) {
      if (obstacles.length > obsCount + 1)
        obsCount += 1
    }   
    
    //Change the gameState when game ends
    if (gameEnds) {
      resetProgress()
      gameState = 4
    } 
  }
  
    //Checks if the player is on top of something 
   private def isOnTop: Boolean = {
    var currentX = obstacles(obsCount).x - pixelsGone
    playerIcon.xAxis + playerIcon.width > currentX && playerIcon.yAxis + playerIcon.height > obstacles(obsCount).y && playerIcon.xAxis < currentX + obstacles(obsCount).width
  }
  

    //Checks whether the player has collided into an obstacle
  private def gameEnds: Boolean = {
    if (playerIcon.xAxis + obstacles(obsCount).width == obstacles(obsCount).x - pixelsGone && playerIcon.yAxis + playerIcon.height > obstacles(obsCount).y) {
      true
    } else {
      false
    }
  }
  
    //Resets the progress so a level can be played again without closing the window
  def resetProgress() = {
    pixelsGone = 0
    obsCount = 0
  }

  //Start of LevelSelect
  private def levelSelect = {
    clear()
    background(blurredBg)
    textSize(60)
    fill(0,0,0)
    text("Level select", 100, 75)

    
    image(level1Logo, 85, 145, 100, 100)
    image(level2Logo, 270, 145, 100, 100)
    image(level3Logo, 455, 145, 100, 100)   
    
    if (gameState == 5 && mouseX >= 85 && mouseX <= 185 && mouseY <= 245 && mouseY >= 145) {
      image(mouseOnLvl1, 85, 145, 100, 100)
    }
    
    if (gameState == 5 && mouseX >= 270 && mouseX <= 370 && mouseY <= 245 && mouseY >= 145) {
      image(mouseOnLvl2, 270, 145, 100, 100)
    }
    
    if (gameState == 5 && mouseX >= 455 && mouseX <= 555 && mouseY <= 245 && mouseY >= 145) {
      image(mouseOnLvl3, 455, 145, 100, 100)
    }
    
  }
   
  private def helpScreen = {
    clear()
    background(blurredBg)
    textSize(60)
    fill(0,0,0)
    text("Help", 100, 100)
    
    val helperText = "Press space to jump\nPress m to go to the main menu\nPress n to switch background music\nPress s to mute the soundtrack\nPress e to mute sound effects"
    textSize(20)
    text(helperText, 100, 150)
  }
  
  private def mainMenu = {
    clear()
    background(blurredBg)
    textSize(60)
    fill(0,0,0)
    text("Trumpoliini", 100, 100)
    
    image(playbutton, 80, 150, 200, 100)
    image(helpbutton, 360, 150, 200, 100)
    
    if (gameState == 2 && mouseX >= 80 && mouseX <= 280 && mouseY >= 150 && mouseY <= 250) {
      image(mouseOnPlaybutton, 80, 150, 200, 100)
    }
    
    if (gameState == 2 && mouseX >= 360 && mouseX <= 560 && mouseY >= 150 && mouseY <= 250) {
      image(mouseOnHelpbutton, 360, 150, 200, 100)
    } 
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