package at.cp.jku.teaching.amprocessing.project;

import java.util.LinkedList;
import java.util.List;

import at.cp.jku.teaching.amprocessing.AudioFile;
import at.cp.jku.teaching.amprocessing.SpectralData;

public class SpectralDifferenceOnsetDetector implements OnsetDetector {

	private static final int MEDIAN_RANGE = 25;
	private static final double THRESHOLD = 0.08;
	private PeakPicking peakPicker;
	
	public SpectralDifferenceOnsetDetector() {
		peakPicker = new AdaptiveThresholding(THRESHOLD, MEDIAN_RANGE);
	}

	@Override
	public List<Double> analyze(AudioFile audiofile) {
		LinkedList<SpectralData> list = audiofile.spectralDataContainer;
		double[] sd = new double[list.size()]; 
		for (int n=0; n<list.size(); n++) {
			SpectralData spectralData = list.get(n);
			double sum = 0;
			for (int i = 1; i < spectralData.magnitudes.length; i++) {
				double magnitude = Math.abs(spectralData.magnitudes[i]);
				double lastMagnitude = n==0 ? 0 : Math.abs(list.get(n-1).magnitudes[i]);
				double h = Math.max(0, magnitude-lastMagnitude);
				sum += Math.pow(h, 2);
			}
			sd[n] = sum;
		}
		ProcessingUtils.normalize(sd);
		List<Integer> peaks = peakPicker.pickPeaks(sd);
		return ProcessingUtils.translateFramesToSeconds(peaks, audiofile.hopTime);
	}



}
