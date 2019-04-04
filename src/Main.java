import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public final static double kSampleRate = 0.01;
    public static void main(String[] args){
        ArrayList<Double> xpos = new ArrayList<>();
        ArrayList<Double> ypos = new ArrayList<>();
        QuinticSpline spline = new QuinticSpline(0,0,10,20,90,45);
        for (double i = 0; i<1; i+=kSampleRate){
            xpos.add(spline.getY(i));
            ypos.add(spline.getX(i));
            Debug.print(Double.toString(spline.getVelocity(i)),1);
        }

        PathChart chart = new PathChart(xpos, ypos);
    }
}
