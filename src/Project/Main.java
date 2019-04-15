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
        TrapezoidalMotionProfileTest test = new TrapezoidalMotionProfileTest(new TrapezoidalMotionProfile(0,19,2,4));
    }
}
