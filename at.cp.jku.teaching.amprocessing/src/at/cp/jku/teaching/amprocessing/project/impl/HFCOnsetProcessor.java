package at.cp.jku.teaching.amprocessing.project.impl;

import java.util.HashMap;
import java.util.Map;

import at.cp.jku.teaching.amprocessing.AudioFile;
import at.cp.jku.teaching.amprocessing.project.OnsetDetector;
import at.cp.jku.teaching.amprocessing.project.util.ProcessingUtils;

public class HFCOnsetProcessor implements OnsetDetector {
	private static final int H = 20;
	private static final double CONSTANT_THRESHOLD = 0.1;
	
	public HFCOnsetProcessor() {
	}

	
	/***
	 * computes the high frequency content 
	 * 1/N SUM(Wk * |Xk(n)|^2)
	 * Wk energy dependent weighting, index + 1 in this case; changes due to transients are more noticable at higher frequencies
	 */
	public double[] analyze(AudioFile m_audiofile) {
		double[] hfc = new double[m_audiofile.spectralDataContainer.size()];
		for (int i = 0; i < m_audiofile.spectralDataContainer.size(); i++) {
			double totalEnergy = 0.0; 
			for (int k = 0; k < m_audiofile.spectralDataContainer.get(i).magnitudes.length; k++) {
				totalEnergy += /* Wk */ (k + 1) * /*|Xk(n)|^2*/ Math.pow(Math.abs(m_audiofile.spectralDataContainer.get(i).magnitudes[k]), 2.0);
			}
			totalEnergy /= (m_audiofile.spectralDataContainer.get(i).magnitudes.length);
			hfc[i] = totalEnergy;
		}

		ProcessingUtils.normalize(hfc);

		return hfc;
	}
	
	@Override
	public Map<String, Number> getParameters() {
		Map<String, Number> parameters = new HashMap<>();
		parameters.put(AdaptiveThresholding.MEDIAN_RANGE, H);
		parameters.put(AdaptiveThresholding.CONSTANT_THRESHOLD, CONSTANT_THRESHOLD);
		return parameters;
	}
	
}
