package jumping
import ddf.minim._

class Music {
  
  def musicSetup() {
     var music = new Minim(new Window)
     var song = music.loadFile("src/jumping/alone.wav")
     song.play()
  }
}