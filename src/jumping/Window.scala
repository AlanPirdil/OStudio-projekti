
/* THIS IS A GAME MADE BY ALAN PIRDIL, MÄRT VESINURM AND AKSELI VÄYRYNEN
* AT AALTO UNIVERSITY. IN ORDER TO RUN THE GAME, RUN WINDOW.SCALA.
* HAVE FUN! */

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

  //Define the area measures
  val areaWidth = 640
  val areaHeight = 320

  //Starts from mainMenu
  var gameState = 2
  def situation = this.gameState

  //Initialize sounds
  val effects = new SoundEffects
  val sounds = new Music

  //Initialize font
  private val usedFont = createFont("src/jumping/karma future.ttf", 32)

  //Initialize player
  val playerIcon = new Player

  //Loading all the images needed in this game
  val bgImg1 = loadImage("src/photos/BG.png")
  val bgImg2 = loadImage("src/photos/level2BG.jpg")
  val bgImg3 = loadImage("src/photos/level3Bg.jpg")
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
  val back = loadImage("src/photos/backStatic(1).png")
  val mouseOnBack = loadImage("src/photos/backHover.png")
  val tryagain = loadImage("src/photos/againStatic.png")
  val mouseOnAgain = loadImage("src/photos/againHover.png")

  //Loading .csv levelfiles
  val level1 = Source.fromFile("src/jumping/level1.csv")
  val level2 = Source.fromFile("src/jumping/level2.csv")
  val level3 = Source.fromFile("src/jumping/level3.csv")

  //Initialize a buffer for obstacles
  var firstObstacles = Buffer[Obstacle]()
  var secondObstacles = Buffer[Obstacle]()
  var thirdObstacles = Buffer[Obstacle]()
  var obstacles = firstObstacles
  val firstX = playerIcon.xAxis
  var obsCount = 0
  var onTop: Boolean = false
  var pixelsGone = 0
  var currentLevel = 1
  var currentBgImg = bgImg1

  //Reading the level from a .csv file
  private def readLevel(levelToRead: Source, obstacleBuffer: Buffer[Obstacle] ) = {
    try {
      var rowNum = 1
      for (row <- levelToRead.getLines) {
        val arrayOfStrings = row.split(",")
        val arrayOfInts = arrayOfStrings.map(_.trim().toInt)
        val arraySize = arrayOfInts.size
        for(i <- 0 until arraySize){
          if(arrayOfInts(i) == 0){
            obstacleBuffer += new Obstacle("wall", firstX + i * 30, rowNum*30 + 90,30,30)
          } else if(arrayOfInts(i) == 1){
            obstacleBuffer += new Obstacle("spikes", firstX + i * 30, rowNum*30 + 90,30,30)
          }
        }
        rowNum += 1
      }
    } catch {
      case e: Exception => "ERROR"
    }
    finally {
      levelToRead.close()
    }
  }

  //Main setup
  override def setup(): Unit = {
    sounds.musicPlay()
    size(areaWidth, areaHeight)
    noStroke()
    readLevel(level1, firstObstacles)
    readLevel(level2, secondObstacles)
    readLevel(level3, thirdObstacles)
    frameRate(60)
    frame.setTitle("Trumpoliini")
  }

  //Defining what happens in case of certain keys are pressed
  override def keyPressed() = {
    if(keyPressed) {
      if(key == 'p' || key == 'P') {
        gameState = 5
      }
      else if(key == 'm' || key == 'M') {
        gameState = 2
      }
      else if(key == 'h' || key == 'H') {
        gameState = 3
      }
      else if(key == 'n' || key == 'N') {
        sounds.nextSong()
      }
      else if(key == 's' || key == 'S') {
        sounds.muteAll()
      }
      else if(key == 'e' || key == 'E') {
        effects.muteAll()
      }
      if (gameState == 5 && key == '1') {
        gameState = 1
      }
    }
  }

  //Defining what happens when mouse is clicked
  override def mousePressed() = {
    effects.rewind1()
    if (gameState == 1) {
      effects.play1()
    }

    if (playerIcon.vy == 20 || isOnTop && gameState == 1) { // Checks if Trump is on the ground
      playerIcon.vy = -15 // Makes Trump jump
    }


    /* The following if statements define the view that will
    * appear in case the player pushes a button w/ left click
    * at certain gameStates. */

    //Go to first level @levelSelect
    if (gameState == 5 && mouseX >= 85 && mouseX <= 185 && mouseY <= 245 && mouseY >= 145) {
      obstacles = firstObstacles
      currentBgImg = bgImg1
      gameState = 1
    }

    //Go to second level @levelSelect
    if (gameState == 5 && mouseX >= 270 && mouseX <= 370 && mouseY <= 245 && mouseY >= 145) {
      obstacles = secondObstacles
      currentBgImg = bgImg2
      gameState = 1
    }

    //Go to third level @levelSelect
    if (gameState == 5 && mouseX >= 455 && mouseX <= 555 && mouseY <= 245 && mouseY >= 145) {
      obstacles = thirdObstacles
      currentBgImg = bgImg3
      gameState = 1
    }

    //Go to levelSelect @mainMenu
    if (gameState == 2 && mouseX >= 80 && mouseX <= 280 && mouseY >= 150 && mouseY <= 250) {
      gameState = 5
    }

    //Go to helpScreen @mainMenu
    if (gameState == 2 && mouseX >= 360 && mouseX <= 560 && mouseY >= 150 && mouseY <= 250) {
      gameState = 3
    }

    //Go back to mainMenu @ helpScreen
    if (gameState == 3 && mouseX >= 400 && mouseX <= 600 && mouseY >= 25 && mouseY <= 125) {
      gameState = 2
    }

    //Go to gameScreen (try again) @endScreen
    if (gameState == 4 && mouseX >= 113 && mouseX <= 263 && mouseY >= 225 && mouseY <= 300) {
      gameState = 1
    }

    //Go to back to mainMenu @endScreen
    if (gameState == 4 && mouseX >= 376 && mouseX <= 526 && mouseY >= 225 && mouseY <= 300) {
      gameState = 2
    }

    //Go back to to mainMenu @victoryScreen
    if (gameState == 6 && mouseX >= 400 && mouseX <= 550 && mouseY >= 230 && mouseY <= 305) {
      gameState = 2
    }

  }



  //Drawing different stages of the game. Runs 60 times per sec.
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
      if(currentBgImg == bgImg1) sounds.level1Music()
      else if(currentBgImg == bgImg2) sounds.level2Music()
      else if(currentBgImg == bgImg3) sounds.level3Music()
    }

    pixelsGone += 4
    obstacles = obstacles.sortBy(_.x)
    var nextObstacle = obstacles(obsCount)
    image(currentBgImg, screenSpeed, 0)
    image(currentBgImg, screenSpeed - areaWidth, 0)
    image(currentBgImg, screenSpeed + areaWidth, 0)
    screenSpeed -= 2
    if(abs(screenSpeed) > areaWidth) screenSpeed= 0
    textFont(usedFont, 32)
    textSize(20)
    fill(0, 0, 0)

    val lastOne = obstacles(obstacles.size - 1).x
    var progress = pixelsGone * 1.0 / lastOne * 1.0

    //Informing the player about the progress & very basic instructions
    if (pixelsGone < 420) {
      textSize(32)
      text("Left click to jump", 200, 70)
      textSize(20)
      text("Your Progress: " + (progress * 100.0).toInt + "%", 250, 40)
    } else {
      if ((progress * 100.0).toInt < 100) {
        text("Your Progress: " + (progress * 100.0).toInt + "%", 250, 40)
      } else {
        text("Your Progress: " + 100 + "%", 250, 40)
      }
    }

    playerIcon.jump()
    image(trumpImg, playerIcon.xAxis, playerIcon.yAxis, playerIcon.width, playerIcon.height)

    //Draws the obstacles
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
      playerIcon.yAxis = nextObstacle.y - playerIcon.height
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
    if (playerIcon.xAxis > obstacles(obsCount).x - pixelsGone + obstacles(obsCount).width) {
      if (obstacles.length > obsCount + 1) {
        obsCount += 1
      }
    }

    //Change the gameState when game ends
    if (gameEnds) {
      resetProgress()
      gameState = 4
    }

    //Go to victory screen if the player won
    if(playerWon) {
      sounds.pauseAll()
      sounds.rewindAll()
      sounds.playVictory()
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
    if(obstacles.exists( ob => playerIcon.xAxis + playerIcon.width == ob.x - pixelsGone && playerIcon.yAxis + playerIcon.height - 10 > ob.y)) {
      effects.play2()
      effects.rewind2()
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
    textFont(usedFont, 64)
    text("Level select", 125, 100)


    image(level1Logo, 85, 145, 100, 100)
    image(level2Logo, 270, 145, 100, 100)
    image(level3Logo, 455, 145, 100, 100)

    //Changing between Static/Hover buttons
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

  //A view which includes information of the controls/keys in this program
  private def helpScreen = {
    clear()
    background(blurredBg)
    textSize(60)
    fill(0,0,0)
    text("Help", 100, 100)
    textFont(usedFont, 32)
    val helperText = "Left click to jump\nPress m to go to the main menu\nPress n to switch background music\nPress s to mute the soundtrack\nPress e to mute sound effects"
    textSize(20)
    text(helperText, 100, 150)
    image(back, 400, 25, 200, 100)

    //Changing between Static and Hover buttons
    if (gameState == 3 && mouseX >= 400 && mouseX <= 600 && mouseY >= 25 && mouseY <= 125) {
      image(mouseOnBack, 400, 25, 200, 100)
    }
  }

  //1st thing that will appear in the game
  private def mainMenu = {
    loop()
    clear()
    background(blurredBg)
    textSize(60)
    fill(0,0,0)
    textFont(usedFont, 64)
    text("Trumpoliini", 150, 100)
    image(playbutton, 80, 150, 200, 100)
    image(helpbutton, 360, 150, 200, 100)

    //Changing between Static/Hover buttons
    if (gameState == 2 && mouseX >= 80 && mouseX <= 280 && mouseY >= 150 && mouseY <= 250) {
      image(mouseOnPlaybutton, 80, 150, 200, 100)
    }

    if (gameState == 2 && mouseX >= 360 && mouseX <= 560 && mouseY >= 150 && mouseY <= 250) {
      image(mouseOnHelpbutton, 360, 150, 200, 100)
    }

  }

  //A screen that will appear in case the player loses the game
  private def endScreen = {
    clear()
    sounds.pauseAll()
    background(blurredBg)
    textSize(60)
    fill(0,0,0)
    textFont(usedFont, 64)
    text("GAME OVER", 150, 75)
    textFont(usedFont, 32)
    val deathDeclaration = "You didn't survive to Mexico.\nCrooked Hillary put you in jail but soon\nyou will get your revenge."
    textSize(20)
    text(deathDeclaration, 150, 125)

    image(tryagain, 113, 225, 150, 75)
    image(back, 376, 225, 150, 75)

    //Changing between Static/Hover buttons
    if (gameState == 4 && mouseX >= 113 && mouseX <= 263 && mouseY >= 225 && mouseY <= 300) {
      image(mouseOnAgain, 113, 225, 150, 75)
    }

    if (gameState == 4 && mouseX >= 376 && mouseX <= 526 && mouseY >= 225 && mouseY <= 300) {
      image(mouseOnBack, 376, 225, 150, 75)
    }


  }

  //A window that will appear if the player wins the level
  private def victoryScreen = {
    clear()
    background(blurredBg)
    textSize(60)
    fill(0,0,0)
    textFont(usedFont, 64)
    text("VICTORY", 100, 75)
    textFont(usedFont, 32)
    val congratulations = "Hooray!\nYou made it to Mexico, congratulations!\nHillary can't catch you when you're here.\nThis means you are safe...\nOR ARE YOU???"
    textSize(20)
    text(congratulations, 100, 125)
    image(back, 400, 230, 150, 75)

    //Changing between Static/Hover buttons
    if (gameState == 6 && mouseX >= 400 && mouseX <= 550 && mouseY >= 230 && mouseY <= 305) {
      image(mouseOnBack, 400, 230, 150, 75)
    }
  }


}
