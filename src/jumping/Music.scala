package jumping
import ddf.minim._

class Music {
  
  var music = new Minim(Window)
  val song1 = music.loadFile("src/jumping/alone.wav")
  var currentSong = song1
  
  def musicPlay() = currentSong.play()
  
  def isPaused = !currentSong.isPlaying()
  
  def isMuted = currentSong.isMuted()
  
  def pause() = currentSong.pause()
  
  def mute() = currentSong.mute()
  
  def unmute() = currentSong.unmute()
}