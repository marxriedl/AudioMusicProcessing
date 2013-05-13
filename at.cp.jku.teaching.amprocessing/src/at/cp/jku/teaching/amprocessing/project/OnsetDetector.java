package at.cp.jku.teaching.amprocessing.project;

import java.util.Map;

import at.cp.jku.teaching.amprocessing.AudioFile;

public interface OnsetDetector {

	/**
	 * Analyzes the given {@link AudioFile} for onsets. 
	 * @param audiofile the audio file to analyze  
	 * @return the normalized detection function
	 */
	double[] analyze(AudioFile audiofile);
	
	Map<String,Number> getParameters();

}
