package at.cp.jku.teaching.amprocessing.project;

import java.util.List;

import at.cp.jku.teaching.amprocessing.AudioFile;

public interface OnsetDetector {

	/**
	 * Analyzes the given {@link AudioFile} and returns a list of onsets.
	 * @param audiofile the audio file to analyze  
	 * @return a list of frame numbers where onsets were detected within the file
	 */
	public abstract List<Double> analyze(AudioFile audiofile);

}
