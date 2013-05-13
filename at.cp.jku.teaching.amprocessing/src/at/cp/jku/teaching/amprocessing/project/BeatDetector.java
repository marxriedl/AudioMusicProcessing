package at.cp.jku.teaching.amprocessing.project;

import java.util.List;

public interface BeatDetector {

	List<Double> analyze(double[] onsetFunction);

}
