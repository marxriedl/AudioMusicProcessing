package at.cp.jku.teaching.amprocessing.project;

import java.util.ArrayList;
import java.util.List;

public class FixedThresholding implements PeakPicking {
	
	private final double threshold;


	public FixedThresholding(double threshold) {
		this.threshold = threshold;
	}
	
	
	public List<Integer> pickPeaks(double[] sd) {
		List<Integer> peakList = new ArrayList<>();
		for (int i = 0; i < sd.length; i++) {
			if (sd[i] >= threshold) {
				if ((sd[i - 1] - threshold) < (sd[i] - threshold)
						&& (sd[i] - threshold) > (sd[i + 1] - threshold)) {
					peakList.add(i);
				}
			}
		}
		return peakList;
	}

}
