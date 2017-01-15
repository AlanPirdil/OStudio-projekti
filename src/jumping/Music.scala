package jumping
import ddf.minim._

class Music {
  
  var music = new Minim(Window)

  val song1 = music.loadFile("src/jumping/alone.wav")
  val song2 = music.loadFile("src/jumping/eskimot.wav")
  val gameSong = music.loadFile("src/jumping/yep.mp3")
  
  var currentSong = song1
  
  def nextSong() = {
    currentSong.pause()
    currentSong.rewind()
    if(currentSong == song1) currentSong = song2
    else currentSong = song1
  }

  def gameMusic() = {
    currentSong.pause()
    currentSong.rewind()
    if(currentSong == song1) currentSong = song2
    else currentSong = song1
  }
  
  def musicPlay() = currentSong.loop()
  
  def isPaused = !currentSong.isPlaying()
  
  def isMuted = currentSong.isMuted()
  
  def pause() = currentSong.pause()
  
  def mute() = currentSong.mute()
  
  def unmute() = currentSong.unmute()
  
}