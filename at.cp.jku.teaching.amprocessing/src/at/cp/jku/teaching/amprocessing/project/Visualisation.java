package at.cp.jku.teaching.amprocessing.project;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;

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

public class Visualisation extends JFrame {

	public Visualisation(List<Double> thresholds, List<Double> hfc, List<Double> peaks) {
		final XYDataset dataset = createDataset(thresholds, hfc, peaks);
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1920, 1080));
		setContentPane(chartPanel);
	}

	private XYDataset createDataset(List<Double> thresholds, List<Double> hfc, List<Double> peaks) {
		final XYSeries series1 = new XYSeries("thresholds");
		final XYSeries series2 = new XYSeries("hfc");
		final XYSeries series3 = new XYSeries("peaks");

		for (int i = 0; i < thresholds.size(); i++) {
			series1.add(i, thresholds.get(i));
			series2.add(i, hfc.get(i));
		}
		for (int i = 0; i < peaks.size(); i++) {
			series3.add(peaks.get(i) * 100, 0.2);
		}

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		return dataset;
	}

	private JFreeChart createChart(XYDataset dataset) {
		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart("Plotvisualisation", // chart title
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
}
