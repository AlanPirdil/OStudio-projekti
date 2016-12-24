package jumping
import scala.math._
import processing.core._

object Window extends PApplet{
  def main(args:Array[String]) {
    PApplet.main(Array[String]("jumping.Window"))
  }

}

class Window extends PApplet {
  // useful variables
  var pallonsäde = 25
  val areaWidth = 640
  val areaHeight = 320
  var gameState = 1
  val bgImg = loadImage("src/jumping/BG.png")
  val trumpImg = loadImage("src/jumping/trump.png")
  var playerIcon = new Player
  //override settings
  def settings() = {
    size(areaWidth,areaHeight)
  }
  //main setup
  override def setup(): Unit = {
    size(areaWidth, areaHeight)
    background(0)
    noStroke()
  }

  
  override def mousePressed() = { //Change to when in contact with platform
     vy = -15
   }
  
  //draw
   var vy = 0
  override def draw(): Unit = {
   if(gameState == 1) {
    clear()
    fill(255)
    if(playerIcon.xAxis > areaWidth) {
      playerIcon.xAxis = 0
    }
    
    vy = min(vy+1, 20)
    playerIcon.yAxis = min(250,playerIcon.yAxis + vy)
    playerIcon.xAxis += 0
    image(bgImg, 0,0,640,320)
    image(trumpImg, playerIcon.xAxis, playerIcon.yAxis, 50, 50)
    }
  }
}