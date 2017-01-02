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
  var pallonsäde = 25
  val areaWidth = 640
  val areaHeight = 320
  var gameState = 1
  val bgImg = loadImage("src/jumping/BG.png")
  val trumpImg = loadImage("src/jumping/trump.png")
  var playerIcon = new Player
  val obstacles = Buffer[Obstacle]()
 
  
  //override settings
  def settings() = {
    size(areaWidth,areaHeight)
  }
  
  
  //main setup
  override def setup(): Unit = {
    size(areaWidth, areaHeight)
    background(bgImg)
    noStroke()
    
    
  }

  
  override def mousePressed() = { //Change to when in contact with platform
    if(vy == 20) {
     vy = -15
    }
   }
  
  
  //draw runs 60 time per second
   var vy = 0
   var sx = 0
  override def draw(): Unit = {
     
   if(gameState == 1) {
    clear() 
    vy = min(vy+1, 20)
    playerIcon.yAxis = min(250,playerIcon.yAxis + vy)
   
    playerIcon.xAxis += 0
   // image(bgImg, 0,0,640,320)
    image(trumpImg, playerIcon.xAxis, playerIcon.yAxis, 50, 50)
    
    
    //TESTIKUUTIO
    rect(obstacles(0).x+200-sx,obstacles(0).y,50,50)
    sx += 2
    if(obstacles(0).x+200-sx == playerIcon.xAxis && obstacles(0).y == playerIcon.yAxis) gameState = 0

    }
  }
}