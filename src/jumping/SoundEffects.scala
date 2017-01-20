package jumping
import ddf.minim._

class SoundEffects {

  val effect = new Minim(Window)
  private val effect1 = effect.loadFile("src/trumpEffects/jump.wav")
  private val effect2 = effect.loadFile("src/trumpEffects/out.wav")
  private val effect3 = effect.loadFile("src/trumpEffects/great_again.wav")

  def muteAll() = {
    if(effect1.isMuted&&effect2.isMuted&&effect3.isMuted){
      effect1.unmute()
      effect2.unmute()
      effect3.unmute()
    } else {
      effect1.mute()
      effect2.mute()
      effect3.mute()
    }
  }

  def play1() = effect1.play()

  def play2() = effect2.play()

  def play3() = effect3.play()

  def isPlaying1 = effect2.isPlaying()

  def isPlaying2 = effect2.isPlaying()

  def isPlaying3 = effect3.isPlaying()

  def rewind1() = effect1.rewind()

  def rewind2() = effect2.rewind()

  def rewind3() = effect3.rewind()

}
