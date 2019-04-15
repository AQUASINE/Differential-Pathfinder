package Project;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.*;

import java.io.IOException;
import java.util.ArrayList;

public class PathChart {

    Plot plt = Plot.create();
    ArrayList<Double> dataX = new ArrayList<>();
    ArrayList<Double> dataY = new ArrayList<>();
    public PathChart(Path path){
        for (double i = 0; i<=path.list.length-1; i+=Constants.kSampleRate){
            dataX.add(path.getX(i));
            dataY.add(path.getY(i));
            Debug.print(path.getX(i),3);
        }
        Debug.print("Loading Chart...",2);
        plt.plot()
                .add(dataX,dataY);
        try{
            plt.show();
        }
        catch (IOException | PythonExecutionException e){
            e.printStackTrace();
        }
    }
}
