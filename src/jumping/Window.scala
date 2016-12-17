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
    fill(255)
  }
  
  
  //draw
  override def draw(): Unit = {
    if(gameScreen == 0){
    }
}
  
}