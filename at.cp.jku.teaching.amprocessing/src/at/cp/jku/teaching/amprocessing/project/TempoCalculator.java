package at.cp.jku.teaching.amprocessing.project;

import java.util.List;

public interface TempoCalculator {

	/**
	 * Calculates the tempo of a piece of music with the given beat locations.
	 * 
	 * @param beatList the locations of the beats in seconds
	 * @return the calculated tempo
	 */
	double analyze(List<Double> beatList);

}
