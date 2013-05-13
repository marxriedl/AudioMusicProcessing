package at.cp.jku.teaching.amprocessing.project;

import java.util.Collections;
import java.util.Map;

import at.cp.jku.teaching.amprocessing.AudioFile;

/**
 * This is a very simple kind of Onset Detector... You have to implement at
 * least 2 more different onset detection functions have a look at the
 * SpectralData Class - there you can also access the magnitude and the phase in
 * each FFT bin...
 * 
 * @author andreas arzt
 * 
 */
public class EnergyDifferenceOnsetDetector implements OnsetDetector {

	@Override
	public double[] analyze(AudioFile audiofile) {
		int length = audiofile.spectralDataContainer.size();
		double[] onsets = new double[length];
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				continue;
			}
			if (audiofile.spectralDataContainer.get(i).totalEnergy
					- audiofile.spectralDataContainer.get(i - 1).totalEnergy > 10) {
				onsets[i] = 1;
			}
		}
		return onsets;
	}
	
	@Override
	public Map<String, Number> getParameters() {
		return Collections.emptyMap();
	}

}
