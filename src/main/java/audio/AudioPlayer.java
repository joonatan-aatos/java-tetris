package audio;

public class AudioPlayer {
	
	public AudioPlayer() {
		
	}
	
	public void playSound(Sound sound) {
		
		sound.play();
	}
	
	public void stopSound(Sound sound) {
		
		sound.stop();
	}
	
	public void loopSound(Sound sound) {
		
		sound.loop();
	}
	
	public void mute(boolean mute) {
		
		Sound.Main_Theme.mute(mute);
	}
}
