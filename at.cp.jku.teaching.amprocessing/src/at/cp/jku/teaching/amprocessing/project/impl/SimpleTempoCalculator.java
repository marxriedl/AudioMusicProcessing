package at.cp.jku.teaching.amprocessing.project.impl;

import java.util.List;

import at.cp.jku.teaching.amprocessing.project.TempoCalculator;

/**
 * A tempo calculator which simply takes the interval between beat locations and
 * calculates the mean of these intervals to get the tempo
 */
public class SimpleTempoCalculator implements TempoCalculator {

	@Override
	public double analyze(List<Double> beatList) {
		double[] intervals_seconds = new double[beatList.size() - 1];
		int i = 0;
		double lastBeatTime = Double.NaN;
		for (double beatTime : beatList) {
			if (!Double.isNaN(lastBeatTime)) {
				intervals_seconds[i++] = beatTime - lastBeatTime;
			}
			lastBeatTime = beatTime;
		}
		double spb = mean(intervals_seconds);
		double bpm = 60 / spb;
		return bpm;
	}

	private double mean(double[] intervals) {
		double sum = 0;
		for (int i = 0; i < intervals.length; i++) {
			sum += intervals[i];
		}
		return sum / intervals.length;
	}

}
