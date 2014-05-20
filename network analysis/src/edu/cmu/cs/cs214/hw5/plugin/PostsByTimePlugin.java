package edu.cmu.cs.cs214.hw5.plugin;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import edu.cmu.cs.cs214.hw5.framework.AnalysisPlugin;
import edu.cmu.cs.cs214.hw5.framework.AnalysisResultListener;
import edu.cmu.cs.cs214.hw5.framework.Post;
import edu.cmu.cs.cs214.hw5.framework.UserData;

/**
 * Creates and displays a pie graph showing the popularity of posts by times of
 * day.
 * 
 */
public class PostsByTimePlugin implements AnalysisPlugin {
	private static final String name = "Post Time Analysis";
	private JPanel panel;
	private ChartPanel chartPanel;
	private final int CHART_WIDTH = 400;
	private final int CHART_HEIGHT = 250;
	private AnalysisResultListener listener;

	private enum TimeOfDay {
		MORNING, AFTERNOON, EVENING, NIGHT;
	}

	/**
	 * pie graph showing the popularity of posts by times of day.
	 */
	public PostsByTimePlugin() {
		this.panel = new JPanel();
		chartPanel = new ChartPanel(null);
		chartPanel.setPreferredSize(new java.awt.Dimension(CHART_WIDTH,
				CHART_HEIGHT));
		panel.add(chartPanel);
	}
	
	/**
	 * create the pie graph data set based on user data
	 * 
	 * @param UserData
	 * @return pie graph data set
	 */
	private PieDataset createDataSet(UserData userData) {
		DefaultPieDataset result = new DefaultPieDataset();
		ArrayList<Post> posts = userData.getPosts();
		if (posts == null || posts.size() == 0) {
			result.setValue("No Data", 100);
			return result;
		}
		String[] times = { "morning", "afternoon", "evening", "night" };
		int[] data = new int[times.length];
		for (Post post : posts) {
			switch (getTimeOfDay(post.getTime())) {
			case AFTERNOON:
				data[1]++;
				break;
			case EVENING:
				data[2]++;
				break;
			case MORNING:
				data[0]++;
				break;
			case NIGHT:
				data[3]++;
				break;
			default:
				break;
			}
		}
		for (int i = 0; i < times.length; i++) {
			result.setValue(times[i] + ":" + data[i], data[i]);
		}
		return result;
	}
	

	/**
	 * create the pie graph of posts by time of day
	 * 
	 * @param PieDataSet
	 *            pie graph
	 * @return chart to be drawn
	 */
	private JFreeChart createChart(PieDataset dataset) {

		JFreeChart chart = ChartFactory.createPieChart3D(name, dataset, true,
				true, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(190);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.6f);
		return chart;
	}

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
		PieDataset pieDataSet = createDataSet(userData);
		JFreeChart chart = createChart(pieDataSet);
		chartPanel.setChart(chart);
		chartPanel.revalidate();
		chartPanel.repaint();
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
