package at.cp.jku.teaching.amprocessing.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.WindowConstants;

import org.jfree.ui.RefineryUtilities;

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
				if ((signal[i - 1] - thresholds[i - 1]) < (signal[i] - thresholds[i])
						&& (signal[i] - thresholds[i]) > (signal[i + 1] - thresholds[i + 1])) {
					peakList.add(i);
				}
			}
		}
		
		final Visualisation2 demo = new Visualisation2(thresholds, signal, peakList);
		demo.pack();
		demo.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
		
		return peakList;
	}

	private double[] generateThresholds(final double[] signal) {
		double[] thresholds = new double[signal.length];
		for (int i = 0; i < signal.length; i++) {
			thresholds[i] = threshold + getMedian(signal, i);
		}
		return thresholds;
	}

	private double getMedian(final double[] signal, final int i) {
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
