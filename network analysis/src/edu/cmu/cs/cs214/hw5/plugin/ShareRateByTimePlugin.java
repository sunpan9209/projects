package edu.cmu.cs.cs214.hw5.plugin;

import java.util.ArrayList;
import java.util.Date;

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

import edu.cmu.cs.cs214.hw5.framework.AnalysisPlugin;
import edu.cmu.cs.cs214.hw5.framework.AnalysisResultListener;
import edu.cmu.cs.cs214.hw5.framework.Post;
import edu.cmu.cs.cs214.hw5.framework.UserData;

/**
 * Creates and displays a bar graph showing the sharing rate of posts by times
 * of day.
 */
public class ShareRateByTimePlugin implements AnalysisPlugin {
	private static final String name = "Share times analysis";
	private JPanel panel;
	private ChartPanel chartPanel;
	private final int CHART_WIDTH = 400;
	private final int CHART_HEIGHT = 250;
	private AnalysisResultListener listener;

	private enum TimeOfDay {
		MORNING, AFTERNOON, EVENING, NIGHT;
	}

	/**
	 * post sharing analysis
	 */
	public ShareRateByTimePlugin() {
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
		String[] rates = { "sharing rate" };
		ArrayList<Post> posts = userData.getPosts();
		if (posts == null || posts.size() == 0) {
			String[] names = { "No Data" };
			double[][] frequency = { { 0 } };
			return DatasetUtilities.createCategoryDataset(rates, names,
					frequency);
		}
		String[] names = { "morning", "afternoon", "evening", "night" };
		double[][] data = new double[1][names.length];
		for (Post post : posts) {
			if (post.getSharedTimes() > 0) {
				switch (getTimeOfDay(post.getTime())) {
				case AFTERNOON:
					data[0][1]++;
					break;
				case EVENING:
					data[0][2]++;
					break;
				case MORNING:
					data[0][0]++;
					break;
				case NIGHT:
					data[0][3]++;
					break;
				default:
					break;
				}
			}
		}
		return DatasetUtilities.createCategoryDataset(rates, names, data);
	}

	/**
	 * create the bar graph
	 * 
	 * @param categoryDataSet
	 *            bar graph
	 * @return chart to be drawn
	 */
	private JFreeChart createChart(CategoryDataset categoryDataSet) {

		JFreeChart chart = ChartFactory.createBarChart3D("Sharing analysis",
				"Sharing times", "user's posts", categoryDataSet,
				PlotOrientation.VERTICAL, true, true, false);
		CategoryPlot plot = chart.getCategoryPlot();
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		return chart;
	}

	/**
	 * get the time section of the day by the exact date
	 * 
	 * @param date
	 * @return about what time in the day, morning or afternoon
	 */
	@SuppressWarnings("deprecation")
	private TimeOfDay getTimeOfDay(Date date) {
		int hours = date.getHours();

		if (hours >= 5 && hours < 12) {
			return TimeOfDay.MORNING;
		} else if (hours >= 12 && hours < 17) {
			return TimeOfDay.AFTERNOON;
		} else if (hours >= 17 && hours < 20) {
			return TimeOfDay.EVENING;
		} else {
			assert (hours >= 20 || hours < 5);
			return TimeOfDay.NIGHT;
		}
	}

	@Override
	public void analyze(UserData userData) {
		CategoryDataset categoryDataSet = createDataset(userData);
		JFreeChart chart = createChart(categoryDataSet);
		chartPanel.setChart(chart);
		panel.revalidate();
		panel.repaint();
		listener.onAnalysisResultArrive(panel, this.getName());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setAnalysisResultListener(AnalysisResultListener listener) {
		this.listener = listener;
	}
}
