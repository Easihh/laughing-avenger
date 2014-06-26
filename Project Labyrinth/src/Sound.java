import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.*;

public class Sound {
	public static Clip 	ArrowBridgePowerUsed;
	public static Clip 	ChestOpen;
	public static Clip 	Death;
	public static Clip 	DoorOpen;
	public static Clip	DragonSound;
	public static Clip 	HammerPowerUsed;
	public static Clip 	HeartSound;
	public static Clip 	MedusaSound;
	public static Clip	MonsterDestroyed;
	public static Clip	PowerEnabled;
	public static Clip 	Sleeper;
	public static Clip	ShotSound;
	public static Clip 	StageMusic;
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
		} catch (LineUnavailableException e){ e.printStackTrace();}
		catch (IOException io){io.printStackTrace();}
		catch (UnsupportedAudioFileException uafe){uafe.printStackTrace();}
        // getAudioInputStream() also accepts a File or InputStream
    }
    public static void resetSound(){
		Sound.ArrowBridgePowerUsed.setFramePosition(0);
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
    }
}