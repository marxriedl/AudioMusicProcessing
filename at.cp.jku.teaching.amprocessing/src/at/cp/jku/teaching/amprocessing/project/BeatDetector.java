package at.cp.jku.teaching.amprocessing.project;

import java.util.List;

public interface BeatDetector {

	List<Integer> analyze(double[] onsetFunction);

}
