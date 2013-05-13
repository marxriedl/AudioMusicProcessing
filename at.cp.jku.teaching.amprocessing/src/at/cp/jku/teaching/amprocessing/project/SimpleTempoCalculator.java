package at.cp.jku.teaching.amprocessing.project;

import java.util.List;

public class SimpleTempoCalculator implements TempoCalculator {

	@Override
	public double analyze(List<Double> beatList) {
		double[] intervals = new double[beatList.size()-1];
		int i=0;
		double lastBeatTime = Double.NaN;
		for (double beatTime : intervals) {
			if(lastBeatTime != Double.NaN) {
				intervals[i++] = beatTime-lastBeatTime;
			}
			lastBeatTime = beatTime;
		}
		return mean(intervals);
	}

	private double mean(double[] intervals) {
		int sum = 0;
		for (int i = 0; i < intervals.length; i++) {
			sum+=intervals[i];
		}
		return sum/intervals.length;
	}

}
