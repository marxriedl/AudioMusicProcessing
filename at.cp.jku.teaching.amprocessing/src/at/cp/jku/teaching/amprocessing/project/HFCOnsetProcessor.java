package at.cp.jku.teaching.amprocessing.project;

import java.util.List;

import at.cp.jku.teaching.amprocessing.AudioFile;

public class HFCOnsetProcessor implements OnsetDetector {
	private static final int H = 20;
	private static final double CONSTANT_THRESHOLD = 0.1;
	private PeakPicking peakPicker;

	public HFCOnsetProcessor() {
		peakPicker = new AdaptiveThresholding(CONSTANT_THRESHOLD, H);
	}

	public List<Double> analyze(AudioFile m_audiofile) {
		double[] hfc = new double[m_audiofile.spectralDataContainer.size()];
		double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
		for (int i = 0; i < m_audiofile.spectralDataContainer.size(); i++) {
			double totalEnergy = 0.0;
			for (int k = 0; k < m_audiofile.spectralDataContainer.get(i).magnitudes.length; k++) {
				totalEnergy += (k + 1) * Math.pow(Math.abs(m_audiofile.spectralDataContainer.get(i).magnitudes[k]), 2.0);
			}
			totalEnergy /= (m_audiofile.spectralDataContainer.get(i).magnitudes.length);
			hfc[i] = totalEnergy;
			if (totalEnergy < min)
				min = totalEnergy;
			if (totalEnergy > max)
				max = totalEnergy;
		}

		ProcessingUtils.normalize(hfc);

		List<Integer> peaks = peakPicker.pickPeaks(hfc);
		return ProcessingUtils.translateFramesToSeconds(peaks, m_audiofile.hopTime);
	}
}
