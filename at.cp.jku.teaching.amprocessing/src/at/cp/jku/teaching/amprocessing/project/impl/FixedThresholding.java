package at.cp.jku.teaching.amprocessing.project.impl;

import java.util.ArrayList;
import java.util.List;

import at.cp.jku.teaching.amprocessing.project.PeakPicking;

/**
 * Implementation of peak picking with fixed thresholding 
 *
 */
public class FixedThresholding implements PeakPicking {
	
	private final double threshold;


	public FixedThresholding(double threshold) {
		this.threshold = threshold;
	}
	
	@Override
	public List<Integer> pickPeaks(double[] sd) {
		List<Integer> peakList = new ArrayList<>();
		for (int i = 0; i < sd.length; i++) {
			if (sd[i] >= threshold) {
				// is larger than threshold
				if ((sd[i - 1] - threshold) < (sd[i] - threshold)
						&& (sd[i] - threshold) > (sd[i + 1] - threshold)) {
					// is peak, i.e. neighbors are smaller
					peakList.add(i);
				}
			}
		}
		return peakList;
	}

}
