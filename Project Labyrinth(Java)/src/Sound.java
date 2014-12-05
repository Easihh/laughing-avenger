import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.*;
/**
 * 
 * This is the Class where Sounds are played from and loaded.Since we do not want to keep loading
 * sound file every time its needed, we instead load all sounds at the start of the game and get a 
 * reference to it so we can play the sound when needed without latency caused by constantly loading 
 * a file.
 */
public class Sound {
	public static Clip 	ArrowBridgePowerUsed,ChestOpen,Death,DoorOpen,DragonSound,HammerPowerUsed,
	HeartSound,MedusaSound,MonsterDestroyed,PowerEnabled,Sleeper,ShotSound,StageMusic,Water;
	/*** Load the given sound specified by Filename in the Memory*/
    public Sound(String Filename) {
    	InputStream source=this.getClass().getResourceAsStream("/Sound/"+Filename+".wav");
        InputStream Sound=new BufferedInputStream(source);
        Clip clip;
		try {
			clip = AudioSystem.getClip();
	        AudioInputStream ais = AudioSystem.getAudioInputStream( Sound );
	        clip.open(ais);
	        if(Filename.equalsIgnoreCase("StageMusic"))StageMusic=clip;
	        else if(Filename.equalsIgnoreCase("HeartSound"))HeartSound=clip;
	        else if(Filename.equalsIgnoreCase("DoorOpen"))DoorOpen=clip;
	        else if(Filename.equalsIgnoreCase("MedusaSound"))MedusaSound=clip;
	        else if(Filename.equalsIgnoreCase("DragonSound"))DragonSound=clip;
	        else if(Filename.equalsIgnoreCase("ShotSound"))ShotSound=clip;
	        else if(Filename.equalsIgnoreCase("MonsterDestroyed"))MonsterDestroyed=clip;
	        else if(Filename.equalsIgnoreCase("ChestOpen"))ChestOpen=clip;
	        else if(Filename.equalsIgnoreCase("Death"))Death=clip;
	        else if(Filename.equalsIgnoreCase("ArrowBridgePowerUsed"))ArrowBridgePowerUsed=clip;
	        else if(Filename.equalsIgnoreCase("Sleeper"))Sleeper=clip;
	        else if(Filename.equalsIgnoreCase("HammerPowerUsed"))HammerPowerUsed=clip;
	        else if(Filename.equalsIgnoreCase("PowerEnabled"))PowerEnabled=clip;
	        else if(Filename.equalsIgnoreCase("Water"))Water=clip;
		} catch (LineUnavailableException e){ e.printStackTrace();}
		catch (IOException io){io.printStackTrace();}
		catch (UnsupportedAudioFileException uafe){uafe.printStackTrace();}
    }
    /*** Reset all Sound as to make them start from the beginning*/
    public static void resetSound(){
		Sound.ArrowBridgePowerUsed.setFramePosition(0);
		Sound.ChestOpen.setFramePosition(0);
		Sound.Death.setFramePosition(0);
		Sound.DoorOpen.setFramePosition(0);
		Sound.DragonSound.setFramePosition(0);
		Sound.HammerPowerUsed.setFramePosition(0);
		Sound.HeartSound.setFramePosition(0);
		Sound.MedusaSound.setFramePosition(0);
		Sound.MonsterDestroyed.setFramePosition(0);
		Sound.PowerEnabled.setFramePosition(0);
		Sound.ShotSound.setFramePosition(0);
		Sound.Sleeper.setFramePosition(0);
		Sound.Water.setFramePosition(0);
    }
}