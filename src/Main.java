import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public final static double kSampleRate = 0.01;
    public static void main(String[] args){
        ArrayList<Double> xdata = new ArrayList<>();
        ArrayList<Double> ydata = new ArrayList<>();
        QuinticSpline spline = new QuinticSpline(0,0,10,50,0,45);
        for (double i = 0; i<1; i+=kSampleRate){
            xdata.add(spline.getY(i));
            ydata.add(spline.getX(i));
            Debug.print(Double.toString(spline.getVelocity(i)),1);
        }

        PathChart chart = new PathChart(xdata, ydata);
    }
}
