package jumping
import processing.core._
import scala.math._

class Player {

  val img = "src/photos/trump.png"
  var xAxis = 30          // initial x
  var yAxis = 160        // initial y
  var vy = 0
  val height = 50
  val width = 35
  
  def jump() = {    
    vy = min(vy+1, 20)   // jump
    this.yAxis = min(250,this.yAxis + vy) //Floor and roof level
  }
}