package Project;

import java.awt.*;

/*
The goal of this program is for a robot to be able to smoothly drive through a series of points in the least amount
of time.

This program draws a path for a robot to follow, determines the speed of the robot at any point given max acceleration,
translates it into motor velocities, and simulates a robot driving the path. This enables the robot to drive smoothly
without stopping through a series of desired points. The motor values produced would then be able to used on a physical
robot to follow the same path.
By Sean Chalmers
AP Computer Science Principles Create Task
2019
 */
public class Main {
    public static double[][] points;
    public static MotionDataManager data;
    public static void main(String[] args) {
        points = new double[][]{
                //m, m, deg
                {0, 0, 0},
                {10, 2, 135},
                {5, 9, 25},
                {0, 2, 90},

        };
        run();
    }


    public static void run(){
        calculateMotion();
        SimulatedRobot robotSim = new SimulatedRobot(data, Constants.kMax_v, Constants.kBaseWidth);
        DrawableRobot[] drawableRobots = {new DrawableRobot(data),new DrawableRobot(robotSim.output)};
        drawableRobots[1].setPathColor(new Color(255,55,255));
        drawableRobots[1].setRobotColor(new Color(150,55,255));
        ThreadAnimationEX.start(drawableRobots, points);
    }

    public static void calculateMotion() {
        FeedforwardPhysicsGen robotPhys = new FeedforwardPhysicsGen(Constants.kBaseWidth, Constants.kMax_v, Constants.kMax_accel);
        Path path = new Path(points);
        data = new MotionDataManager();
        TrapezoidalMotionProfile profile = new TrapezoidalMotionProfile(0, path.total_length, Constants.kMax_accel, Constants.kMax_v);

        double last_value = 0;
        double wraparound_adder = 0;
        for(double t = 0; !profile.isFinished(t); t += Constants.kSampleRate) {
            TrapezoidalMotionProfile.State curr_state = profile.calculate(t);
            double distanceFraction = curr_state.position / path.total_length;
            double curr_value = path.getAngle(distanceFraction);
            if (t != 0) { //detects large skips in angle to prevent spikes
                if (curr_value >= last_value + 90) { wraparound_adder -= 360; }
                else if (curr_value <= last_value - 90) { wraparound_adder += 360; }
            }
            last_value = curr_value;

            data.addX(path.getX(distanceFraction));
            data.addY(path.getY(distanceFraction));
            data.addV(curr_state.velocity);
            data.addAngle(curr_value + wraparound_adder);
            data.addT(t);
        }

        for(int i = 0; i <= data.size()-1; i++){
            //deg/sec
            double curr_value = i == 0 ? 0 : (data.getAngle(i) - data.getAngle(i-1)) / Constants.kTimeStep;
            data.addAngularV(curr_value);
            data.addLWheelV(robotPhys.wheelLV(data, i));
            data.addRWheelV(robotPhys.wheelRV(data, i));
        }
    }
}
