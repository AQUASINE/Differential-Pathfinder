package Project;

import Test.PathChart;
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
        TrapezoidalMotionProfile profile = new TrapezoidalMotionProfile(0, path.total_length, Constants.kMax_accel, Constants.kMax_v);
        ArrayList<Double> m_dataX = new ArrayList<>();
        ArrayList<Double> m_dataY = new ArrayList<>();
        ArrayList<Double> m_dataV = new ArrayList<>();
        ArrayList<Double> m_dataAngle = new ArrayList<>();
        Plot plt = Plot.create();
        double t = 0;
        while(!profile.isFinished(t)){
            TrapezoidalMotionProfile.State curr_state = profile.calculate(t);
            m_dataX.add(path.getX(curr_state.position/path.total_length));
            m_dataY.add(path.getY(curr_state.position/path.total_length));
            m_dataV.add(curr_state.velocity);
            m_dataAngle.add(path.getAngle(curr_state.position/path.total_length));
            t += Constants.kSampleRate;
            Debug.print(profile.calculate(t).position*Math.cos(Math.toRadians(path.getAngleFromSplines(t))),0);
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
