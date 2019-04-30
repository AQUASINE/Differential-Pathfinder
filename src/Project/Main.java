package Project;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import java.io.IOException;
import java.util.ArrayList;

/*
The goal of this program is for a robot to be able to smoothly drive through a series of points in the least amount
of time.

This program draws a path for a robot to follow, determines the speed of the robot at any point given max acceleration,
translates it intomotor velocities, and simulates a robot driving the path. This enables the robot to drive smoothly
without stopping through a series of desired points. The motor values produced would then be able to used on a physical
robot to follow the same path.
By Sean Chalmers
AP Computer Science Principles Create Task
2019
 */
public class Main {
    public static ArrayList<Double> m_dataX, m_dataY, m_dataV, m_dataAngle, m_dataAngularV, m_dataWheel_l, m_dataWheel_r, m_dataT;
    public static double[][] list;
    public static void main(String[] args) {

        list = new double[][]{
                //m, m, deg
                {0, 0, 90},
                {10, 2, 135},
                {5, 9, 25},
                {0, 0, 90},

        };

        run();
    }


    public static void run(){

        ArrayList<Double[]> m_data, m_dataWheelV;
        Robot robot = new Robot(Constants.kBaseWidth, Constants.kMax_v, Constants.kMax_accel);
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

        double t = 0;
        int x = 0;
        double last_value = 0;
        double wraparound_adder = 0;
        while(!profile.isFinished(t)) {
            TrapezoidalMotionProfile.State curr_state = profile.calculate(t);
            double distanceFraction = curr_state.position / path.total_length;
            m_dataX.add(path.getX(distanceFraction));
            m_dataY.add(path.getY(distanceFraction));
            m_dataV.add(curr_state.velocity);
            //find the raw angle value from how far along it is along the path
            double curr_value = path.getAngle(distanceFraction);
            if (t != 0) {
                //detects large skips in angle to prevent spikes
                if (curr_value >= last_value + 90) { wraparound_adder -= 360; }
                else if (curr_value <= last_value - 90) { wraparound_adder += 360; }
            }
            //set the previous value for when
            last_value = curr_value;
            //add to the list with the adjusted angle
            m_dataAngle.add(curr_value + wraparound_adder);

            m_dataT.add(t);
            m_data.add(new Double[]{m_dataX.get(x), m_dataY.get(x), m_dataV.get(x), m_dataAngle.get(x)});
            t += Constants.kSampleRate;
            x++;
        }

        for(int y = 0; y <= m_dataAngle.size()-1; y++){
            //deg/sec
            double curr_value = y == 0 ? 0 : (m_dataAngle.get(y) - m_dataAngle.get(y-1))/Constants.kTimeStep;
            m_dataAngularV.add(curr_value);
            m_dataWheel_l.add(robot.wheel_v_l(m_dataV.get(y), m_dataAngularV.get(y)*Constants.kTimeStep));
            m_dataWheel_r.add(robot.wheel_v_r(m_dataV.get(y), m_dataAngularV.get(y)*Constants.kTimeStep));
        }
        ThreadAnimationEX.start();
    }
}
