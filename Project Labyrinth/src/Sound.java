import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Sound {
	static Clip StageMusic;
	static Clip HeartSound;
	static Clip DoorOpen;
	static Clip MedusaSound;
	static Clip	DragonSound;
	static Clip	ShotSound;
	static Clip	MonsterDestroyed;
	static Clip ChestOpen;
	static Clip Death;
	static Clip PowerUsed;
	static Clip Sleeper;
    public Sound(String Filename) {
        File Sound= new File("src/Sound/"+Filename+".wav");
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
	        else if(Filename.equalsIgnoreCase("PowerUsed"))PowerUsed=clip;
	        else if(Filename.equalsIgnoreCase("Sleeper"))Sleeper=clip;
		} catch (LineUnavailableException e){ e.printStackTrace();}
		catch (IOException io){io.printStackTrace();}
		catch (UnsupportedAudioFileException uafe){uafe.printStackTrace();}
        // getAudioInputStream() also accepts a File or InputStream
    }
}