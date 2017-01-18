package jumping
import ddf.minim._

class Music {
  
  var music = new Minim(Window)

  val song1 = music.loadFile("src/music/alone.wav")
  val song2 = music.loadFile("src/music/eskimot.wav")
  val song3 = music.loadFile("src/music/joystick.wav")
  val song4 = music.loadFile("src/music/get_in.wav")
  
  val wallSong = music.loadFile("src/music/yep.mp3")
  val victorySong = music.loadFile("src/music/mexico.wav")
  
  
  var currentSong = song1
  
  def playVictory() = {
    victorySong.play()
    victorySong.rewind()
  }
  
  def nextSong() = {
    currentSong.pause()
    currentSong.rewind()
    if(currentSong == song1) currentSong = song2
    else if(currentSong == song2) currentSong = song3
    else if(currentSong == song3) currentSong = song4
    else if(currentSong == song4) currentSong = song1
  }

  def gameMusic() = {
    if(currentSong != song3) {
      currentSong.pause()
      currentSong = song3
      currentSong.play()
    }
  }
  
  def musicPlay() = if(!currentSong.isPlaying) currentSong.loop()
  
  def isPaused = !currentSong.isPlaying()
  
  def isMuted = currentSong.isMuted()
  
  def pause() = currentSong.pause()
  
  def mute() = currentSong.mute()
  
  def unmute() = currentSong.unmute()
  
}