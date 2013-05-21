/*
 * Processor.java
 *
 * This is the class where you can implement your onset detection / tempo extraction methods
 * Of course you may also define additional classes.
 */
package at.cp.jku.teaching.amprocessing;

import java.util.LinkedList;
import java.util.List;

import at.cp.jku.teaching.amprocessing.project.BeatDetector;
import at.cp.jku.teaching.amprocessing.project.OnsetDetector;
import at.cp.jku.teaching.amprocessing.project.PeakPicking;
import at.cp.jku.teaching.amprocessing.project.TempoCalculator;
import at.cp.jku.teaching.amprocessing.project.impl.AdaptiveThresholding;
import at.cp.jku.teaching.amprocessing.project.impl.AutoCorrelationBeatDetection;
import at.cp.jku.teaching.amprocessing.project.impl.HFCOnsetProcessor;
import at.cp.jku.teaching.amprocessing.project.impl.SimpleTempoCalculator;
import at.cp.jku.teaching.amprocessing.project.util.ProcessingUtils;

/**
 * 
 * @author andreas arzt
 */
public class Processor {

	private String m_filename;
	private AudioFile m_audiofile;
	// this List should contain your results of the onset detection step (onset times in seconds)
	private LinkedList<Double> m_onsetList;
	// this may contain your intermediate results (in frames, before conversion to time in seconds)
	private LinkedList<Integer> m_onsetListFrames;
	// this variable should contain your result of the tempo estimation algorithm
	private double m_tempo;
	// this List should contain your results of the beat detection step (beat times in seconds)
	private LinkedList<Double> m_beatList;
	// this may contain your intermediate beat results (in frames, before conversion to time in seconds)
	private LinkedList<Integer> m_beatListFrames;

	public Processor(String filename) {
		System.out.println("Initializing Processor...");
		m_filename = filename;
		m_onsetList = new LinkedList<Double>();
		m_onsetListFrames = new LinkedList<Integer>();
		m_beatList = new LinkedList<Double>();
		m_beatListFrames = new LinkedList<Integer>();

		System.out.println("Reading Audio-File " + filename);
		System.out.println("Performing FFT...");
		// an AudioFile object is created with the following Paramters: AudioFile(WAVFILENAME, FFTLENGTH in seconds, HOPLENGTH in seconds)
		// if you would like to work with multiple resolutions you simple create multiple AudioFile objects with different parameters
		// given an audio file with 44.100 Hz the parameters below translate to an FFT with size 2048 points
		// Note that the value is not taken to be precise; it is adjusted so that the FFT Size is always power of 2.
		m_audiofile = new AudioFile(m_filename, 0.046439, 0.01);
		// this starts the extraction of the basis features (the STFT)
		m_audiofile.processFile();
	}

	// This method is called from the Runner and is the starting point of your onset detection / tempo extraction code
	public void analyze() {
		System.out.println("Running analysis...");

		System.out.println("Detecting onsets...");
		OnsetDetector onsetDetector = new HFCOnsetProcessor();
		
		double[] onsetFunction = onsetDetector.analyze(m_audiofile);

		System.out.println("Picking onsets from detection function...");
		PeakPicking peakPicker = new AdaptiveThresholding(onsetDetector.getParameters());
		List<Integer> peaks = peakPicker.pickPeaks(onsetFunction);
		m_onsetList.addAll(ProcessingUtils.translateFramesToSeconds(peaks, m_audiofile.hopTime));

		System.out.println("Detecting beats...");
		BeatDetector beatDetector = new AutoCorrelationBeatDetection();
		m_beatList.addAll(ProcessingUtils.translateFramesToSeconds(beatDetector.analyze(onsetFunction), m_audiofile.hopTime));

		System.out.println("Calculating tempo...");
		TempoCalculator tempoCalculator = new SimpleTempoCalculator();
		m_tempo = tempoCalculator.analyze(m_beatList);
		
		System.out.println("Analysis done");
	}

	public LinkedList<Double> getOnsets() {
		return m_onsetList;
	}

	public double getTempo() {
		return m_tempo;
	}

	public LinkedList<Double> getBeats() {
		return m_beatList;
	}
}
