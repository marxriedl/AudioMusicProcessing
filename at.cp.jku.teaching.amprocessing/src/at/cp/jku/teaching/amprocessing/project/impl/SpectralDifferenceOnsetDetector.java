package at.cp.jku.teaching.amprocessing.project.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.cp.jku.teaching.amprocessing.AudioFile;
import at.cp.jku.teaching.amprocessing.SpectralData;
import at.cp.jku.teaching.amprocessing.project.OnsetDetector;
import at.cp.jku.teaching.amprocessing.project.util.ProcessingUtils;

/**
 * Implementation of an onset detector using spectral difference.
 */
public class SpectralDifferenceOnsetDetector implements OnsetDetector {

	private static final int MEDIAN_RANGE = 25;
	private static final double CONSTANT_THRESHOLD = 0.08;

	public SpectralDifferenceOnsetDetector() {
	}

	/**
	 * SD(n) = Σ (H(|Xk(n)|-|Xk(n-1)|))² <br>
	 * and <br>
	 * H(x) = (x + |x|)/2
	 */
	@Override
	public double[] analyze(AudioFile audiofile) {
		List<SpectralData> list = audiofile.spectralDataContainer;
		double[] sd = new double[list.size()];
		for (int n = 0; n < list.size(); n++) {
			SpectralData spectralData = list.get(n);
			double sum = 0;
			int length = spectralData.magnitudes.length;
			for (int k = 0; k < length; k++) {
				// |Xk(n)|
				double magnitude = Math.abs(spectralData.magnitudes[k]);
				// |Xk(n-1)|
				double lastMagnitude = n == 0 ? 0 : Math.abs(list.get(n - 1).magnitudes[k]);
				// x = |Xk(n)|-|Xk(n-1)|
				double x = (magnitude - lastMagnitude) * (length - k);
				// H(x) = (x + |x|)/2, i.e. zero for negative arguments
				double h = Math.max(0, x);
				// Σ H(x)²
				sum += Math.pow(h, 2);
			}
			// SD(n) = Σ H(x)²
			sd[n] = sum;
		}
		ProcessingUtils.normalize(sd);
		return sd;
	}

	@Override
	public Map<String, Number> getParameters() {
		Map<String, Number> parameters = new HashMap<>();
		// parameters for adaptive thresholding
		parameters.put(AdaptiveThresholding.MEDIAN_RANGE, MEDIAN_RANGE);
		parameters.put(AdaptiveThresholding.CONSTANT_THRESHOLD, CONSTANT_THRESHOLD);
		return parameters;
	}

}
