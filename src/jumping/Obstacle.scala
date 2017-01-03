package jumping
import processing.core._

class Obstacle (var x: Int, var y: Int, val kanta: Int, val korkeus: Int) {
  
  def topX = this.x
  
  def firstY = this.y
  
  def botX = this.x + kanta
  
  def lastY = this.y + korkeus

}

