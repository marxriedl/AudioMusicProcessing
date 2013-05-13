package at.cp.jku.teaching.amprocessing.project;

import java.util.ArrayList;
import java.util.List;

import at.cp.jku.teaching.amprocessing.AudioFile;

/** 
 * This is a very simple kind of Onset Detector... You have to implement at least 2 more different onset detection functions
 * have a look at the SpectralData Class - there you can also access the magnitude and the phase in each FFT bin...
 * @author andreas arzt
 *
 */
public class EnergyDifferenceOnsetDetector implements OnsetDetector {
	
    @Override
	public List<Double> analyze(AudioFile audiofile) {
        List<Double> onsetList = new ArrayList<>();
		for (int i = 0; i < audiofile.spectralDataContainer.size(); i++) {
            if (i == 0) {
                continue;
            }
            if (audiofile.spectralDataContainer.get(i).totalEnergy - audiofile.spectralDataContainer.get(i - 1).totalEnergy > 10) {
				onsetList.add(i * audiofile.hopTime);            
				}
        }
        return onsetList;
    }

}
