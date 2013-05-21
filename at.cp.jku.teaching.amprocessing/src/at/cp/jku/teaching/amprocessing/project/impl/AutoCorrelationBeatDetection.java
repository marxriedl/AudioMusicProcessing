package at.cp.jku.teaching.amprocessing.project.impl;

import java.util.ArrayList;
import java.util.List;

import at.cp.jku.teaching.amprocessing.project.BeatDetector;
import at.cp.jku.teaching.amprocessing.project.util.ProcessingUtils;

public class AutoCorrelationBeatDetection implements BeatDetector {
	private static final double PULSE_LENGTH = 0.5;
	private static final int FRAMES_PER_SECOND = 100;
	private final static double N = 1;
	private final static int beatWindow = 300;
	private final static int LOWERBOUND_MILLISECONDS = 300;
	private final static int UPPERBOUND_MILLISECONDS = 1000;
	private final static int LOWERBEAT = 60;
	private final static int UPPERBEAT = 200;

	@Override
	public List<Integer> analyze(double[] onsetFunction) {
		int[] timeinstants = new int[(UPPERBOUND_MILLISECONDS - LOWERBOUND_MILLISECONDS) * FRAMES_PER_SECOND / 1000];
		for (int i = 0; i < timeinstants.length; i++) {
			timeinstants[i] = (LOWERBOUND_MILLISECONDS * FRAMES_PER_SECOND / 1000) + i;
		}

		List<Integer> beats = new ArrayList<Integer>();

		int last_beat_pos = 0;
		double last_bpm = 0.0;
		for (int i = 0; i < onsetFunction.length / (N * FRAMES_PER_SECOND); i++) {
			int start = i * (int) (N * FRAMES_PER_SECOND);
			int end = i * (int) (N * FRAMES_PER_SECOND) + (int) (N * FRAMES_PER_SECOND);

			int max_beat_pos = windowedAutoCorrelation(onsetFunction, timeinstants, start, end);
			int fpb = timeinstants[max_beat_pos]; // frames per beat

			int max_pos = windowedCrossCorrelation(onsetFunction, fpb, start, end);

			int position = start + max_pos;
			while ((position + fpb) < end) {
				beats.add(findInDeltaWindow(onsetFunction, position, beatWindow));
				position += fpb;
				last_beat_pos = position;
			}
		}
		return beats;
	}

	private int windowedCrossCorrelation(double[] onsetFunction, double fpb, int start, int end) {
		double max = Double.MIN_VALUE;
		int max_beat_pos = 0;
		for (int t = 0; t < fpb; t++) {
			double sum = 0.0;
			for (int k = start; k < end; k++) {
				sum += onsetFunction[k] * pulseTrain(fpb, k + t);
			}
			if (sum > max) {
				max = sum;
				max_beat_pos = t;
			}
		}
		return max_beat_pos;
	}

	private int findInDeltaWindow(double[] function, int position, int window) {
		if (function[position] > (0.1 + ProcessingUtils.getMedian(function, position, 20))) {
			return position;
		}
		int start = position - (window / 2);
		start = start < 0 ? 0 : start;
		int end = position + (window / 2);
		end = end > function.length ? function.length : end;

		for (int i = start; i < end; i++) {
			if (function[i] > (0.1 + ProcessingUtils.getMedian(function, position, 20)))
				return i;
		}

		return position;
	}

	/**
	 * windowedCrossCorrelation
	 * 
	 * @param onsetFunction
	 *            the detection function from any OnsetProcessor
	 * @param indizes
	 *            function to correlate with
	 * @param start
	 *            position in onsetFunction where we want to correlate onset
	 *            with correlationFunction
	 * @param end
	 *            position in onsetFunction
	 * @return
	 */
	private int windowedAutoCorrelation(double[] onsetFunction, int[] indizes, int start, int end) {
		double max = Double.MIN_VALUE;
		int max_beat_pos = 0;
		for (int t = 0; t < indizes.length; t++) {
			double sum = 0.0;
			for (int k = start; k < end; k++) {
				sum += onsetFunction[(k + indizes[t]) % onsetFunction.length] * onsetFunction[k];
			}
			if (sum > max) {
				max = sum;
				max_beat_pos = t;
			}
		}
		return max_beat_pos;
	}

	private int pulseTrain(double fpb, int pos) {
		double local_pos = pos % fpb;
		if (local_pos < (PULSE_LENGTH * fpb) / 2 || local_pos >= fpb - PULSE_LENGTH * fpb) {
			return 1;
		}

		return 0;
	}
}
