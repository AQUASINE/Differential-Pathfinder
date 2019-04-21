package Project;

import Test.PathChart;
import Test.TrapezoidalMotionProfileTest;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args){
        ArrayList<Double> m_dataX, m_dataY, m_dataV, m_dataAngle, m_dataAngularV, m_dataWheel_l, m_dataWheel_r, m_dataT;
        ArrayList<Double[]> m_data, m_dataWheelV;
        double[][] list = {
                {0,0,90},
                {10,8,30},
                {0,0,90},
        };

        Robot robot = new Robot(1.2, Constants.kMax_v, Constants.kMax_accel);
        Path path = new Path(list);
        TrapezoidalMotionProfile profile = new TrapezoidalMotionProfile(0, path.total_length, Constants.kMax_accel, Constants.kMax_v);
        m_dataT = new ArrayList<>();
        m_dataX = new ArrayList<>();
        m_dataY = new ArrayList<>();
        m_dataV = new ArrayList<>();
        m_dataWheel_l = new ArrayList<>();
        m_dataAngularV = new ArrayList<>();
        m_dataAngle = new ArrayList<>();
        m_data = new ArrayList<>();
        m_dataWheel_r = new ArrayList<>();

        Plot plt = Plot.create();
        double t = 0;
        int x = 0;
        double last_value = 0;
        double wraparound_adder = 0;
        while(!profile.isFinished(t)){
            TrapezoidalMotionProfile.State curr_state = profile.calculate(t);
            m_dataX.add(path.getX(curr_state.position/path.total_length));
            m_dataY.add(path.getY(curr_state.position/path.total_length));
            m_dataV.add(curr_state.velocity);
            double curr_value = path.getAngle(curr_state.position/path.total_length);
            if (t!=0){
                if(curr_value >= last_value + 90){
                    wraparound_adder -= 360;
                }
                else if(curr_value <= last_value - 90){
                    wraparound_adder += 360;
                }
            }
            last_value = curr_value;
            m_dataAngle.add(curr_value + wraparound_adder);
            m_dataT.add(t);
            m_data.add(new Double[]{m_dataX.get(x), m_dataY.get(x), m_dataV.get(x), m_dataAngle.get(x)});
            t += Constants.kSampleRate;
            x++;
            Debug.print(profile.calculate(t).position*Math.cos(Math.toRadians(path.getAngleFromSplines(t))),0);
        }


        for(int y = 0; y <= m_dataAngle.size()-1; y++){
            //deg/sec
            double curr_value = y == 0 ? 0 : (m_dataAngle.get(y) - m_dataAngle.get(y-1))/Constants.kTimeStep;
            m_dataAngularV.add(curr_value);
            m_dataWheel_l.add(robot.wheel_v_l(m_dataV.get(y), m_dataAngularV.get(y)*Constants.kTimeStep));
            m_dataWheel_r.add(robot.wheel_v_r(m_dataV.get(y), m_dataAngularV.get(y)*Constants.kTimeStep));
        }
        plt.plot()
                .add(m_dataT,m_dataV)
                .add(m_dataT,m_dataWheel_l)
                .add(m_dataT,m_dataWheel_r);
        try{
            plt.show();

        }
        catch (IOException | PythonExecutionException e){
            e.printStackTrace();
        }

    }
}
