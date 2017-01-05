package jumping
import ddf.minim._

class SoundEffects {
   
  val effect = new Minim(Window)
  private val effect1 = effect.loadFile("src/trumpEffects/jump.wav")
  private val effect2 = effect.loadFile("src/trumpEffects/out.wav")
  private val effect3 = effect.loadFile("src/trumpEffects/great_again.wav")
  
  def play1() = effect1.play()
  
  def play2() = effect2.play()
  
  def play3() = effect3. play()
}