package at.cp.jku.teaching.amprocessing.project.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessingUtils {

	private static final int window = 3000;

	private ProcessingUtils() {
		// avoid instantiation
	}

	/**
	 * Normalizes the given function in a windowed fashion. <br>
	 * 
	 * @param sd
	 *            the function to normalize
	 */
	public static void normalizeWindow(double[] sd) {
		for (int i = 0; i < sd.length; i++) {
			int start = i - (window / 2);
			start = start < 0 ? 0 : start;
			int end = i + (window / 2);
			end = end > sd.length ? sd.length : end;
			Range range = range(sd, start, end);
			sd[i] = (sd[i] - range.min) / range.range();
		}
	}

	/**
	 * Normalizes the given function so every value is in the range of 0 to 1.
	 * 
	 * @param sd
	 *            the function to normalize
	 */
	public static void normalize(double[] sd) {
		Range range = range(sd);
		for (int i = 0; i < sd.length; i++) {
			sd[i] = (sd[i] - range.min) / range.range();
		}
	}

	/**
	 * Calculates the value range of the given function within the given window
	 * 
	 * @param sd
	 *            the function to calculate the range from
	 * @param start
	 *            the start index where to start checking the range from
	 * @param end
	 *            the end index where to end checking the range
	 * @return the range of the function's values
	 */
	public static Range range(double[] sd, int start, int end) {
		double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
		for (int i = start; i < end; i++) {
			max = sd[i] > max ? sd[i] : max;
			min = sd[i] < min ? sd[i] : min;
		}
		Range range = new Range(min, max);
		return range;
	}

	/**
	 * Calculates the value range of the given function
	 * 
	 * @param sd
	 *            the function to calculate the range from
	 * @return the range of the function's values
	 */
	public static Range range(double[] sd) {
		return range(sd, 0, sd.length);
	}

	/**
	 * Class modeling a range with minimum and maximum value.
	 */
	public final static class Range {

		public final double min;
		public final double max;

		public Range(double min, double max) {
			this.min = min;
			this.max = max;
		}

		public double range() {
			return max - min;
		}

	}

	/**
	 * Translates a list of positions in frames into a list of positions in
	 * seconds.
	 * 
	 * @param framePositions
	 *            the list of positions in frames
	 * @param hopTime
	 *            the hopTime of the audio file
	 * @return the translated list of positions in seconds
	 */
	public static List<Double> translateFramesToSeconds(List<Integer> framePositions, double hopTime) {
		List<Double> result = new ArrayList<>(framePositions.size());
		for (Integer frame : framePositions) {
			result.add(frame * hopTime);
		}
		return result;
	}

	/**
	 * Calculates the median of the values of a signal around a given position
	 * 
	 * @param signal
	 *            the signal
	 * @param i
	 *            the position to calculate the median around
	 * @param median_range
	 *            the range how many neighbors are considered for the
	 *            calculation
	 * @return the median of the surrounding values.
	 */
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
