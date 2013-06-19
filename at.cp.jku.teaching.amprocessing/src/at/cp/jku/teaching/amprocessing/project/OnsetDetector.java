package at.cp.jku.teaching.amprocessing.project;

import java.util.Map;

import at.cp.jku.teaching.amprocessing.AudioFile;

public interface OnsetDetector {

	/**
	 * Analyzes the given {@link AudioFile} for onsets.
	 * 
	 * @param audiofile
	 *            the audio file to analyze
	 * @return the normalized detection function
	 */
	double[] analyze(AudioFile audiofile);

	/**
	 * Parameters used by the peak picking algorithms to adjust their behavior
	 * 
	 * @return a map containing the parameter name as key and the value of the
	 *         parameter
	 */
	Map<String, Number> getParameters();

}
