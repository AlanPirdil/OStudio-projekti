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
  // define the area measures
  val areaWidth = 640
  val areaHeight = 320
  
  //Starts from main menu
  var gameState = 2
  def situation = this.gameState
  
  //initialise sounds
  val effects = new SoundEffects
  val sounds = new Music
  
  //initialise player
  val playerIcon = new Player // Initiate a player
  
  //loads all needed images
  val bgImg = loadImage("src/photos/BG.png")
  val blurredBg = loadImage("src/photos/blurredBg.png")
  val level1Logo = loadImage("src/photos/firstStatic.png")
  val level2Logo = loadImage("src/photos/secondStatic.png")
  val level3Logo = loadImage("src/photos/thirdStatic.png")
  val mouseOnLvl1 = loadImage("src/photos/firstHover.png")
  val mouseOnLvl2 = loadImage("src/photos/secondHover.png")
  val mouseOnLvl3 = loadImage("src/photos/thirdHover.png")
  val playbutton = loadImage("src/photos/playStatic.png")
  val helpbutton = loadImage("src/photos/helpStatic.png")
  val mouseOnPlaybutton = loadImage("src/photos/playHover.png")
  val mouseOnHelpbutton = loadImage("src/photos/helpHover.png")
  val wallImgTest = loadImage("src/photos/wall.png")
  val spikes = loadImage("src/photos/spikes.png")
  val trumpImg = loadImage(playerIcon.img)
  val backbutton = loadImage("src/photos/back.png")
  val tryagain = loadImage("src/photos/tryagain.png")
  
  //loads levelfiles
  val level1 = Source.fromFile("src/jumping/level1.csv")
  val level2 = Source.fromFile("src/jumping/level2.csv")
  
  //initialise a buffer for obstacles
  var obstacles = Buffer[Obstacle]() 
  val firstX = playerIcon.xAxis
  var obsCount = 0 
  var onTop: Boolean = false
  var pixelsGone = 0
  var chosenLevel = level2

  private def readLevel() = {
    try {
      var rivinumero = 1
      for (rivi <- chosenLevel.getLines) {
        val arrayOfStrings = rivi.split(",")
        val arrayOfInts = arrayOfStrings.map(_.trim().toInt)
        val koko = arrayOfInts.size
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
      if(key == 'p' || key == 'P') {
        loop()
        gameState = 5
      }
      else if(key == 'm' || key == 'M') {
        loop()
        gameState = 2
      }
      else if(key == 'h' || key == 'H') {
        loop()
        gameState = 3
      }
      else if(key == 'n' || key == 'N') {
        sounds.nextSong()
      }
      else if(key == 's' || key == 'S') {
        if(sounds.isPaused) sounds.musicPlay()
        else sounds.pause()        
      }
      if (gameState == 5 && key == '1') {
        gameState = 1
      }
    }
  }
  
  //What happens when mouse is clicked
  override def mousePressed() = { 
    effects.rewind1()
    if (gameState == 1) {
      effects.play1()
    }
    
    if (playerIcon.vy == 20 || isOnTop && gameState == 1) { // Checks if Trump is on the ground
     playerIcon.vy = -15 // Makes Trump jump
    } 
    
    if (gameState == 5 && mouseX >= 85 && mouseX <= 185 && mouseY <= 245 && mouseY >= 145) {
      gameState = 1
    }
    
    if (gameState == 5 && mouseX >= 270 && mouseX <= 370 && mouseY <= 245 && mouseY >= 145) {
      gameState = 1  
    }
    
    if (gameState == 5 && mouseX >= 455 && mouseX <= 555 && mouseY <= 245 && mouseY >= 145) {
      gameState = 1
    }
    if (gameState == 2 && mouseX >= 80 && mouseX <= 280 && mouseY >= 150 && mouseY <= 250) {
      gameState = 5
    }
    
    if (gameState == 2 && mouseX >= 360 && mouseX <= 560 && mouseY >= 150 && mouseY <= 250) {
      gameState = 3
    }
    
    if (gameState == 3 && mouseX >= 400 && mouseX <= 600 && mouseY >= 25 && mouseY <= 125) {
      gameState = 2
    }
    
    if (gameState == 4 && mouseX >= 113 && mouseX <= 263 && mouseY >= 225 && mouseY <= 300) {
      gameState = 1
    }
    
    if (gameState == 4 && mouseX >= 376 && mouseX <= 526 && mouseY >= 225 && mouseY <= 300) {
      gameState = 2
    }
       
  }
  
  
  
  //draw runs 60 time per second
 
  override def draw(): Unit = {
   if(gameState == 1) gameScreen
   else if(gameState == 2) mainMenu
   else if(gameState == 3) helpScreen
   else if(gameState == 4) endScreen
   else if(gameState == 5) levelSelect
   else if(gameState == 6) victoryScreen
  }

  var screenSpeed = playerIcon.xAxis

  //Start of gameScreen
  def gameScreen = {
    if(pixelsGone == 0) {
      sounds.pauseAll()
      sounds.rewindAll()
      sounds.gameMusic()
    }
    pixelsGone += 4
    obstacles = obstacles.sortBy { _.x}
    var nextObstacle = obstacles(obsCount)
    image(bgImg, screenSpeed, 0)
    image(bgImg, screenSpeed - areaWidth, 0)
    image(bgImg, screenSpeed + areaWidth, 0)
    screenSpeed -= 2
    if(abs(screenSpeed) > areaWidth) screenSpeed= 0
    
    textSize(20)
    fill(0, 0, 0)
    val lastOne = obstacles(obstacles.size - 1).x
    var progress = pixelsGone * 1.0 / lastOne * 1.0
    if ((progress * 100.0).toInt < 100) {
      text("Your Progress: " + (progress * 100.0).toInt + "%", 250, 40) 
    } else {
      text("Your Progress: " + 100 + "%", 250, 40) 
    }
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
    else if(isOnTop && obstacles(obsCount).obsType == "spikes") {
      effects.play2()
      effects.rewind2()
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
    
    //Go to victory screen if the player won
    if(playerWon) {
      resetProgress()
      gameState = 6
    } 
  }
    //Checks if the player has won
    private def playerWon: Boolean = pixelsGone > 7200
    
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
    image(backbutton, 400, 25, 200, 100)
  }
  
  private def mainMenu = {
    loop()
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
    clear()
    sounds.pauseAll()
    background(blurredBg)
    textSize(60)
    fill(0,0,0)
    text("GAME OVER", 100, 100)
    
    val deathDeclaration = "You didn't survive to Mexico.\nCrooked Hillary put you to jail but Soon(tm)\nyou will get your revenge."
    textSize(20)
    text(deathDeclaration, 80, 150)
    
    image(tryagain, 113, 225, 150, 75)
    image(backbutton, 376, 225, 150, 75)
    
  }
   
  private def victoryScreen = {
    clear()
    sounds.pauseAll()
    sounds.playVictory()
    sounds.rewindAll()
    background(blurredBg)
    textSize(60)
    fill(0,0,0)
    text("VICTORY", 100, 100)
    
    val congratulations = "Hooray!\nYou made it to Mexico, congratulations!\nHillary can't catch you when you're here.\nThis means you are safe...\nOR ARE YOU???"
    textSize(20)
    text(congratulations, 100, 150)
    noLoop()
  }
    
  
}