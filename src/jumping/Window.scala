package jumping
import processing.core._
object Window extends PApplet {
  
  // useful variables
  val areaWidth = 640
  val areaHeight = 640
  
  
  //main setup
  override def setup(): Unit = {
    size(areaWidth, areaHeight)
    background(0)
    noStroke()
    fill(102)
  }
  
  //draw
  override def draw(): Unit = {
    rect(10,10,10,10)
  }
}