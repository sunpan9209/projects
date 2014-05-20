package edu.cmu.cs.cs214.hw5.plugin;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import edu.cmu.cs.cs214.hw5.framework.*;

/**
 * create a bar graph showing your most popular keywords in your posts
 */
public class KeyWordPlugin implements AnalysisPlugin {

	private HashMap<String, Integer> keyWords;
	private final int SIZE = 5;
	private static final String NAME = "Key Words Analysis";
	private JPanel panel;
	private ChartPanel chartPanel;
	private final int CHART_WIDTH = 450;
	private final int CHART_HEIGHT = 250;
	private AnalysisResultListener listener;

	/**
	 * create the panel for keyword analysis
	 */
	public KeyWordPlugin() {
		keyWords = new HashMap<String, Integer>();
		this.panel = new JPanel();
		chartPanel = new ChartPanel(null);
		chartPanel.setPreferredSize(new java.awt.Dimension(CHART_WIDTH,
				CHART_HEIGHT));
		panel.add(chartPanel);
	}

	/**
	 * create the bar graph data set based on user data
	 * 
	 * @param UserData
	 * @return bar graph data set
	 */
	private CategoryDataset createDataset(UserData userData) {
		String[] rates = { "Frequency" };
		ArrayList<Post> posts = userData.getPosts();
		if (posts == null || posts.size() == 0) {
			String[] names = { "No Data" };
			double[][] frequency = { { 0 } };
			return DatasetUtilities.createCategoryDataset(rates, names,
					frequency);
		}
		preProcess(posts);
		List<Map.Entry<String, Integer>> words = new ArrayList<Map.Entry<String, Integer>>(
				keyWords.entrySet());
		Collections.sort(words, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}
		});
		int size = words.size();
		if (size > SIZE) {
			size = SIZE;
		}
		String[] mostWords = new String[size];
		double[][] data = new double[1][size];
		for (int i = 0; i < size; i++) {
			mostWords[i] = words.get(i).getKey();
			data[0][i] = words.get(i).getValue();
		}
		return DatasetUtilities.createCategoryDataset(rates, mostWords, data);
	}

	/**
	 * create the bar graph of frequency by keywords
	 * 
	 * @param categoryDataSet
	 *            bar graph
	 * @return chart to be drawn
	 */
	private JFreeChart createChart(CategoryDataset categoryDataSet) {

		JFreeChart chart = ChartFactory.createBarChart3D(
				"Keyword frequency analysis", "Keyword", "frequency",
				categoryDataSet, PlotOrientation.VERTICAL, true, true, false);
		CategoryPlot plot = chart.getCategoryPlot();
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
		renderer.setSeriesPaint(0, Color.blue);
		renderer.setDrawBarOutline(false);
		return chart;
	}

	@Override
	public void analyze(UserData userData) {
		keyWords = new HashMap<String, Integer>();
		CategoryDataset categoryDataSet = createDataset(userData);
		JFreeChart chart = createChart(categoryDataSet);
		chartPanel.setChart(chart);
		panel.revalidate();
		panel.repaint();
		listener.onAnalysisResultArrive(panel, this.getName());
	}

	/**
	 * Preprocess the hashMap to store the frequency of words
	 * 
	 * @param posts
	 */
	private void preProcess(ArrayList<Post> posts) {
		for (Post p : posts) {
			String message = p.getMessage();
			String[] words = message.split("\\W");
			for (String word : words) {
				if (word.matches(".*\\W.*") || word.length() <= 4
						|| word.equals("http")) {
					continue;
				}
				if (keyWords.containsKey(word)) {
					keyWords.put(word, keyWords.get(word) + 1);
				} else {
					keyWords.put(word, 1);
				}
			}
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void setAnalysisResultListener(AnalysisResultListener listener) {
		this.listener = listener;
	}
}
