package at.cp.jku.teaching.amprocessing.project;

import java.lang.reflect.Array;
import java.util.List;

public interface PeakPicking {
	
	/**
	 * Picks the peaks of the provided array of values and returns a list of the indices of these peaks.
	 * @param signal an {@link Array} containing the data
	 * @return a list of indices reflecting the peaks within the provided data
	 */
	List<Integer> pickPeaks(double[] signal);

}