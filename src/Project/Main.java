package Project;

import Test.TrapezoidalMotionProfileTest;

public class Main {

    public static void main(String[] args){
        double[][] list = {
                {2,3,0},
                {4,8,30},
                {7,2,90},
                {5,10,45},
                {4,1,60},
                {5,5,90}
        };

        //Path path = new Path(list);
        //PathChart chart = new PathChart(path);
        TrapezoidalMotionProfile profile = new TrapezoidalMotionProfile(2,-40,2,4);
        TrapezoidalMotionProfileTest test = new TrapezoidalMotionProfileTest(profile);
    }
}
