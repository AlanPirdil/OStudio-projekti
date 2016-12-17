package jumping
import processing.core._

object Window extends PApplet{
  def main(args:Array[String]) {
    PApplet.main(Array[String]("jumping.Window"))
  }

}

class Window extends PApplet {
  // useful variables
  val areaWidth = 640
  val areaHeight = 320
  var gameScreen = 0
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
  
  //gameScreens
  def playView()={
    fill(255)
    ellipse(playerIcon.xAxis,playerIcon.yAxis,25,25)
  }
  
  
  //draw
  override def draw(): Unit = {
    if(gameScreen == 0){
      playView()
    }
}
  
}