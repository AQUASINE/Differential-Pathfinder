
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;

import java.util.ArrayList;

public class PathChart {
    public PathChart(ArrayList<Double> dataX, ArrayList<Double> dataY){
        XYChart chart = QuickChart.getChart("Graph", "x", "y", "f(x)", dataX, dataY);
        Debug.print("Loading Chart...",2);
        new SwingWrapper(chart).displayChart();
    }
}
