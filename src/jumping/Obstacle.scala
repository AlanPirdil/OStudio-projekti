package jumping
import processing.core._

class Obstacle (val obsType: String, var x: Int, var y: Int, val width: Int, val height: Int) {
  
  def topX = this.x
  
  def firstY = this.y
  
  def botX = this.x + width
  
  def lastY = this.y + height

}

