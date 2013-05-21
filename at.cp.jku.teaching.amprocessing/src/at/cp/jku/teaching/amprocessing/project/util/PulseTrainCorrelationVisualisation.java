package at.cp.jku.teaching.amprocessing.project.util;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import at.cp.jku.teaching.amprocessing.project.impl.AutoCorrelationBeatDetection;

@SuppressWarnings("serial")
public class PulseTrainCorrelationVisualisation extends JFrame {

	public PulseTrainCorrelationVisualisation(double[] onsetFunction, int fpb, int offset) {
		final XYDataset dataset = createDataset(onsetFunction, fpb, offset);
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1920, 1080));
		setContentPane(chartPanel);
	}

	private XYDataset createDataset(double[] onsetFunction, int fpb, int offset) {
		final XYSeries series1 = new XYSeries("onsetFunction");
		final XYSeries series2 = new XYSeries("pulseTrain");
		 final XYSeries series3 = new XYSeries("beats");

		AutoCorrelationBeatDetection autoCorrelationBeatDetection = new AutoCorrelationBeatDetection();

		for (int i = 0; i < onsetFunction.length; i++) {
			series1.add(i, onsetFunction[i]);
			series2.add(i, autoCorrelationBeatDetection.pulseTrain(fpb, i+offset));
		}
		
		int b = fpb-offset;
		while(b < onsetFunction.length) {
			series3.add(b, 1d);
			b += fpb;
		}

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		return dataset;
	}

	private JFreeChart createChart(XYDataset dataset) {
		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart("Plotvisualisation", // chart
																						// title
				"X", // x axis label
				"Y", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

		// final StandardLegend legend = (StandardLegend) chart.getLegend();
		// legend.setDisplaySeriesShapes(true);

		// get a reference to the plot for further customisation...
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		renderer.setSeriesLinesVisible(1, true);
		renderer.setSeriesLinesVisible(2, false);
		renderer.setSeriesLinesVisible(3, false);
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesShapesVisible(1, false);
		renderer.setSeriesShapesVisible(2, true);
		renderer.setSeriesShapesVisible(3, true);
		plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;
	}

	public static void visualise(double[] onsetFunction, int fpb, int offset) {
		final PulseTrainCorrelationVisualisation vis = new PulseTrainCorrelationVisualisation(onsetFunction, fpb,
				offset);
		vis.pack();
		vis.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		RefineryUtilities.centerFrameOnScreen(vis);
		vis.setVisible(true);
	}

}
