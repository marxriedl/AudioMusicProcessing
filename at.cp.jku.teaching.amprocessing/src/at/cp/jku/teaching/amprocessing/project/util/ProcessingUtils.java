package at.cp.jku.teaching.amprocessing.project.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProcessingUtils {
	
	private static final int window = 3000;

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
	
	public static double getMedian(final double[] signal, final int i, final int median_range) {
		int start = i - (median_range / 2);
		start = start < 0 ? 0 : start;
		int end = i + (median_range / 2);
		end = end > signal.length ? signal.length : end;
		double data[] = new double[(end - start)];
		int m = 0;
		for (int s = start; s < end; s++) {
			data[m++] = signal[s];
		}
		Arrays.sort(data);
		if (data.length % 2 == 0) {
			return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2;
		} else {
			return data[data.length / 2];
		}
	}

}
