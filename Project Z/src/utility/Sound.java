package utility;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Sound {
	
	public static Clip overWorldMusic;
	public static Clip sword;
	public static Clip swordCombine;
	public static Clip selector;
	public static Clip candle;
	public static Clip enemyHit;
	public static Clip enemyKill;
	public static Clip newItem;
	public static Clip newInventItem;
	public Sound(String filename){
		InputStream source=this.getClass().getResourceAsStream("/sound/"+filename+".wav");
		InputStream sound=new BufferedInputStream(source);
		Clip clip;
		try {clip=AudioSystem.getClip();
		AudioInputStream ais=AudioSystem.getAudioInputStream(sound);
		clip.open(ais);
		if(filename.equalsIgnoreCase("overWorld"))overWorldMusic=clip;
		if(filename.equalsIgnoreCase("sword"))sword=clip;
		if(filename.equalsIgnoreCase("swordCombine"))swordCombine=clip;
		if(filename.equalsIgnoreCase("selector"))selector=clip;
		if(filename.equalsIgnoreCase("candle"))candle=clip;
		if(filename.equalsIgnoreCase("enemyHit"))enemyHit=clip;
		if(filename.equalsIgnoreCase("enemyKill"))enemyKill=clip;
		if(filename.equalsIgnoreCase("newItem"))newItem=clip;
		if(filename.equalsIgnoreCase("newInventItem"))newInventItem=clip;
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();}
	}
	public static void setOverWorldMusic() {
		Sound.overWorldMusic.setFramePosition(0);
		Sound.overWorldMusic.start();
		int length_In_Seconds=(int)Math.ceil(Sound.overWorldMusic.getMicrosecondLength()/1000000.0);
		int framePerSeconds=Sound.overWorldMusic.getFrameLength()/length_In_Seconds;
		int loopStart=framePerSeconds*44;// loop at the 44th seconds.
		Sound.overWorldMusic.setLoopPoints(loopStart, -1);
		Sound.overWorldMusic.loop(Clip.LOOP_CONTINUOUSLY);
	}
}