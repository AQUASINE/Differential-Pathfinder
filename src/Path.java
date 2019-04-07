import java.util.ArrayList;

public class Path {
    ArrayList<QuinticSpline> path_splines;
    private final double max_accel
            //m/s^2
            = 1.4;
    private final double max_v
            //m/s
            = 8;
    private final double max_decel
            //m/s^2
            = -1.4;
    double total_length = 0;
    double accel_time, accel_dist, decel_time, decel_dist, max_v_dist, max_v_time, total_time;
    public Path(double[][] x_y_deg_list){
        path_splines = new ArrayList<>();
        Debug.print("Max Index: " + (x_y_deg_list.length-1), 2);
        for (int i = 0; i <= x_y_deg_list.length-2; i++){
            Debug.print(i,3);
            path_splines.add(new QuinticSpline(x_y_deg_list[i],x_y_deg_list[i+1]));
            total_length += path_splines.get(i).arc_length();
        }
        generate_motion_profile(max_accel, max_v, max_decel);
    }
    public void generate_motion_profile(double max_accel, double max_v, double min_accel){
        accel_time = max_v/(2*max_accel);
        accel_dist = 1/2*accel_time*max_accel;
        decel_time = -max_v/(2*min_accel);
        decel_dist = 1/2*decel_time*min_accel;
        max_v_dist = total_length-(accel_dist + decel_dist);
        max_v_time = max_v_dist/max_v;
        total_time = accel_time+max_v_time+decel_time;
        Debug.print("Seconds: " + total_time, 2);
    }
    public double getX(double t){
         int i = 0;
        if (t>1){
            i = (int)t;
            t -= i;
        }
        try {
            return path_splines.get(i).getX(t);
        }
        catch (IndexOutOfBoundsException e)
        {
            return -1;
        }
    }
    public double getY(double t){
        int i = 0;
        if (t>1){
            i = (int)t;
            t -= i;
        }
        try {
            return path_splines.get(i).getX(t);
        }
        catch (IndexOutOfBoundsException e)
        {
            return -1;
        }
    }
}
