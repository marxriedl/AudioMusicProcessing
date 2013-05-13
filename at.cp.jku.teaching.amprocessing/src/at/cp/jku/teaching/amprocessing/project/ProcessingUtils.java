package at.cp.jku.teaching.amprocessing.project;

import java.util.ArrayList;
import java.util.List;


public class ProcessingUtils {
	
	private static final int window = 150;

	private ProcessingUtils() {
		// avoid instantiation
	}
	
	public static void normalizeWindow(double[] sd) {
		for(int i=0; i<sd.length; i++) {
			int start = i - (window / 2);
			start = start < 0 ? 0 : start;
			int end = i + (window / 2);
			end = end > sd.length ? sd.length : end;
		Range range = range(sd, start, end);
			sd[i] = (sd[i]-range.min)/range.range();
		}
	}
	
	public static void normalize(double[] sd) {
		Range range = range(sd);
		for (int i = 0; i < sd.length; i++) {
			sd[i] = (sd[i]-range.min)/range.range();
		}
	}
	
	public static Range range(double[] sd, int start, int end) {
		double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
		for (int i=start; i<end; i++) {
			max = sd[i] > max ? sd[i] : max;
			min = sd[i] < min ? sd[i] : min;
		}
		Range range = new Range(min, max);
		return range;
	}

	public static Range range(double[] sd) {
		return range(sd, 0, sd.length);
	}
	
	public final static class Range {
		
		public final double min;
		public final double max;
		
		public Range(double min, double max) {
			this.min = min;
			this.max = max;
		}
		
		public double range() {
			return max-min;
		}
		
	}

	public static List<Double> translateFramesToSeconds(List<Integer> peaks,
			double hopTime) {
		List<Double> result = new ArrayList<>(peaks.size());
		for (Integer frame : peaks) {
			result.add(frame * hopTime);
		}
		return result;
	}

}
