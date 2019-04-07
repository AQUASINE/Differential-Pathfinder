
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.*;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathChart {
    Plot plt = Plot.create();

    XYChart chart;
    String xaxis = "x";
    String yaxis = "y";
    String title = "title";
    String seriesName = "(x(t),y(t))";

    public PathChart(ArrayList<Double> dataX, ArrayList<Double> dataY){
        chart = QuickChart.getChart(title, xaxis, yaxis, seriesName, dataX, dataY);
        Debug.print("Loading Chart...",2);
        new SwingWrapper(chart).displayChart();
        plt.plot()
                .add(dataX);
        try{
            plt.show();
        }
        catch (IOException | PythonExecutionException e){
            e.printStackTrace();
        }
    }
    public void setTitle(String title){
        this.title = title;
        chart.setTitle(this.title);
    }
    public void setAxes(String xaxis, String yaxis){
        this.xaxis = xaxis;
        this.yaxis = yaxis;
        chart.setXAxisTitle(this.xaxis);
        chart.setYAxisTitle(this.yaxis);
    }
}
