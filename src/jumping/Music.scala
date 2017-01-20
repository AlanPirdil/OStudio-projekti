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


    def muteAll() = {
    if(song1.isMuted){
      song1.mute()
      song2.mute()
      song3.mute()
      song4.mute()
      wallSong.mute()
      victorySong.mute()
    } else {
      song1.unmute()
      song2.unmute()
      song3.unmute()
      song4.unmute()
      wallSong.unmute()
      victorySong.unmute()
    }
  }

  def pauseAll() = {
    song1.pause()
    song2.pause()
    song3.pause()
    song4.pause()
    wallSong.pause()
    victorySong.pause()
    currentSong.pause()
  }

  def rewindAll() = {
    song1.rewind()
    song2.rewind()
    song3.rewind()
    song4.rewind()
    wallSong.rewind()
    victorySong.rewind()
  }

  def playVictory() = {
    currentSong = victorySong
    currentSong.play()
    currentSong.rewind()
  }

  def nextSong() = {
    currentSong.pause()
    currentSong.rewind()
    if(currentSong == song1) currentSong = song2
    else if(currentSong == song2) currentSong = song3
    else if(currentSong == song3) currentSong = song4
    else if(currentSong == song4) currentSong = song1
    else if(currentSong == wallSong) currentSong = song1
    else if(currentSong == victorySong) currentSong = song1

    currentSong.play()
  }

  def level1Music() = {
      currentSong = wallSong
      currentSong.rewind()
      currentSong.play()
  }
  
  def level2Music() = {
      currentSong = song3
      currentSong.rewind()
      currentSong.play()
  }

  def level3Music() = {
      currentSong = song4
      currentSong.rewind()
      currentSong.play()
  }

  def musicPlay() = if(!currentSong.isPlaying) currentSong.loop()

  def isPaused = !currentSong.isPlaying()

  def isMuted = currentSong.isMuted()

  def pause() = currentSong.pause()

  def mute() = currentSong.mute()

  def unmute() = currentSong.unmute()

}
