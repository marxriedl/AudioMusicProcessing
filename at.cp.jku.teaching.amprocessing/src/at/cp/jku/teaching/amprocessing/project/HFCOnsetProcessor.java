package at.cp.jku.teaching.amprocessing.project;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jfree.ui.RefineryUtilities;

import at.cp.jku.teaching.amprocessing.AudioFile;
import at.cp.jku.teaching.amprocessing.Processor;

public class HFCOnsetProcessor {
	private static final int H = 20;
	private static final double CONSTANT_THRESHOLD = 0.1;
	private AudioFile m_audiofile;

	public HFCOnsetProcessor(AudioFile m_audiofile) {
		this.m_audiofile = m_audiofile;
	}

	public LinkedList<Double> analyze() {
		List<Double> m_hfc = new ArrayList<Double>();
		double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
		for (int i = 0; i < m_audiofile.spectralDataContainer.size(); i++) {
			double totalEnergy = 0.0;
			for (int k = 0; k < m_audiofile.spectralDataContainer.get(i).magnitudes.length; k++) {
				totalEnergy += (k + 1) * Math.pow(Math.abs(m_audiofile.spectralDataContainer.get(i).magnitudes[k]), 2.0);
			}
			totalEnergy /= (m_audiofile.spectralDataContainer.get(i).magnitudes.length);
			m_hfc.add(totalEnergy);
			if (totalEnergy < min)
				min = totalEnergy;
			if (totalEnergy > max)
				max = totalEnergy;
		}

		double range = max - min;
		for (int i = 0; i < m_hfc.size(); i++) {
			m_hfc.set(i, (m_hfc.get(i) - min) / range);
		}
		/*
		 * if (m_hfc.get(i) > (CONSTANT_THRESHOLD + adaptive_threshold) && !isOnset) { isOnset = true; localMax = m_hfc.get(i); localMaxPos = i; } if (isOnset && localMax < m_hfc.get(i)) { localMax =
		 * m_hfc.get(i); localMaxPos = i; } if (m_hfc.get(i) < (CONSTANT_THRESHOLD + adaptive_threshold) && m_hfc.get(i - 1) > (CONSTANT_THRESHOLD + adaptive_threshold) && isOnset) { isOnset = false;
		 * localMax = Double.MIN_VALUE; m_onsetList.add(localMaxPos * m_audiofile.hopTime); }
		 */

		List<Double> thresholds = generateThresholds(m_hfc);

		LinkedList<Double> m_onsetList = new LinkedList<Double>();
		for (int i = 1; i < m_hfc.size() - 1; i++) {
			if (m_hfc.get(i) >= thresholds.get(i)) {
				if ((m_hfc.get(i - 1) - thresholds.get(i - 1)) < (m_hfc.get(i) - thresholds.get(i))
						&& (m_hfc.get(i) - thresholds.get(i)) > (m_hfc.get(i + 1) - thresholds.get(i + 1))) {
					m_onsetList.add(i * m_audiofile.hopTime);

				}
			}
		}

		final Visualisation demo = new Visualisation(thresholds, m_hfc, m_onsetList);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
		return m_onsetList;
	}

	private List<Double> generateThresholds(final List<Double> list) {
		List<Double> thresholds = new ArrayList<Double>();
		for (int i = 0; i < list.size(); i++) {
			thresholds.add(CONSTANT_THRESHOLD + getMedian(list, i));
		}
		return thresholds;
	}

	private double getMean(final List<Double> list, final int i) {
		int start = i - (H / 2) < 0 ? 0 : i - (H / 2);
		int end = i + (H / 2) > list.size() ? list.size() : i + (H / 2);
		double mean = 0.0;
		for (int s = start; s < end; s++) {
			mean += list.get(s);
		}
		return mean / (end - start);
	}

	private double getMedian(final List<Double> list, final int i) {
		int start = i - (H / 2) < 0 ? 0 : i - (H / 2);
		int end = i + (H / 2) > list.size() ? list.size() : i + (H / 2);
		double data[] = new double[(end - start)];
		int m = 0;
		for (int s = start; s < end; s++) {
			data[m++] = list.get(s);
		}
		double sortedData[] = sortData(data);
		if (sortedData.length % 2 == 0) {
			return (sortedData[(sortedData.length / 2) - 1] + sortedData[sortedData.length / 2]) / 2;
		} else {
			return sortedData[sortedData.length / 2];
		}
	}

	private double[] sortData(final double[] evalData) {
		double[] sortedArray = evalData;
		int n = sortedArray.length;
		do {
			int newn = 1;
			for (int i = 0; i < n - 1; ++i) {
				if (sortedArray[i] > sortedArray[i + 1]) {
					swap(sortedArray, i, i + 1);
					newn = i + 1;
				} // ende if
			} // ende for
			n = newn;
		} while (n > 1);
		return sortedArray;
	}

	private void swap(double[] sortedArray, final int i, final int j) {
		double temp = sortedArray[i];
		sortedArray[i] = sortedArray[j];
		sortedArray[j] = temp;
	}
}
