package at.cp.jku.teaching.amprocessing.project;

import java.util.List;

/**
 * Interface for a beat detector
 * 
 */
public interface BeatDetector {

	/**
	 * Analyzes the given onset function and returns a list of frame positions,
	 * where beats where detected
	 * 
	 * @param onsetFunction
	 *            the onsetFunction used for the beat detection
	 * @return a list of frame positions, where beats where detected
	 */
	List<Integer> analyze(double[] onsetFunction);

}
