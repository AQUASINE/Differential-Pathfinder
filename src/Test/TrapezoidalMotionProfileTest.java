package Test;

import Project.*;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;

public class TrapezoidalMotionProfileTest {
    Plot plt = Plot.create();
    ArrayList<Double> dataX = new ArrayList<>();
    ArrayList<Double> dataY = new ArrayList<>();
    public TrapezoidalMotionProfileTest(TrapezoidalMotionProfile trapezoid){
        Debug.print("MP Total Time:" + trapezoid.totalTime(),3);
        for (double i = 0; i<=trapezoid.totalTime(); i+= Constants.kSampleRate){
            dataX.add(i);
            dataY.add(trapezoid.calculate(i).velocity);
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
