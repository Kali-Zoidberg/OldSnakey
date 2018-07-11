import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Sound {

	private Integer siID;
	private Integer liID;
	private AudioLine audioLine;
	private String sSoundName;
	private String sLineName;
	private String sFileName;
	private File fSoundFile;
	private float dVolumeLinear = 0;
	private float dVolumeDB = 0;
	private double dRadius = 0;
	private double dFrequency = 0;
	private long lDuration = 0;
	private long lStartTime = 0;
	private long lEndTime = 0;
	private Clip soundClip;
	private String sTag;
	
	Sound(String name, String fileName, String lineName)
	{
		sSoundName = name;
		sFileName = fileName;
		sLineName = lineName;
		
		liID = AudioMaster.getLineID(sLineName);
		this.putOnLine(liID);
	}
	
	/**
	 * Puts the sound on a new Audio Line.
	 */
	private void putOnLine(Integer lineID)
	{
		System.out.println("Line ID: " + lineID);
		if (lineID > 0)
		{
			audioLine = AudioMaster.getLine(lineID);
			audioLine.putSound(this);
		}	
	}
	
	public void open()
	{
		try 
		{
			fSoundFile = new File(sFileName);
	        AudioInputStream audioIn = AudioSystem.getAudioInputStream(fSoundFile);              
	        soundClip = AudioSystem.getClip();
	        //Open the sound file before playing to ensure the sound plays as commanded.
	        soundClip.open(audioIn);
	      
	        
		} catch (UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (LineUnavailableException e) 
		{
			e.printStackTrace();
		}
	}

	public void play() {
		//Plays the sound.
		if (soundClip.isOpen())
			soundClip.start();

	}
	
	/**
	 *Checks the desired duration by checking the bounds of the sound clip.
	 *@param startTime Start time of the desired duration.
	 *@param endTime End time of the desired duration.
	 */
	private boolean durationInBounds(long startTime, long endTime)
	{
		
		long clipEndTime = soundClip.isOpen() ? 0: soundClip.getMicrosecondLength();
			
		return !(startTime > endTime || startTime < 0 
				|| endTime < 0 || startTime > clipEndTime);
		
	}
	
	public void setSoundClip(String filename) 
	{ 
		sFileName = filename; 
		this.open();
		//set sound thingy to file name?
	}
	
	/**
	 * Sets the duration of the audioclip in milliseconds.
	 * @param msStartTime The start time of the audio clip in milliseconds
	 * @param msEndTime The end time of the audio clip in milliseconds
	 */
	public boolean setDuration(long msStartTime, long msEndTime) 
	{
		
		
		if (!this.durationInBounds(msStartTime, msEndTime))
			return false;
		
		lStartTime = msStartTime;
		lEndTime = msEndTime;
		return true;
	}
	
	/**
	 * Sets the volume of the Sound using Decibels. (Allows for better fine tuning.)
	 * @param decibel Increase the volume by the specified decibel amount.
	 */
	public void setVolDB(float decibel)
	{
		
		if (soundClip.isOpen())
		{
			
			FloatControl volControl = (FloatControl) soundClip.getControl(
					FloatControl.Type.MASTER_GAIN);			
			
			if (decibel < volControl.getMaximum())
			{
				volControl.setValue(decibel);
				dVolumeDB = decibel;
			}
			else 
			{
				volControl.setValue(volControl.getMaximum());
				dVolumeLinear = 1;
			}
			
		}
	}
	
	
	/**
	 * LinearVolume Control.
	 * @param The volume to set the clip to. Use 0-100 with 100 being the maximum 
	 * amplitude. of the speakers.
	 */
	
	public void setVolLinear(float volFrac)
	{
		
		if (volFrac <= 0)
			volFrac = 0.001f;
		dVolumeLinear = volFrac;
		
		float volDB = (float) (Math.log10(volFrac) * 20);
		System.out.println("volDB " + volDB);
			
		this.setVolDB(volDB);
			
	}
	
	
	public String getSoundClip() { return sFileName; }
	
	public float getVolumeLinear() { return dVolumeLinear; }
	public float getVolumeDecibel() { return dVolumeDB; }
	public double getRadius() { return dRadius; }
	public double getFrequency() { return dFrequency; }
	public long getDuration() { return lEndTime - lStartTime; }
	public String getTag() { return sTag; }
	
	/**
	 * Creates an integer object for the ID out of a primitive int.
	 * @param id The int id.
	 */
	public void setID(int id) { siID = new Integer(id); }
	
	/**
	 * Returns the integer value of the intergerID object.
	 * @return Returns the integer value.
	 */
	
	public int getID() { 
		try
		{
			return siID.intValue(); 
		} catch (NullPointerException e) {
			
			return 0;
		}
	}
}