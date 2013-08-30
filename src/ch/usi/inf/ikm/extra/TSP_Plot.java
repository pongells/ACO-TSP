package ch.usi.inf.ikm.extra;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import ch.usi.inf.ikm.tsp.City;
import ch.usi.inf.ikm.tsp.Solution;
/**	
 * IKM-AI CUP - TSP PROBLEM
 * 
 * This class is used to create the plot of a Solution.
 * 
 * @author Stefano Pongelli
 */
public class TSP_Plot extends ApplicationFrame {
	private static final long serialVersionUID = 1L;
	private final XYPlot plot;
	private JFreeChart chart;
	private final String title;
	
    public TSP_Plot(final String title, final Solution sol) {
        super(title);
        this.title = title;
        
        final XYSeries series = new XYSeries("path",false,true);
        final LinkedList<City> path = sol.getPath();
        for (final City c : path) {
        	  series.add(c.getX(),c.getY());
		}
        series.add(path.getFirst().getX(),path.getFirst().getY());
        final XYSeriesCollection data = new XYSeriesCollection(series);
        
        chart = ChartFactory.createXYLineChart(
            title+" (length: "+sol.tourLength()+")",
            "X", 
            "Y", 
            data,
            PlotOrientation.VERTICAL,
            false,
            false,
            false
        );
        
        chart.setBackgroundPaint(Color.white);

        plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.black);
        plot.setRangeGridlinePaint(Color.black);
        plot.setOutlinePaint(Color.black);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        renderer.setSeriesStroke(0, new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[] {2.0f, 6.0f}, 0.0f));
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-1.5, -1.5, 3.0, 3.0));
        renderer.setSeriesShapesFilled(0, true);
        renderer.setSeriesShapesVisible(0, true);
        plot.setRenderer(renderer);

        final ChartPanel chartPanel = new ChartPanel(chart);
        setContentPane(chartPanel);
        
      
    }
    
    public void createPlotPNG() {
    	try {
			ChartUtilities.saveChartAsPNG(new File(title+".png"), chart, 800, 600);
			System.out.println("--> created "+title+".png");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public XYPlot getPlot() {
    	return plot;
    }
    
    public JFreeChart getChart() {
    	return this.chart;
    }
}
