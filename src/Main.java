import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public final static double kSampleRate = 0.01;
    public static void main(String[] args){
        double[][] list = {
                {2,3,0},
                {4,8,30},
                {7,2,90}
        };
        ArrayList<Double> xpos,ypos;
        xpos = new ArrayList<>();
        ypos = new ArrayList<>();
        QuinticSpline spline = new QuinticSpline(0,0,10,20,90,45);
        Path path = new Path(list);
        for (double i = 0; i<list.length-2; i+=kSampleRate){
            xpos.add(path.getY(i));
            ypos.add(path.getX(i));

            Debug.print(Double.toString(spline.getY(i)),1);
        }
        spline.arc_length();
        PathChart chart = new PathChart(xpos, ypos);
        chart.setTitle("Position");
    }
}
