package at.cp.jku.teaching.amprocessing.project;

import java.util.ArrayList;
import java.util.List;


public class ProcessingUtils {
	
	private ProcessingUtils() {
		// avoid instantiation
	}
	
	public static void normalize(double[] sd) {
		Range range = range(sd);
		for (int i = 0; i < sd.length; i++) {
			sd[i] = (sd[i]-range.min)/range.range();
		}
	}

	public static Range range(double[] sd) {
		double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
		for (double d : sd) {
			max = d > max ? d : max;
			min = d < min ? d : min;
		}
		Range range = new Range(min, max);
		return range;
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
