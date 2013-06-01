package at.cp.jku.teaching.amprocessing.project.impl;

import java.util.ArrayList;
import java.util.List;

import at.cp.jku.teaching.amprocessing.project.BeatDetector;
import at.cp.jku.teaching.amprocessing.project.util.ProcessingUtils;
import at.cp.jku.teaching.amprocessing.project.util.PulseTrainCorrelationVisualisation;

public class AutoCorrelationBeatDetection implements BeatDetector {
	private static final double PULSE_LENGTH = 0.5;
	private static final int FRAMES_PER_SECOND = 100;
	private final static double CORRELATION_WINDOW_SECONDS = 5;
	private final static int DELTA_WINDOW = 30;
	private final static int LOWERBOUND_MILLISECONDS = 300;
	private final static int UPPERBOUND_MILLISECONDS = 1000;
	private final static int LOWERBEAT = 60;
	private final static int UPPERBEAT = 200;

	@Override
	public List<Integer> analyze(double[] onsetFunction) {
		int start_tau = (LOWERBOUND_MILLISECONDS * FRAMES_PER_SECOND / 1000);
		int end_tau = (UPPERBOUND_MILLISECONDS * FRAMES_PER_SECOND / 1000);

		for (int i = 0; i < onsetFunction.length; i++) {
			if (onsetFunction[i] <= (0.1 + ProcessingUtils.getMedian(onsetFunction, i, DELTA_WINDOW)))
				onsetFunction[i] = 0.0;
		}

		List<Integer> beats = new ArrayList<Integer>();

		int last_beat_pos = 0;
		double last_bpm = 0.0;
		double framesPerWindow = CORRELATION_WINDOW_SECONDS * FRAMES_PER_SECOND;
		for (int i = 0; i < onsetFunction.length / framesPerWindow; i++) {
			int start = i * (int) framesPerWindow;
			int end = i * (int) framesPerWindow + (int) framesPerWindow;

			int fpb = windowedAutoCorrelation(onsetFunction, start, end, start_tau, end_tau); // frames per beat

			int max_pos = windowedCrossCorrelation(onsetFunction, fpb, start, end);

			// PulseTrainCorrelationVisualisation.visualise(onsetFunction, fpb, max_pos);

			int position = start - max_pos + fpb;
			while ((position + fpb) < end) {
				beats.add(findInDeltaWindow(onsetFunction, position, DELTA_WINDOW));
				position += fpb;
				last_beat_pos = position;
			}
		}
		return beats;
	}

	private int windowedCrossCorrelation(double[] onsetFunction, int fpb, int start, int end) {
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
		// search for nearest peak
		for (int i = 0; i < window / 2; i++) {
			for (int offset : new int[] { i, -i }) {
				if (isPeak(function, position + offset)) {
					System.out.println("found peak, offset=" + offset);
					return position + offset;
				}
			}
		}
		// no peak found within window
		return position;
	}

	private boolean isPeak(double[] f, int i) {
		return i >= 0 && i < f.length && (i - 1 < 0 || f[i - 1] < f[i]) && (i + 1 >= f.length || f[i + 1] <= f[i])
				&& f[i] >= (0.1 + ProcessingUtils.getMedian(f, i, DELTA_WINDOW));
	}

	/**
	 * windowedAutoCorrelation
	 * 
	 * @param onsetFunction
	 *            the detection function from any OnsetProcessor
	 * @param start
	 *            position in onsetFunction where we want to start correlating
	 * @param end
	 *            position in onsetFunction where we want to end correlating
	 * @param start_tau
	 *            the offset to start checking with
	 * @param end_tau
	 *            the offset to end checking with
	 * @return the offset where the maximum auto correlation occurs with start_tau <= offset < end_tau
	 */
	private int windowedAutoCorrelation(double[] onsetFunction, int start, int end, int start_tau, int end_tau) {
		double max = Double.MIN_VALUE;
		int max_beat_pos = 0;
		for (int t = start_tau; t < end_tau; t++) {
			double sum = 0.0;
			for (int k = start; k < end; k++) {
				int kShifted = k + t;
				kShifted = kShifted >= onsetFunction.length ? (kShifted % onsetFunction.length) : kShifted;
				sum += onsetFunction[kShifted] * onsetFunction[k];
			}
			if (sum > max) {
				max = sum;
				max_beat_pos = t;
			}
		}
		return max_beat_pos;
	}

	public int pulseTrain(int fpb, int pos) {
		double local_pos = pos % fpb;
		if (local_pos < (PULSE_LENGTH * fpb)) {// / 2 || local_pos >= fpb - PULSE_LENGTH / 2 * fpb) {
			return 1;
		}

		return 0;
	}
}
