package jumping
import processing.core._
import scala.math._

class Player {

  val img = "src/jumping/trump.png"
  var xAxis = 30          // initial x
  var yAxis = 160        // initial y
  var vy = 0

  
  def jump() = {    
    vy = min(vy+1, 20)   // jump
    this.yAxis = min(250,this.yAxis + vy) //Floor level
  }
}