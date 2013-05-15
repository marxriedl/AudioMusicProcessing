package at.cp.jku.teaching.amprocessing.project.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import at.cp.jku.teaching.amprocessing.AudioFile;
import at.cp.jku.teaching.amprocessing.SpectralData;
import at.cp.jku.teaching.amprocessing.project.OnsetDetector;
import at.cp.jku.teaching.amprocessing.project.util.ProcessingUtils;

public class SpectralDifferenceOnsetDetector implements OnsetDetector {

	private static final int MEDIAN_RANGE = 25;
	private static final double CONSTANT_THRESHOLD = 0.08;
	
	public SpectralDifferenceOnsetDetector() {
	}

	@Override
	public double[] analyze(AudioFile audiofile) {
		LinkedList<SpectralData> list = audiofile.spectralDataContainer;
		double[] sd = new double[list.size()];
		for (int n=0; n<list.size(); n++) {
			SpectralData spectralData = list.get(n);
			double sum = 0;
			int length = spectralData.magnitudes.length;
			for (int k = 0; k < length; k++) {
				double magnitude = Math.abs(spectralData.magnitudes[k]);
				double lastMagnitude = n==0 ? 0 : Math.abs(list.get(n-1).magnitudes[k]);
				double h = Math.max(0, (magnitude-lastMagnitude)*(length-k));
				sum += Math.pow(h, 2);
			}
			sd[n] = sum;
		}
		ProcessingUtils.normalize(sd);
		return sd;
	}
	
	@Override
	public Map<String, Number> getParameters() {
		Map<String, Number> parameters = new HashMap<>();
		parameters.put(AdaptiveThresholding.MEDIAN_RANGE, MEDIAN_RANGE);
		parameters.put(AdaptiveThresholding.CONSTANT_THRESHOLD, CONSTANT_THRESHOLD);
		return parameters;
	}



}
