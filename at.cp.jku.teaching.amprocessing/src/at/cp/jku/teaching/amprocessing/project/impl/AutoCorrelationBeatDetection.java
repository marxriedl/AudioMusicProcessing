package at.cp.jku.teaching.amprocessing.project.impl;

import java.util.ArrayList;
import java.util.List;

import at.cp.jku.teaching.amprocessing.project.BeatDetector;
import at.cp.jku.teaching.amprocessing.project.util.ProcessingUtils;

public class AutoCorrelationBeatDetection implements BeatDetector {
	/**
	 * Relative pulse length of the pulse train
	 */
	private static final double PULSE_LENGTH = 0.3;
	private static final int FRAMES_PER_SECOND = 100;
	private final static double CORRELATION_WINDOW_SECONDS = 30;
	/**
	 * Size of the window to look for neighboring onset peaks when adjusting
	 * the beat position in frames
	 */
	private final static int DELTA_WINDOW_FRAMES = 20;
	private final static int LOWERBOUND_MILLISECONDS = 300;
	private final static int UPPERBOUND_MILLISECONDS = 1000;

	@Override
	public List<Integer> analyze(double[] onsetFunction) {
		int start_tau = (LOWERBOUND_MILLISECONDS * FRAMES_PER_SECOND / 1000);
		int end_tau = (UPPERBOUND_MILLISECONDS * FRAMES_PER_SECOND / 1000);

		/* pre processing step, set every value != onset to zero */
		for (int i = 0; i < onsetFunction.length; i++) {
			if (onsetFunction[i] <= (0.1 + ProcessingUtils.getMedian(onsetFunction, i, DELTA_WINDOW_FRAMES)))
				onsetFunction[i] = 0.0;
		}

		List<Integer> beats = new ArrayList<Integer>();

		double framesPerWindow = CORRELATION_WINDOW_SECONDS * FRAMES_PER_SECOND;
		for (int i = 0; i < onsetFunction.length / framesPerWindow; i++) {
			int start = i * (int) framesPerWindow;
			int end = i * (int) framesPerWindow + (int) framesPerWindow;

			int fpb = windowedAutoCorrelation(onsetFunction, start, end, start_tau, end_tau); // frames
																								// per
																								// beat

			int max_pos = windowedCrossCorrelation(onsetFunction, fpb, start, end);

			// PulseTrainCorrelationVisualisation.visualise(onsetFunction, fpb,
			// max_pos);

			int position = start - max_pos + fpb;
			while ((position + fpb) < end) {
				beats.add(findInDeltaWindow(onsetFunction, position, DELTA_WINDOW_FRAMES));
				position += fpb;
			}
		}
		return beats;
	}

	/**
	 * implements a windowed correlation of the onsetfunction with a computed beat pulse train (generated from fpb) 
	 * @param onsetFunction the given onsetFunction
	 * @param fpb frames per beat, from this information a pulsetrain is generated
	 * @param start start of the window in the onsetFunction
	 * @param end end of the window in the onsetFunction
	 * @return
	 */
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

	/**
	 * For a given function and position looks for peaks around the position. <br>
	 * Used to adjust the position of the presumed beats to match the real
	 * onsets.
	 * <p>
	 * 
	 * @param function
	 *            the function
	 * @param position
	 *            the position of the presumed beat
	 * @param window
	 *            the size of the window to look in
	 * @return the "best" position for the beat, i.e. a peak within the window
	 *         near the original position or the original position itself, if no
	 *         such peak was found
	 */
	private int findInDeltaWindow(double[] function, int position, int window) {
		// search for nearest peak
		for (int i = 0; i < window / 2; i++) {
			// go from inside (at the original position) out
			for (int offset : new int[] { i, -i }) {
				if (isPeak(function, position + offset)) {
					return position + offset;
				}
			}
		}
		// no peak found within window
		return position;
	}

	/**
	 * Decides whether the given function has a peak on the given position or
	 * not. <br>
	 * It returns true, when function[pos-1] and function[pos+1] are smaller
	 * than function[pos].
	 * 
	 * @param function
	 *            the function containing possible peaks
	 * @param pos
	 *            the position of the function to query
	 * @return true, when the given function has a peak at the given position
	 */
	private boolean isPeak(double[] function, int pos) {
		return pos >= 0 && pos < function.length && (pos - 1 < 0 || function[pos - 1] < function[pos])
				&& (pos + 1 >= function.length || function[pos + 1] <= function[pos]);
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
	 * @return the offset where the maximum auto correlation occurs with
	 *         start_tau <= offset < end_tau
	 */
	private int windowedAutoCorrelation(double[] onsetFunction, int start, int end, int start_tau, int end_tau) {
		double max = Double.MIN_VALUE;
		int max_beat_pos = 0;
		for (int t = start_tau; t < end_tau; t++) {
			double sum = 0.0;
			for (int k = start; k < end; k++) {
				int kShifted = k + t;
				kShifted = kShifted >= onsetFunction.length ? 0 : kShifted;
				sum += onsetFunction[kShifted] * onsetFunction[k];
			}
			if (sum > max) {
				max = sum;
				max_beat_pos = t;
			}
		}
		return max_beat_pos;
	}

	/**
	 * Returns the value of a pulse train with a given period at a given
	 * position. <br>
	 * The pulse starts half a PULSE_LENGTH before 0 and ends half a
	 * PULSE_LENGTH after 0 and repeats periodically. <br>
	 * --__----__----__--...
	 * <p>
	 * 
	 * @param fpb
	 *            frames per beat, i.e. the period of the pulse train
	 * @param pos
	 *            the position of the function
	 * @return the value of the pulse train at position pos, i.e. 0 or 1
	 */
	public int pulseTrain(int fpb, int pos) {
		double local_pos = pos % fpb;
		if (local_pos < (PULSE_LENGTH * fpb) / 2 || local_pos >= fpb - PULSE_LENGTH / 2 * fpb) {
			return 1;
		}

		return 0;
	}
}
