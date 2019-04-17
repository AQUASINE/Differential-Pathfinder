package Project;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        double[][] list = {
                {0,0,90},
                {10,8,30},
                {0,0,90},
        };

        Path path = new Path(list);
        PathChart chart = new PathChart(path);
        TrapezoidalMotionProfile profile = new TrapezoidalMotionProfile(0, path.total_length, Constants.kMax_accel, Constants.kMax_v);
        ArrayList<Double> m_dataX = new ArrayList<>();
        ArrayList<Double> m_dataY = new ArrayList<>();
        Plot plt = Plot.create();
        double t = 0;
        while(!profile.isFinished(t)){
            m_dataX.add(path.getX(profile.calculate(t).position/path.total_length));
            m_dataY.add(path.getY(profile.calculate(t).position/path.total_length));
            t += Constants.kSampleRate;
            Debug.print(profile.calculate(t).position*Math.cos(Math.toRadians(path.getAngle(t))),0);
        }
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
