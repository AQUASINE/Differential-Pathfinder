package Test;

import Project.*;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;

public class TrapezoidalMotionProfileTest {

    ArrayList<Double> m_dataX;
    ArrayList<Double> m_dataY;

    Plot plt = Plot.create();
    public TrapezoidalMotionProfileTest(TrapezoidalMotionProfile trapezoid){

        m_dataX = new ArrayList<>();
        m_dataY = new ArrayList<>();

        Debug.print("MP Total Time: " + trapezoid.totalTime(),3);
        for (double i = 0; i<=trapezoid.totalTime(); i+= Constants.kSampleRate){
            m_dataX.add(i);
            m_dataY.add(trapezoid.calculate(i).velocity);
        }

        Debug.print("Loading Chart...",2);
        plt.plot()
                .add(m_dataX,m_dataY);
        try{
            plt.show();
        }
        catch (IOException | PythonExecutionException e){
            e.printStackTrace();
        }
    }
}
