package at.cp.jku.teaching.amprocessing.project.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.cp.jku.teaching.amprocessing.project.PeakPicking;
import at.cp.jku.teaching.amprocessing.project.util.ProcessingUtils;

public class AdaptiveThresholding implements PeakPicking {

	public static final String MEDIAN_RANGE = "MEDIAN_RANGE";
	public static final String CONSTANT_THRESHOLD = "THRESHOLD";
	private final int median_range;
	private final double threshold;

	public AdaptiveThresholding(Map<String, Number> parameters) {
		this.threshold = parameters.get(CONSTANT_THRESHOLD).doubleValue();
		this.median_range = parameters.get(MEDIAN_RANGE).intValue();
	}

	@Override
	public List<Integer> pickPeaks(double[] signal) {
		double[] thresholds = generateThresholds(signal);

		List<Integer> peakList = new ArrayList<>();
		for (int i = 1; i < signal.length - 1; i++) {
			if (signal[i] >= thresholds[i]) {
				// is larger than threshold
				if ((signal[i - 1] - thresholds[i - 1]) < (signal[i] - thresholds[i])
						&& (signal[i] - thresholds[i]) > (signal[i + 1] - thresholds[i + 1])) {
					// is peak, i.e. neighbors are smaller
					peakList.add(i);
				}
			}
		}

		return peakList;
	}

	private double[] generateThresholds(final double[] signal) {
		double[] thresholds = new double[signal.length];
		for (int i = 0; i < signal.length; i++) {
			thresholds[i] = threshold + ProcessingUtils.getMedian(signal, i, median_range);
		}
		return thresholds;
	}

}
